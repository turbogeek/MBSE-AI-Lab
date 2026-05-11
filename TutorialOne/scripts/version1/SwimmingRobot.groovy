import com.dassault_systemes.modeler.kerml.find.Finder
import com.dassault_systemes.modeler.kerml.libraries.standard.ScalarValuesLibrary
import com.dassault_systemes.modeler.kerml.model.*
import com.dassault_systemes.modeler.kerml.model.kerml.*
import com.dassault_systemes.modeler.sysml.model.ElementsFactory
import com.dassault_systemes.modeler.sysml.model.sysml.*
import com.nomagic.magicdraw.core.Application
import com.nomagic.magicdraw.core.Project
import com.nomagic.magicdraw.openapi.uml.SessionManager
import java.util.Arrays

// Logger setup
String scriptDir = "E:\\_Documents\\git\\TutorialForCatiaMagicApiMCP\\scripts"
File loggerFile = new File(scriptDir, "SysMLv2Logger.groovy")
if (!loggerFile.exists()) {
    System.err.println("Logger file not found: " + loggerFile.getAbsolutePath())
    return
}
def loggerClass = new GroovyClassLoader(getClass().getClassLoader()).parseClass(loggerFile)
File runLog = new File("E:\\_Documents\\git\\TutorialForCatiaMagicApiMCP\\Tutorials\\TutorialOne\\logs", "SwimmingRobot.log")
if (!runLog.getParentFile().exists()) {
    runLog.getParentFile().mkdirs()
}
def logger = loggerClass.newInstance("SwimmingRobotTutorial", runLog)

try {
    Project project = Application.getInstance().getProject()
    if (project == null) {
        logger.error("No active MagicDraw project!")
        return
    }

    // Get selected element
    Namespace selectedNamespace = null
    def browser = Application.getInstance().getMainFrame().getBrowser()
    if (browser != null) {
        def selectedNodes = browser.getActiveTree()?.getSelectedNodes()
        if (selectedNodes != null && selectedNodes.length > 0) {
            def element = selectedNodes[0].getUserObject()
            if (element instanceof Namespace) {
                selectedNamespace = (Namespace) element
            }
        }
    }

    if (selectedNamespace == null) {
        logger.error("Please select a Namespace in the Containment Tree.")
        return
    }

    boolean sessionCreated = false
    try {
        if (!SessionManager.getInstance().isSessionCreated(project)) {
            SessionManager.getInstance().createSession(project, "Generate Swimming Robot Model")
            sessionCreated = true
        }

        logger.info("Initializing model generation...")
        ElementsFactory factory = ElementsFactory.get(selectedNamespace)
        ScalarValuesLibrary scalarValuesLib = ScalarValuesLibrary.getInstance(selectedNamespace)

        // 0. Create Package "Swimming Robot"
        Package robotPkg = factory.createPackage()
        robotPkg.setOwner(selectedNamespace)
        robotPkg.setDeclaredName("Swimming Robot")
        logger.info("Created Package: Swimming Robot")

        // 1. Create Requirement REQ-1
        RequirementUsage req1 = factory.createRequirementUsage()
        req1.setOwner(robotPkg)
        req1.setDeclaredName("REQ-1")
        
        Documentation doc = factory.createDocumentation()
        doc.setOwner(req1)
        doc.setBody("The robot shall be able to swim in a pool.")
        logger.info("Created Requirement: REQ-1 (Swimming Robot)")

        // 2. Create Part Definition "Swimming Robot"
        PartDefinition robotDef = factory.createPartDefinition()
        robotDef.setOwner(robotPkg)
        robotDef.setDeclaredName("Swimming Robot")
        logger.info("Created Part Definition: Swimming Robot")

        // 3. Create Part Usage "robot1"
        PartUsage robot1 = factory.createPartUsage()
        robot1.setOwner(robotPkg)
        robot1.setDeclaredName("robot1")
        FeatureTypings.addType(robot1, robotDef)
        logger.info("Created Part Usage: robot1")

        // 4. Create Attribute Usage "cost"
        AttributeUsage cost = factory.createAttributeUsage()
        cost.setOwner(robot1)
        cost.setDeclaredName("cost")
        FeatureTypings.addType(cost, scalarValuesLib.Real().getElement())
        
        // Set Default Value ($500)
        LiteralRational literalRational = factory.createLiteralRational()
        literalRational.setValue(500.0)
        Elements.setOwningMembership(literalRational, cost, KerMLPackage.eINSTANCE.getFeatureValue())
        logger.info("Created Attribute: cost = 500.0")

        // 5. Create Satisfaction (SatisfyRequirementUsage)
        SatisfyRequirementUsage satisfy = factory.createSatisfyRequirementUsage()
        satisfy.setOwner(robot1)
        satisfy.setDeclaredName("Satisfies REQ-1")
        
        ReferenceSubsetting subsetting = factory.createReferenceSubsetting()
        subsetting.setSubsettingFeature(satisfy)
        subsetting.setSubsettedFeature(req1)
        subsetting.setOwner(satisfy)
        
        logger.info("Created Satisfaction: robot1 satisfies REQ-1")

        // 6. Listing Elements for Validation
        logger.info("--- Model Validation Summary ---")
        logger.info("Requirement: " + req1.getHumanName() + " [Doc: " + doc.getBody() + "]")
        logger.info("Definition: " + robotDef.getHumanName())
        logger.info("Usage: " + robot1.getHumanName() + " (Typed by: " + robotDef.getDeclaredName() + ")")
        logger.info("Attribute: " + cost.getHumanName() + " = " + literalRational.getValue())
        logger.info("Allocation/Satisfy created successfully.")
        logger.info("---------------------------------")

        if (sessionCreated) {
            SessionManager.getInstance().closeSession(project)
        }

    } catch (Exception e) {
        if (sessionCreated) {
            SessionManager.getInstance().cancelSession(project)
        }
        logger.error("Script execution failed. Model changes reverted.", e)
    }

} catch (Throwable t) {
    logger.error("Critical script failure", t)
}
