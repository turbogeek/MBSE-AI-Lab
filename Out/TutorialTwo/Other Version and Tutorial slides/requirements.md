# SysMLv2 Satisfy Matrix — Requirements

> Scope: a GUI Satisfy matrix for a **SysMLv2** model rendered in a
> modeless Swing dialog, usable inside a running Cameo/MagicDraw session.
> API surface: `com.dassault_systemes.modeler.sysml.*` and
> `com.dassault_systemes.modeler.kerml.*`.
>
> Each requirement is one sentence + one testable acceptance criterion so
> the planner can generate a test list from this document. Unresolved
> items are flagged as **OQ-n** in §6.

---

## 1. Goals

| ID | Goal |
|----|------|
| **G-1** | Build a usable read-only Satisfy matrix Swing tool that runs inside Cameo against a SysMLv2 model. |
| **G-2** | Teach the reader the SysMLv2 API surface — `sysml.*` + `kerml.*` — through concrete, idiomatic code. |
| **G-3** | Teach how SysMLv2 **Satisfy** is modeled: direct `SatisfyRequirementUsage` and implied via feature hierarchy. |
| **G-4** | Show good Swing + Java2D presentation patterns in the Cameo environment (no fighting with EDT, no `System.exit`, etc.). |

## 2. Functional requirements

### 2.1 Matrix construction

**FR-1. Default axes.** The default matrix shall place `RequirementUsage`
instances on the **columns** and any satisfying usage (PartUsage,
ActionUsage, StateUsage, UseCaseUsage, ViewUsage, …) on the **rows**.
*Acceptance:* Opening the matrix on TF-1 with no configuration produces N
columns and M rows where N and M match the counts in TF-1.

**FR-2. Element-type filter per axis.** The user shall be able to restrict
each axis to a subset of SysMLv2 metaclasses (e.g. "RequirementUsage
excluding ViewpointUsage", "only PartUsage + ActionUsage on rows").
*Acceptance:* Excluding ViewpointUsage from TF-1's columns removes every
viewpoint and lowers the filled-cell count to only those whose column is
a pure RequirementUsage.

**FR-3. Scope per axis.** Each axis shall have its own scope control with
options: (a) a user-picked Namespace, (b) a package, (c) the whole
model including standard libraries. Default scope excludes the standard
libraries (per the KerML `LibraryPackage.isStandard()` heuristic).
*Acceptance:* Each axis exposes a "Set scope…" button that opens a
Namespace picker; changing the scope re-queries and re-renders without a
full dialog restart. Default scope hides every element under a
`LibraryPackage` whose `isStandard()` returns true.

### 2.2 Relationships

**FR-4. Satisfy relationships.** The matrix shall strictly show relationships
where the element on the row satisfies the requirement on the column using
`SatisfyRequirementUsage`.
*Acceptance:* Cells are filled only where a `SatisfyRequirementUsage` relates the row to the column.

**FR-5. Implied relationships.** When "Show implied" is on and a
sub-feature of element `E` satisfies requirement `R`, the matrix
shall also render a cell for (`E`, `R`), propagated up the feature-
membership chain to the top-level element. The cell shall be visually
distinguishable from a direct one (see FR-7).
*Acceptance:* On TF-1 with a part P containing sub-part Q whose
sub-feature satisfies R1, toggling implied on adds a styled cell at (P,
R1) that is distinct from the direct cell at (Q, R1).

### 2.3 Cells

**FR-6. Cell content.** A cell shall be either empty or contain exactly
one indicator (e.g. an arrow or 'X'). If multiple relationships exist
between the same (row, col) pair, the indicator carries a count badge.
*Acceptance:* Two `SatisfyRequirementUsage` instances between the same
(elem, req) pair render one indicator with badge "2".

**FR-7. Cell annotations.** The script shall render these annotations
when toggled on:
- Implied-vs-direct styling (distinct color, shape, or dash).
*Acceptance:* Toggling "Show implied" produces a visibly different rendering for implied cells in TF-1 compared to direct satisfaction cells.

**FR-8. Column-label angle.** Column header labels shall render at **45°**
(rising from the cell boundary) rather than vertical, to improve
readability and axial compactness.
*Acceptance:* Headers slope up-right; the header band height equals roughly
`label_length * sin(45°)`; labels remain fully legible on TF-1.

### 2.4 Interactions

**FR-9. Modeless dialog.** The matrix shall appear in a modeless
`JDialog` owned by the Cameo main frame, so the user can navigate the
containment tree with the matrix still visible.
*Acceptance:* Opening the dialog does not disable the containment tree;
the user can select other elements while the dialog is open.

**FR-10. Cell interactions.** The dialog shall support:
- Single-click on a filled cell → open the backing relationship element
  in the containment tree.
*Acceptance:* Clicking a cell selects the corresponding `SatisfyRequirementUsage` in the containment tree.

### 2.5 Presentation controls

**FR-11. Hierarchy toggle per axis.** When on, an axis shall show its
elements indented by containment depth with parent Namespaces as visible
group rows/columns. Toggle is independent per axis.
*Acceptance:* Toggling hierarchy on produces a tree-indented axis
without removing any leaves; toggling off flattens back to the original
list order.

**FR-12. Sort.** The user shall be able to sort each axis by (a) name,
or (b) hierarchical containment (requires FR-11 on).
*Acceptance:* Switching sort modes reorders the axis in place without
losing the current cell selection.

### 2.6 Trigger and environment

**FR-13. Launch path.** MVP shall support at least the "run from Cameo
script console" path — the user opens
`scripts/v2Matrix/MatrixDialog.groovy` in Cameo and executes it.
Optional: a menu action; a REST test-harness trigger for Claude smoke
tests.
*Acceptance:* The README gives exact steps that produce the dialog on TF-1.

**FR-14. Logging.** The script shall use `scripts/SysMLv2Logger.groovy`
instantiated with a dedicated log file at `logs/v2Matrix.log`, cleared
on start per the `dedicated-log-file` pattern.
*Acceptance:* After a run, `logs/v2Matrix.log` contains exactly one
`=== v2Matrix run started ===` line followed by the run's output.

## 3. Non-functional requirements

| ID | Requirement |
|----|-------------|
| **NFR-1** | No `System.exit`, no `Runtime.halt`. Dialog close → `dispose()`. |
| **NFR-2** | No GStrings at Cameo API boundaries; enforced by the MCP validator's lint. |
| **NFR-3** | Render a 500×500 matrix from TF-1 in ≤5 s on the reference workstation. Above 500×500, show a confirm dialog before building. |
| **NFR-4** | Every `com.nomagic.*` / `com.dassault_systemes.*` import passes `javadoc_verify_fqn` before the code is handed back. |
| **NFR-5** | The script tolerates being relaunched repeatedly within the same Cameo session — no static singletons that leak across runs; old dialogs are disposed. |

## 4. Test fixtures

| ID | Description |
|----|-------------|
| **TF-1** | `v2Matrix/fixtures/ReferenceProject.mdzip` — a small SysMLv2 project with: N requirements, M satisfying usages, K direct satisfy edges, L implied-only edges. Exact counts in **OQ-1**. |
| **TF-2** | Canonical rendered outputs committed alongside TF-1: `ReferenceProject-default.png`. Used for visual acceptance tests. |

## 5. Non-goals (v1)

- **NG-1.** No custom layout engines (force-directed, etc.); cells live on a
  fixed grid.
- **NG-2.** No live model subscription — the matrix is a snapshot; user
  refreshes explicitly.
- **NG-3.** No integration with Cameo's built-in persistent Dependency
  Matrix diagram; output is Swing-only.
- **NG-4.** No model mutation. Creating or deleting relationships from the matrix is out of scope for this read-only view.
- **NG-5.** No exporting (CSV, HTML, etc.) or saving views.

## 6. Open questions

Each **OQ** must be resolved before the corresponding requirement can be
planned.

| ID | Question |
|---|---|
| **OQ-1** | For **TF-1**: do you have an existing SysMLv2 file we can use, or should the plan include building one? If building: target counts for N/M/K/L? |
| **OQ-2** | "Standard libraries" for scope filtering (FR-3 default): is the rule "Namespace is not under root package named `SysML`, `KerML`, `Standard`, or any user-listed customization" good enough, or do you want a user-editable exclusion list? |
| **OQ-3** | Launch ergonomics (FR-13): is "open .groovy in script console and run" acceptable for MVP, or is a menu action a must-have? |
