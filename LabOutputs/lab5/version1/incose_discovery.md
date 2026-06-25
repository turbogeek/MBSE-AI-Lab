# INCOSE Systems Engineering Template - IoT Super Toaster SoS

## 1. Business or Mission Analysis

### 1.1 Problem Statement

Modern consumers demand high-performance, connected kitchen appliances. Traditional toasters lack precision, remote monitoring, predictive maintenance, and advanced safety features, leading to suboptimal toasting experiences and potential fire hazards.

### 1.2 Mission Objectives

Develop an advanced IoT-connected "Super Toaster" as a System of Systems (SoS). The system must provide rapid, perfect toasting, remote management via a smartphone app, cloud-based telemetry for manufacturer MTBF tracking, and automatic emergency service dispatch in case of thermal runaway.

### 1.3 Key Stakeholders (Business Level)

- Consumers (End Users)
- Appliance Manufacturer
- Emergency Services (Fire Department)

## 2. Stakeholder Needs and Requirements Definition

### 2.1 Use Cases / Operational Scenarios

- **Smart Toasting:** User configures toasting preferences via the mobile app; the toaster heats the bread and alerts the app when done.
- **Telemetry Logging:** Toaster sends usage statistics and sensor data to the Manufacturer's Cloud for MTBF analysis and firmware updates.
- **Emergency Response:** If thermal runaway or a fire is detected, the toaster attempts an emergency shutoff and automatically notifies the Fire Department API.

### 2.2 Measures of Effectiveness (MOEs)

- Toasting time (must be under 2 minutes for 2 slices).
- System MTBF (target 10,000 hours).
- Total system weight (maximum 3 kg).
- Peak power consumption (maximum 1500W).

## 3. System Requirements Definition

### 3.1 Functional Requirements

- The system must toast 2 slices of bread simultaneously.
- The toaster must connect to a Wi-Fi network to interact with the Smartphone App, IoT Cloud, and Fire Department API.
- The system must support variation in heating technology (Nichrome Coil vs. Induction Heating) and chassis color.

### 3.2 Non-Functional / Quality Requirements

- **Performance:** Toasting cycle must complete perfectly in under 2 minutes.
- **Power:** Peak power consumption shall not exceed 1500W.
- **Reliability:** Target Mean Time Between Failures (MTBF) shall be 10,000 hours.
- **Physical:** Maximum weight of the toaster unit shall not exceed 3 kg.

### 3.3 System Constraints

- Must comply with standard residential electrical constraints.
- Must ensure secure communication (API/Wi-Fi) with external cloud and emergency services.

## 4. Architecture Definition

### 4.1 Logical Architecture / Subsystems

- **SuperToaster_SoS (Context):** The overarching System of Systems.
- **LogicalController:** Manages state transitions, Wi-Fi communication, and action execution.
- **HeatingSubsystem:** Handles thermal energy generation (variable by technology).
- **SensorArray:** Monitors temperature and detects smoke/fire.
- **MobileApp:** User interface for remote configuration.
- **ManufacturerCloud:** Centralized logging and firmware server.
- **EmergencyService:** External Fire Department API integration.

### 4.2 Interfaces and Interactions

- Wi-Fi data link between LogicalController and local router to MobileApp, ManufacturerCloud, and EmergencyService.
- API connections for telemetry data (to Cloud) and emergency dispatch signals (to Fire Dept).

### 4.3 System Behavior

- **SmartToastingProcess (Swimlanes):**
  - `userConfigures` (via MobileApp)
  - `toasterHeats` (via HeatingSubsystem)
  - `cloudLogs` (via ManufacturerCloud)
  - `detectsSmoke` (via SensorArray triggering EmergencyShutoff)
- **State Machine:** States include `Standby`, `Heating`, `Cooling`, `MaintenanceRequired`, and `EmergencyShutoff`.

## 5. Design Definition

### 5.1 Physical Components

- **Chassis:** Available in various color variants.
- **Heating Element:** Specialized into `NichromeCoil` or `InductionHeating` variants.
- **IoT Microcontroller:** Handles logic and Wi-Fi.

### 5.2 Allocation Matrix

- LogicalController is allocated to the IoT Microcontroller.
- HeatingSubsystem is allocated to the chosen Heating Element variant.
- Cloud telemetry actions are allocated to the ManufacturerCloud interface.
