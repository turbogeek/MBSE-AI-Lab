# v2Matrix — Requirements

> Scope: a GUI traceability matrix for a **SysMLv2** model rendered in a
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
| **G-1** | Build a usable traceability matrix Swing tool that runs inside Cameo against a SysMLv2 model (local or Teamwork-Cloud-hosted). |
| **G-2** | Teach the reader the SysMLv2 API surface — `sysml.*` + `kerml.*` — through concrete, idiomatic code. |
| **G-3** | Teach how SysMLv2 **Satisfy** is modeled: direct `SatisfyRequirementUsage`, implied via feature hierarchy, and subject-based inference. |
| **G-4** | Show good Swing + Java2D presentation patterns in the Cameo environment (no fighting with EDT, no `System.exit`, etc.). |

## 2. Functional requirements

### 2.1 Matrix construction

**FR-1. Default axes.** The default matrix shall place `RequirementUsage`
instances on the **columns** and any satisfying usage (PartUsage,
ActionUsage, StateUsage, UseCaseUsage, ViewUsage, …) on the **rows**.
*Acceptance:* Opening the matrix on TF-1 with no configuration produces N
columns and M rows where N and M match the counts in TF-1.

**FR-2. Swap axes.** The user shall be able to swap rows ↔ columns with a
single control without closing the dialog.
*Acceptance:* After swap the cell set is identical, counts are
transposed, and arrow direction follows FR-9.

**FR-3. Element-type filter per axis.** The user shall be able to restrict
each axis to a subset of SysMLv2 metaclasses (e.g. "RequirementUsage
excluding ViewpointUsage", "only PartUsage + ActionUsage on rows").
*Acceptance:* Excluding ViewpointUsage from TF-1's columns removes every
viewpoint and lowers the filled-cell count to only those whose column is
a pure RequirementUsage.

**FR-3b. Element-type filter for the relationship.** The user shall be
able to restrict which relationship kinds are considered. Examples:
`Dependency`, `AllocationUsage`, `SatisfyRequirementUsage`,
`FeatureMembership`, standard property, common attribute value,
`Specialization` (inheritance), `Subsetting`, and the derived kinds
from FR-6/FR-7. This is distinct from FR-3 (which filters the axis
elements themselves).
*Acceptance:* With kind restricted to `SatisfyRequirementUsage`, cells
are filled only where a SatisfyRequirementUsage relates the row to the
column, even if other kinds (e.g. Dependency) exist between them.

**FR-4. Scope per axis.** Each axis shall have its own scope control with
options: (a) a user-picked Namespace, (b) a package, (c) the whole
model including standard libraries. Default scope excludes the standard
libraries (per the KerML `LibraryPackage.isStandard()` heuristic — see
`v2Matrix/research-notes.md` §FR-4).
*Acceptance:* Each axis exposes a "Set scope…" button that opens a
Namespace picker; changing the scope re-queries and re-renders without a
full dialog restart. Default scope hides every element under a
`LibraryPackage` whose `isStandard()` returns true.

### 2.2 Relationships

**FR-5. Relationship-kind selector.** The user shall pick one kind (or
"Any") for the matrix. Kinds are grouped:

| Group | Kinds |
|---|---|
| End-based  | `Dependency`, `Succession`, `Transition`, `Connection`, `Flow` |
| Specialized usage | `SatisfyRequirementUsage`, `AllocationUsage`, `ExhibitStateUsage`, `PerformActionUsage` |
| Structural | `FeatureMembership`, `PartOwnership`, `NamespaceOwnership` |
| Derived    | *subject-based* (FR-7), *implied* (FR-6) |

*Acceptance:* Switching the selector changes which cells are filled on
TF-1 according to the group's semantics; "Any" is the union.

**FR-6. Implied relationships.** When "Show implied" is on and a
sub-feature of element `E` has relationship `R` to target `T`, the matrix
shall also render a cell for (`E`, `T`), propagated up the feature-
membership chain to the top-level element. The cell shall be visually
distinguishable from a direct one (see FR-10).
*Acceptance:* On TF-1 with a part P containing sub-part Q whose
sub-feature satisfies R1, toggling implied on adds a styled cell at (P,
R1) that is distinct from the direct cell at (Q, R1).

**FR-7. Derived kind — subject.** The user shall be able to select a
derived "subject" kind: rows are values that appear as a RequirementUsage's
`subject`, columns are the requirements themselves, and a cell fills when
the requirement's subject equals the row element.
*Acceptance:* With a requirement R whose `subject` is PartUsage `P1`,
switching to "subject" shows a filled cell at (P1, R).

### 2.3 Cells

**FR-8. Cell content.** A cell shall be either empty or contain exactly
one aggregated arrow. If multiple relationships of the active kind exist
between the same (row, col) pair, the arrow carries a count badge.
*Acceptance:* Two `SatisfyRequirementUsage` instances between the same
(elem, req) pair render one arrow with badge "2".

**FR-9. Arrow direction.** Arrows shall run from the satisfying element
toward the requirement at a 45° angle. When columns are the requirement
side, the arrow runs lower-left → upper-right; when rows are the
requirement side (axes swapped), it runs upper-left → lower-right.
*Acceptance:* Screenshot match against the canonical
`v2Matrix/fixtures/ReferenceProject-default.png` and
`ReferenceProject-swapped.png` within a pixel-tolerance threshold.

**FR-10. Cell annotations.** The script shall render these annotations
when toggled on:
- Multiplicity marker (circle around the arrow) when the relationship is
  1:many or many:1.
- Implied-vs-direct styling (distinct color or dash).
- User-configurable palette. MVP palettes: **Standard**, **Colorblind-safe**,
  **Dark mode**, **Hello Kitty** (pink/white whimsical).
*Acceptance:* Each toggle produces a visibly different rendering in TF-1.

**FR-10a. Column-label angle.** Column header labels shall render at **45°**
(rising from the cell boundary) rather than vertical, to improve
readability and axial compactness.
*Acceptance:* Headers slope up-right; the header band height equals roughly
`label_length * sin(45°)`; labels remain fully legible on TF-1.

**FR-10b. Legend.** The dialog shall display a **legend panel** listing the
distinct visual styles used in the current view. At minimum the legend
shows: direct cell, implied cell, subject cell, and badge (multi-count)
— each with a miniature rendering in the current palette.
*Acceptance:* Toggling the palette combo re-renders the legend swatches;
toggling show-implied off hides the implied row in the legend; swapping
palette between Standard/Colorblind/Dark/Hello Kitty changes all swatches
visibly.

### 2.4 Interactions

**FR-11. Modeless dialog.** The matrix shall appear in a modeless
`JDialog` owned by the Cameo main frame, so the user can navigate the
containment tree with the matrix still visible.
*Acceptance:* Opening the dialog does not disable the containment tree;
the user can select other elements while the dialog is open.

**FR-12. Cell interactions.** The dialog shall support:
- Single-click on a filled cell → open the backing relationship element
  in the containment tree.
- Double-click on a filled cell → prompt to **delete** the relationship
  (mutates under a `SessionManager` session; cancel on exception).
- Double-click on an empty cell → prompt to **create** the relationship
  (same session discipline).
- Right-click → context menu: "Open in tree", "Copy element IDs",
  "Close matrix".
*Acceptance:* Each interaction on TF-1 produces the specified effect;
the resulting `logs/v2Matrix.log` contains a matching log line.

### 2.5 Presentation controls

**FR-13. Hierarchy toggle per axis.** When on, an axis shall show its
elements indented by containment depth with parent Namespaces as visible
group rows/columns. Toggle is independent per axis.
*Acceptance:* Toggling hierarchy on produces a tree-indented axis
without removing any leaves; toggling off flattens back to the original
list order.

**FR-14. Sort.** The user shall be able to sort each axis by (a) name,
or (b) hierarchical containment (requires FR-13 on).
*Acceptance:* Switching sort modes reorders the axis in place without
losing the current cell selection.

### 2.6 Matrix-view persistence

**FR-15. Save matrix as a view.** The user shall be able to save the
current matrix configuration as a model element. The saved view captures
axis element-types, scopes, relationship kind, annotation toggles, sort,
and hierarchy toggles. The metaclass of the saved view is **OQ-2**.
*Acceptance:* After save → close → reopen the tool → select the saved
view, the matrix renders with identical content to the pre-save state.
All model writes go through one `SessionManager` session per save.

**FR-16. Auto-launch from a selected view.** When the tool is launched
with a saved matrix view selected in the containment tree, it shall
immediately render that view. When multiple views exist, the user shall
be able to select one or more to open (one dialog per view).
*Acceptance:* With view V selected, running the trigger (FR-18) opens
the dialog pre-configured to V without showing the "choose a view"
picker.

### 2.7 Exports

**FR-17. Exports.** The user shall be able to export the current matrix
to each of: **PNG**, **SVG**, **HTML** (self-contained, embedded SVG or
table), **CSV** (one row per matrix row, one column per matrix column,
semicolon-separated relationship-element IDs in each cell).
*Acceptance:* Each format opens in a standard tool (Excel for CSV,
browser for SVG/HTML, any image viewer for PNG). For CSV and HTML the
round-trip invariant holds: re-reading the export recovers the same set
of (row, col) pairs with the same relationship IDs.

### 2.8 Trigger and environment

**FR-18. Launch path.** MVP shall support at least the "run from Cameo
script console" path — the user opens
`scripts/v2Matrix/MatrixDialog.groovy` in Cameo and executes it.
Optional: a menu action; a REST test-harness trigger for Claude smoke
tests.
*Acceptance:* The README (this repo's, under a new v2Matrix section)
gives exact steps that produce the dialog on TF-1.

**FR-19. Logging.** The script shall use `scripts/SysMLv2Logger.groovy`
instantiated with a dedicated log file at `logs/v2Matrix.log`, cleared
on start per the `dedicated-log-file` pattern.
*Acceptance:* After a run, `logs/v2Matrix.log` contains exactly one
`=== v2Matrix run started ===` line followed by the run's output.

## 3. Non-functional requirements

| ID | Requirement |
|----|-------------|
| **NFR-1** | No `System.exit`, no `Runtime.halt`. Dialog close → `dispose()`. |
| **NFR-2** | No GStrings at Cameo API boundaries; enforced by the MCP validator's lint. |
| **NFR-3** | All model mutations (FR-12 delete/create, FR-15 save) run inside one `SessionManager` session with `cancelSession` on exception. |
| **NFR-4** | Render a 500×500 matrix from TF-1 in ≤5 s on the reference workstation. Above 500×500, show a confirm dialog before building. |
| **NFR-5** | Every `com.nomagic.*` / `com.dassault_systemes.*` import passes `javadoc_verify_fqn` before the code is handed back. |
| **NFR-6** | The script tolerates being relaunched repeatedly within the same Cameo session — no static singletons that leak across runs; old dialogs are disposed. |
| **NFR-7** | MVP runs against SysMLv2 API version 26xR1 (profile: **TBD**, see OQ-11). |

## 4. Test fixtures

| ID | Description |
|----|-------------|
| **TF-1** | `v2Matrix/fixtures/ReferenceProject.mdzip` — a small SysMLv2 project with: N requirements, M satisfying usages, K direct satisfy edges, L implied-only edges, at least one 1:many case, and at least one requirement with a `subject` for FR-7. Exact counts in **OQ-3**. |
| **TF-2** | Canonical rendered outputs committed alongside TF-1: `ReferenceProject-default.png`, `ReferenceProject-swapped.png`, `ReferenceProject.csv`, `.svg`, `.html`. Used for acceptance tests of FR-9 and FR-17. |

## 5. Non-goals (v1)

- **NG-1.** No custom layout engines (force-directed, etc.); cells live on a
  fixed grid.
- **NG-2.** No live model subscription — the matrix is a snapshot; user
  refreshes explicitly.
- **NG-3.** No integration with Cameo's built-in persistent Dependency
  Matrix diagram; output is Swing-only.
- **NG-4.** No saving of exported images inside the model (exports go to
  the filesystem).
- **NG-5.** No explicit undo/redo UI — Cameo's own undo stack picks up the
  session-scoped mutations, but we don't add dedicated undo controls.
- **NG-6.** No authoring mode for derived kinds beyond FR-7's subject —
  custom derived kinds are out of scope for v1.

## 6. Open questions

Each **OQ** must be resolved before the corresponding requirement can be
planned. I'll ask the top few via a direct prompt; the rest can be
answered asynchronously by editing this doc.

| ID | Question |
|---|---|
| **OQ-1** | When `kind = Any` (FR-5) and multiple kinds connect the same (row, col), does the cell show one aggregated arrow (with a count) or one badge per kind? |
| **OQ-2** | What metaclass represents a **saved matrix view** (FR-15)? Options: (a) a new stereotype `«MatrixView»` on a Namespace, (b) a `ViewUsage` with tagged values, (c) a JSON blob attached as a Comment / fileAttachment, (d) other. |
| **OQ-3** | For **TF-1**: do you have an existing SysMLv2 file we can use, or should the plan include building one? If building: target counts for N/M/K/L and how many subject-based requirements? |
| **OQ-4** | "Standard libraries" for scope filtering (FR-4 default): is the rule "Namespace is not under root package named `SysML`, `KerML`, `Standard`, or any user-listed customization" good enough, or do you want a user-editable exclusion list? |
| **OQ-5** | Direction of arrow for non-Satisfy kinds (FR-9). Always row → col regardless of semantic direction, or always semantic (so direction varies per kind)? |
| **OQ-6** | HTML export (FR-17): static, or interactive with cells that link back via Cameo's URI scheme / model IDs? |
| **OQ-7** | Launch ergonomics (FR-18): is "open .groovy in script console and run" acceptable for MVP, or is a menu action a must-have? |
| **OQ-8** | Multiplicity circle annotation (FR-10): a 1:many satisfy is typically visible *across* cells (one element, many requirements) rather than *within* a single cell. Do you want the circle on the axis label, or do you have a within-cell interpretation in mind? |
| **OQ-9** | Export failure policy (FR-17): on a write error, (a) abort with a dialog, (b) log and continue, or (c) retry N times? |
| **OQ-10** | Teamwork Cloud specifics: any locking or branch discipline needed for FR-15 saves on TWC-hosted projects, or do we rely on the user to have the project checked out? |
| **OQ-11** | Environment (NFR-7): which MCP profile should we create/activate for this project (apiVersion `26xR1`, modelingTypes `["SysMLv2"]` + possibly `["KerML"]`)? Should I add this profile now or wait for the planner to propose? |
