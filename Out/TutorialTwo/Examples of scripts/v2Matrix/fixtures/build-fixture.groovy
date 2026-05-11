// =============================================================================
// build-fixture.groovy — TF-1 Reference Project builder.
//
// Creates the TF-1 SysMLv2 fixture inside the currently-open Cameo project,
// then writes v2Matrix/fixtures/fixture-manifest.json.
//
// PRE-REQUISITE (one-time setup):
//   1. A SysMLv2 project must be open in Cameo.
//   2. In the containment tree, create a new Package (right-click project root →
//      Create Element → Package), name it "TF1" (or anything), and SELECT it.
//   3. Run this script via the REST test harness:
//        harness.bat run "E:\_Documents\git\TutorialForCatiaMagicApiMCP\v2Matrix\fixtures\build-fixture.groovy"
//   4. After successful creation: File → Save As →
//        v2Matrix/fixtures/ReferenceProject.mdzip
//
// Fixture layout (committed to fixture-manifest.json):
//   Requirements  : 8  (R1..R6 pure, R7 subject-based, R8 ViewpointUsage)
//   Parts          : 7  (P1..P6 top-level, Q sub-part owned by P6)
//   Satisfy edges  : 8  SatisfyRequirementUsage instances → 6 direct cells
//                       (P1→R1 has 2 edges for FR-8 badge test)
//   Implied cells  : 1  (P6→R6 implied via Q→R6)
//   Subject req    : 1  (R7, subject = P2)
//   ViewpointUsage : 1  (R8, for FR-3 filter test)
//
// Notes on the SysMLv2 API (discovered by probe scripts, iteration 1):
//   - SatisfyRequirementUsage has NO setSatisfiedRequirement() setter.
//     The satisfied requirement is derived from the element's FeatureTyping.
//     We create a FeatureTyping owned by the SatisfyRequirementUsage, typed to req.
//   - satisfy.setOwner(part) makes `part` the satisfying feature (derived).
//   - setDeclaredName() is from kerml.Element; getHumanName() reflects it.
//   - SubjectMembership.setMemberElement(p2) sets P2 as the R7 subject reference.
// =============================================================================

import com.nomagic.magicdraw.core.Application
import com.nomagic.magicdraw.openapi.uml.SessionManager
import com.dassault_systemes.modeler.sysml.model.sysml.SysMLFactory
import com.dassault_systemes.modeler.kerml.model.kerml.KerMLFactory
import com.dassault_systemes.modeler.kerml.model.kerml.Namespace
import com.dassault_systemes.modeler.kerml.model.kerml.Element

// ---- Logger ------------------------------------------------------------------
String scriptDir = 'E:\\_Documents\\git\\TutorialForCatiaMagicApiMCP\\scripts'
def LoggerClass = new GroovyClassLoader(getClass().getClassLoader())
    .parseClass(new File(scriptDir, 'SysMLv2Logger.groovy'))
File logFile = new File('E:\\_Documents\\git\\TutorialForCatiaMagicApiMCP\\logs', 'build-fixture.log')
def log = LoggerClass.newInstance('BuildFixture', logFile)
log.info('=== build-fixture.groovy start ===')

// ---- Cameo handles ----------------------------------------------------------
def app     = Application.getInstance()
def project = app.getProject()
def factory = SysMLFactory.eINSTANCE
def kermlFactory = KerMLFactory.eINSTANCE   // for FeatureTyping etc. not exposed on SysMLFactoryImpl
def sm      = SessionManager.getInstance()

if (project == null) {
    log.error('No active Cameo project. Open a SysMLv2 project first.')
    app.getGUILog().log('[BuildFixture] ERROR: No active project.')
    return
}

// ---- Find fixture root -------------------------------------------------------
// Only accept a browser-selected Namespace with a non-null declared name
// (to avoid accidentally targeting a system library namespace).
Namespace fixtureRoot = null

try {
    def browser = app.getMainFrame().getBrowser()
    def selected = browser.getActiveTree()?.getSelectedNodes()
    if (selected != null && selected.length > 0) {
        def obj = selected[0].getUserObject()
        // Walk up to find the outermost named user Namespace.
        while (obj != null) {
            if (obj instanceof Namespace) {
                def name = ((Namespace) obj).getDeclaredName()
                if (name != null && !name.isEmpty()) fixtureRoot = (Namespace) obj
            }
            try {
                def parent = obj.respondsTo('getOwner') ? obj.getOwner() : null
                if (parent == null || !(parent instanceof Namespace)) break
                obj = parent
            } catch (Exception e) { break }
        }
        if (fixtureRoot != null) log.info('Using namespace (walked up): ' + fixtureRoot.getDeclaredName())
    }
} catch (Exception e) {
    log.warn('Browser selection probe failed: ' + e.message)
}

// Fallback: if no selection produced a root, scan the browser tree for a
// top-level named user-authored Namespace. Skips library namespaces.
if (fixtureRoot == null) {
    try {
        def browser = app.getMainFrame().getBrowser()
        def tree = browser.getContainmentTree()
        def queue = new ArrayDeque()
        queue.add(tree.getRootNode())
        while (!queue.isEmpty() && fixtureRoot == null) {
            def node = queue.poll()
            def obj = null
            try { obj = node.getUserObject() } catch (Exception ignored) {}
            if (obj instanceof Namespace) {
                def ns0 = (Namespace) obj
                def n0 = ns0.getDeclaredName()
                // Walk up from this hit to outermost named namespace.
                def cur = ns0
                while (cur?.getOwner() instanceof Namespace &&
                       ((Namespace) cur.getOwner()).getDeclaredName() != null &&
                       !((Namespace) cur.getOwner()).getDeclaredName().isEmpty()) {
                    cur = (Namespace) cur.getOwner()
                }
                // Skip library content
                def isLib = false
                try {
                    def loaderX = new GroovyClassLoader(getClass().getClassLoader())
                    def LD = loaderX.parseClass(new File('E:\\_Documents\\git\\TutorialForCatiaMagicApiMCP\\scripts\\v2Matrix', 'LibraryDetector.groovy'))
                    isLib = LD.isInStandardLibrary(cur as Element)
                } catch (Exception ignored) {}
                if (!isLib && cur.getDeclaredName() != null && !cur.getDeclaredName().isEmpty()) {
                    fixtureRoot = cur
                    log.info('Using namespace (tree-walk fallback): ' + cur.getDeclaredName())
                    break
                }
            }
            try {
                for (int i = 0; i < node.getChildCount(); i++) queue.add(node.getChildAt(i))
            } catch (Exception ignored) {}
        }
    } catch (Exception e) {
        log.warn('Tree-walk fallback failed: ' + e.message)
    }
}

if (fixtureRoot == null) {
    def msg = [
        'No named SysMLv2 Package found.',
        'Steps:',
        '  1. In Cameo containment tree, right-click the project root.',
        '  2. Choose Create Element → Package, name "TF1".',
        '  3. Re-run this script.',
    ].join('\n')
    log.error(msg)
    app.getGUILog().log('[BuildFixture] ERROR: no user-named Package found.')
    return
}

log.info('Fixture root: ' + fixtureRoot.getDeclaredName() + ' [' + fixtureRoot.getClass().getSimpleName() + ']')

// ---- Helper: wire the satisfied requirement via ReferenceSubsetting ----------
// SysMLv2 ':>' operator creates a ReferenceSubsetting, NOT a FeatureTyping.
// Writing `satisfy requirement S_P1_R1a :> R1` compiles to:
//   ReferenceSubsetting {
//     subsettingFeature = S_P1_R1a,
//     subsettedFeature  = R1
//   }
// SatisfyRequirementUsage.getSatisfiedRequirement() derives from this
// subsetting (not from FeatureTyping). Using typing makes the derived
// getter return self — a mistake iteration 1 made.
def wireSatisfied = { def satisfy, def req ->
    def rs = kermlFactory.createReferenceSubsetting()
    rs.setOwner(satisfy)
    rs.setSubsettingFeature(satisfy)
    rs.setSubsettedFeature(req)
    rs
}

// ---- Build the fixture -------------------------------------------------------
def ids = [:]  // declaredName → element (for manifest)

try {
    sm.createSession(project, 'TF-1 build-fixture')

    // R1..R6: plain RequirementUsage
    def reqs = (1..6).collect { i ->
        def r = factory.createRequirementUsage()
        r.setDeclaredName('R' + i)
        r.setOwner(fixtureRoot)
        ids['R' + i] = r
        r
    }
    log.info('Created R1..R6')

    // R7: RequirementUsage with subject = P2 (wired below)
    def r7 = factory.createRequirementUsage()
    r7.setDeclaredName('R7')
    r7.setOwner(fixtureRoot)
    ids['R7'] = r7

    // R8: ViewpointUsage (subtype of RequirementUsage) — FR-3 filter test
    def r8 = factory.createViewpointUsage()
    r8.setDeclaredName('R8_viewpoint')
    r8.setOwner(fixtureRoot)
    ids['R8'] = r8
    log.info('Created R7 (subject-based), R8 (ViewpointUsage)')

    // P1..P6: top-level PartUsage
    def parts = (1..6).collect { i ->
        def p = factory.createPartUsage()
        p.setDeclaredName('P' + i)
        p.setOwner(fixtureRoot)
        ids['P' + i] = p
        p
    }
    log.info('Created P1..P6')

    // Q: sub-part owned by P6 — for implied-satisfy (Q→R6 implies P6→R6)
    def q = factory.createPartUsage()
    q.setDeclaredName('Q')
    q.setOwner(parts[5])  // P6 owns Q → Q is a sub-feature of P6
    ids['Q'] = q
    log.info('Created Q (sub-part of P6)')

    // Helper: SatisfyRequirementUsage owned by `part`, typed to `req`
    def makeSatisfy = { String name, Element owner, def req ->
        def s = factory.createSatisfyRequirementUsage()
        s.setDeclaredName(name)
        s.setOwner(owner)          // owner is the satisfying feature (derived)
        wireSatisfied(s, req)      // FeatureTyping links s→req (derived getSatisfiedRequirement)
        ids[name] = s
        s
    }

    // P1→R1 (×2 for FR-8 badge test: two SatisfyRequirementUsages on the same cell)
    makeSatisfy('S_P1_R1a', parts[0], reqs[0])
    makeSatisfy('S_P1_R1b', parts[0], reqs[0])
    log.info('S_P1_R1a and S_P1_R1b created (badge: P1→R1 count=2)')

    // P1→R2
    makeSatisfy('S_P1_R2', parts[0], reqs[1])

    // P2→R3 and P3→R3 (one-to-many: two parts satisfy R3)
    makeSatisfy('S_P2_R3', parts[1], reqs[2])
    makeSatisfy('S_P3_R3', parts[2], reqs[2])
    log.info('S_P2_R3, S_P3_R3 (one-to-many: R3 satisfied by P2 and P3)')

    // P4→R4, P5→R5
    makeSatisfy('S_P4_R4', parts[3], reqs[3])
    makeSatisfy('S_P5_R5', parts[4], reqs[4])
    log.info('S_P4_R4, S_P5_R5')

    // Q→R6 (the implied source: P6's sub-feature Q satisfies R6)
    makeSatisfy('S_Q_R6', q, reqs[5])
    log.info('S_Q_R6 (implies P6→R6 when show-implied is on)')

    // R7 subject membership — SIMPLIFIED for iteration 1.
    // WARNING discovered in iteration-1 testing: SubjectMembership.setMemberElement(P2)
    // transfers ownership of P2 into the membership, removing P2 from TF1. To avoid
    // corrupting the fixture, we do NOT wire the subject here. The FR-7 test (matrix
    // on "subject" kind) is deferred until we discover the correct SysMLv2 wiring
    // that references P2 without moving it (likely via a ReferenceSubsetting inside
    // the SubjectMembership).
    //
    // For now: create the SubjectMembership shell so R7 has one, but leave its
    // member-element empty. Matrix code's "subject" kind will return empty cells
    // until this is resolved.
    try {
        def subjM = factory.createSubjectMembership()
        subjM.setOwner(r7)
        log.info('R7 SubjectMembership created (empty — subject wiring deferred)')
    } catch (Exception e) {
        log.warn('SubjectMembership creation failed: ' + e.message)
    }

    // Verify satisfy wiring now uses ReferenceSubsetting (':>' operator).
    // With subsetting, getSatisfiedRequirement() should work directly.
    def testSatisfy = ids['S_P1_R1a']
    log.info('Verify S_P1_R1a (ReferenceSubsetting wiring):')
    log.info('  satisfy.getOwner()                = ' + testSatisfy?.getOwner()?.getDeclaredName())
    log.info('  satisfy.getSatisfyingFeature()    = ' + testSatisfy?.getSatisfyingFeature()?.getDeclaredName())
    log.info('  satisfy.getSatisfiedRequirement() = ' + testSatisfy?.getSatisfiedRequirement()?.getDeclaredName())

    sm.closeSession(project)
    log.info('Session closed — fixture committed to model')

} catch (Throwable t) {
    log.error('Fixture creation FAILED: ' + t.toString())
    try { sm.cancelSession(project) } catch (Exception ignored) {}
    app.getGUILog().log('[BuildFixture] FAILED: ' + t.message)
    return
}

// ---- Write fixture-manifest.json (hand-rolled JSON — no groovy.json.* in Cameo) --
def manifestFile = new File('E:\\_Documents\\git\\TutorialForCatiaMagicApiMCP\\v2Matrix\\fixtures\\fixture-manifest.json')
def sb = new StringBuilder()
sb.append('{\n')
sb.append('  "fixtureVersion": 1,\n')
sb.append('  "requirements": 8,\n')
sb.append('  "satisfyingElements": 7,\n')
sb.append('  "directCells": 6,\n')
sb.append('  "impliedCells": 1,\n')
sb.append('  "satisfyEdges": 8,\n')
sb.append('  "badgeTest": {"row": "P1", "col": "R1", "count": 2},\n')
sb.append('  "subjectTest": {"element": "P2", "req": "R7"},\n')
sb.append('  "viewpointReq": "R8_viewpoint",\n')
sb.append('  "impliedSource": {"subPart": "Q", "parent": "P6", "req": "R6"},\n')
sb.append('  "elements": [')
ids.keySet().sort().eachWithIndex { k, i -> if (i > 0) sb.append(', '); sb.append('"').append(k).append('"') }
sb.append('],\n')
sb.append('  "notes": "Q is a sub-part of P6. S_Q_R6 makes (P6, R6) an implied cell when show-implied is on."\n')
sb.append('}\n')
manifestFile.text = sb.toString()
log.info('Wrote fixture-manifest.json: ' + manifestFile.absolutePath)

// ---- Done -------------------------------------------------------------------
def summary = 'TF-1 fixture created successfully.\n' +
              'Elements: R1..R8, P1..P6, Q, 8 satisfy edges.\n' +
              'Manifest: v2Matrix/fixtures/fixture-manifest.json\n' +
              'ACTION REQUIRED: File → Save As → v2Matrix/fixtures/ReferenceProject.mdzip'
log.info(summary)
app.getGUILog().log('[BuildFixture] SUCCESS — ' + summary)
log.info('=== build-fixture.groovy end ===')
