# INCOSE Systems Engineering Template: Autonomous Insect Hunting Drone

This document gathers information according to the standard INCOSE Systems Engineering Handbook life cycle stages for the Insect Hunting Drone.

## 1. Business or Mission Analysis

### 1.1 Problem Statement

Agricultural and outdoor areas often suffer from pest insect infestations, requiring constant monitoring and elimination without harming beneficial insects. Traditional methods involve pesticides, which are environmentally damaging. A targeted, autonomous mechanical solution is required.

### 1.2 Mission Objectives

* Eliminate pest insects autonomously using a laser weapon within a defined geofence.
* Preserve beneficial insects through AI computer vision identification.
* Operate sustainably using solar power for self-charging in the field.
* Ensure stringent safety for humans, animals, and the environment.

### 1.3 Key Stakeholders

* **Agricultural/Land Managers**: End users managing pest control.
* **Environmental Regulators**: Ensure minimal ecological disruption and safe operation.
* **Safety Authorities**: Ensure laser and flight safety compliance.
* **System Maintainers**: Personnel managing drone health and telemetry.

## 2. Stakeholder Needs and Requirements Definition

### 2.1 Use Cases / Operational Scenarios

* **Patrol and Hunt**: Autonomous flight within a 1 mile x 1 mile geofence up to 25m altitude, scanning for insects, classifying them, and engaging pests with the laser.
* **Recharge**: Detect low battery, safely land, deploy solar panels, and recharge before resuming the hunt.
* **Data Reporting**: Transmit kill counts and status to the human operator via the communication system.
* **Emergency Halt**: Automatically shut down lasers and rotors if a human is detected, weather degrades beyond limits, or a thermal anomaly is detected.

### 2.2 MOEs, MOPs, and TPMs

* **Measures of Effectiveness (MOEs)**
  * **Overall Performance**: Sustained pest reduction in the geofenced area.
  * **Bugs Killed**: Number of confirmed pest eliminations per day (Target: > 500 pests/day).
* **Measures of Performance (MOPs)**
  * **Flight Time**: Continuous operational flight duration on a full charge (Target: > 45 minutes).
  * **Weight**: Total takeoff weight including 1 kg payload (Target: < 5 kg).
* **Technical Performance Measures (TPMs)**
  * **Power Consumption**: Total power draw during laser engagement (Target: < 250W).
  * **Estimated Cost**: Unit production cost (Target: < $1500).

## 3. System Requirements Definition

### 3.1 Functional Requirements

1. **Autonomous Navigation:** The system shall navigate autonomously along its planned trajectory within a tolerance of +/- 2.0 meters horizontally and +/- 0.5 meters vertically.
2. **Geofence Enforcement:** The system shall automatically halt horizontal progression within 5.0 meters of the boundary of a predefined 1x1 mile geofence, and shall not exceed a maximum altitude of 25.0 meters AGL.
3. **Computer Vision Identification:** The system shall identify and classify insects as "pest" or "beneficial" with a minimum accuracy of 95% under nominal daylight conditions.
4. **Pest Elimination:** Upon positive identification of a pest, the system shall engage the target using its laser weapon, achieving a confirmed elimination rate of at least 90% per attempted engagement.
5. **Target Discrimination:** The system shall exhibit a false-positive engagement rate (engaging a beneficial insect or non-insect) of less than 0.1%.
6. **Autonomous Recharge:** Upon detecting battery capacity below 20%, the system shall autonomously land within a 1.0 x 1.0 meter safe zone and recharge its batteries to 100% capacity using integrated solar panels within 6 hours of optimal sunlight.
7. **Telemetry Reporting:** The system shall transmit a telemetry and status report (including battery level, location, and kill count) to the human operator interface at an interval not exceeding 5 minutes.
8. **Battery Thermal Safety:** The system shall continuously monitor battery temperature and autonomously sever the main power circuit within 500 milliseconds if the temperature exceeds 60°C.
9. **Laser Interlock Safety:** The system shall disable the laser firing circuit within 50 milliseconds if the drone's pitch or roll exceeds 30 degrees, or if a human is detected in the field of view.
10. **Rotor Safety:** The system's rotor assemblies shall feature physical guards that prevent a 1-inch diameter spherical probe from contacting the spinning blades from any lateral direction.

### 3.2 Non-Functional / Quality Requirements

* **Payload**: The system shall support a payload capacity of up to 1 kg.
* **Weather Resistance**: The system shall operate safely in varied weather, including moderate rain.
* **Safety - Rotors**: The system shall have physical guards or emergency stop mechanisms on all rotor blades to prevent injury.
* **Safety - Battery**: The system's battery subsystem shall include thermal monitoring to prevent thermal runaway/fires.
* **Safety - Laser**: The laser weapon shall have safety interlocks (eye-safe compliance, disabling fire when tilted toward humans).

## 4. Architecture Definition

### 4.1 Logical Architecture / Subsystems

* **Flight Controller Subsystem**: Manages flight stability, navigation, and geofencing.
* **Laser Weapon Subsystem**: Handles targeting and firing the laser.
* **Sensor Array Subsystem**: Houses cameras and sensors for AI computer vision.
* **Power Subsystem**: Manages battery storage, thermal monitoring, and solar charging.
* **Navigation and Communication Subsystem**: GPS and data links.

### 4.2 Power Consumption Estimates (Logical Level)

* Motors: ~150W
* Electronics/Sensors: ~30W
* Laser Weapon: ~50W (Peak)
* Total Estimated: ~230W

### 4.3 System Behavior (State Machine Overview)

* **Charging**: Landed, charging via solar. Transitions to Patrolling when battery is full.
* **Patrolling**: Flying within geofence, searching for targets.
* **Targeting**: Target detected. AI evaluates pest vs beneficial.
* **Engaging**: Laser active (safety interlocks permitting).
* **Emergency Halt**: Rotors/Laser disabled due to safety triggers.

## 5. Design Definition

### 5.1 Physical Architecture

* **Flight Controller**: Pixhawk or similar ECU.
* **Laser Weapon**: 5W Blue Laser module with tilt-sensor interlock.
* **Sensor Array**: High-frame-rate RGB camera and IR sensor.
* **Power Subsystem**: Li-Po Battery Pack with thermistor array, lightweight solar film array.
* **Communication**: Wifi
* **GPS**: gps module with antenna
* **Processor**: ai computer vision
*

### 5.2 Power Consumption Estimates (Physical Level)

* Quad Brushless Motors: 160W
* AI Compute Module (e.g., Jetson Nano): 15W
* Communication/GPS: 5W
* Laser Module: 45W
* Total Physical Estimate: 225W
