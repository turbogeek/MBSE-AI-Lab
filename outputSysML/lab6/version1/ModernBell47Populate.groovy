import com.nomagic.magicdraw.core.Application
import com.nomagic.magicdraw.core.Project
import com.nomagic.magicdraw.openapi.uml.SessionManager
import com.nomagic.uml2.ext.jmi.helpers.StereotypesHelper
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Package
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Class
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Comment
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Property
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.DataType
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.PrimitiveType
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Classifier
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.LiteralString
import com.nomagic.magicdraw.sysml.util.SysMLProfile
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.LiteralReal
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.LiteralInteger
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.LiteralBoolean
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Profile
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Stereotype
import com.nomagic.uml2.ext.magicdraw.mdusecases.UseCase
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Operation
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Parameter
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.ParameterDirectionKindEnum
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Generalization
import com.nomagic.uml2.ext.magicdraw.classes.mddependencies.Dependency
import com.nomagic.uml2.ext.magicdraw.activities.mdfundamentalactivities.Activity
import com.nomagic.uml2.ext.magicdraw.statemachines.mdbehaviorstatemachines.StateMachine
import com.nomagic.uml2.ext.magicdraw.statemachines.mdbehaviorstatemachines.Region
import com.nomagic.uml2.ext.magicdraw.statemachines.mdbehaviorstatemachines.State
import com.nomagic.uml2.ext.magicdraw.statemachines.mdbehaviorstatemachines.Pseudostate
import com.nomagic.uml2.ext.magicdraw.statemachines.mdbehaviorstatemachines.PseudostateKindEnum
import com.nomagic.uml2.ext.magicdraw.statemachines.mdbehaviorstatemachines.Transition
import com.nomagic.uml2.ext.magicdraw.compositestructures.mdinternalstructures.Connector
import com.nomagic.uml2.ext.magicdraw.compositestructures.mdinternalstructures.ConnectorEnd
import com.nomagic.uml2.ext.magicdraw.compositestructures.mdports.Port
import com.nomagic.magicdraw.openapi.uml.PresentationElementsManager
import com.nomagic.magicdraw.openapi.uml.ModelElementsManager
import com.nomagic.magicdraw.uml.symbols.shapes.ShapeElement
import com.nomagic.magicdraw.uml.symbols.paths.PathElement
import com.nomagic.magicdraw.uml.symbols.PresentationElement
import com.nomagic.magicdraw.uml.symbols.DiagramPresentationElement
import com.nomagic.uml2.ext.magicdraw.actions.mdbasicactions.OpaqueAction
import com.nomagic.uml2.ext.magicdraw.activities.mdbasicactivities.ControlFlow
import com.nomagic.uml2.ext.magicdraw.activities.mdbasicactivities.InitialNode
import com.nomagic.uml2.ext.magicdraw.activities.mdbasicactivities.ActivityFinalNode
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Diagram
import com.nomagic.magicdraw.uml.DiagramTypeConstants
import com.nomagic.magicdraw.sysml.util.SysMLConstants
import java.io.File


// Setup Logger
String scriptDir = "E:\\_Documents\\git\\MBSE-AI-Lab\\scripts"
File loggerFile = new File(scriptDir, "SysMLv2Logger.groovy")
def loggerClass = new GroovyClassLoader(getClass().getClassLoader()).parseClass(loggerFile)
File runLog = new File("E:\\_Documents\\git\\MBSE-AI-Lab\\outputSysML\\lab6\\version1", "ModernBell47Populate.log")
if (!runLog.getParentFile().exists()) {
    runLog.getParentFile().mkdirs()
}
def logger = loggerClass.newInstance("ModernBell47Populate", runLog)

try {
    Project project = Application.getInstance().getProject()
    if (project == null) {
        logger.error("No active MagicDraw project found!")
        return
    }

    logger.info("Starting Full-Fidelity Modern Bell 47 model population (SysMLv1)...")

    // Find target package
    Package selectedNamespace = null
    def browser = Application.getInstance().getMainFrame().getBrowser()
    if (browser != null) {
        def selectedNodes = browser.getActiveTree()?.getSelectedNodes()
        if (selectedNodes != null && selectedNodes.length > 0) {
            def element = selectedNodes[0].getUserObject()
            if (element instanceof Package) {
                selectedNamespace = (Package) element
            }
        }
    }

    if (selectedNamespace == null) {
        selectedNamespace = project.getPrimaryModel()
    }

    logger.info("Target Root Package: " + selectedNamespace.getName())

    // Recursive package lookup helper
    def findPackageRecursive
    findPackageRecursive = { Package parent, String name ->
        for (Element child : parent.getOwnedElement()) {
            if (child instanceof Package) {
                Package pkg = (Package) child
                if (pkg.getName().contains(name)) {
                    return pkg
                }
                Package sub = findPackageRecursive(pkg, name)
                if (sub != null) return sub
            }
        }
        return null
    }

    // Locate MagicGrid packages
    Package stakeholdersPkg = findPackageRecursive(selectedNamespace, "Stakeholders")
    Package requirementsPkg = findPackageRecursive(selectedNamespace, "Requirements")
    Package usecasesPkg = findPackageRecursive(selectedNamespace, "Use Cases")
    Package logicalPkg = findPackageRecursive(selectedNamespace, "Logical Architecture")
    Package physicalPkg = findPackageRecursive(selectedNamespace, "Physical Architecture")

    boolean sessionCreated = false
    try {
        if (!SessionManager.getInstance().isSessionCreated(project)) {
            SessionManager.getInstance().createSession(project, "Populate Full MagicGrid Model")
            sessionCreated = true
        }

        def factory = project.getElementsFactory()
        Profile sysmlProfile = StereotypesHelper.getProfile(project, "SysML")
        if (sysmlProfile == null) {
            logger.error("SysML Profile not found in project!")
            return
        }

        Stereotype blockStereotype = StereotypesHelper.getStereotype(project, "Block", sysmlProfile)
        Stereotype reqStereotype = StereotypesHelper.getStereotype(project, "Requirement", sysmlProfile)
        Stereotype valPropStereotype = StereotypesHelper.getStereotype(project, "ValueProperty", sysmlProfile)
        Stereotype stakeholderStereotype = StereotypesHelper.getStereotype(project, "Stakeholder", sysmlProfile)
        Stereotype verifyStereotype = StereotypesHelper.getStereotype(project, "Verify", sysmlProfile)
        Stereotype testCaseStereotype = StereotypesHelper.getStereotype(project, "TestCase", sysmlProfile)

        // Resolve CST Simulation Profile
        Stereotype simConfigStereotype = null
        Profile simProfile = StereotypesHelper.getProfile(project, "SimulationProfile")
        if (simProfile != null) {
            simConfigStereotype = StereotypesHelper.getStereotype(project, "SimulationConfig", simProfile)
        }
        if (simConfigStereotype == null) {
            simConfigStereotype = StereotypesHelper.getStereotype(project, "SimulationConfig")
        }

        def applyStereotypeSafe = { Element elem, Stereotype stereotype ->
            if (stereotype != null && StereotypesHelper.canApplyStereotype(elem, stereotype)) {
                StereotypesHelper.addStereotype(elem, stereotype)
            }
        }

        // Initialize MagicGrid Packages
        if (stakeholdersPkg == null) {
            stakeholdersPkg = factory.createPackageInstance()
            stakeholdersPkg.setName("1. Stakeholders")
            stakeholdersPkg.setOwner(selectedNamespace)
            logger.info("Created: 1. Stakeholders")
        }
        if (requirementsPkg == null) {
            requirementsPkg = factory.createPackageInstance()
            requirementsPkg.setName("2. Requirements")
            requirementsPkg.setOwner(selectedNamespace)
            logger.info("Created: 2. Requirements")
        }
        if (usecasesPkg == null) {
            usecasesPkg = factory.createPackageInstance()
            usecasesPkg.setName("3. Use Cases")
            usecasesPkg.setOwner(selectedNamespace)
            logger.info("Created: 3. Use Cases")
        }
        if (logicalPkg == null) {
            logicalPkg = factory.createPackageInstance()
            logicalPkg.setName("4. Logical Architecture")
            logicalPkg.setOwner(selectedNamespace)
            logger.info("Created: 4. Logical Architecture")
        }
        if (physicalPkg == null) {
            physicalPkg = factory.createPackageInstance()
            physicalPkg.setName("5. Physical Architecture")
            physicalPkg.setOwner(selectedNamespace)
            logger.info("Created: 5. Physical Architecture")
        }

        // Subpackages for structured requirements
        Package sysReqsPkg = factory.createPackageInstance()
        sysReqsPkg.setName("System Requirements")
        sysReqsPkg.setOwner(requirementsPkg)

        Package fcsReqsPkg = factory.createPackageInstance()
        fcsReqsPkg.setName("Flight Control Requirements")
        fcsReqsPkg.setOwner(requirementsPkg)

        Package epsReqsPkg = factory.createPackageInstance()
        epsReqsPkg.setName("Electrical Requirements")
        epsReqsPkg.setOwner(requirementsPkg)

        Package pwrReqsPkg = factory.createPackageInstance()
        pwrReqsPkg.setName("Powertrain Requirements")
        pwrReqsPkg.setOwner(requirementsPkg)

        // 1. Create Stakeholders & Concerns
        def createStakeholder = { String name, String concern ->
            Class sh = factory.createClassInstance()
            sh.setName(name)
            sh.setOwner(stakeholdersPkg)
            applyStereotypeSafe(sh, stakeholderStereotype)
            Comment comment = factory.createCommentInstance()
            comment.setBody(concern)
            comment.setOwner(sh)
            logger.info("Created Stakeholder: " + name)
        }

        createStakeholder("Bell Helicopter Design Team", "Concern: Minimize total weight, control acquisition costs, optimize performance.")
        createStakeholder("FAA Certifying Authority", "Concern: Compliance with Part 27, ensuring safety and reliability, preventing single-point mechanical or battery failures.")
        createStakeholder("Helicopter Operator", "Concern: Reduce maintenance downtime, minimize maintenance man-hours per flight hour, maximize component overhaul intervals.")

        // 2. Create Requirements
        def createRequirement = { Package pkg, String id, String name, String text ->
            Class req = factory.createClassInstance()
            req.setName(name)
            req.setOwner(pkg)
            applyStereotypeSafe(req, reqStereotype)
            if (reqStereotype != null) {
                StereotypesHelper.setStereotypePropertyValue(req, reqStereotype, "Id", id)
                StereotypesHelper.setStereotypePropertyValue(req, reqStereotype, "Text", text)
            }
            logger.info("Created Requirement: " + id + " - " + name)
            return req
        }

        // System level
        def reqCap = createRequirement(sysReqsPkg, "REQ-01", "REQ_Cap", "The helicopter shall have a 2-seat capacity.")
        def reqEngine = createRequirement(sysReqsPkg, "REQ-02", "REQ_Engine", "The helicopter shall utilize a modern turboshaft engine.")
        def reqSafety = createRequirement(sysReqsPkg, "REQ-03", "REQ_Part27_Safety", "The tail rotor and supporting structures shall withstand limit loads multiplied by a factor of safety of 1.5.")
        def reqFatigue = createRequirement(sysReqsPkg, "REQ-04", "REQ_Fatigue", "Primary structural parts of the anti-torque drive system shall be designed for a minimum fatigue life of 5,000 flight hours.")
        def reqElecBackup = createRequirement(sysReqsPkg, "REQ-05", "REQ_Elec_Backup", "The electric tail rotor system shall include a backup battery system capable of supplying operational yaw thrust power for at least 7 to 10 minutes (matching/exceeding max autorotative descent duration).")
        def reqThermal = createRequirement(sysReqsPkg, "REQ-06", "REQ_Thermal_Safety", "The battery subsystem shall comply with FAA Special Conditions, demonstrating that a thermal runaway event in any single cell will not propagate.")
        def reqRedundantPower = createRequirement(sysReqsPkg, "REQ-07", "REQ_Redundant_Power", "The electrical architecture shall feature dual redundant power distribution lines and dual motor windings to ensure yaw control is maintained despite a single wiring or winding failure.")
        def reqClutch = createRequirement(sysReqsPkg, "REQ-08", "REQ_Mech_Clutch", "The mechanical tail rotor driveshaft shall be connected downstream of the freewheeling clutch for synchronization in autorotation.")
        def reqLubrication = createRequirement(sysReqsPkg, "REQ-09", "REQ_Lubrication", "The tail rotor gearboxes shall be designed to operate for at least 30 minutes after loss of lubrication.")
        def reqShielding = createRequirement(sysReqsPkg, "REQ-10", "REQ_Driveshaft_Shielding", "The mechanical tail boom driveshaft shall be shielded to prevent damage from whipping or blade strikes in the event of a shaft coupling failure.")

        // Subsystem level
        def reqFcs = createRequirement(fcsReqsPkg, "REQ-FCS-01", "REQ_FCS_Latency", "The flight controller yaw command processing latency shall be less than 50 milliseconds.")
        def reqEps = createRequirement(epsReqsPkg, "REQ-EPS-01", "REQ_EPS_Capacity", "The emergency backup battery energy capacity shall be at least 1.5 kWh.")
        def reqPwr = createRequirement(pwrReqsPkg, "REQ-PWR-01", "REQ_PWR_Efficiency", "The anti-torque mechanical drive transmission efficiency shall exceed 96%.")

        // 3. Use Cases & Verification Tests
        Package testsPkg = factory.createPackageInstance()
        testsPkg.setName("Verification Tests")
        testsPkg.setOwner(usecasesPkg)

        def diagramsToLayout = []

        def drawActivityDiagram = { Activity act, String name ->
            try {
                // Populate semantic nodes inside the Activity namespace
                com.nomagic.uml2.ext.magicdraw.activities.mdbasicactivities.InitialNode initNode = factory.createInitialNodeInstance()
                initNode.setName("Start")
                initNode.setOwner(act)
                act.getNode().add(initNode)
                
                com.nomagic.uml2.ext.magicdraw.actions.mdbasicactions.OpaqueAction runTest = factory.createOpaqueActionInstance()
                runTest.setName("Run " + name)
                runTest.setOwner(act)
                act.getNode().add(runTest)
                
                com.nomagic.uml2.ext.magicdraw.actions.mdbasicactions.OpaqueAction verifyResult = factory.createOpaqueActionInstance()
                verifyResult.setName("Verify Criteria")
                verifyResult.setOwner(act)
                act.getNode().add(verifyResult)
                
                com.nomagic.uml2.ext.magicdraw.activities.mdbasicactivities.ActivityFinalNode finalNode = factory.createActivityFinalNodeInstance()
                finalNode.setName("End")
                finalNode.setOwner(act)
                act.getNode().add(finalNode)
                
                // Connect them with ControlFlow edges
                def createFlow = { Element src, Element tgt, String flowName ->
                    com.nomagic.uml2.ext.magicdraw.activities.mdbasicactivities.ControlFlow flow = factory.createControlFlowInstance()
                    flow.setName(flowName)
                    flow.setSource(src)
                    flow.setTarget(tgt)
                    flow.setOwner(act)
                    act.getEdge().add(flow)
                    return flow
                }
                
                createFlow(initNode, runTest, "f1")
                createFlow(runTest, verifyResult, "f2")
                createFlow(verifyResult, finalNode, "f3")
                
                // Create SysML Activity Diagram
                com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Diagram diagram = ModelElementsManager.getInstance().createDiagram(SysMLConstants.SYSML_ACTIVITY_DIAGRAM, act)
                diagram.setName(name + " Flow")
                DiagramPresentationElement diagramPE = project.getDiagram(diagram)
                diagramsToLayout.add(diagramPE)
                
                // Create shapes on diagram
                def shapeMap = [:]
                for (Element node : act.getNode()) {
                    ShapeElement shape = PresentationElementsManager.getInstance().createShapeElement(node, diagramPE)
                    shapeMap[node] = shape
                }
                
                // Create paths on diagram
                for (Element edge : act.getEdge()) {
                    def sourceShape = shapeMap[edge.getSource()]
                    def targetShape = shapeMap[edge.getTarget()]
                    if (sourceShape != null && targetShape != null) {
                        PresentationElementsManager.getInstance().createPathElement(edge, sourceShape, targetShape)
                    }
                }
                logger.info("  Created and populated Activity Diagram: " + name + " Flow")
            } catch (Exception e) {
                logger.error("Failed to create Activity Diagram: " + name, e)
            }
        }

        def createTestCase = { String name, Class targetReq ->
            Activity act = factory.createActivityInstance()
            act.setName(name)
            act.setOwner(testsPkg)
            applyStereotypeSafe(act, testCaseStereotype)
            
            // Create verify dependency
            Dependency dep = factory.createDependencyInstance()
            dep.setOwner(testsPkg)
            dep.getClient().add(act)
            dep.getSupplier().add(targetReq)
            applyStereotypeSafe(dep, verifyStereotype)
            
            logger.info("Created Test Case: " + name + " (Verifies " + targetReq.getName() + ")")
            
            // Generate Activity Diagram
            drawActivityDiagram(act, name)
            
            return act
        }

        createTestCase("Test_YawAuthority", reqSafety)
        createTestCase("Test_BatteryDuration", reqElecBackup)
        createTestCase("Test_ThermalRunaway", reqThermal)
        createTestCase("Test_PowerRedundancy", reqRedundantPower)
        createTestCase("Test_GearboxRunDry", reqLubrication)
        createTestCase("Test_DriveshaftShielding", reqShielding)

        // General Use Cases
        def useCaseNames = [
            "Counter Main Rotor Torque", "Manage Backup Electrical Power", "Perform Autorotation Landing",
            "Maintain Tail Rotor Authority During Engine Failure", "Regular Maintenance", "Emergency Maintenance",
            "Pre-Flight Check", "Startup", "Shutdown", "Takeoff", "Hover", "Cruise", "Descent", "Landing",
            "Maneuvering", "Mountain Flying", "Crop Dusting", "Aerial Photography", "External Load Operations",
            "Passenger Transport", "Utility Operations", "Ambulance Operations", "Law Enforcement Operations",
            "Firefighting Operations", "Emergency Engine Failure", "Post-Flight Check", "Flight Test", "Training",
            "Engine Rebuild", "Long Term Storage", "Export and Shipping", "Night Flight", "Instrument Flight",
            "Instrument Flight with Training", "Instrument Flight with Export and Shipping", "Night Flight with Training",
            "Night Flight with Export and Shipping", "Night Flight with Training and Export and Shipping"
        ]

        useCaseNames.each { name ->
            UseCase uc = factory.createUseCaseInstance()
            uc.setName(name)
            uc.setOwner(usecasesPkg)
        }
        logger.info("Created Use Cases (" + useCaseNames.size() + " items)")

        // 4. Logical Architecture (Blocks, Parts, Operations, States, ValueProperties)
        Class helicopterBlock = factory.createClassInstance()
        helicopterBlock.setName("Modern Bell 47")
        helicopterBlock.setOwner(logicalPkg)
        applyStereotypeSafe(helicopterBlock, blockStereotype)
        logger.info("Created block: Modern Bell 47")

        def createSubsystemBlock = { String name ->
            Class b = factory.createClassInstance()
            b.setName(name)
            b.setOwner(logicalPkg)
            applyStereotypeSafe(b, blockStereotype)
            
            Property part = factory.createPropertyInstance()
            part.setName(name.toLowerCase().replaceAll(" ", ""))
            part.setType(b)
            part.setAggregation(com.nomagic.uml2.ext.magicdraw.classes.mdkernel.AggregationKindEnum.COMPOSITE)
            part.setOwner(helicopterBlock)
            logger.info("Allocated Part: " + part.getName() + " under Modern Bell 47")
            return b
        }

        Class engineBlock = createSubsystemBlock("Engine")
        Class mainRotorBlock = createSubsystemBlock("Main Rotor")
        Class cabinBlock = createSubsystemBlock("Cabin")
        Class flightControllerBlock = createSubsystemBlock("Flight Controller")
        Class antiTorqueBlock = createSubsystemBlock("Anti-Torque Subsystem")
        Class epsBlock = createSubsystemBlock("Electrical Power Subsystem")
        Class powertrainBlock = createSubsystemBlock("Powertrain Subsystem")

        // Value Properties & Rollups
        PrimitiveType realType = StereotypesHelper.getPrimitiveByName(project, "Real")
        PrimitiveType stringType = StereotypesHelper.getPrimitiveByName(project, "String")

        def createValProp = { Class block, String name, Classifier type, Object defaultValue ->
            Property prop = factory.createPropertyInstance()
            prop.setName(name)
            prop.setType(type)
            prop.setOwner(block)
            applyStereotypeSafe(prop, valPropStereotype)
            
            if (defaultValue != null) {
                if (type.getName().contains("String")) {
                    LiteralString ls = factory.createLiteralStringInstance()
                    ls.setValue(defaultValue.toString())
                    prop.setDefaultValue(ls)
                } else if (type.getName().contains("Real")) {
                    LiteralReal lr = factory.createLiteralRealInstance()
                    lr.setValue(Double.parseDouble(defaultValue.toString()))
                    prop.setDefaultValue(lr)
                }
            }
            return prop
        }

        // Create design parameters
        createValProp(antiTorqueBlock, "drivetrainType", stringType, "Electric")
        createValProp(antiTorqueBlock, "materialType", stringType, "Composite")
        createValProp(antiTorqueBlock, "batteryTech", stringType, "Solid-State")
        createValProp(antiTorqueBlock, "cabinStyle", stringType, "Enclosed")
        createValProp(antiTorqueBlock, "payloadMass", realType, 150.0)
        createValProp(antiTorqueBlock, "designLifeYears", realType, 40.0)

        // Create rollups (TPMs/MOPs)
        createValProp(antiTorqueBlock, "totalMass", realType, 0.0)
        createValProp(antiTorqueBlock, "totalAcquisitionCost", realType, 0.0)
        createValProp(antiTorqueBlock, "lifecycleCost", realType, 0.0)
        createValProp(antiTorqueBlock, "systemMTBF", realType, 0.0)
        createValProp(antiTorqueBlock, "downtimeRatio", realType, 0.0)
        createValProp(antiTorqueBlock, "probabilityOfFailure", realType, 0.0)
        createValProp(antiTorqueBlock, "lawsuitExposureRisk", realType, 0.0)
        createValProp(antiTorqueBlock, "tradeWeightedScore", realType, 0.0)

        // 5. Create Operations Hierarchy with Parameters (Inputs & Outputs)
        def createOperation = { Class ownerBlock, String opName, List<Map> params ->
            Operation op = factory.createOperationInstance()
            op.setName(opName)
            op.setOwner(ownerBlock)
            
            params.each { p ->
                Parameter param = factory.createParameterInstance()
                param.setName(p.name)
                param.setType(p.type)
                param.setDirection(p.dir)
                param.setOwner(op)
            }
            logger.info("Created Operation: " + opName + " under " + ownerBlock.getName())
            return op
        }

        // Modern Bell 47 Operations
        createOperation(helicopterBlock, "ExecuteFlightMission", [
            [name: "FlightPlan", type: stringType, dir: ParameterDirectionKindEnum.IN],
            [name: "PilotCommands", type: stringType, dir: ParameterDirectionKindEnum.IN],
            [name: "VehicleTrajectory", type: stringType, dir: ParameterDirectionKindEnum.OUT],
            [name: "MissionStatus", type: stringType, dir: ParameterDirectionKindEnum.OUT]
        ])
        createOperation(helicopterBlock, "PerformEmergencyLanding", [
            [name: "EngineFailureSignal", type: realType, dir: ParameterDirectionKindEnum.IN],
            [name: "Altitude", type: realType, dir: ParameterDirectionKindEnum.IN],
            [name: "LandingStatus", type: stringType, dir: ParameterDirectionKindEnum.OUT]
        ])

        // Subsystem Operations
        createOperation(antiTorqueBlock, "CounterMainRotorTorque", [
            [name: "MainRotorTorque", type: realType, dir: ParameterDirectionKindEnum.IN],
            [name: "YawCommand", type: realType, dir: ParameterDirectionKindEnum.IN],
            [name: "CounterThrust", type: realType, dir: ParameterDirectionKindEnum.OUT],
            [name: "AntiTorquePowerConsumed", type: realType, dir: ParameterDirectionKindEnum.OUT]
        ])

        createOperation(epsBlock, "RegulateBackupPower", [
            [name: "EngineStatus", type: stringType, dir: ParameterDirectionKindEnum.IN],
            [name: "BatterySOC", type: realType, dir: ParameterDirectionKindEnum.IN],
            [name: "BackupVoltage", type: realType, dir: ParameterDirectionKindEnum.OUT],
            [name: "BackupCurrent", type: realType, dir: ParameterDirectionKindEnum.OUT]
        ])

        createOperation(powertrainBlock, "TransmitTorque", [
            [name: "EngineTorque", type: realType, dir: ParameterDirectionKindEnum.IN],
            [name: "MainRotorRPM", type: realType, dir: ParameterDirectionKindEnum.OUT],
            [name: "TailRotorRPM", type: realType, dir: ParameterDirectionKindEnum.OUT]
        ])

        // 6. State Machines (System & EPS Subsystem)
        def createStateMachine = { Class ownerBlock, String smName, List<String> statesList, List<List<String>> transitionsList ->
            StateMachine sm = factory.createStateMachineInstance()
            sm.setName(smName)
            sm.setOwner(ownerBlock)
            
            Region region = factory.createRegionInstance()
            region.setOwner(sm)
            
            Pseudostate init = factory.createPseudostateInstance()
            init.setKind(PseudostateKindEnum.INITIAL)
            init.setOwner(region)
            
            Map<String, State> states = [:]
            statesList.each { sName ->
                State s = factory.createStateInstance()
                s.setName(sName)
                s.setOwner(region)
                states[sName] = s
            }
            
            if (statesList.size() > 0) {
                Transition initTrans = factory.createTransitionInstance()
                initTrans.setSource(init)
                initTrans.setTarget(states[statesList[0]])
                initTrans.setOwner(region)
            }
            
            transitionsList.each { t ->
                State src = states[t[0]]
                State tgt = states[t[1]]
                if (src != null && tgt != null) {
                    Transition trans = factory.createTransitionInstance()
                    trans.setSource(src)
                    trans.setTarget(tgt)
                    trans.setOwner(region)
                }
            }
            logger.info("Created State Machine: " + smName + " under " + ownerBlock.getName())
            return sm
        }

        // State Machine Diagram Populator Helper
        def drawSMDiagram = { StateMachine sm, String name ->
            try {
                com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Diagram diagram = ModelElementsManager.getInstance().createDiagram("SysML State Machine Diagram", sm)
                diagram.setName(name)
                DiagramPresentationElement diagramPE = project.getDiagram(diagram)
                diagramsToLayout.add(diagramPE)
                
                def shapeMap = [:]
                for (Region r : sm.getRegion()) {
                    for (Element vertex : r.getSubvertex()) {
                        ShapeElement shape = PresentationElementsManager.getInstance().createShapeElement(vertex, diagramPE)
                        shapeMap[vertex] = shape
                    }
                    for (Transition t : r.getTransition()) {
                        def sourceShape = shapeMap[t.getSource()]
                        def targetShape = shapeMap[t.getTarget()]
                        if (sourceShape != null && targetShape != null) {
                            PresentationElementsManager.getInstance().createPathElement(t, sourceShape, targetShape)
                        }
                    }
                }
                logger.info("  Created and populated State Machine Diagram: " + name)
            } catch (Exception e) {
                logger.error("Failed to create State Machine Diagram: " + name, e)
            }
        }

        // System state machine
        def systemSM = createStateMachine(helicopterBlock, "System State Machine",
            ["Off", "PreFlightCheck", "Starting", "NormalFlight", "EmergencyAutorotation", "Shutdown"],
            [
                ["Off", "PreFlightCheck"],
                ["PreFlightCheck", "Starting"],
                ["Starting", "NormalFlight"],
                ["NormalFlight", "EmergencyAutorotation"],
                ["EmergencyAutorotation", "Shutdown"],
                ["Shutdown", "Off"]
            ]
        )
        drawSMDiagram(systemSM, "System State Machine Diagram")

        // EPS state machine
        def epsSM = createStateMachine(epsBlock, "EPS State Machine",
            ["Standby", "Charging", "ActiveDischarge", "EmergencyBackup", "ThermalRunawayAlarm"],
            [
                ["Standby", "Charging"],
                ["Charging", "ActiveDischarge"],
                ["ActiveDischarge", "EmergencyBackup"],
                ["EmergencyBackup", "ThermalRunawayAlarm"],
                ["ThermalRunawayAlarm", "Standby"]
            ]
        )
        drawSMDiagram(epsSM, "EPS State Machine Diagram")

        // Engine state machine
        def engineSM = createStateMachine(engineBlock, "Engine State Machine",
            ["Off", "Cranking", "Running", "Failed"],
            [
                ["Off", "Cranking"],
                ["Cranking", "Running"],
                ["Running", "Failed"],
                ["Failed", "Off"]
            ]
        )
        drawSMDiagram(engineSM, "Engine State Machine Diagram")

        // Rotor state machine
        def rotorSM = createStateMachine(mainRotorBlock, "Rotor State Machine",
            ["Stationary", "SpoolingUp", "Engaged", "Decelerating"],
            [
                ["Stationary", "SpoolingUp"],
                ["SpoolingUp", "Engaged"],
                ["Engaged", "Decelerating"],
                ["Decelerating", "Stationary"]
            ]
        )
        drawSMDiagram(rotorSM, "Rotor State Machine Diagram")

        // FCS state machine
        def fcsSM = createStateMachine(flightControllerBlock, "FCS State Machine",
            ["Off", "SelfTest", "Active", "FailSafe"],
            [
                ["Off", "SelfTest"],
                ["SelfTest", "Active"],
                ["Active", "FailSafe"],
                ["FailSafe", "Off"]
            ]
        )
        drawSMDiagram(fcsSM, "FCS State Machine Diagram")

        // 7. Physical Components & Generalization Alternatives
        Package physicalAlternativesPkg = factory.createPackageInstance()
        physicalAlternativesPkg.setName("Alternative Components")
        physicalAlternativesPkg.setOwner(physicalPkg)

        // Base blocks
        Class baseBattery = factory.createClassInstance()
        baseBattery.setName("Physical Battery Pack")
        baseBattery.setOwner(physicalPkg)
        applyStereotypeSafe(baseBattery, blockStereotype)

        Class baseDrivetrain = factory.createClassInstance()
        baseDrivetrain.setName("Drivetrain Component")
        baseDrivetrain.setOwner(physicalPkg)
        applyStereotypeSafe(baseDrivetrain, blockStereotype)

        // Alternatives with generalization
        def createAlternative = { Class parentBlock, String altName ->
            Class alt = factory.createClassInstance()
            alt.setName(altName)
            alt.setOwner(physicalAlternativesPkg)
            applyStereotypeSafe(alt, blockStereotype)
            
            Generalization gen = factory.createGeneralizationInstance()
            gen.setGeneral(parentBlock)
            gen.setSpecific(alt)
            gen.setOwner(alt)
            logger.info("Created Alternative: " + altName + " -> " + parentBlock.getName())
            return alt
        }

        createAlternative(baseBattery, "CATL NMC Lithium-Ion Pack")
        createAlternative(baseBattery, "Advanced Solid-State Pack")
        createAlternative(baseDrivetrain, "Traditional Aluminum Driveshafts")
        createAlternative(baseDrivetrain, "Composite Carbon-Fiber Driveshafts")

        // 8. Traceability (Satisfy Relationships)
        Stereotype satisfyStereotype = StereotypesHelper.getStereotype(project, "Satisfy", sysmlProfile)
        
        def createSatisfyRelationship = { Element client, Element supplier ->
            Dependency dep = factory.createDependencyInstance()
            dep.setOwner(logicalPkg)
            dep.getClient().add(client)
            dep.getSupplier().add(supplier)
            applyStereotypeSafe(dep, satisfyStereotype)
            logger.info("Created Satisfy Relationship: " + client.getName() + " -> " + supplier.getName())
            return dep
        }
        
        createSatisfyRelationship(helicopterBlock, reqCap)
        createSatisfyRelationship(helicopterBlock, reqEngine)
        createSatisfyRelationship(antiTorqueBlock, reqSafety)
        createSatisfyRelationship(antiTorqueBlock, reqFatigue)
        createSatisfyRelationship(antiTorqueBlock, reqLubrication)
        createSatisfyRelationship(epsBlock, reqElecBackup)
        createSatisfyRelationship(epsBlock, reqThermal)
        createSatisfyRelationship(epsBlock, reqRedundantPower)
        createSatisfyRelationship(epsBlock, reqEps)
        createSatisfyRelationship(flightControllerBlock, reqFcs)
        createSatisfyRelationship(powertrainBlock, reqClutch)
        createSatisfyRelationship(powertrainBlock, reqPwr)
        createSatisfyRelationship(powertrainBlock, reqShielding)

        // 9. Interface Blocks & Flow Properties
        Stereotype interfaceBlockStereotype = StereotypesHelper.getStereotype(project, "InterfaceBlock", sysmlProfile)
        Stereotype flowPropStereotype = StereotypesHelper.getStereotype(project, "FlowProperty", sysmlProfile)
        
        def createInterfaceBlock = { String name, String flowPropName, Classifier type ->
            Class ib = factory.createClassInstance()
            ib.setName(name)
            ib.setOwner(logicalPkg)
            applyStereotypeSafe(ib, interfaceBlockStereotype)
            
            Property fp = factory.createPropertyInstance()
            fp.setName(flowPropName)
            fp.setType(type)
            fp.setOwner(ib)
            applyStereotypeSafe(fp, flowPropStereotype)
            
            logger.info("Created Interface Block: " + name + " with Flow Property " + flowPropName)
            return ib
        }
        
        Class powerFlowIB = createInterfaceBlock("PowerFlow", "torque", realType)
        Class electricalFlowIB = createInterfaceBlock("ElectricalFlow", "current", realType)
        Class controlSignalIB = createInterfaceBlock("ControlSignal", "yawCommand", realType)

        // 10. Proxy Ports on logical blocks
        Stereotype proxyPortStereotype = StereotypesHelper.getStereotype(project, "ProxyPort", sysmlProfile)
        
        def createProxyPort = { Class block, String name, Class typeIB ->
            com.nomagic.uml2.ext.magicdraw.compositestructures.mdports.Port port = factory.createPortInstance()
            port.setName(name)
            port.setType(typeIB)
            block.getOwnedAttribute().add(port)
            applyStereotypeSafe(port, proxyPortStereotype)
            logger.info("  Created ProxyPort: " + name + " on " + block.getName())
            return port
        }
        
        def port_engine_out = createProxyPort(engineBlock, "p_out_shaft", powerFlowIB)
        def port_pwr_in = createProxyPort(powertrainBlock, "p_in_shaft", powerFlowIB)
        def port_pwr_out_main = createProxyPort(powertrainBlock, "p_out_main_rotor", powerFlowIB)
        def port_pwr_out_tail = createProxyPort(powertrainBlock, "p_out_tail_rotor", powerFlowIB)
        def port_eps_in = createProxyPort(epsBlock, "p_in_mech", powerFlowIB)
        def port_eps_out = createProxyPort(epsBlock, "p_out_elec", electricalFlowIB)
        def port_fcs_out = createProxyPort(flightControllerBlock, "p_out_cmd", controlSignalIB)
        def port_at_in_elec = createProxyPort(antiTorqueBlock, "p_in_elec", electricalFlowIB)
        def port_at_in_mech = createProxyPort(antiTorqueBlock, "p_in_mech", powerFlowIB)
        def port_at_in_cmd = createProxyPort(antiTorqueBlock, "p_in_cmd", controlSignalIB)

        // 11. Port Connectors
        def findPartProperty = { Class block, String partName ->
            for (Property p : block.getOwnedAttribute()) {
                if (p.getName().equalsIgnoreCase(partName)) {
                    return p
                }
            }
            return null
        }
        
        Property partEngine = findPartProperty(helicopterBlock, "engine")
        Property partPowertrain = findPartProperty(helicopterBlock, "powertrainsubsystem")
        Property partEps = findPartProperty(helicopterBlock, "electricalpowersubsystem")
        Property partFcs = findPartProperty(helicopterBlock, "flightcontroller")
        Property partAntiTorque = findPartProperty(helicopterBlock, "anti-torquesubsystem")

        def createPortConnector = { Class ownerBlock, Property part1, com.nomagic.uml2.ext.magicdraw.compositestructures.mdports.Port port1, Property part2, com.nomagic.uml2.ext.magicdraw.compositestructures.mdports.Port port2, String connName ->
            Connector conn = factory.createConnectorInstance()
            conn.setName(connName)
            ownerBlock.getOwnedConnector().add(conn)
            
            ConnectorEnd end1 = factory.createConnectorEndInstance()
            end1.setRole(port1)
            end1.setPartWithPort(part1)
            conn.getEnd().add(end1)
            
            ConnectorEnd end2 = factory.createConnectorEndInstance()
            end2.setRole(port2)
            end2.setPartWithPort(part2)
            conn.getEnd().add(end2)
            
            logger.info("Created Port Connector: " + connName + " between " + part1.getName() + "." + port1.getName() + " and " + part2.getName() + "." + port2.getName())
            return conn
        }
        
        if (partEngine != null && partPowertrain != null) {
            createPortConnector(helicopterBlock, partEngine, port_engine_out, partPowertrain, port_pwr_in, "conn_engine_pwr")
        }
        if (partPowertrain != null && partEps != null) {
            createPortConnector(helicopterBlock, partPowertrain, port_pwr_out_tail, partEps, port_eps_in, "conn_pwr_generator")
        }
        if (partEps != null && partAntiTorque != null) {
            createPortConnector(helicopterBlock, partEps, port_eps_out, partAntiTorque, port_at_in_elec, "conn_elec_torque")
        }
        if (partFcs != null && partAntiTorque != null) {
            createPortConnector(helicopterBlock, partFcs, port_fcs_out, partAntiTorque, port_at_in_cmd, "conn_fcs_cmd")
        }

        // 12. Constraint Block (Parametric Trade Study Formula)
        Stereotype constraintBlockStereotype = StereotypesHelper.getStereotype(project, "ConstraintBlock", sysmlProfile)
        
        Class constraintBlock = factory.createClassInstance()
        constraintBlock.setName("TradeStudyCalculations")
        constraintBlock.setOwner(logicalPkg)
        applyStereotypeSafe(constraintBlock, constraintBlockStereotype)
        
        createValProp(constraintBlock, "dtMass", realType, 0.0)
        createValProp(constraintBlock, "matMassMult", realType, 0.0)
        createValProp(constraintBlock, "cabMassMod", realType, 0.0)
        createValProp(constraintBlock, "batMassMod", realType, 0.0)
        createValProp(constraintBlock, "totalMass", realType, 0.0)
        
        com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Constraint constr = factory.createConstraintInstance()
        constr.setName("mass_eq")
        constraintBlock.getOwnedRule().add(constr)
        
        com.nomagic.uml2.ext.magicdraw.classes.mdkernel.OpaqueExpression expr = factory.createOpaqueExpressionInstance()
        expr.getBody().add("totalMass = (dtMass * matMassMult) + cabMassMod + batMassMod")
        constr.setSpecification(expr)
        logger.info("Created ConstraintBlock: TradeStudyCalculations with constraint mass_eq")

        // 13. Simulation Config for CST Simulation Engine
        Package behaviorPkg = factory.createPackageInstance()
        behaviorPkg.setName("Behavior Models")
        behaviorPkg.setOwner(logicalPkg)

        Class simConfig = factory.createClassInstance()
        simConfig.setName("ModernBell47SimulationConfig")
        simConfig.setOwner(behaviorPkg)
        applyStereotypeSafe(simConfig, simConfigStereotype)

        if (simConfigStereotype != null) {
            StereotypesHelper.setStereotypePropertyValue(simConfig, simConfigStereotype, "executionTarget", helicopterBlock)
            StereotypesHelper.setStereotypePropertyValue(simConfig, simConfigStereotype, "silent", true)
            logger.info("Created Cameo Simulation Toolkit SimulationConfig pointing to Modern Bell 47")
        } else {
            logger.warn("SimulationConfig stereotype not found, creating simulation placeholder block.")
        }

        // Create Simulation Configuration Diagram
        try {
            com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Diagram simDiag = null
            try {
                simDiag = ModelElementsManager.getInstance().createDiagram("Simulation Config Diagram", behaviorPkg)
                logger.info("Created Simulation Config Diagram successfully.")
            } catch (Exception e) {
                logger.warn("Could not create Simulation Config Diagram via name 'Simulation Config Diagram', trying fallback 'Simulation Config'")
                try {
                    simDiag = ModelElementsManager.getInstance().createDiagram("Simulation Config", behaviorPkg)
                    logger.info("Created Simulation Config Diagram via fallback name.")
                } catch (Exception e2) {
                    logger.error("Could not create simulation diagram via standard names. Creating a standard Class Diagram as placeholder.", e2)
                    simDiag = ModelElementsManager.getInstance().createDiagram(DiagramTypeConstants.UML_CLASS_DIAGRAM, behaviorPkg)
                }
            }
            
            if (simDiag != null) {
                simDiag.setName("Modern Bell 47 Simulation Configuration Diagram")
                DiagramPresentationElement simDiagPE = project.getDiagram(simDiag)
                diagramsToLayout.add(simDiagPE)
                
                // Show the simConfig block and the helicopterBlock on this diagram
                PresentationElementsManager.getInstance().createShapeElement(simConfig, simDiagPE)
                PresentationElementsManager.getInstance().createShapeElement(helicopterBlock, simDiagPE)
                logger.info("  Created and populated Simulation Configuration Diagram.")
            }
        } catch (Exception e) {
            logger.error("Failed to create Simulation Configuration Diagram", e)
        }

        if (sessionCreated) {
            SessionManager.getInstance().closeSession(project)
            sessionCreated = false
            logger.info("Advanced Model population complete and session closed successfully.")
        }

        // 14. Layout Diagrams outside the session
        logger.info("Layouting diagrams...")
        for (def diagPE : diagramsToLayout) {
            try {
                diagPE.open()
                com.nomagic.magicdraw.uml.symbols.layout.Layouting.layout(diagPE)
                logger.info("Successfully laid out diagram: " + diagPE.getDiagram().getName())
            } catch (Exception e) {
                logger.warn("Could not layout diagram " + diagPE.getDiagram().getName() + ": " + e.message)
            }
        }

    } catch (Exception e) {
        if (sessionCreated) {
            SessionManager.getInstance().cancelSession(project)
        }
        logger.error("Error occurred during model modifications. Reverted session.", e)
        throw e
    }

    // 9. Static Trade Study Calculations & Print Table
    logger.info('---------------------------------------------------------------------------------')
    logger.info('            FULL-FIDELITY PARAMETRIC TRADE STUDY EVALUATION MATRIX               ')
    logger.info('---------------------------------------------------------------------------------')
    logger.info('| Config | Drivetrain | Material | Battery | Cabin | Payload | Life (yr) | Mass (kg) | Cost ($) | MTBF (hr) | P_fail | Lawsuit ($) | LifeCost ($) | Score |')
    logger.info('|--------|------------|----------|---------|-------|---------|-----------|-----------|----------|-----------|--------|-------------|--------------|-------|')

    def runParametricTrade = { String cfgName, String dtType, String matType, String batType, String cabStyle, double payload, double designLife ->
        double dtMass = (dtType == "Mechanical") ? 55.0 : 62.0
        double dtCost = (dtType == "Mechanical") ? 25000.0 : 32000.0
        double dtMtbf = (dtType == "Mechanical") ? 1500.0 : 5000.0
        double mmhFh = (dtType == "Mechanical") ? 0.25 : 0.05

        double matMassMult = (matType == "Composite") ? 0.70 : 1.0
        double matCostMult = (matType == "Composite") ? 1.80 : 1.0
        double matMtbfMult = (matType == "Composite") ? 3.0 : 1.0
        double matMmhFhMult = (matType == "Composite") ? 0.50 : 1.0

        double cabMassMod = (cabStyle == "Enclosed") ? 40.0 : 10.0
        
        double batMassMod = 0.0
        double batCostMod = 0.0
        if (dtType == "Electric") {
            if (batType == "Solid-State") {
                batMassMod = -3.6
                batCostMod = 2300.0
            }
        }

        double totalMass = (dtMass * matMassMult) + cabMassMod + batMassMod
        double totalAcqCost = (dtCost * matCostMult) + batCostMod
        double finalMtbf = dtMtbf * matMtbfMult
        double finalMmhFh = mmhFh * matMmhFhMult

        double totalHours = designLife * 300.0
        double pFail = 1.0 - Math.exp(-totalHours / finalMtbf)

        double hazardMultiplier = 1.0
        if (dtType == "Electric") {
            hazardMultiplier = (batType == "Solid-State") ? 0.03 : 0.3
        }
        double lawsuitRisk = pFail * 2000000.0 * hazardMultiplier
        double lifecycleCost = totalAcqCost + (totalHours * finalMmhFh * 100.0) + lawsuitRisk

        double scoreMass = Math.max(0.0, 100.0 - (totalMass * 0.8))
        double scoreCost = Math.max(0.0, 100.0 - (lifecycleCost / 1500.0))
        double scoreMtbf = Math.min(100.0, (finalMtbf / 150.0))
        double scoreSafety = (dtType == "Electric") ? 100.0 : 20.0
        
        double weightedScore = (scoreMass * 0.2) + (scoreCost * 0.3) + (scoreMtbf * 0.2) + (scoreSafety * 0.3)

        logger.info(String.format("| %-6s | %-10s | %-8s | %-7s | %-5s | %-7.1f | %-9.1f | %-9.1f | %-8.0f | %-9.0f | %-6.4f | %-11.0f | %-12.0f | %-5.1f |",
            cfgName, dtType, matType, batType, cabStyle, payload, designLife, totalMass, totalAcqCost, finalMtbf, pFail, lawsuitRisk, lifecycleCost, weightedScore))
    }

    runParametricTrade("CFG-1", "Mechanical", "Aluminum", "N/A", "Enclosed", 150.0, 10.0)
    runParametricTrade("CFG-2", "Mechanical", "Aluminum", "N/A", "Enclosed", 150.0, 40.0)
    runParametricTrade("CFG-3", "Mechanical", "Composite", "N/A", "Enclosed", 150.0, 40.0)
    runParametricTrade("CFG-4", "Electric", "Aluminum", "Lithium-Ion", "Enclosed", 150.0, 10.0)
    runParametricTrade("CFG-5", "Electric", "Aluminum", "Lithium-Ion", "Enclosed", 150.0, 40.0)
    runParametricTrade("CFG-6", "Electric", "Composite", "Solid-State", "Enclosed", 150.0, 40.0)
    runParametricTrade("CFG-7", "Electric", "Composite", "Solid-State", "Utility", 150.0, 40.0)

    logger.info('---------------------------------------------------------------------------------')
    logger.info('Parametric Trade Study analysis complete.')

} catch (Throwable t) {
    logger.error("Critical failure during script execution", t)
}
