# Tutorial One: Swimming Robot Script - Progress Log

## Objective
Create a SysMLv2 model in Cameo via the Groovy API using the Test Harness.

## Task Details
- **REQ-1**: "Swimming Robot" (Requirement)
  - Doc: "The robot shall be able to swim in a pool."
  - Attribute: `cost` (ScalarValue::Real = $500)
- **Part**: `robot1` : "Swimming Robot"
- **Relation**: `robot1` satisfies `REQ-1`.
- **Validation**: List all created elements in the log.

## TODO List
- [x] Create `SwimmingRobot` folder on Desktop.
- [x] Create `SwimmingRobot` folder on Desktop.
- [x] Develop the Groovy script.
- [x] Deploy to Test Harness (port 8765).
- [x] Verify results in Cameo Console and dedicated log.
- [x] Document lessons learned.

## Execution History
- **Run 1**: Failed. Error: `unable to resolve class LiteralReal`.
- **Run 2**: Failed. Error: `SysMLv2Logger(String, File)` constructor not found (sync issue).
- **Run 3**: Success. Fixed `LiteralReal` to `LiteralRational` and synced `SysMLv2Logger.groovy`.

## Lessons Learned
1. **SysMLv2 Literal Types**: SysMLv2 does not have a `LiteralReal` in the `kerml` package; use `LiteralRational` for real numbers and set its value using `setValue(double)`.
2. **Factory Access**: Always use `ElementsFactory.get(namespace)` to ensure the correct context for element creation.
3. **Logger Synchronization**: When working with multiple repositories, ensure utilities like `SysMLv2Logger.groovy` are in sync across all `scripts` directories.
4. **Harness Port**: The test harness in this environment is active on port **8765**, not 8768.
5. **Session Management**: Always wrap model modifications in `SessionManager` create/close blocks to prevent model corruption and ensure UI updates.
6. **SysMLv2 Terminology & Satisfaction**: Use explicit terminology matching the metamodel (e.g. `SatisfyRequirementUsage` instead of "satisfy"). Set up satisfaction by creating a `ReferenceSubsetting` owned by the `SatisfyRequirementUsage` and setting its subsetted feature to the requirement. Furthermore, ensure elements are created inside a dedicated `Package` rather than cluttering the root namespace.
