# Insect Hunting Drone: System Verification & Test Plan

This document defines the verification approach and test procedures for the functional requirements outlined in the `incose_discovery.md` document. The verification methods follow standard INCOSE practices: Inspection (I), Analysis (A), Demonstration (D), and Test (T).

## Verification Matrix & Procedures

### 1. Autonomous Navigation

* **Requirement:** The system shall navigate autonomously along its planned trajectory within a tolerance of +/- 2.0 meters horizontally and +/- 0.5 meters vertically.
* **Method:** Test (T)
* **Procedure:** Deploy the drone in an open test field. Upload a predefined waypoint path. Track the actual trajectory using an independent, high-precision RTK GPS tracker attached to the drone's chassis. Calculate the deviation between the planned path and actual path.
* **Pass Criteria:** Maximum recorded deviation is ≤ 2.0m horizontally and ≤ 0.5m vertically across the entire flight.

### 2. Geofence Enforcement

* **Requirement:** The system shall automatically halt horizontal progression within 5.0 meters of the boundary of a predefined 1x1 mile geofence, and shall not exceed a maximum altitude of 25.0 meters AGL.
* **Method:** Demonstration (D)
* **Procedure:** Upload a 1x1 mile geofence profile. Command the drone via the operator interface to fly to a waypoint outside the geofence boundary at an altitude of 30 meters. Observe the drone's telemetry and physical position.
* **Pass Criteria:** The drone halts its horizontal movement at least 5.0m before the boundary line and its altitude peaks at ≤ 25.0m AGL.

### 3. Computer Vision Identification

* **Requirement:** The system shall identify and classify insects as "pest" or "beneficial" with a minimum accuracy of 95% under nominal daylight conditions.
* **Method:** Test (T)
* **Procedure:** Expose the drone's optical sensors to a standardized set of 1,000 live and mock insect encounters (50% pest, 50% beneficial) inside a controlled flight chamber illuminated at >500 lux (daylight equivalent). Log the AI classifications.
* **Pass Criteria:** The system correctly classifies ≥ 950 of the 1,000 targets.

### 4. Pest Elimination

* **Requirement:** Upon positive identification of a pest, the system shall engage the target using its laser weapon, achieving a confirmed elimination rate of at least 90% per attempted engagement.
* **Method:** Test (T)
* **Procedure:** Release 100 sterile target pests in an enclosed test environment. Allow the drone to autonomously engage. Record the number of laser firing events and physically count the number of successfully eliminated targets.
* **Pass Criteria:** (Eliminated Targets / Total Engagements) ≥ 0.90.

### 5. Target Discrimination

* **Requirement:** The system shall exhibit a false-positive engagement rate (engaging a beneficial insect or non-insect) of less than 0.1%.
* **Method:** Test (T)
* **Procedure:** Release 1,000 known beneficial insects into the test environment. Allow the drone to operate autonomously for 4 hours. Record all laser engagement events.
* **Pass Criteria:** Zero (0) or at most one (1) engagement event is recorded against a beneficial insect.

### 6. Autonomous Recharge

* **Requirement:** Upon detecting battery capacity below 20%, the system shall autonomously land within a 1.0 x 1.0 meter safe zone and recharge its batteries to 100% capacity using integrated solar panels within 6 hours of optimal sunlight.
* **Method:** Demonstration (D) / Test (T)
* **Procedure:** Fly the drone until telemetry indicates battery capacity has dropped to 19%. Observe the autonomous return-to-base and landing sequence. Verify the physical landing coordinates. Then, expose the landed drone to a standard AM1.5 solar simulator.
* **Pass Criteria:** Landing gear touches down entirely within the marked 1.0 x 1.0 meter zone, and the battery management system reports 100% charge within 6 hours of exposure.

### 7. Telemetry Reporting

* **Requirement:** The system shall transmit a telemetry and status report (including battery level, location, and kill count) to the human operator interface at an interval not exceeding 5 minutes.
* **Method:** Test (T)
* **Procedure:** Run the drone in a simulated patrol mode for 60 minutes. Log all incoming telemetry packets at the operator station. Calculate the time delta between each consecutive packet.
* **Pass Criteria:** The time delta between any two consecutive status reports is ≤ 300 seconds.

### 8. Battery Thermal Safety

* **Requirement:** The system shall continuously monitor battery temperature and autonomously sever the main power circuit within 500 milliseconds if the temperature exceeds 60°C.
* **Method:** Test (T)
* **Procedure:** In a lab environment, bypass the physical thermistor with a signal generator mimicking a 61°C temperature spike. Use an oscilloscope to measure the time delay between the signal insertion and the main power relay voltage dropping to 0V.
* **Pass Criteria:** Time delay is ≤ 500 milliseconds.

### 9. Laser Interlock Safety

* **Requirement:** The system shall disable the laser firing circuit within 50 milliseconds if the drone's pitch or roll exceeds 30 degrees, or if a human is detected in the field of view.
* **Method:** Test (T)
* **Procedure:** (A) While the laser firing circuit is active (laser physically disconnected/safed for lab safety), mechanically tilt the drone's IMU to > 30 degrees. (B) Introduce a human visual dummy into the camera's FOV. Use an oscilloscope to measure the time to circuit deactivation in both scenarios.
* **Pass Criteria:** Circuit deactivation occurs within ≤ 50 milliseconds in both scenarios.

### 10. Rotor Safety

* **Requirement:** The system's rotor assemblies shall feature physical guards that prevent a 1-inch diameter spherical probe from contacting the spinning blades from any lateral direction.
* **Method:** Inspection (I)
* **Procedure:** With the drone completely powered off and safed, an inspector utilizes a standardized 1-inch diameter rigid spherical probe. The inspector attempts to push the probe through the gaps in the rotor guards from all lateral angles toward the blade path.
* **Pass Criteria:** The probe physically cannot enter the blade envelope from any lateral angle without deforming the guard beyond its elastic limit.
