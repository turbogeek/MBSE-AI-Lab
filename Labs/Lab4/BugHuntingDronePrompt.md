# Insect Hunting Drone Prompt

Copy and paste the following prompt into your Antigravity (or LLM) chat to automatically generate, validate, and Cameo-test a SysMLv2 model for an autonomous insect hunting drone.

***

**Copy below the three backticks:**

***

I want you to model a solar-powered Insect Hunting Drone. Please execute the following workflow step-by-step:

1. **Folder Creation:** Check the directory `outputSysML/lab2/versionX`. Find the next available version number (e.g., version1, version2) that does not exist, and create that folder.

2. **Discovery Document:** Create an INCOSE discovery document based on the template in `LLM_Assets/incose_template.md`. Populate each section with the following requirements:
   - Powered by solar energy. It must be able to land, charge itself via solar, and take off again to continue hunting.
   - Equipped with a laser weapon to kill insects.
   - Equipped with sensors and AI computer vision to identify insects and determine if they are pests or beneficial.
   - Equipped with a GPS system for navigation and a communication system to report findings to a human operator.
   - Capable of autonomous operation before returning to base.
   - Capable of operating in varied weather, including moderate rain.
   - Payload capacity up to 1 kg.
   - Max operating altitude of 25 meters within a geofenced area (1 mile x 1 mile) around the base station.
   - Implement strict safety protocols: the drone's rotor blades must have physical guards or emergency stop mechanisms to prevent injury, the battery subsystem must include thermal monitoring to prevent battery fires, and the laser weapon must have safety interlocks (e.g., eye-safe compliance, disabling fire when tilted toward humans).
   - Define specific MOEs (Measures of Effectiveness), MOPs (Measures of Performance), and TPMs (Technical Performance Measures) for attributes like overall performance, weight, flight time, bugs killed, and estimated cost.
   - Outline power consumption estimates for motors, electronics, laser, and sensors at the concept/logical level, and refine these estimates for the physical level.
   Save this document as `incose_discovery.md` in the newly created version folder.

3. **SysMLv2 Generation:** Using the discovery document you just created, generate a fully compliant SysMLv2 model for the drone. Follow the MagicGrid structure by defining `Stakeholders`, `Requirements`, `SystemContext`, `LogicalArchitecture`, and `PhysicalArchitecture` packages.
   - **Stakeholders & Concerns:** Explicitly define the key stakeholders and their `concern` elements. Include concerns related to cost, weight, and power consumption.
   - **Requirements:** Define requirement usages using the shortName for requirement numbers (e.g., `requirement <REQ1> 'Requirement Name' { ... }`). Use the `frame` keyword inside each requirement to frame the concerns defined in the Stakeholders package. Use attribute usages (ISQ/SI standard values) within requirements to define MOE, MOP, and TPM targets (e.g., flight time, bugs killed).
   - **System Context:** Include `HumanOperator`, `Environment`, and `InsectHuntingDrone`.
   - **Logical & Physical Architecture:** Decompose the drone into `NavigationSystem`, `LaserWeapon`, `SensorArray`, `PowerSubsystem`, and `FlightController`. Define power consumption and weight attributes on the motors, electronics, laser, and sensors for both logical and physical parts. Implement a rollup (calculation or binding) to sum these attributes at the system level.
   - **State Machine:** **Crucially, define a state machine and behaviors (actions) within the Logical Architecture** to demonstrate how the drone transitions between patrolling, targeting, firing the laser safely, returning for charging, and executing emergency safety stops.
   - **Views:** Define robust **views** within the model that auto-populate by using the `expose` keyword. Include custom tabular views (e.g., inheriting from `TabularViews::gt` or `TabularViews::rt`) to list each element type separately: Concerns, Stakeholders, States, Parts, MOEs, and TPMs.
   Read and apply the rules in `LLM_skills/skill.md`, and follow the iterative process in `LLM_skills/methodology_skill.md`. Save this model as `bug_drone.sysml` in the same version folder.

4. **Local Validation:** Use the `validate_model.py` script to validate the `bug_drone.sysml` file. If there are errors, consult `LLM_skills/fix_recipies.md` and `LLM_skills/pattern_fix_recipies.md`, fix the code, save, and re-validate. Repeat until the script returns zero syntax errors.

5. **Cameo Test Harness Validation:** I have already started the Cameo REST service via `sysml-validator\utilityScripts\start-v2language-test-harness.groovy`. Once your local validation passes, send a POST request to `http://localhost:8770/load-sysml` with the JSON payload `{"filePath": "<absolute_path_to_the_sysml_file>"}`. If the REST API returns any compilation errors from Cameo, fix the code, update the file, and try again. Repeat until the file loads into Cameo without any errors.
