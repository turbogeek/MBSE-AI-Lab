# Minimalist Toaster Prompt

Copy and paste the following prompt into your Antigravity (or LLM) chat to automatically generate, validate, and Cameo-test a SysMLv2 model for a simple toaster.

**Copy below this line:**

***

I want you to model a standard 2-slot toaster. Please execute the following workflow step-by-step:

1. **Folder Creation:** Check the directory `outputSysML/lab1/versionX`. Find the next available version number (e.g., version1, version2) that does not exist, and create that folder.

2. **Discovery Document:** Create an INCOSE discovery document based on the template in `LLM_Assets/incose_template.md`. Populate each section with realistic information commensurate with a standard 2-slot toaster (e.g., heating elements, power consumption, timer, user safety, and pop-up mechanism). Save this document as `incose_discovery.md` in the newly created version folder.

3. **SysMLv2 Generation:** Using the discovery document you just created, generate a fully compliant SysMLv2 model for the toaster. Follow the MagicGrid structure by defining `Stakeholders`, `Requirements`, `SystemContext`, `LogicalArchitecture`, and `PhysicalArchitecture` packages.
   - **Stakeholders & Concerns:** Explicitly define the key stakeholders and their `concern` elements.
   - **Requirements:** Define requirement usages using the shortName for requirement numbers (e.g., `requirement <REQ1> 'Requirement Name' { ... }`). Use the `frame` keyword inside each requirement to frame the concerns defined in the Stakeholders package.
   - **Views:** Define views within the model that auto-populate by using the `expose` keyword to expose relevant architecture elements.
   Read and apply the rules in `LLM_skills/skill.md`, and follow the iterative process in `LLM_skills/methodology_skill.md`. Save this model as `toaster.sysml` in the same version folder.

4. **Local Validation:** Use the `validate_model.py` script to validate the `toaster.sysml` file. If there are errors, consult `LLM_skills/fix_recipies.md` and `LLM_skills/pattern_fix_recipies.md`, fix the code, save, and re-validate. Repeat until the script returns zero syntax errors.

5. **Cameo Test Harness Validation:** I have already started the Cameo REST service via `sysml-validator\utilityScripts\start-v2language-test-harness.groovy`. Once your local validation passes, send a POST request to `http://localhost:8770/load-sysml` with the JSON payload `{"filePath": "<absolute_path_to_the_sysml_file>"}`. If the REST API returns any compilation errors from Cameo, fix the code, update the file, and try again. Repeat until the file loads into Cameo without any errors.
