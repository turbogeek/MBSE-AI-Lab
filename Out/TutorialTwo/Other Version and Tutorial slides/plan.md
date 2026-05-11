# Implementation Plan: SysMLv2 Satisfy Matrix (Tutorial Two)

## Goal
Develop a Groovy script for Cameo Systems Modeler that provides a GUI "Satisfy Matrix" for SysMLv2 models, adhering to the pared-down requirements defined in `requirements.md`. The tool will be a read-only Swing dialog displaying direct and implied `SatisfyRequirementUsage` relationships.

## Proposed Changes

### 1. Script Architecture (`Tutorials/TutorialTwo/scripts/version1/SatisfyMatrix.groovy`)
- **UI Framework:** Standard `JDialog` (modeless) with a `JTable`.
- **API Surface:** Use `com.dassault_systemes.modeler.sysml.*` and `com.dassault_systemes.modeler.kerml.*`.
- **Data Model:**
  - Rows: Elements satisfying requirements (e.g., `PartUsage`, `ActionUsage`).
  - Columns: Requirements (`RequirementUsage`).
  - Cells: Empty or marked with a visual indicator ('X' or similar).
- **Core Logic:**
  - Retrieve all `RequirementUsage` elements for the columns.
  - Retrieve all elements for the rows.
  - Query direct `SatisfyRequirementUsage` relationships.
  - Recursively query feature memberships to identify *implied* satisfy relationships (FR-5).
- **Features (per `requirements.md`):**
  - **FR-1:** Setup default axes.
  - **FR-2 & FR-3:** Simple dropdowns/pickers for scoping and element filtering.
  - **FR-5:** "Show Implied" toggle to dynamically calculate and display implied relationships with distinct visual styling.
  - **FR-8:** Render column headers at a 45° angle.
  - **FR-10:** Double-click on a cell selects the relationship in the Containment Tree.
  - **FR-11 & FR-12:** Basic sorting and hierarchy indentation toggles.

### 2. Presentation File (`Tutorials/TutorialTwo/presentation.html`)
- Copy the styling and structure from `TutorialOne`'s presentation.
- Create new slides focusing on the "Why" (creating tool extensions) and the "How" (building a Swing GUI via Groovy, querying relationships, and displaying them).
- Add slides explicitly calling out the distinction between direct and implied Satisfy relationships.

### 3. Documentation Updates
- Add `Tutorials/TutorialTwo/plan.md` (this file) to the repository to document the roadmap.
- Update `Tutorials/TutorialTwo/notes.md` with any final tweaks before execution.

## Verification Plan

- **Automated Logging:** The script will output diagnostic info to `Tutorials\TutorialTwo\logs\SatisfyMatrix.log`.
- **Manual Verification:** The user will run the script in Cameo and verify that the matrix appears, accurately maps the elements from the test model, and distinguishes implied relationships when toggled.
