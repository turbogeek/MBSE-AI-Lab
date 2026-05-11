# INCOSE Systems Engineering Template: Standard 2-Slot Toaster

## 1. Business or Mission Analysis

### 1.1 Problem Statement
Consumers require a quick, reliable, and safe method to toast bread products in residential kitchens.

### 1.2 Mission Objectives
* To toast 1 to 2 slices of bread evenly.
* To provide user-adjustable browning control.
* To operate safely without burning users or causing fires.

### 1.3 Key Stakeholders (Business Level)
* **Home Users**: The primary end-users who operate the toaster.
* **Safety Regulators**: Ensure the product meets electrical and fire safety standards.
* **Manufacturers**: Focus on cost-effective assembly and reliability.

## 2. Stakeholder Needs and Requirements Definition

### 2.1 Use Cases / Operational Scenarios
* **Toast Bread**: User inserts bread, sets browning level, and presses the lever. The toaster heats the bread until the timer expires, then pops it up.
* **Cancel Toasting**: User decides to stop toasting early and presses a cancel button to immediately pop up the bread and stop heating.

### 2.2 Measures of Effectiveness (MOEs)
* **Time to Toast**: Under 3 minutes for maximum browning.
* **Evenness**: 90% consistent browning across the surface of standard bread.

## 3. System Requirements Definition

### 3.1 Functional Requirements
* The system shall heat 2 slots simultaneously.
* The system shall pop up the bread when the toasting cycle is complete.
* The system shall allow the user to adjust the toasting time.

### 3.2 Non-Functional / Quality Requirements
* The system shall operate on standard 120V AC household power.
* The system's exterior shall remain cool to the touch (under 40°C) during operation.

### 3.3 System Constraints
* The toaster must fit on a standard kitchen counter.

## 4. Architecture Definition

### 4.1 Logical Architecture / Subsystems
* **HeatingSubsystem**: Generates the heat.
* **ControlSubsystem**: Manages the timer and power delivery.
* **MechanicalSubsystem**: Handles the lever, bread carriage, and pop-up mechanism.

### 4.2 Interfaces and Interactions
* **Control to Heating**: ControlSubsystem delivers power to HeatingSubsystem based on the timer.
* **Mechanical to Control**: Engaging the lever closes the circuit to start the ControlSubsystem timer.

### 4.3 System Behavior
* **State: Idle**: Unplugged or plugged in but not active.
* **State: Toasting**: Lever depressed, heating elements on, timer running.
* **State: Popping**: Timer expires, mechanism releases, elements off.

## 5. Design Definition

### 5.1 Physical Components
* **Nichrome Wire Heating Elements**: Physical implementation of HeatingSubsystem.
* **PCB and Potentiometer**: Physical implementation of ControlSubsystem.
* **Spring-loaded Carriage and Electromagnet**: Physical implementation of MechanicalSubsystem.

### 5.2 Allocation Matrix
* **Nichrome Wire** satisfies the requirement to heat slots.
* **PCB Timer** satisfies the requirement to adjust toasting time.
* **Spring-loaded Carriage** satisfies the requirement to pop up the bread.
