# INCOSE Systems Engineering Template: Solar-Powered Insect Hunting Drone

This document gathers information according to the standard INCOSE Systems Engineering Handbook life cycle stages for the Solar-Powered Insect Hunting Drone.

## 1. Business or Mission Analysis

### 1.1 Problem Statement
Agricultural and outdoor areas often suffer from pest insect infestations, requiring constant monitoring and elimination without harming beneficial insects. Traditional methods involve chemical pesticides, which are environmentally damaging and non-selective. A targeted, autonomous, solar-powered mechanical solution is required to hunt and eliminate pests.

### 1.2 Mission Objectives
* **Autonomous Pest Control**: Eliminate pest insects autonomously using a laser weapon within a defined geofence.
* **Environmental Sustainability**: Operate sustainably using solar power for self-charging in the field.
* **Safety Protocols**: Ensure safety for humans, animals, and the drone environment.
* **Traceability and Telemetry**: Report system health, locations, and pest eliminations to human operators.

### 1.3 Key Stakeholders (Business Level)
* **Agricultural/Land Managers**: End-users who require effective pest reduction.
* **Environmental Regulators**: Ensure minimal ecological disruption, selective targeting, and zero carbon operations.
* **Safety Authorities**: Enforce compliance with laser radiation safety standards and unmanned flight regulations.
* **System Maintainers**: Personnel managing drone health, fleet logistics, and telemetry systems.

---

## 2. Stakeholder Needs and Requirements Definition

### 2.1 Use Cases / Operational Scenarios
* **Patrol and Hunt**: Autonomous flight within a geofenced area, utilizing AI vision to identify, classify, and target pests.
* **Autonomous Solar Recharge**: Autonomously land at a base station to deploy solar arrays, recharge batteries, and resume the mission.
* **Telemetry and Reporting**: Periodically transmit flight state, battery levels, and kill count telemetry.
* **Emergency Safety Stop**: Autonomously shut down the laser and rotors when safety limits are breached.

### 2.2 Measures of Effectiveness (MOEs)
* **MOE-01: Pest Reduction Rate**: Confirmed pest eliminations per day (Target: > 500 pests/day).
* **MOP-01: Flight Time**: Continuous operational flight duration on a full charge (Target: > 45 minutes).
* **MOP-02: Total Weight**: Total takeoff weight including 1 kg payload (Target: < 5 kg).
* **TPM-01: Peak Power Consumption**: Total power draw during active laser firing (Target: < 250W).
* **TPM-02: Unit Production Cost**: Target cost of the system (Target: < $1500).

---

## 3. System Requirements Definition

### 3.1 Functional Requirements
* **REQ1: Autonomous Navigation**: The system shall navigate autonomously along its planned trajectory within a tolerance of +/- 2.0 meters horizontally and +/- 0.5 meters vertically.
* **REQ2: Geofence Enforcement**: The system shall automatically halt horizontal progression within 5.0 meters of the boundary of a predefined 1x1 mile geofence, and shall not exceed a maximum altitude of 25.0 meters AGL.
* **REQ3: Computer Vision Identification**: The system shall identify and classify insects as "pest" or "beneficial" with a minimum accuracy of 95% under nominal daylight conditions.
* **REQ4: Pest Elimination**: Upon positive identification of a pest, the system shall engage the target using its laser weapon, achieving a confirmed elimination rate of at least 90% per attempted engagement.
* **REQ5: Target Discrimination**: The system shall exhibit a false-positive engagement rate (engaging a beneficial insect or non-insect) of less than 0.1%.
* **REQ6: Autonomous Recharge**: Upon detecting battery capacity below 20%, the system shall autonomously land within a 1.0 x 1.0 meter safe zone and recharge its batteries to 100% capacity using integrated solar panels within 6 hours of optimal sunlight.
* **REQ7: Telemetry Reporting**: The system shall transmit a telemetry and status report (including battery level, location, and kill count) to the human operator interface at an interval not exceeding 5 minutes.

### 3.2 Non-Functional / Quality Requirements
* **REQ8: Battery Thermal Safety**: The system shall continuously monitor battery temperature and autonomously sever the main power circuit within 500 milliseconds if the temperature exceeds 60°C.
* **REQ9: Laser Interlock Safety**: The system shall disable the laser firing circuit within 50 milliseconds if the drone's pitch or roll exceeds 30 degrees, or if a human is detected in the field of view.
* **REQ10: Rotor Safety**: The system's rotor assemblies shall feature physical guards that prevent a 1-inch diameter spherical probe from contacting the spinning blades from any lateral direction.

### 3.3 System Constraints
* **REQ11: Flight Time Limit**: The system shall maintain an operational flight duration of at least 45 minutes on a full battery charge.
* **REQ12: Weight Limit**: The system takeoff weight, including all subsystems and a maximum payload capacity of 1 kg, shall be less than 5.0 kg.

---

## 4. Architecture Definition

### 4.1 Logical Architecture / Subsystems
* **NavigationSystem**: Handles GPS coordinate processing and path planning.
* **LaserWeapon**: Focuses and fires the insect-killing laser beam under safety interlock constraints.
* **SensorArray**: Gathers optical and IR data, executing AI computer vision algorithms to identify insects.
* **PowerSubsystem**: Manages the batteries, thermal safety limits, and solar recharging interfaces.
* **FlightController**: Commands the flight control surfaces and motors to maintain flight stability.

### 4.2 Interfaces and Interactions
* **Power Link**: PowerSubsystem supplies electrical energy to all other subsystems (Navigation, Laser, Sensor, FlightController).
* **Control Link**: FlightController receives navigation guidance from NavigationSystem and safety signals from PowerSubsystem/SensorArray.
* **Safety Interlock**: SensorArray and flight attitude sensors send disable signals directly to the LaserWeapon.

### 4.3 System Behavior & Power Estimates (Logical Level)
The drone exhibits the following state transitions:
1. **Charging**: Landed, panels active. Transitions to Patrolling once battery is full.
2. **Patrolling**: Airborn, scanning geofenced field. Transitions to Targeting on insect detection, or Charging on low battery.
3. **Targeting**: Classifying insect. Transitions to Engaging if a pest is confirmed, or Patrolling if beneficial/uncertain.
4. **Engaging**: Laser active, tracking pest. Transitions to Patrolling once target is destroyed.
5. **Emergency Halt**: Triggered if battery temp > 60°C, pitch/roll > 30°, or human detected. All motors and lasers are disabled.

**Logical Power Estimates:**
* Motors & Flight Controller: ~150W
* Laser Weapon (Peak): ~50W
* Sensor Array & Vision Compute: ~30W
* Power Subsystem Management: ~10W
* Navigation / Comms: ~5W
* **Total Logical Estimate: ~245W**

---

## 5. Design Definition

### 5.1 Physical Components
* **GPSModule**: High-accuracy GPS receiver and antenna.
* **BlueLaserModule**: A 5W Blue Laser module with high efficiency and built-in hardware disable pin.
* **RGBAndIRCamera**: High-resolution camera paired with an AI processor (e.g. Jetson Nano) running the insect classification model.
* **LiPoBatterySolar**: Lightweight Li-Po battery pack with integrated solar film and thermistor arrays.
* **QuadCopterFrame**: Carbon-fiber quadcopter frame with integrated blade guards and brushless DC motors.

### 5.2 Allocation Matrix & Power Estimates (Physical Level)
* **GPSModule** satisfies **NavigationSystem**
* **BlueLaserModule** satisfies **LaserWeapon**
* **RGBAndIRCamera** satisfies **SensorArray**
* **LiPoBatterySolar** satisfies **PowerSubsystem**
* **QuadCopterFrame** satisfies **FlightController**

**Physical Power Estimates:**
* Quadcopter Motors & Frame: ~160W
* Blue Laser Module: ~45W
* RGB and IR Camera with Processor: ~15W (compute) + ~10W (camera) = ~25W
* GPS and Comms Module: ~5W
* Power Management Electronics: ~8W
* **Total Physical Estimate: ~243W**
