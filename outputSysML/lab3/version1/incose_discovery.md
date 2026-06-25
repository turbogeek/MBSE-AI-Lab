# INCOSE Discovery Document: Standard 2-Slot Toaster

## 1. Introduction

The purpose of this document is to define the system-level requirements, stakeholders, and architectural boundaries for a standard 2-slot toaster. The toaster is designed for residential consumer use to toast bread and similar items.

## 2. Stakeholders and Concerns

- **Consumer (User):** Ease of use, safety (prevent burns and fires), even toasting, speed, and reliability.
- **Manufacturer:** Manufacturability, cost of production, compliance with safety standards.
- **Regulator:** Electrical safety, fire prevention.
- **Retailer:** Product display, packaging, marketing.
- **Maintenance Technician:** Ease of repair, availability of spare parts.
- **Environmentalist:** Recyclability, energy efficiency, material sourcing.
- **Emergency Responder:** Fire safety, electrical hazard prevention.
- **Safety Evaluator:** Product safety, fire safety, electrical hazard prevention.
- **Artisan Bakery Partner:** Accurate representation of their bread products, customer satisfaction.
- **Repairability Partner:** Modular design, ease of component replacement, availability of repair guides.

## 3. System Context

The toaster interacts with the following external elements:

- **User:** Interacts with the interface (timer dial, push-down lever).
- **Power Source:** 120V AC wall outlet providing electrical energy.
- **Environment:** Kitchen countertop (ambient air, heat dissipation).
- **Bread:** The item being toasted.

## 4. Key Requirements

- **REQ1 - Heating:** The toaster shall provide heating elements capable of reaching toasting temperatures.
- **REQ2 - Power Consumption:** The toaster shall operate on standard 120V AC and consume no more than 1500W.
- **REQ3 - Timer:** The toaster shall include an adjustable timer to control the duration of the toasting cycle.
- **REQ4 - User Safety:** The toaster shall maintain its exterior surface temperature below a safe threshold to prevent burns.
- **REQ5 - Pop-up Mechanism:** The toaster shall automatically eject the bread and terminate heating when the timer expires.
- **REQ6 - Optical Sensing:** The toaster shall include an optical sensor to monitor the browning level of the bread and automatically stop heating when the target shade is reached.
- **REQ7 - Connectivity:** The toaster shall include Wi-Fi connectivity to download specialized toasting profiles from partner bakeries.
- **REQ8 - Touchscreen UI:** The toaster shall feature a touchscreen interface for users to select bread types, browning levels, and custom profiles.
- **REQ9 - Repairability:** The toaster shall be designed with modular, easily replaceable heating elements and control boards to facilitate end-user repair.
- **REQ10 - Energy Efficiency:** The toaster shall be energy efficient and consume no more than 1500W.
- **REQ11 - Fire Safety:** The toaster shall be safe to use and not pose a fire hazard.
- **REQ12 - Durability:** The toaster shall be durable and long-lasting.
- **REQ13 - Ease of Use:** The toaster shall be easy to use and understand.
- **REQ14 - Aesthetics:** The toaster shall be aesthetically pleasing and fit in well with other kitchen appliances.

## 5. Logical Architecture

- **Power Subsystem:** Manages the intake of electrical power and distribution to the heating elements.
- **Control Subsystem:** Encompasses the timer, darkness setting, touchscreen interface, and the switch mechanism.
- **Heating Subsystem:** Converts electrical energy to radiant heat.
- **Mechanical Subsystem:** Includes the slots, the carriage, the lever, and the spring-loaded pop-up mechanism.
- **Safety Subsystem:** Ensures the toaster operates within safe temperature limits and prevents electrical hazards.
- **Optical Sensing Subsystem:** Monitors the color and surface temperature of the bread in real-time.
- **Connectivity Subsystem:** Manages Wi-Fi connections and communication with cloud services for profile downloads.
**Government Regulator:** Electrical Safety, Fire Prevention.
**Government Environmentalist:** Energy Efficiency, Recyclability.

## 6. Views

- **Toaster View:** Exposes the logical architecture of the toaster.
