import com.dassault_systemes.modeler.kerml.model.kerml.Namespace
import com.dassault_systemes.modeler.kerml.model.kerml.Element
import com.dassault_systemes.modeler.sysml.model.sysml.RequirementUsage
import com.dassault_systemes.modeler.sysml.model.sysml.SatisfyRequirementUsage
import com.dassault_systemes.modeler.kerml.model.kerml.Feature
import com.nomagic.magicdraw.core.Application
import com.nomagic.magicdraw.core.Project
import com.nomagic.magicdraw.openapi.uml.SessionManager

import javax.swing.*
import java.awt.*
import java.awt.geom.AffineTransform
import java.util.List
import java.util.Set
import java.util.HashSet
import java.util.Map
import java.util.HashMap
import java.util.ArrayList

// -----------------------------------------------------------------------------------
// TUTORIAL TWO: SysMLv2 Satisfy Matrix (Version 2)
// 
// This script demonstrates generating a GUI Satisfy Matrix finding all 
// requirements and elements, crossing their SatisfyRequirementUsage relationships
// (both direct and implied), using custom Java2D Graphics rendering on a JPanel canvas.
// Version 2 includes robust error handling on the EDT and improved visualizations.
// -----------------------------------------------------------------------------------

// Assume SysMLv2Logger.groovy is in a relative path "scripts", or prompt user
File loggerFile = new File("scripts", "SysMLv2Logger.groovy")
if (!loggerFile.exists()) {
    JFileChooser chooser = new JFileChooser()
    chooser.setDialogTitle("Select SysMLv2Logger.groovy")
    if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
        loggerFile = chooser.getSelectedFile()
    } else {
        println("User canceled logger selection")
        return
    }
}
def SysMLv2Logger = new GroovyClassLoader(getClass().getClassLoader()).parseClass(loggerFile)

// Updated log target relative to the chosen logger file or user home
File logDir = loggerFile.getParentFile() != null ? new File(loggerFile.getParentFile().getParentFile(), "logs") : new File(System.getProperty("user.dir"), "logs")
File runLog = new File(logDir, "SatisfyMatrix.log")
runLog.getParentFile().mkdirs()
def logger = SysMLv2Logger.newInstance("SatisfyMatrixGUI", runLog)

try {
    Project project = Application.getInstance().getProject()
    if (project == null) {
        logger.error("No active MagicDraw project!")
        return
    }

    def browser = Application.getInstance().getMainFrame().getBrowser()
    def selectedNodes = browser.getActiveTree()?.getSelectedNodes()
    Namespace namespace = null
    
    if (selectedNodes != null && selectedNodes.length > 0) {
        def element = selectedNodes[0].getUserObject()
        if (element instanceof Namespace) {
            namespace = (Namespace) element
        }
    }
    
    if (namespace == null) {
        logger.error("Please select a Namespace in the Containment Tree before running this script.")
        return
    }

    logger.info("--- Generating GUI satisfy matrix for : " + namespace.getHumanName() + " ---")

    Set<RequirementUsage> allRequirements = new HashSet<RequirementUsage>()
    Set<Element> satisfyingElements = new HashSet<Element>()
    
    // map of Satisfying Element -> (Requirement -> Direct Satisfy element)
    Map<Element, Map<RequirementUsage, SatisfyRequirementUsage>> directSatisfyMap = new HashMap<>()
    
    // map of Satisfying Element -> Set of Requirements it implies satisfaction of
    Map<Element, Set<RequirementUsage>> impliedSatisfyMap = new HashMap<>()

    boolean showImplied = true

    def analyzeElements
    analyzeElements = { Element currentElement ->
        if (currentElement instanceof RequirementUsage && !(currentElement instanceof SatisfyRequirementUsage)) {
            allRequirements.add((RequirementUsage) currentElement)
        }
        
        if (currentElement instanceof SatisfyRequirementUsage) {
            SatisfyRequirementUsage satisfy = (SatisfyRequirementUsage) currentElement
            def satisfiedReq = satisfy.getSatisfiedRequirement()
            
            Element satisfyingFeature = satisfy.getSatisfyingFeature()
            if (satisfyingFeature == null) {
                satisfyingFeature = satisfy.getOwner()
            }
            
            if (satisfyingFeature != null && satisfiedReq != null) {
                satisfyingElements.add(satisfyingFeature)
                
                Map<RequirementUsage, SatisfyRequirementUsage> reqs = directSatisfyMap.get(satisfyingFeature)
                if (reqs == null) {
                    reqs = new HashMap<RequirementUsage, SatisfyRequirementUsage>()
                    directSatisfyMap.put(satisfyingFeature, reqs)
                }
                reqs.put((RequirementUsage)satisfiedReq, satisfy)
            }
        }
        for (Element child : currentElement.getOwnedElement()) {
            analyzeElements(child)
        }
    }

    // Start analyzing from the selected namespace
    analyzeElements(namespace)
    
    // Calculate implied satisfy
    if (showImplied) {
        for (Map.Entry<Element, Map<RequirementUsage, SatisfyRequirementUsage>> entry : directSatisfyMap.entrySet()) {
            Element satisfier = entry.getKey()
            for (RequirementUsage req : entry.getValue().keySet()) {
                Element parent = satisfier.getOwner()
                while (parent != null && parent.getID() != namespace.getOwner()?.getID()) {
                    if (parent instanceof Feature && !(parent instanceof RequirementUsage)) {
                        satisfyingElements.add(parent)
                        Set<RequirementUsage> impliedReqs = impliedSatisfyMap.get(parent)
                        if (impliedReqs == null) {
                            impliedReqs = new HashSet<RequirementUsage>()
                            impliedSatisfyMap.put(parent, impliedReqs)
                        }
                        impliedReqs.add(req)
                    }
                    parent = parent.getOwner()
                }
            }
        }
    }
    
    List<RequirementUsage> reqList = new ArrayList<>(allRequirements)
    reqList.sort { a, b -> (a.getName() ?: "Unnamed").compareToIgnoreCase(b.getName() ?: "Unnamed") }
    
    List<Element> elemList = new ArrayList<>(satisfyingElements)
    elemList.sort { a, b -> (a.getName() ?: "Unnamed").compareToIgnoreCase(b.getName() ?: "Unnamed") }
    
    if (reqList.isEmpty()) {
        logger.info("No Requirements found in the selected Namespace.")
        return
    }

    SwingUtilities.invokeLater(new Runnable() {
        public void run() {
            try {
                String title = "Satisfy Matrix: " + namespace.getHumanName()
                JDialog dialog = new JDialog(Application.getInstance().getMainFrame(), title, false)
                dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE)
                dialog.setLayout(new BorderLayout())

                JPanel topPanel = new JPanel(new BorderLayout())
                
                JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT))
                JCheckBox impliedCheckbox = new JCheckBox("Show Implied", true)
                controlPanel.add(impliedCheckbox)
                
                JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT))
                legendPanel.add(new JLabel("Legend: "))
                JLabel directLegend = new JLabel("Direct (↗)")
                directLegend.setForeground(new Color(16, 185, 129))
                legendPanel.add(directLegend)
                JLabel impliedLegend = new JLabel("Implied (↗)")
                impliedLegend.setForeground(new Color(59, 130, 246))
                legendPanel.add(impliedLegend)
                
                topPanel.add(controlPanel, BorderLayout.WEST)
                topPanel.add(legendPanel, BorderLayout.EAST)
                
                dialog.add(topPanel, BorderLayout.NORTH)

                JPanel canvas = new JPanel() {
                    int cellWidth = 30
                    int rowHeight = 25
                    int reqNameColWidth = 250
                    int headerHeight = 150 
                    
                    @Override
                    protected void paintComponent(Graphics g) {
                        try {
                            super.paintComponent(g)
                            Graphics2D g2 = (Graphics2D) g
                            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
                            g2.setFont(new Font("SansSerif", Font.PLAIN, 12))
                            FontMetrics fm = g2.getFontMetrics()
                        
                        int startX = reqNameColWidth
                        int startY = headerHeight
                        
                        boolean renderImplied = impliedCheckbox.isSelected()

                        // 1. Draw Column Headers (Top row, 45 degree angle)
                        g2.setColor(Color.BLACK)
                        for (int c = 0; c < reqList.size(); c++) {
                            String colName = reqList.get(c).getName() ?: "Unnamed"
                            
                            AffineTransform orig = g2.getTransform()
                            int cellCenterX = startX + (c * cellWidth) + (cellWidth / 2)
                            g2.translate(cellCenterX, startY - 5)
                            g2.rotate(-Math.PI / 4.0) // 45 degrees up-right
                            
                            g2.drawString(colName, 0, 0)
                            g2.setTransform(orig)
                            
                            g2.setColor(Color.LIGHT_GRAY)
                            g2.drawLine(startX + (c * cellWidth), startY, startX + (c * cellWidth), startY + (elemList.size() * rowHeight))
                            g2.setColor(Color.BLACK)
                        }
                        
                        g2.setColor(Color.LIGHT_GRAY)
                        g2.drawLine(startX + (reqList.size() * cellWidth), startY, startX + (reqList.size() * cellWidth), startY + (elemList.size() * rowHeight))
                        g2.setColor(Color.BLACK)
                        
                        // 2. Draw Rows (Satisfying Elements)
                        for (int r = 0; r < elemList.size(); r++) {
                            Element feat = elemList.get(r)
                            String rowName = feat.getName() ?: "Unnamed"
                            
                            int textY = startY + (r * rowHeight) + ((rowHeight - fm.getHeight()) / 2) + fm.getAscent()
                            
                            g2.setColor(Color.BLACK)
                            String drawName = rowName
                            if (fm.stringWidth(drawName) > reqNameColWidth - 10) {
                                while(drawName.length() > 3 && fm.stringWidth(drawName + "...") > reqNameColWidth - 10) {
                                    drawName = drawName.substring(0, drawName.length() - 1)
                                }
                                drawName += "..."
                            }
                            g2.drawString(drawName, 5, textY)
                            
                            g2.setColor(Color.LIGHT_GRAY)
                            g2.drawLine(0, startY + (r * rowHeight), startX + (reqList.size() * cellWidth), startY + (r * rowHeight))
                            
                            // Draw cells
                            for (int c = 0; c < reqList.size(); c++) {
                                RequirementUsage req = reqList.get(c)
                                
                                Map<RequirementUsage, SatisfyRequirementUsage> satisfied = directSatisfyMap.get(feat)
                                Set<RequirementUsage> implied = impliedSatisfyMap.get(feat)
                                
                                int cellX = startX + (c * cellWidth)
                                int cellY = startY + (r * rowHeight)
                                
                                boolean isDirect = (satisfied != null && satisfied.containsKey(req))
                                boolean isImplied = renderImplied && (implied != null && implied.contains(req))
                                
                                if (isDirect || isImplied) {
                                    if (isDirect) {
                                        g2.setColor(new Color(16, 185, 129)) // Green
                                    } else {
                                        g2.setColor(new Color(59, 130, 246)) // Blue
                                    }
                                    int cx = cellX + cellWidth / 2
                                    int cy = cellY + rowHeight / 2
                                    int arrowSize = 12
                                    int halfArrow = (int)(arrowSize / 2)
                                    
                                    java.awt.Stroke oldStroke = g2.getStroke()
                                    if (isDirect) {
                                        // Bolder arrow for direct
                                        g2.setStroke(new java.awt.BasicStroke(3.0f))
                                    } else {
                                        // Dashed-body arrow for implied
                                        g2.setStroke(new java.awt.BasicStroke(2.0f, java.awt.BasicStroke.CAP_BUTT, java.awt.BasicStroke.JOIN_MITER, 10.0f, (float[])[4.0f, 4.0f], 0.0f))
                                    }
                                    
                                    AffineTransform cellOrig = g2.getTransform()
                                    g2.translate(cx, cy)
                                    g2.rotate(-Math.PI / 4.0) // 45 degrees up-right (negative is up-right in Java2D)
                                    
                                    // Draw arrow stem pointing right (since we rotated)
                                    g2.drawLine(-halfArrow, 0, halfArrow, 0)
                                    
                                    // Draw arrowhead (solid lines)
                                    g2.setStroke(new java.awt.BasicStroke(isDirect ? 3.0f : 2.0f))
                                    g2.drawLine(halfArrow - 5, -4, halfArrow, 0)
                                    g2.drawLine(halfArrow - 5, 4, halfArrow, 0)
                                    
                                    g2.setTransform(cellOrig)
                                    g2.setStroke(oldStroke)
                                }
                            }
                        }
                        
                        g2.setColor(Color.LIGHT_GRAY)
                        g2.drawLine(0, startY + (elemList.size() * rowHeight), startX + (reqList.size() * cellWidth), startY + (elemList.size() * rowHeight))
                        } catch (Throwable t) {
                            logger.error("Error in paintComponent", t)
                        }
                    }
                    
                    @Override
                    public Dimension getPreferredSize() {
                        return new Dimension(reqNameColWidth + (reqList.size() * cellWidth) + 80, headerHeight + (elemList.size() * rowHeight) + 80)
                    }
                }
                
                impliedCheckbox.addActionListener { e -> 
                    try {
                        canvas.repaint()
                    } catch (Throwable t) {
                        logger.error("Error in ActionListener", t)
                    }
                }
                canvas.setBackground(Color.WHITE)
                
                canvas.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent e) {
                        try {
                            if (e.getClickCount() == 2) {
                                int x = e.getX()
                                int y = e.getY()
                            
                                int startX = canvas.reqNameColWidth
                                int startY = canvas.headerHeight
                                int cWidth = canvas.cellWidth
                                int rHeight = canvas.rowHeight
                                
                                def mainBrowser = Application.getInstance().getMainFrame().getBrowser()
                                
                                if (x >= startX && x <= startX + (reqList.size() * cWidth) && y >= startY && y <= startY + (elemList.size() * rHeight)) {
                                    int c = (x - startX) / cWidth
                                    int r = (y - startY) / rHeight
                                    if (c >= 0 && c < reqList.size() && r >= 0 && r < elemList.size()) {
                                        RequirementUsage req = reqList.get(c)
                                        Element feat = elemList.get(r)
                                        Map<RequirementUsage, SatisfyRequirementUsage> satisfied = directSatisfyMap.get(feat)
                                        if (satisfied != null && satisfied.containsKey(req)) {
                                            mainBrowser.getContainmentTree().openNode(satisfied.get(req))
                                        } else {
                                            mainBrowser.getContainmentTree().openNode(feat)
                                        }
                                    }
                                } else if (x >= 0 && x < startX && y >= startY && y <= startY + (elemList.size() * rHeight)) {
                                    int r = (y - startY) / rHeight
                                    if (r >= 0 && r < elemList.size()) {
                                        mainBrowser.getContainmentTree().openNode(elemList.get(r))
                                    }
                                } else if (x >= startX && x <= startX + (reqList.size() * cWidth) && y >= 0 && y < startY) {
                                    int c = (x - startX) / cWidth
                                    if (c >= 0 && c < reqList.size()) {
                                        mainBrowser.getContainmentTree().openNode(reqList.get(c))
                                    }
                                }
                            }
                        } catch (Throwable t) {
                            logger.error("Error in mouseClicked", t)
                        }
                    }
                })

                JScrollPane scrollPane = new JScrollPane(canvas)
                dialog.add(scrollPane, BorderLayout.CENTER)

                dialog.setSize(900, 700)
                dialog.setLocationRelativeTo(Application.getInstance().getMainFrame())
                dialog.setVisible(true)
            } catch (Throwable t) {
                logger.error("Error setting up GUI", t)
            }
        }
    })

    logger.info("--- GUI Generation Complete ---")

} catch (Throwable t) {
    logger.error("Critical failure during analysis", t)
}
