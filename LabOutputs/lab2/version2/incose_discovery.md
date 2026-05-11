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

## 2. Stakeholder Needs and Requirements Definition

### 2.1 Use Cases / Operational Scenarios

* **Search and Destroy**: The drone patrols the geofenced area, detects an insect, uses computer vision to identify it as a pest, and fires a laser to eliminate it.
* **Solar Charging**: When the battery drops below a critical threshold, the drone lands in a safe, unshaded area, deploys its solar panels to recharge, and resumes the mission once full.
* **Beneficial Insect Encounter**: The drone identifies an insect as beneficial (e.g., a bee), logs the encounter, and aborts targeting.

### 2.2 Measures of Effectiveness (MOEs)

* **Pest Identification Accuracy**: 100% accuracy in identifying beneficial vs. pest insects.
* **Operational Uptime**: Must sustain long periods of autonomy using solar energy.
* **Weather Resilience**: Ability to operate effectively in moderate rain without damage.

## 3. System Requirements Definition

### 3.1 Functional Requirements

* The system shall autonomously navigate via GPS within a geofenced area of 1 sq mile.
* The system shall identify and classify insects using computer vision and AI.
* The system shall eliminate classified pests using a directed laser weapon.
* The system shall land and recharge its battery using solar power.
* The system shall transmit mission logs and video/telemetry to the base station via a communication link.

### 3.2 Non-Functional / Quality Requirements

* The system shall operate safely in moderate rain.
* The system shall not exceed a maximum altitude of 25 meters.
* The system shall support a payload capacity of up to 1 kg.

### 3.3 System Constraints

* The drone's laser must be properly calibrated to ensure it is lethal to insects but harmless to humans and large animals.
* Operations are restricted to the 1-mile by 1-mile geofenced boundary.

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

## 5. Design Definition

### 5.1 Physical Components

* **Flight Controller Board**: Physical implementation of FlightController.
* **GPS & IMU Sensors**: Physical implementation of NavigationSystem.
* **AI Vision Processing Unit (VPU)**: Physical implementation of SensorArray.
* **Blue Diode Laser Module**: Physical implementation of LaserWeapon.
* **Photovoltaic Panels & Battery**: Physical implementation of PowerSubsystem.
* **LTE/RF Antenna**: Physical implementation of CommunicationSystem.

### 5.2 Allocation Matrix

* **AI Vision Processing Unit (VPU)** satisfies the requirement to identify and classify insects.
* **Blue Diode Laser Module** satisfies the requirement to eliminate classified pests.
* **Photovoltaic Panels & Battery** satisfies the requirement to land and recharge.
* **GPS & IMU Sensors** satisfies the requirement to autonomously navigate via GPS.
