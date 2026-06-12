# Requirements Writing Skill

## Overview
This skill defines the best practices for writing high-quality, testable systems engineering requirements. Following these guidelines ensures that requirements are clear, concise, unambiguous, and verifiable.

## 1. Characteristics of Good Requirements (INCOSE standard)
- **Necessary:** The requirement must define an essential capability. If it is removed, a deficiency exists.
- **Appropriate:** The level of detail must align with the architectural level (e.g., Stakeholder vs. System vs. Component).
- **Unambiguous:** The requirement can only be interpreted in one way.
- **Complete:** It needs no further amplification to be understood.
- **Singular:** It states only one capability or constraint. Avoid "and", "or" combinations.
- **Feasible:** It can be realized within the constraints of the project (cost, schedule, technology).
- **Verifiable/Testable:** There must be a definitive way to prove the requirement was met via Inspection, Analysis, Demonstration, or Test (IADT).

## 2. Standard Requirement Structure (EARS / INCOSE)
Requirements should generally follow a standard template:
`[Condition] The [System] shall [Action] [Object] [Performance Measure].`

*Example:* "While in autonomous flight mode, the drone shall maintain its altitude within +/- 0.5 meters of the target altitude."

## 3. Writing for Testability
To make a requirement testable, apply the following rules:
1. **Quantify:** Replace vague adjectives ("fast", "reliable", "safe", "accurate") with precise numbers and tolerances.
   - *Bad:* The system shall use computer vision to identify insects.
   - *Good:* The system shall identify target insects with a minimum accuracy of 95% under daylight conditions.
2. **Specify Conditions:** State the operating conditions under which the performance must be met (e.g., under nominal load, in moderate rain).
3. **Avoid Design Constraints:** Focus on *what* the system must do, not *how* it does it, unless the 'how' is a strict constraint.
   - *Bad:* The system shall use GPS for navigation.
   - *Good:* The system shall navigate autonomously within a tolerance of +/- 2 meters of the planned trajectory.
4. **Use 'Shall':** Use "shall" to indicate a binding requirement, "will" to indicate a statement of fact, and "should" to indicate a goal or preference.
5. **Clear Triggers:** For reactive requirements, clearly state the trigger.
   - *Good:* Upon detecting a battery temperature exceeding 60°C, the system shall disable the main power relay within 500 milliseconds.

## 4. Verification Methods (IADT)
Every requirement must map to a verification method:
- **Inspection (I):** Verification through visual or physical examination (e.g., verifying a physical rotor guard exists).
- **Analysis (A):** Verification using models, calculations, or simulations (e.g., proving structural integrity under stress).
- **Demonstration (D):** Verification by observing expected operational behavior without precise measurement (e.g., showing the drone can land).
- **Test (T):** Verification using controlled, measurable, and repeatable procedures with defined pass/fail criteria (e.g., measuring battery temperature response times).
