# INCOSE Systems Engineering Template: Solar-Powered Insect Hunting Drone

## 1. Business or Mission Analysis

### 1.1 Problem Statement

Agricultural environments and certain residential areas suffer from pest insects that damage crops or spread disease. Traditional chemical pesticides have negative environmental impacts. There is a need for a targeted, chemical-free, autonomous pest control solution.

### 1.2 Mission Objectives

* To autonomously identify and eliminate pest insects within a designated geofenced area.
* To operate sustainably using solar energy for charging.
* To distinguish between harmful pests and beneficial insects (e.g., bees) with high accuracy.
* To report operational data and pest statistics back to human operators.

### 1.3 Key Stakeholders (Business Level)

* **Farmers/Property Owners**: Primary users and beneficiaries of pest reduction.
* **Environmental Agencies**: Interested in chemical-free pest control methods.
* **Human Operator**: Person monitoring and managing the drone fleet.

## 2. Stakeholder Needs and Requirements Definition

### 2.1 Use Cases / Operational Scenarios

* **Autonomous Hunting Cycle**: Drone takes off, patrols the geofenced area, uses AI vision to identify pests, fires a low-power laser to eliminate them, and continues patrol.
* **Solar Recharging**: Upon low battery, the drone lands in a designated safe area, deploys or exposes solar panels, recharges, and resumes hunting.
* **Beneficial Insect Avoidance**: Drone detects a bee or butterfly, categorizes it as beneficial, logs the encounter, and withholds laser fire.
* **Reporting**: Drone continuously or periodically transmits GPS location, battery status, and elimination metrics to the operator.

### 2.2 Measures of Effectiveness (MOEs)

* **Elimination Rate**: Number of pests eliminated per hour of flight.
* **Accuracy**: False positive rate (beneficial insects incorrectly targeted) must be 0%.
* **Uptime**: Ratio of hunting time to charging time under standard daylight conditions.

## 3. System Requirements Definition

### 3.1 Functional Requirements

* The system shall autonomously navigate using GPS within a defined 1 mile by 1 mile geofence.
* The system shall use computer vision and AI to classify insects as pest or beneficial.
* The system shall eliminate identified pests using a directed laser weapon.
* The system shall land and recharge its internal batteries using integrated solar panels.
* The system shall transmit telemetry and mission logs to a human operator via a communication link.

### 3.2 Non-Functional / Quality Requirements

* The system shall be capable of operating in moderate rain and varying weather conditions.
* The system shall have a maximum operating altitude of 25 meters.
* The system shall carry a functional payload of up to 1 kg.
* The system's laser must be class-compliant to avoid unintended eye damage to humans or larger animals.

### 3.3 System Constraints

* Operations are restricted strictly within the defined geofenced coordinates.

## 4. Architecture Definition

### 4.1 Logical Architecture / Subsystems

* **FlightController**: Manages motor speeds, balance, and physical movement.
* **NavigationSystem**: Manages GPS coordinates, geofencing enforcement, and path planning.
* **SensorArray**: Includes cameras, weather sensors, and AI processing units.
* **WeaponSystem**: The laser emitter and targeting gimbals.
* **PowerSubsystem**: Manages the solar panels, battery storage, and power distribution.
* **CommunicationSystem**: Transmits and receives data to/from the base station.

### 4.2 Interfaces and Interactions

* **Sensor-to-Weapon Interface**: SensorArray identifies target and provides 3D coordinates to the WeaponSystem.
* **Nav-to-Flight Interface**: NavigationSystem provides trajectory waypoints to the FlightController.
* **Operator Interface**: CommunicationSystem links the internal drone data bus to the Human Operator.

### 4.3 System Behavior

* **State: Charging**: Drone is grounded, solar panels active, motors off, laser disabled.
* **State: Hunting**: Drone airborne below 25m, navigating geofence, sensors active.
* **State: Targeting**: Target acquired, AI confirms pest, flight stabilizes, laser fires, logs result.

## 5. Design Definition

### 5.1 Physical Components

* **Quad-Rotor Chassis**: Physical implementation of flight capability.
* **High-Res CMOS Cameras & Edge AI TPU**: Physical implementation of SensorArray.
* **Low-Power Blue Diode Laser**: Physical implementation of WeaponSystem.
* **Photovoltaic Canopy & Li-Po Battery**: Physical implementation of PowerSubsystem.
* **GPS Receiver Module & LTE/RF Antenna**: Physical implementation of Navigation and Communication systems.

### 5.2 Allocation Matrix

* The **High-Res CMOS Cameras & Edge AI TPU** satisfy the requirement to classify insects.
* The **Low-Power Blue Diode Laser** satisfies the requirement to eliminate pests.
* The **Photovoltaic Canopy** satisfies the requirement to recharge via solar energy.
* The **GPS Receiver Module** satisfies the requirement to navigate within the geofence.
