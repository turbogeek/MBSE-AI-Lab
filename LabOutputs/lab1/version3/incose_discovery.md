# INCOSE Systems Engineering Template - 2-Slot Toaster

## 1. Business or Mission Analysis
### 1.1 Problem Statement
Consumers require a quick, reliable, and safe appliance to evenly toast bread slices in a standard kitchen environment.

### 1.2 Mission Objectives
Develop a standard 2-slot toaster that provides consistent toasting, adjustable browning control, and prioritizes user safety (e.g., burn prevention and electrical safety).

### 1.3 Key Stakeholders (Business Level)
- End Users (Consumers)
- Manufacturer
- Safety Regulatory Agencies (e.g., UL, CE)

## 2. Stakeholder Needs and Requirements Definition
### 2.1 Use Cases / Operational Scenarios
- User inserts bread, sets the browning level, and lowers the lever.
- The toaster heats the bread evenly for a specified duration based on the timer.
- Once the timer expires, the toaster automatically pops the bread up and cuts power to the heating elements.
- User can manually cancel the toasting process midway.

### 2.2 Measures of Effectiveness (MOEs)
- Toasting evenness across both sides.
- Time to complete a toasting cycle.
- Exterior surface temperature (burn prevention).

## 3. System Requirements Definition
### 3.1 Functional Requirements
- The system must provide two standard-sized bread slots.
- The system must apply heat using electrical heating elements.
- The system must include a timer mechanism adjustable by the user.
- The system must include a pop-up mechanism to eject the bread.
- The system must include a manual cancel function.

### 3.2 Non-Functional / Quality Requirements
- Power consumption should not exceed 1000 watts.
- Exterior housing must remain cool to the touch (under 50°C).
- The toaster must have a lifespan of at least 5 years under normal daily use.

### 3.3 System Constraints
- Must operate on standard 120V (or local standard) AC power.
- Must comply with residential appliance safety standards.

## 4. Architecture Definition
### 4.1 Logical Architecture / Subsystems
- HeatingSystem: Generates thermal energy to toast the bread.
- ControlSystem: Manages the timer, browning settings, and manual cancel.
- EjectionSystem: Handles the mechanical lowering and raising of the bread.
- PowerSystem: Manages electrical flow from the wall outlet to the components.

### 4.2 Interfaces and Interactions
- PowerSystem provides electrical power to the HeatingSystem and ControlSystem.
- ControlSystem signals the EjectionSystem and HeatingSystem to stop when the timer finishes.
- EjectionSystem physically holds the bread and interacts with the user.

### 4.3 System Behavior
- States: Idle, Toasting, Ejecting, Cancelled.

## 5. Design Definition
### 5.1 Physical Components
- Nichrome Heating Elements
- Mechanical Spring Lever
- Microcontroller / Mechanical Timer Dial
- AC Power Cord and Plug
- Heat-resistant Plastic/Metal Casing

### 5.2 Allocation Matrix
- Nichrome Heating Elements -> HeatingSystem
- Mechanical Spring Lever -> EjectionSystem
- Mechanical Timer Dial -> ControlSystem
- AC Power Cord and Plug -> PowerSystem
- Casing -> Physical housing
