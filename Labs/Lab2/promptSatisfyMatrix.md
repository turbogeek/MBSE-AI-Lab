# Tutorial Two: Satisfy Matrix Groovy Script

Act as a SysMLv2 modeling expert for the CATIA Magic Open API with expertise in the Groovy Language, Java, the SysMLv2 standard, and the Cameo API. Your task is to develop, test, and validate a Groovy script that will show a Satisfy matrix with the given requirements and architecture in this file: "Tutorials\TutorialTwo\requirements.md".

**CRITICAL VERSIONING RULE:** Before writing any code, examine the `Tutorials\TutorialTwo\scripts` directory to identify the highest existing version folder (e.g., `version1`, `version2`). You MUST create a new, incremented version folder (e.g., `version3`, `version4`) and put the new scripts into this new directory. **DO NOT overwrite existing scripts.**

## Important instructions for Groovy scripts using API for SysMLv2

- Wrap all model changes in a SessionManager transaction (if mutating the model, though the matrix is read-only).
- Create a dedicated script logger targeting `Tutorials\TutorialTwo\logs\SatisfyMatrix.log` and use it for all diagnostic output. The logger script is located at `scripts\SysMLv2Logger.groovy` relative to the workspace root. Load it using its absolute path constructed from the workspace directory. DO NOT rely on the GUI console alone, and DO NOT use JFileChooser.
- **CRITICAL:** Event handlers (`actionPerformed`, `mouseClicked`) and rendering methods (`paintComponent`) execute on the AWT Event Dispatch Thread. Any uncaught exceptions will be intercepted by MagicDraw and display an internal error dialog to the user. You MUST wrap the entire bodies of these methods in a `try-catch (Throwable t)` block and log errors using the dedicated logger!
- Deploy and run the script via the Cameo Test Harness at <http://localhost:8765/run> to test it.
- If the agent encounters `UnmodifiableEList` or structural errors, it must inspect the log, adapt its approach, and try again until the log confirms successful execution without errors.
- Ensure that the script defines element ownership precisely. (e.g. standard elements owned by the package, properties owned by usage blocks, satisfaction owned by usage).
- ONLY use the `ElementsFactory.get(namespace)` and avoid `LiteralReal`. Use `LiteralRational` for attribute defaults to prevent type mismatch errors.
- Optimize the rendering loop and data structures (like pre-calculating implied relationships) to ensure GUI repaints remain highly performant for large matrices.
- Ensure visual indicators for BOTH direct and implied relationships are arrows drawn at a 45-degree angle pointing up-right, differentiated by color.

## Validation Loop

1. Generate the script and save it to the newly created version directory using the relative path (e.g., `Tutorials\TutorialTwo\scripts\version<N>\SatisfyMatrix.groovy`).
2. Trigger the `/run` endpoint on the test harness, passing the absolute path to the generated script in the `scriptPath` JSON payload.
3. Check the /status and tail the /log.
4. If compilation or runtime errors occur, fix the script and re-run.
