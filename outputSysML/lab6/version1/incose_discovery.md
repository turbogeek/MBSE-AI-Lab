# INCOSE Systems Engineering Template: Modern Bell 47 Helicopter Trade Study

This document details the systems engineering scope, requirements, and architecture for the Modern Bell 47 Helicopter trade study comparing a traditional mechanical linkage tail rotor with an electric tail rotor (EDAT) configuration.

---

## 1. Business or Mission Analysis

### 1.1 Problem Statement

The vintage Bell 47 is a highly successful two-seat light utility helicopter. However, its original piston engine and mechanical tail rotor drive system (long driveshafts, intermediate and tail gearboxes, hangar bearings, and mechanical pitch-control linkage) present significant operational drawbacks. These mechanical components are heavy, complex to maintain, have lower reliability (MTBF), and introduce single points of mechanical failure.

To modernize the design, Bell Helicopter wants to use a modern engine (e.g., a modern light turboshaft or advanced fuel-injected piston engine) while keeping the same compact, 2-seat size and form factor. As part of this design cycle, we must conduct a trade study to compare the traditional mechanical linkage anti-torque drive with a modern electric tail rotor (similar to Bell's EDAT technology).

### 1.2 Mission Objectives

* **Torque Compensation & Yaw Control**: Provide yaw authority to counter main rotor torque in all flight regimes (hover, forward flight, and autorotation).
* **Modern Propulsion Integration**: Integrate an advanced powerplant with the helicopter main rotor transmission and electrical power systems.
* **Operating Cost & Maintenance Reduction**: Lower operating costs and reduce maintenance overhead (inspections, lubrication, replacements).
* **Enhanced Safety**: Prevent catastrophic loss of yaw control through design redundancy or emergency backup.

### 1.3 Key Stakeholders

* **Bell Helicopter Design Team**: Needs to minimize weight penalties and unit manufacturing costs while achieving high performance.
* **FAA / Certifying Authorities**: Enforce compliance with 14 CFR Part 27 safety standards.
* **Helicopter Operators**: Seek low operating costs, high availability (MTBF), and low maintenance hours per flight hour (MMH/FH).
* **Pilots & Passengers**: Demand superior safety margins, particularly during critical events like engine failure.

---

## 2. Stakeholder Needs and Requirements Definition

### 2.1 Use Cases / Operational Scenarios

* **Normal Flight Operations**:
  * `Counter Main Rotor Torque`: Maintain stable yaw heading in flight.
  * `Startup`, `Shutdown`, `Pre-Flight Check`, `Post-Flight Check`.
  * Flight maneuvers: `Takeoff`, `Hover`, `Cruise`, `Descent`, `Landing`, `Maneuvering`.
* **Specialized Mission Profiles**:
  * `Mountain Flying`, `Crop Dusting`, `Aerial Photography`, `External Load Operations`, `Passenger Transport`, `Utility Operations`, `Ambulance Operations`, `Law Enforcement Operations`, `Firefighting Operations`, `Night Flight`, `Instrument Flight`.
* **Emergency Engine Failure**:
  * `Maintain Tail Rotor Authority During Engine Failure`: System switches to emergency battery power (electric) or synchronizes mechanical linkage downstream of clutch.
  * `Perform Autorotation Landing`: Execute a controlled glide and touchdown from service ceiling within 7 to 10 minutes.
  * `Manage Backup Electrical Power`.
* **Support & Maintenance**:
  * `Regular Maintenance`, `Emergency Maintenance`, `Flight Test`, `Training`, `Engine Rebuild`, `Long Term Storage`, `Export and Shipping`.
* **Lifecycle Configurations**:
  * `Instrument Flight with Training`, `Instrument Flight with Export and Shipping`, `Night Flight with Training`, `Night Flight with Export and Shipping`, `Night Flight with Training and Export and Shipping`.

### 2.2 Measures of Effectiveness (MOEs) & Technical Performance Measures (TPMs)

* **MOE-01: Yaw Control Safety Margin**: Duration of tail rotor authority remaining under main engine failure conditions (Target: 7–10 minutes for electric battery backup; mechanical is N/A since it depends on main rotor autorotation speed).
* **MOE-02: Operating Cost (Direct Maintenance)**: Direct maintenance cost per flight hour associated with the tail rotor system (Target: < $30/hr).
* **MOP-01: System Mass**: Total weight of the anti-torque subsystem, including all gearboxes, shafts, motors, generators, wiring, and batteries (Target: < 100 kg).
* **TPM-01: Tail Rotor Reliability (MTBF)**: Mean time between failures for yaw control system components (Target: > 3,000 hours).
* **TPM-02: Maintenance Ratio (MMH/FH)**: Maintenance man-hours per flight hour (Target: < 0.10).

---

## 3. System Requirements Definition

### 3.1 Functional Requirements

* **REQ-01: Yaw Control Authority**: The anti-torque system shall provide yaw thrust sufficient to counter torque peaks of up to 300 hp from the main rotor under sea-level hover conditions.
* **REQ-02: Power Transmission**: The anti-torque system shall transmit control inputs from the cockpit pedals to the tail rotor blade angle or motor controller within 100 milliseconds.

### 3.2 Safety Requirements (FAA 14 CFR Part 27 Compliance)

* **REQ-03: Structural Factor of Safety (§ 27.303)**: The tail rotor structure, blade grips, and support casings shall withstand limit loads multiplied by a factor of safety of 1.5 without permanent deformation or structural failure.
* **REQ-04: Fatigue Life (§ 27.571)**: All primary structural parts of the anti-torque drive system shall be designed to prevent catastrophic fatigue failure for a minimum service life of 5,000 flight hours.

### 3.3 Configuration-Specific Safety Requirements

#### Electric Tail Rotor (EDAT) Requirements

* **REQ-05: Backup Battery Capacity**: The electric tail rotor system shall include a backup battery system capable of supplying operational yaw thrust power for at least 7 to 10 minutes following a primary engine or generator failure. This capacity corresponds to the maximum time required to execute an autorotative descent from the helicopter's highest flight envelope altitude of 12,000 ft AGL at a rate of 1,700 fpm (~7.1 minutes), plus a safety margin.
* **REQ-06: Thermal Runaway Mitigation**: The battery subsystem shall comply with FAA Special Conditions, demonstrating that a thermal runaway event in any single cell will not propagate to adjacent cells or cause catastrophic hazard to the cabin.
* **REQ-07: Dual Redundant Power Paths**: The electrical architecture shall feature dual redundant power distribution lines and dual motor windings to ensure yaw control is maintained despite a single wiring or winding failure.

#### Mechanical Linkage Requirements

* **REQ-08: Autorotation Synchronization**: The mechanical tail rotor driveshaft shall be connected to the main rotor transmission downstream of the freewheeling clutch, ensuring synchronization during autorotative descent.
* **REQ-09: Lubrication Loss Operation**: The tail rotor gearbox and intermediate gearbox shall be designed to operate for at least 30 minutes after a complete loss of oil lubrication.
* **REQ-10: Driveshaft Shielding**: The mechanical tail boom driveshaft shall be shielded to prevent damage from whipping or blade strikes in the event of a shaft coupling failure.

---

## 4. Architecture and Design Definition

### 4.1 Logical Architecture Subsystems

* **Engine Subsystem**: Provides mechanical power for flight.
* **Main Rotor & Transmission**: Delivers lift and thrust.
* **Cabin**: Housing for 2 crew members and cockpit controls.
* **Flight Controller**: Computes attitude, heading, and pilot control inputs.
* **Anti-Torque Drive Subsystem**: Evaluated as two alternative trade options:

```
[Modern Bell 47]
   ├── [Engine]
   ├── [Main Rotor & Transmission]
   ├── [Cabin (2 Seats)]
   ├── [Flight Controller]
   └── [Anti-Torque Subsystem] ◄── (Variant Choice)
      ├── Option A: [Traditional Mechanical Linkage]
      └── Option B: [Electric Tail Rotor System (EDAT)]
```

---

## 5. Trade Study Analysis (Parametric Trade Space)

Instead of a single pre-decided static analysis, the trade study evaluates a multi-dimensional parametric trade space of alternative configurations. The parameters below are instantiated as value properties in the SysMLv1 model to evaluate trade-offs dynamically under different constraints.

### 5.1 Trade Dimensions & Parameters

#### 1. Drivetrain Configuration

* **Mechanical Linkage**: Aluminum shafts, bearings, 45°/90° gearboxes, mechanical controls.
  * *Baseline Mass*: 55.0 kg
  * *Baseline Cost*: $25,000 USD
  * *Baseline MTBF*: 1,500 hours (high wear on bearings/gears)
  * *Emergency Safety Margin*: 0.0 minutes (requires active main rotor synchronization; zero backup power)
* **Electric Tail Rotor (EDAT)**: Generator, brushless motor, electronic controller, wiring, batteries.
  * *Baseline Mass*: 62.0 kg (reflecting a lightweight hybrid battery pack + motor + controllers)
  * *Baseline Cost*: $32,000 USD (lowered due to cheaper CATL-based cells and integrated power electronics)
  * *Baseline MTBF*: 5,000 hours (solid-state electronics, brushless motor)
  * *Emergency Safety Margin*: 7 to 10 minutes (sufficient to maintain yaw control for the entire duration of an engine-out autorotative descent from a 12,000 ft ceiling)

#### 2. Structural Materials Selection

* **Aluminum Alloys (Standard)**:
  * *Mass Multiplier*: 1.0
  * *Cost Multiplier*: 1.0
  * *Fatigue Life*: 5,000 hours (high risk of cumulative fatigue damage over time)
* **Carbon Fiber Composites (Premium)**:
  * *Mass Multiplier*: 0.70 (-30% weight reduction)
  * *Cost Multiplier*: 1.80 (+80% cost increase)
  * *Fatigue Life*: 15,000 hours (extremely fatigue-tolerant, lowers maintenance check frequency)

#### 3. Battery Chemistry (For Electric Variant)

* **Lithium-Ion (Conventional LFP/NMC - e.g. CATL cells)**:
  * *Specific Energy*: 180 Wh/kg pack-level
  * *Pack Weight (1.5 kWh Capacity)*: ~8.3 kg
  * *Lifespan*: 1,500 charge cycles (requires replacement every 3-5 years)
  * *Unit Cost*: $1,200 USD (aviation-certified pack using high-volume CATL industrial cells)
  * *Thermal Runaway Propagation Risk*: Moderate (requires standard thermal barriers)
* **Solid-State (Advanced)**:
  * *Specific Energy*: 320 Wh/kg pack-level
  * *Pack Weight (1.5 kWh Capacity)*: ~4.7 kg
  * *Lifespan*: 5,000 charge cycles (retains capacity for up to 15 years)
  * *Unit Cost*: $3,500 USD (developmental chemistry, higher premium)
  * *Thermal Runaway Propagation Risk*: Extremely Low (inherently safer solid electrolyte)

#### 4. Cabin Configuration

* **Enclosed Cabin (Doors-On)**:
  * *Cabin Mass*: +40 kg
  * *Yaw Aerodynamic Drag*: Lower (smooth airflow, reduced power required for anti-torque in forward flight)
  * *Passenger Comfort*: High (climate-controlled)
* **Utility Cabin (Doors-Off)**:
  * *Cabin Mass*: +10 kg (-30 kg reduction)
  * *Yaw Aerodynamic Drag*: Higher (turbulent airflow, increases tail rotor load in side-winds or forward flight)
  * *Passenger Comfort*: Low (exposed to elements)

#### 5. Passenger & Cargo Payload

* **Standard Patrol Load**: 150 kg (pilot + basic gear)
* **Maximum Utility Load**: 250 kg (two passengers + maximum cargo; increases anti-torque power demand by 15% due to higher lift/torque requirements)

---

### 5.2 Lifecycle, Reliability, and Legal Risk Parameters

The helicopter is designed for a **10 to 40+ year operational life**. Long-term costs are calculated using the following parametric relationships:

* **Downtime & Overhaul Frequencies**:
  * *Mechanical Overhaul*: Gearboxes require tear-down and inspection every 1,000 flight hours; driveshaft bearings replaced every 2,000 hours. High maintenance downtime (MMH/FH = 0.25).
  * *Electrical Overhaul*: Electric motors require bearing replacement every 10,000 hours. Battery replacement intervals depend on battery chemistry (3-5 years for Li-Ion vs. 12-15 years for Solid-State). Low maintenance downtime (MMH/FH = 0.05).
* **Lawsuit & Liability Exposure Estimation**:
  * The cumulative probability of catastrophic anti-torque system failure over a 40-year airframe life is modeled as:
    $$P_{fail} = 1 - e^{-\frac{Total\_Hours}{MTBF}}$$
  * Legal liability exposure is calculated as:
    $$Liability\_Risk = P_{fail} \times Severe\_Failure\_Penalty \times Exposure\_Factor$$
  * *Traditional Mechanical Linkage*: Higher fatigue accumulation in gearboxes/shafts over decades increases $P_{fail}$ if maintenance is deferred, leading to high structural lawsuit risks.
  * *Electric Tail Rotor*: Reduced mechanical fatigue risk, but introduces a non-zero hazard rate for battery thermal runaway over 40 years. Solid-State chemistry drops this hazard rate by an order of magnitude, drastically lowering legal and insurance exposure.

---

### 5.3 Parametric Trade Study Evaluation Formula

The trade study is implemented inside the model via a parametric constraint equation evaluating a **Weighted Score (WS)** for each combination:

$$WS = W_{mass} \cdot S_{mass} + W_{cost} \cdot S_{lifecycle\_cost} + W_{reliability} \cdot S_{MTBF} + W_{safety} \cdot S_{safety\_margin} - W_{legal} \cdot S_{liability\_risk}$$

Where $S$ is the normalized score (0 to 10) for each attribute under the selected parameters. Designers adjust weights ($W$) depending on whether the mission prioritizes minimum acquisition cost, military/utility performance, or commercial passenger safety and low liability.
