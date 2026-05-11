# Lab 5 Implementation Plan: Super Toaster System of Systems

## Goal
Design a comprehensive, final "Lab 5" prompt for the SysMLv2 tutorial series. The goal is to challenge the user and the LLM agent to generate a highly advanced "Super Toaster" model that pushes the boundaries of the SysMLv2 language features supported by the validator and test harness.

## Scope & Proposed Requirements

The prompt will instruct the agent to execute a workflow resulting in a massive SysMLv2 model that covers the following advanced domains:

1.  **System of Systems (SoS) Context**: Define a `SuperToaster_SoS` package.
    *   **Subjects**: SuperToaster, UserSmartphoneApp, ManufacturerCloud, FireDepartmentAPI.
    *   **Interconnections**: Use `interface`, `port`, and `connection` / `flow` to route data (status, alerts, firmware updates) between these entities.

2.  **Requirements & Multi-Level Traceability**:
    *   Define `ConceptRequirements` (e.g., user wants smart toast).
    *   Define `LogicalRequirements` (e.g., IoT connectivity) that `derive` from the concept requirements.
    *   Define `PhysicalRequirements` (e.g., Wi-Fi antenna specifications) that `derive` from logical requirements.
    *   Include `require constraint` on performance requirements to mathematically bound the system.

3.  **Action Modeling (Swimlanes)**:
    *   Define an `action` for the "Smart Toasting Process".
    *   Sub-actions: `UserSelectsProfile`, `AppSendsConfig`, `ToasterHeats`, `CloudLogsData`, `ToasterDetectsSmoke`, `FireDeptNotified`.
    *   Demonstrate swimlane behavior by assigning (`perform action`) these actions to the specific SoS parts.

4.  **State Machine (Simulation Ready)**:
    *   Define a complex state machine for the toaster controller.
    *   Include Event Occurrences: `accept StartToasting via wifiPort`, `after 2 [min]`, `when temp > 400 ['°C']`.
    *   States: `Standby`, `Heating`, `Cooling`, `MaintenanceRequired`, `EmergencyShutoff`.

5.  **Variant Modeling**:
    *   Define a `variation part def HeatingElement`.
    *   Create variants: `variant part def NichromeCoil` and `variant part def InductionCoil`.
    *   Define `variation part def ChassisColor` with variants (e.g., `StainlessSteel`, `MatteBlack`).

6.  **Trade Studies & Quantitative Analysis**:
    *   Define attributes for Cost, Weight, Power Consumption, and MTBF.
    *   Implement `calc` functions to roll up total weight and total MTBF.
    *   Define an `analysis` (or equivalent constraints) to compare the `NichromeCoil` vs `InductionCoil` based on power efficiency, cost, and repairability.

7.  **Expert Personas Applied**:
    *   Instruct the LLM to explicitly assume roles (Consumer Kitchen Appliance Expert, Software Architect, Reliability Engineer) when drafting the INCOSE discovery document and the SysML definitions.

## Open Questions for the User

1. **Trade Study Validation**: Trade studies (`analysis` definitions) often require specific library packages (like `TradeStudy` from the OMG standard libraries). Does the local test harness/Cameo environment have the `Analysis` or `TradeStudies` domain library configured, or should we simulate this via standard `calc` definitions and constraints?
2. **Swimlanes/Actions**: True swimlanes in SysMLv2 are typically modeled by defining an `action` with sub-actions and allocating or connecting those sub-actions to specific `part` usages. Should we explicitly require this allocation pattern, or rely on `perform action` inside the respective parts?
3. **Variants**: Do you want the variants (e.g., Coil vs Induction) to be explicitly bound in different system configurations (e.g., `part def ToasterPro` vs `part def ToasterBasic`), or just left as a variation point in the physical architecture?

## Verification Plan

Once the prompt is written, we will test it by:
1. Acting as the LLM to process the prompt and generate the `incose_discovery.md` and `super_toaster.sysml`.
2. Running the `validate_model.py` script.
3. Sending the model to the Cameo Test Harness REST API to ensure all advanced syntax (variants, derives, state events) load seamlessly.
4. If the LLM lacks the skill to write these advanced concepts properly, we will harvest the correct syntax and update `LLM_skills/skill.md`.
