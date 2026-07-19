# Lab 6: Modern Bell 47 Trade Study (SysMLv1 & Cameo Groovy API)

CoThe following prompt into your Antigravity (or LLM) chat to automatically generate, validate, and Cameo-test a SysMLv1 model and trade study for the Modern Bell 47 helicopter using the Cameo Open API.

***

**Copy below this line:**

***

Act as an expert Systems Engineer, Aerospace Designer, and CATIA Magic / Cameo Open API developer. I want you to model a Modern Bell 47 helicopter and conduct a trade study comparing an electric tail rotor versus a traditional mechanical linkage tail rotor. Please execute the following workflow step-by-step:

1. **Folder Creation:** Check the directory `outputSysML/lab6/versionX`. Find the next available version number (e.g., `version1`, `version2`) that does not exist, and create that folder.

2. **Discovery Document:** Create an INCOSE discovery document based on your systems engineering research. Save this document as `incose_discovery.md` in the newly created version folder. The document must contain:
   - **System Scope**: Modern Bell 47 helicopter (2-seat, modern engine, same size/form factor).
   - **Trade Study Scope**: Electric Tail Rotor (Safran ENGINeUS, generator, 7-10 min backup lithium battery) vs. Traditional Mechanical Linkage (driveshafts, gearboxes, pitch mechanism).
   - **Helicopter Safety Requirements**: FAA 14 CFR Part 27 requirements (factor of safety 1.5, fatigue tolerance).
   - **Variant Safety Requirements**:
     - *Electric Tail Rotor*: Battery thermal safety and propagation protection, redundant power lines, battery monitoring, and 7-10 min backup power margin in case of engine failure (enabling powered yaw control for the entire duration of an autorotative descent from a 12,000 ft ceiling at 1,700 fpm).
     - *Mechanical Linkage*: Drivetrain shear protection, gearbox lubrication failure tolerance, and mechanical clutch synchronization.
   - **Measures of Effectiveness (MOEs) / Technical Performance Measures (TPMs)**: Total Anti-Torque Mass, Acquisition Cost, Mean Time Between Failures (MTBF), Maintenance Man-Hours per Flight Hour (MMH/FH), and Safety Margin.

3. **Cameo Model Generation (SysMLv1 via Groovy):** Write a Groovy script to populate the existing MagicGrid template packages in the active Cameo project. The model is a SysMLv1 model. The script must execute the following model additions:
   - **Find MagicGrid Packages**: Look up the existing packages in the containment tree: `1. Stakeholders`, `2. Requirements`, `3. Use Cases`, `4. Logical Architecture` (or Concept/Logical).
   - **Stakeholders & Concerns**: Under `1. Stakeholders`, create:
     - `Bell Helicopter Design Team` (Concern: Cost, Weight, Design Complexity)
     - `FAA Certifying Authority` (Concern: Safety, Reliability, Part 27 Compliance)
     - `Helicopter Operator` (Concern: Maintenance Costs, Availability)
   - **Requirements**: Under `2. Requirements`, create:
     - General: `REQ_Cap` (2-seat capacity), `REQ_Engine` (modern turboshaft engine).
     - FAA Safety: `REQ_Part27_Safety` (factor of safety 1.5), `REQ_Fatigue` (fatigue life).
     - Electric Rotor: `REQ_Elec_Backup` (7-10 min backup battery), `REQ_Thermal_Safety` (thermal runaway containment).
     - Traditional Linkage: `REQ_Mech_Clutch` (clutch for autorotation), `REQ_Lubrication` (gearbox run-dry capability).
   - **Use Cases**: Under `3. Use Cases`, create:
     - `Counter Main Rotor Torque`
     - `Manage Backup Electrical Power`
     - `Perform Autorotation Landing`
     - `Maintain Tail Rotor Authority During Engine Failure`
     - `Reglar Maintenance`
     - `Emergency Maintenance`
     - `Pre-Flight Check`
     - `Startup`
     - `Shutdown`
     - `Takeoff`
     - `Hover`
     - `Cruise`
     - `Descent`
     - `Landing`
     - `Maneuvering`
     - `Mountain Flying`
     - `Crop Dusting`
     - `Aerial Photography`
     - `External Load Operations`
     - `Passenger Transport`
     - `Utility Operations`
     - `Ambulance Operations`
     - `Law Enforcement Operations`
     - `Firefighting Operations`  
     - `Emergency Engine Failure`
       - `Perform Autorotation Landing`
     - `Post-Flight Check`
     - `Flight Test`
     - `Training`
     - `Engine Rebuild`
     - `Long Term Storage`
     - `Export and Shipping`
     - `Night Flight`
     - `Instrument Flight`
     - `Instrument Flight with Training`
     - `Instrument Flight with Export and Shipping`
     - `Night Flight with Training`
     - `Night Flight with Export and Shipping`
     - `Night Flight with Training and Export and Shipping`
   - **Logical Architecture**: Under `4. Logical Architecture`:
     - Create a Block `Modern Bell 47`.
     - Create parts: `Engine`, `Main Rotor`, `Cabin`, `Flight Controller`, `Anti-Torque Subsystem`.
     - Define Value Properties representing the trade space dimensions:
       - Drivetrain Selection: `drivetrainType` (String: "Mechanical" or "Electric")
       - Materials Selection: `materialType` (String: "Aluminum" or "Composite")
       - Battery Technology: `batteryTech` (String: "Lithium-Ion" or "Solid-State")
       - Cabin Style: `cabinStyle` (String: "Enclosed" or "Utility")
       - Mission Payload: `payloadMass` (Real in kg)
       - Operating Life: `designLifeYears` (Real, e.g. 10.0 to 40.0 years)
     - Define Calculated Rollup Value Properties:
       - `totalMass` (Real)
       - `totalAcquisitionCost` (Real)
       - `lifecycleCost` (Real, accounting for overhauls, battery swaps, and downtime)
       - `systemMTBF` (Real, based on configuration reliability)
       - `downtimeRatio` (Real, based on maintenance hours per flight hour)
       - `probabilityOfFailure` (Real, cumulative over the design life)
       - `lawsuitExposureRisk` (Real, proportional to probability of failure and safety hazards)
       - `tradeWeightedScore` (Real, calculated via score weighting)
   - **Parametric Trade Study Evaluation**: Design the Groovy script to include a parametric evaluation method (simulating a SysML Parametric Diagram or Constraint Block). This method must read the selected trade dimensions (drivetrainType, materialType, batteryTech, cabinStyle, payloadMass, designLifeYears) and run the rollup calculations (Mass, Cost, MTBF, Lifecycle Cost, Probability of Failure, Lawsuit Exposure, and Weighted Score). It should print a trade matrix comparing multiple configurations (e.g., Composite Electric Solid-State Enclosed vs. Aluminum Mechanical Utility) to demonstrate how shifts in design life (10 vs 40 years) or mission payload alter the recommended anti-torque design.

4. **Groovy Execution & Script Requirements**:
   - Save your generated Groovy script as `ModernBell47Populate.groovy` in the same version folder.
   - Wrap all model changes in a `SessionManager` transaction.
   - Use `com.nomagic.uml2.ext.jmi.helpers.StereotypesHelper` to apply SysML stereotypes (e.g. `«Block»`, `«Requirement»`, `«ValueProperty»`, `«Satisfy»`).
   - Load and use `scripts/SysMLv2Logger.groovy` for all script logging.
   - Call the Cameo Test Harness at `http://localhost:8765/run` by sending a JSON payload containing the absolute path of `ModernBell47Populate.groovy` under `scriptPath`.
   - Check harness execution output, debug any compilation/JMI API issues using Javadoc or guide searches, and re-run until the script completes without errors and correctly populates the MagicGrid structure in Cameo.
