# INCOSE Systems Engineering Template: Solar-Powered Insect Hunting Drone

## 1. Business or Mission Analysis

### 1.1 Problem Statement

Insects and pests cause significant damage to agriculture and can be a nuisance in residential properties. Chemical pesticides are increasingly being phased out due to environmental damage and human health risks. A sustainable, precise, and autonomous method is needed to eradicate harmful insects without collateral damage.

### 1.2 Mission Objectives

* Eliminate targeted pest insects in a defined 1-mile by 1-mile geofenced area.
* Operate fully autonomously with self-charging solar capabilities.
* Ensure zero harm to beneficial insects (e.g., bees, butterflies) through AI-driven computer vision classification.

### 1.3 Key Stakeholders (Business Level)

* **Farmers/Landowners**: Rely on the drone to protect crops.
* **Environmental Protectors**: Require avoidance of beneficial insects and chemicals.
* **System Operators**: Manage deployment and receive telemetry.
* **Safety Regulators**: Require that autonomous laser operations are safe for humans and that the battery/rotors pose no danger.

## 2. Stakeholder Needs and Requirements Definition

### 2.1 Use Cases / Operational Scenarios

* **Search and Destroy**: The drone patrols the geofenced area, detects an insect, uses computer vision to identify it as a pest, and fires a laser to eliminate it.
* **Solar Charging**: When the battery drops below a critical threshold, the drone lands in a safe, unshaded area, deploys its solar panels to recharge, and resumes the mission once full.
* **Emergency Stop**: If thermal sensors detect a battery anomaly or if the laser tilt sensor detects a human profile, the drone immediately halts operation, grounds itself safely, and alerts the operator.

### 2.2 Measures of Effectiveness (MOEs)

* **Pest Identification Accuracy**: 100% accuracy in identifying beneficial vs. pest insects.
* **Safety Incident Rate**: 0 incidents involving eye damage, battery fire, or rotor strikes.
* **Operational Uptime**: Must sustain long periods of autonomy using solar energy.

## 3. System Requirements Definition

### 3.1 Functional Requirements

* The system shall autonomously navigate via GPS within a geofenced area of 1 sq mile.
* The system shall identify and classify insects using computer vision and AI.
* The system shall eliminate classified pests using a directed laser weapon.
* The system shall land and recharge its battery using solar power.
* The system shall transmit mission logs and telemetry to the base station via a communication link.

### 3.2 Non-Functional / Quality Requirements

* The system shall operate safely in moderate rain.
* The system shall not exceed a maximum altitude of 25 meters.
* The system shall support a payload capacity of up to 1 kg.

### 3.3 System Constraints (Safety Protocols)

* The drone's rotor blades must have physical guards or emergency stop mechanisms to prevent injury.
* The battery subsystem must include thermal monitoring to prevent battery fires.
* The laser weapon must have safety interlocks (e.g., eye-safe compliance, disabling fire when tilted toward humans).

## 4. Architecture Definition

### 4.1 Logical Architecture / Subsystems

* **NavigationSystem**: Handles GPS, path planning, and geofence enforcement.
* **SensorArray**: Includes high-resolution cameras and AI processing for insect classification.
* **LaserWeapon**: Emits the focused beam to eliminate pests.
* **FlightController**: Manages the rotors, altitude, and flight stability.
* **CommunicationSystem**: Handles data transmission to the operator.
* **PowerSubsystem**: Manages solar charging, energy storage, and distribution.

### 4.2 Interfaces and Interactions

* **Sensor-Weapon Link**: The SensorArray passes 3D coordinates and a "fire authorization" signal to the LaserWeapon upon positive pest ID.
* **Power-Flight Link**: The PowerSubsystem commands the FlightController to land when battery reserves fall below a set threshold for solar charging.

### 4.3 System Behavior

* **State: Patrolling**: Airborne, scanning for insects, navigating via GPS.
* **State: Targeting**: Hovering, laser engaged, confirming kill.
* **State: Charging**: Landed, motors off, solar panels absorbing energy.
* **State: Emergency Grounded**: Landed, all offensive and flight systems disabled due to safety trigger.

## 5. Design Definition

### 5.1 Physical Components

* **Flight Controller Board**: Physical implementation of FlightController.
* **GPS & IMU Sensors**: Physical implementation of NavigationSystem.
* **AI Vision Processing Unit (VPU)**: Physical implementation of SensorArray.
* **Blue Diode Laser Module**: Physical implementation of LaserWeapon.
* **Photovoltaic Panels & Thermally Monitored Battery**: Physical implementation of PowerSubsystem.
* **LTE/RF Antenna**: Physical implementation of CommunicationSystem.

### 5.2 Allocation Matrix

* **AI Vision Processing Unit (VPU)** satisfies the requirement to identify and classify insects.
* **Blue Diode Laser Module** satisfies the requirement to eliminate classified pests.
* **Photovoltaic Panels & Thermally Monitored Battery** satisfies the requirements to land/recharge and prevent battery fires.
* **GPS & IMU Sensors** satisfies the requirement to autonomously navigate via GPS.
* **Flight Controller Board** satisfies the requirement to manage the rotors, altitude, and flight stability.
* **CommunicationSystem** satisfies the requirement to handle data transmission to the operator.
