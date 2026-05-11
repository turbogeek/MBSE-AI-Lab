# INCOSE Systems Engineering Template: Standard 2-Slot Toaster

This document gathers information according to the standard INCOSE Systems Engineering Handbook life cycle stages for a standard 2-slot toaster.

## 1. Business or Mission Analysis

### 1.1 Problem Statement
Consumers require a quick, reliable, and safe method to toast sliced bread and similar baked goods (like bagels or toaster pastries) in a typical home kitchen environment. The solution must be affordable, easy to use, and require minimal counter space.

### 1.2 Mission Objectives
* To reliably and evenly heat sliced bread products to a desired level of browning.
* To ensure user safety from electrical and thermal hazards.
* To provide an intuitive user interface for controlling the toasting process.

### 1.3 Key Stakeholders (Business Level)
* **Consumers/Home Users**: Primary users of the toaster.
* **Appliance Manufacturer**: Business entity designing and selling the toaster.
* **Safety Regulators (e.g., UL, CE)**: Ensure the product meets electrical and fire safety standards.

## 2. Stakeholder Needs and Requirements Definition

### 2.1 Use Cases / Operational Scenarios
* **Toast Bread**: User inserts bread, selects browning level, and presses the lever. The toaster heats the bread and pops it up when done.
* **Cancel Toasting**: User manually stops the toasting process before the timer completes.
* **Clean Toaster**: User removes the crumb tray, empties it, and wipes down the exterior.

### 2.2 Measures of Effectiveness (MOEs)
* **Time to Toast**: Average time to reach medium browning (e.g., < 2 minutes).
* **Browning Evenness**: Percentage of the bread surface evenly browned.
* **Safety**: Maximum exterior surface temperature during operation (must not burn users upon brief contact).

## 3. System Requirements Definition

### 3.1 Functional Requirements
* The system shall accommodate up to two standard slices of bread simultaneously.
* The system shall heat the bread using resistive heating elements.
* The system shall automatically eject the bread upon completion of the toasting cycle.
* The system shall provide a manual cancel function to abort the toasting cycle.

### 3.2 Non-Functional / Quality Requirements
* The system shall operate on standard household power (120V AC, 60Hz or 240V AC, 50Hz depending on region).
* The system's maximum power consumption shall not exceed 900 Watts.
* The system shall prevent access to live electrical components during normal operation.
* The system shall have a removable crumb tray for easy cleaning.

### 3.3 System Constraints
* The system footprint shall not exceed 12 inches (length) by 8 inches (width).
* The system casing must be constructed from heat-resistant, food-safe materials.

## 4. Architecture Definition

### 4.1 Logical Architecture / Subsystems
* **Heating Subsystem**: Responsible for converting electrical energy into thermal energy.
* **Control Subsystem**: Manages the duration of the toasting cycle based on user input.
* **Mechanical Subsystem**: Holds the bread and physically lowers/raises it.
* **Enclosure Subsystem**: Protects internal components, ensures safety, and provides the user interface.

### 4.2 Interfaces and Interactions
* **Power Interface**: Connects the Control and Heating Subsystems to the external electrical grid.
* **User Interface**: Connects the user (Actor) to the Control and Mechanical Subsystems (e.g., browning dial, lever).
* **Thermal Interface**: Transfer of heat from the Heating Subsystem to the bread payload.

### 4.3 System Behavior
* **Idle State**: Toaster is plugged in, lever is up, heating elements are off.
* **Toasting State**: Lever is down, switch is engaged, timer is running, heating elements are active.
* **Pop-up Action**: Timer completes (or cancel is pressed), heating elements turn off, spring mechanism releases the lever to eject the bread.

## 5. Design Definition

### 5.1 Physical Components
* **Resistive Nichrome Wire**: Implementation of the Heating Subsystem.
* **Bimetallic Strip / Electronic Timer Circuit**: Implementation of the Control Subsystem.
* **Spring-Loaded Carriage and Electromagnet (or Mechanical Latch)**: Implementation of the Mechanical Subsystem.
* **Plastic/Metal Housing and Crumb Tray**: Implementation of the Enclosure Subsystem.
* **Power Cord and Plug**: Physical implementation of the Power Interface.

### 5.2 Allocation Matrix
* The **Resistive Nichrome Wire** satisfies the functional requirement to heat the bread.
* The **Bimetallic Strip / Electronic Timer Circuit** satisfies the control of the toasting duration.
* The **Spring-Loaded Carriage** satisfies the requirement to automatically eject the bread.
* The **Plastic/Metal Housing** satisfies safety requirements by preventing access to live components and managing external temperature.
