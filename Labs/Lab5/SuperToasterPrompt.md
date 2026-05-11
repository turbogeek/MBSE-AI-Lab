# Super Toaster System of Systems Prompt

Copy and paste the following prompt into your Antigravity (or LLM) chat to automatically generate, validate, and Cameo-test a massive SysMLv2 model for an advanced, IoT-connected Super Toaster. This model tests the absolute limits of the SysMLv2 language, including multi-level traceability, complex state machines, swimlane actions, and variant modeling.

***

**Copy below the three backticks:**

***

Act as an expert Systems Engineer, Software Architect, and Consumer Kitchen Appliance Designer. I want you to model an advanced IoT-connected "Super Toaster" as a System of Systems. Please execute the following workflow step-by-step:

1. **Folder Creation:** Check the directory `outputSysML/lab5/versionX`. Find the next available version number (e.g., version1, version2) that does not exist, and create that folder.

2. **Discovery Document:** Create an INCOSE discovery document based on the template in `LLM_Assets/incose_template.md`. Populate it with the following scope:
   - **System of Systems:** The toaster connects via Wi-Fi to a Smartphone App (for user profiles, doneness status, alerts), an IoT Cloud Service (for firmware updates, logging, and MTBF tracking by the Manufacturer), and a Fire Department API (for automatic dispatch if thermal runaway occurs).
   - **Performance Requirements:** Needs to toast 2 slices perfectly in under 2 minutes. Needs a peak power limit of 1500W. Max weight of 3 kg. Target MTBF of 10,000 hours.
   - **Features:** Must support variation (different colors, different heating tech: traditional Nichrome Coil vs modern Induction Heating).
   Save this document as `incose_discovery.md` in the newly created version folder.

3. **SysMLv2 Generation:** Using the discovery document, generate a highly complex, fully compliant SysMLv2 model. Save it as `super_toaster.sysml` in the same version folder. Follow these strict architectural requirements:
   - **System of Systems Context:** Define a `SuperToaster_SoS` package with parts for `toaster`, `mobileApp`, `manufacturerCloud`, and `emergencyService`. Route connections and ports between them for Wi-Fi and API data flows.
   - **Multi-Level Traceability:** Create distinct packages for Concept, Logical, and Physical requirements. Trace requirements via documentation. Use `constraint { ... }` within performance requirements.
   - **Action Modeling (Swimlanes):** Create a robust `action def SmartToastingProcess` with sub-actions like `userConfigures`, `toasterHeats`, `cloudLogs`, and `detectsSmoke`. Use `perform action` within the respective parts to allocate these behaviors to the specific actors in the SoS, mimicking swimlanes.
   - **State Machine (Simulation Ready):** Define a highly detailed state machine within the toaster's Logical Controller. Use proper Event Occurrences (e.g., `accept StartToasting`, `accept TimerExpired`, `accept TempAlert`). Define states: `Standby`, `Heating`, `Cooling`, `MaintenanceRequired`, `EmergencyShutoff`.
   - **Variant Modeling:** Define a `variation part def HeatingElement` with standard part definitions like `part def NichromeCoil` that specialize (`:>`) the variation. Do the same for Chassis Color.
   - **Calculations & Rollups:** Define `calc` functions or bound attributes to roll up the total mass, total cost, and total MTBF. Compare the induction vs coil variants via attributes.
   - **Views:** Define tabular and basic views that use the `expose` keyword (e.g., `expose LogicalArchitecture;`).

4. **Local Validation:** Use the `validate_model.py` script to validate the `super_toaster.sysml` file. If there are syntax errors, consult `LLM_skills/fix_recipies.md`, fix the code, save, and re-validate. Repeat until zero syntax errors exist.

5. **Cameo Test Harness Validation:** Send a POST request to `http://localhost:8770/load-sysml` with the JSON payload `{"filePath": "<absolute_path_to_the_sysml_file>"}`. Resolve any REST API compilation errors from Cameo (like missing library imports, unresolvable frames, or bad variations) until the file loads successfully.
