# INCOSE Systems Engineering Template - Insect Hunting Drone

## 1. Business or Mission Analysis
### 1.1 Problem Statement
Insects and pests damage crops and disrupt human activities. Current solutions involve chemical pesticides that harm the environment, or manual removal which is labor-intensive and inefficient.

### 1.2 Mission Objectives
Develop an autonomous, solar-powered drone capable of identifying and eliminating pest insects using a laser weapon, while preserving beneficial insects and ensuring human safety.

### 1.3 Key Stakeholders (Business Level)
- Farmers / Agricultural Operators
- Human Operators
- Environmental Regulatory Agencies

## 2. Stakeholder Needs and Requirements Definition

### 2.1 Use Cases / Operational Scenarios
- The drone autonomously patrols a geofenced area.
- The drone lands and charges itself via solar energy when battery is low.
- The drone identifies insects using AI and sensors, determines if they are pests or beneficial.
- The drone eliminates pest insects using a laser weapon.
- The drone reports findings to a human operator via a communication system.

### 2.2 Measures of Effectiveness (MOEs)
- Overall performance (successful pest eradication rate).
- Total system weight.
- Flight time per charge.
- Number of bugs killed per mission.
- Estimated cost per unit.

## 3. System Requirements Definition

### 3.1 Functional Requirements
- Powered by solar energy: land, charge via solar, take off.
- Laser weapon: kill identified pest insects.
- Sensors and AI computer vision: identify insects (pest vs. beneficial).
- GPS navigation and communication: navigate and report to human operator.
- Autonomous operation: capable of completing missions before returning to base.

### 3.2 Non-Functional / Quality Requirements
- Weather resistance: operate in varied weather, including moderate rain.
- Payload capacity: up to 1 kg.
- Operating limits: max altitude of 25 meters, within a geofenced area (1 mile x 1 mile) around base station.

### 3.3 System Constraints
- Rotor safety: physical guards or emergency stop mechanisms.
- Battery safety: thermal monitoring to prevent battery fires.
- Laser safety: safety interlocks (eye-safe compliance, disabling fire when tilted toward humans).

## 4. Architecture Definition

### 4.1 Logical Architecture / Subsystems
- NavigationSystem: handles GPS and flight path.
- LaserWeapon: handles targeting and firing.
- SensorArray: handles vision, target identification.
- PowerSubsystem: handles solar charging, battery, thermal monitoring.
- FlightController: manages flight states, emergency stops, coordination.

### 4.2 Interfaces and Interactions
- Power subsystem provides power to all components.
- Sensor array sends targeting data to FlightController and LaserWeapon.
- Navigation subsystem provides location data.
- Communication system reports to HumanOperator.

### 4.3 System Behavior
- States: Patrolling, Targeting, Firing Laser Safely, Returning for Charging, Emergency Stop.

## 5. Design Definition

### 5.1 Physical Components
- Solar Panels
- Battery Pack with Thermal Sensors
- Rotor Blades with Physical Guards
- Camera / Vision Sensors
- GPS Module
- RF Communications Module
- Microcontroller / AI Processor
- Laser Emitter with Tilt Sensors

### 5.2 Allocation Matrix
- Solar Panels + Battery Pack -> PowerSubsystem
- Camera -> SensorArray
- Microcontroller -> FlightController
- GPS Module -> NavigationSystem
- Laser Emitter -> LaserWeapon
