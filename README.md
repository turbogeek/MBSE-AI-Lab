# MBSE-AI-Lab: Model-Based Systems Engineering & AI Integration Lab

MBSE-AI-Lab is a development laboratory and tutorial repository designed to teach and demonstrate how to leverage Large Language Models (LLMs) and agentic AI tools to automate systems engineering workflows, generate compliant SysMLv2 models, and interact with the CATIA Magic / Cameo Systems Modeler APIs.

The repository provides hands-on exercises (labs) and supporting utilities to guide engineers and AI agents in automating model construction and validation.

---

## Key Tracks & Capabilities

### 1. Dassault / Cameo Open API Automation (Groovy)

* **Model Generation**: Programmatic creation of packages, requirements, part definitions, usages, value properties, and relationships (such as satisfaction) using the MagicDraw/Cameo Open API.
* **Interactive GUIs**: Construction of custom Swing-based interactive dialogs (e.g., a Satisfy Matrix) directly within the Cameo user interface.
* **Test Harness Execution**: Execution and validation of Groovy scripts inside a running Cameo instance using a local REST test harness.

### 2. SysMLv2 Text Modeling

* **Systems Engineering Workflows**: Creation of INCOSE-compliant discovery documents to capture stakeholder needs, measures of effectiveness (MOEs), and safety protocols.
* **MagicGrid Architecture**: Generation of fully compliant SysMLv2 text models adhering to the MagicGrid structure (Stakeholders, Requirements, System Context, Logical Architecture, and Physical Architecture).
* **Advanced Language Features**: Implementation of state machines, variant modeling, action modeling (swimlanes), and weight/power rollup calculations.
* **Multi-Stage Validation**: Parsing and compiling SysMLv2 models using a local validator and loading them into Cameo via a dedicated REST validation harness.

---

## Repository Structure

* **`Labs/`**: Contains the prompt files and requirements for the five hands-on labs.
* **`LLM_skills/`**: Contains guides and instruction sets (e.g., `requirements_writing_skill.md`) to establish best practices for systems engineering requirement generation.
* **`Out/`**: Contains logs, versioned Groovy scripts, presentation slides, and screenshots from reference runs.
  * `TutorialOne/`: Focuses on the Swimming Robot model generation.
  * `TutorialTwo/`: Focuses on the interactive Satisfy Matrix Swing GUI.
* **`LabOutputs/` & `outputSysML/`**: Directories where generated artifacts (INCOSE documents, SysMLv2 models, and test logs) are stored, organized by lab and version.
* **`Example Automation Plugin Config file/`**: Contains a sample `plugin.xml` file demonstrating how to configure the CATIA Magic/Cameo Automation plugin to reference an external Groovy installation.
* **`Antigravity/`**: Holds the VS Code workspace configuration (`MBSE-AI-Lab.code-workspace`).
* **`VerificationPrompt.md`**: The environment check prompt used to validate local tools and run the initial verification test.

---

## The Labs

### Lab 1: Swimming Robot Model (Groovy Open API)

* **Objective**: Write a Groovy script using the Cameo Open API to create a "Swimming Robot" package, a requirement (REQ-1), a part definition, a part usage with a cost attribute, and a satisfy relationship.
* **Focus**: Basic model creation, session management transaction handling, and test harness execution.
* **Instructions**: [promptSwimmingRobotModel.md](Labs/Lab1/promptSwimmingRobotModel.md)

### Lab 2: Satisfy Matrix GUI (Groovy Open API)

* **Objective**: Create a Groovy script that renders a Swing GUI dialog inside Cameo displaying a Satisfy Matrix with direct and implied relationships.
* **Focus**: Safe UI thread handling (AWT Event Dispatch Thread), custom component painting (rendering relationship arrows at a 45-degree angle), and exception logging.
* **Instructions**: [promptSatisfyMatrix.md](Labs/Lab2/promptSatisfyMatrix.md)

### Lab 3: Minimalist Toaster (SysMLv2 Text)

* **Objective**: Write an INCOSE discovery document and generate a compliant SysMLv2 model (`toaster.sysml`) for a standard 2-slot toaster.
* **Focus**: MagicGrid package structure, framing stakeholder concerns in requirements, and local/Cameo validation loops.
* **Instructions**: [MinimalistToasterPrompt.md](Labs/Lab3/MinimalistToasterPrompt.md)

### Lab 4: Insect Hunting Drone (SysMLv2 Text)

* **Objective**: Generate a SysMLv2 model (`bug_drone.sysml`) for a solar-powered autonomous insect-hunting drone.
* **Focus**: State machines (patrolling, targeting, firing, charging), custom tabular views (Concerns, Stakeholders, States, Parts), and system-level power/weight rollups.
* **Instructions**: [BugHuntingDronePrompt.md](Labs/Lab4/BugHuntingDronePrompt.md)

### Lab 5: Super Toaster System of Systems (SysMLv2 Text)

* **Objective**: Develop an advanced, simulation-ready SysMLv2 model (`super_toaster.sysml`) for an IoT-connected toaster.
* **Focus**: System of Systems (SoS) context, multi-level traceability (Concept vs. Logical vs. Physical), action modeling (swimlane behavior allocation), variant modeling (Induction vs. Nichrome Heating), and mass/cost/MTBF rollups.
* **Instructions**: [SuperToasterPrompt.md](Labs/Lab5/SuperToasterPrompt.md)

---

## Environment Setup & Validation

To run the labs, several local test harnesses and tools must be running. You can check the readiness of your environment by executing the steps outlined in [VerificationPrompt.md](VerificationPrompt.md).

### Required Services & Ports

* **Groovy Test Harness**: Runs at `http://localhost:8765/run`. This endpoint accepts a JSON payload with a `scriptPath` pointing to a Groovy file and executes it within the running Cameo/MagicDraw application via the automation plugin.
* **Python Test Harness**: Runs at `http://localhost:8764/run`.
* **SysMLv2 Language Test Harness**: Runs at `http://localhost:8770/load-sysml`. This service validates and compiles `.sysml` files, loading them directly into Cameo.

### Cameo Plugin Configuration

Because the embedded Groovy compiler in Cameo does not support some modern language features, you must configure the automation plugin to reference a local Groovy installation.
A template configuration is provided in [plugin.xml](Example%20Automation%20Plugin%20Config%20file/plugin.xml).

---

## Scripting Best Practices & Guidelines

When developing Groovy scripts for the Cameo Open API, adhere to the following rules:

1. **Transaction Management**: Always wrap model modifications within a `SessionManager` transaction.
2. **UI Thread Safety**: Methods executing on the AWT Event Dispatch Thread (`actionPerformed`, `paintComponent`, etc.) must wrap their entire body in a `try-catch (Throwable t)` block and log errors to a dedicated file using the `SysMLv2Logger` utility to avoid silent failures or application lockups.
3. **Element Ownership**: Define element ownership explicitly. Standard elements should be owned by their parent package, properties by their containing usage block, and satisfy relationships by their respective usage features.
4. **Data Types**: Use `LiteralRational` instead of `LiteralReal` for default attribute values to prevent type mismatch errors in the Cameo compiler.
5. **No GStrings**: Avoid using Groovy GStrings (e.g., `"${variable}"`); use standard Java string concatenation or `.toString()` to maintain compatibility.
6. **Script Versioning**: To prevent overwriting work, always inspect the target output directory, identify the highest existing version (e.g., `version2`), increment it (e.g., `version3`), and save your new scripts in that new directory.
