package SuperToaster_SoS {
    private import ScalarValues::*;
    private import ISQ::*;
    private import SI::*;

    // --- System Context ---
    package Context {
        part def Environment;
        part def SuperToasterSystem;
        part def MobileApp;
        part def ManufacturerCloud;
        part def EmergencyService;

        part contextSystem : SuperToasterSystem {
            port wifiPort;
        }
        part app : MobileApp {
            port wifiPort;
        }
        part cloud : ManufacturerCloud {
            port apiPort;
        }
        part fireDept : EmergencyService {
            port apiPort;
        }

        connection c1 connect contextSystem.wifiPort to app.wifiPort;
        connection c2 connect contextSystem.wifiPort to cloud.apiPort;
        connection c3 connect contextSystem.wifiPort to fireDept.apiPort;
    }

    // --- Stakeholders & Concerns ---
    package Stakeholders {
        private import Context::*;
        part def Consumer;
        part def Manufacturer;
        part def FireDepartment;

        part consumer : Consumer;
        part manufacturer : Manufacturer;

        concern def SafetyConcernDef {
            subject system : Context::SuperToasterSystem;
            stakeholder operator : Consumer;
            require constraint { true }
        }

        concern def PerformanceConcernDef {
            subject system : Context::SuperToasterSystem;
            stakeholder operator : Consumer;
            require constraint { true }
        }
        
        concern def ReliabilityConcernDef {
            subject system : Context::SuperToasterSystem;
            stakeholder maker : Manufacturer;
            require constraint { true }
        }

        concern safetyConcern : SafetyConcernDef {
            subject system = Context::contextSystem;
            stakeholder operator = consumer;
        }

        concern performanceConcern : PerformanceConcernDef {
            subject system = Context::contextSystem;
            stakeholder operator = consumer;
        }

        concern reliabilityConcern : ReliabilityConcernDef {
            subject system = Context::contextSystem;
            stakeholder maker = manufacturer;
        }
    }

    // --- Requirements ---
    package Requirements {
        private import Stakeholders::*;

        requirement def PerformanceReq;
        requirement def SafetyReq;
        requirement def ReliabilityReq;
        requirement def PhysicalReq;

        requirement <'REQ-PERF-01'> 'Toasting Time' : PerformanceReq {
            doc /* Must toast 2 slices perfectly in under 2 minutes. */
            frame Stakeholders::performanceConcern;
            attribute toastingTime : TimeValue;
            require constraint { toastingTime <= 120[s] }
        }

        requirement <'REQ-PERF-02'> 'Peak Power' : PerformanceReq {
            doc /* Peak power limit of 1500W. */
            frame Stakeholders::performanceConcern;
            attribute peakPower : PowerValue;
            require constraint { peakPower <= 1500[W] }
        }

        requirement <'REQ-PHYS-01'> 'Max Weight' : PhysicalReq {
            doc /* Max weight of 3 kg. */
            frame Stakeholders::performanceConcern;
            attribute weight : MassValue;
            require constraint { weight <= 3.0[kg] }
        }

        requirement <'REQ-REL-01'> 'Target MTBF' : ReliabilityReq {
            doc /* Target MTBF of 10,000 hours. */
            frame Stakeholders::reliabilityConcern;
            attribute mtbf : TimeValue;
            require constraint { mtbf >= 10000[h] }
        }
        
        requirement <'REQ-SAFE-01'> 'Thermal Runaway Protection' : SafetyReq {
            doc /* Automatic emergency service dispatch if thermal runaway occurs. */
            frame Stakeholders::safetyConcern;
        }
    }

    // --- Logical Architecture ---
    package LogicalArchitecture {
        private import Context::*;
        private import Requirements::*;

        // Signals for state machine
        attribute def StartToasting;
        attribute def TimerExpired;
        attribute def TempAlert;

        part def LogicalController {
            attribute currentState : String;
            
            state def ControllerStates {
                entry;
                then Standby;
                
                state Standby;
                state Heating;
                state Cooling;
                state MaintenanceRequired;
                state EmergencyShutoff;

                transition t1
                    first Standby
                    accept StartToasting
                    then Heating;

                transition t2
                    first Heating
                    accept TimerExpired
                    then Cooling;

                transition t3
                    first Heating
                    accept TempAlert
                    then EmergencyShutoff;
                    
                transition t4
                    first Cooling
                    then Standby;
            }
            
            exhibit state operatingState references ControllerStates::Standby;
        }

        part def HeatingSubsystem;
        part def SensorArray;

        part def ToasterLogical {
            part controller : LogicalController;
            part heater : HeatingSubsystem;
            part sensors : SensorArray;
            
            attribute totalWeight : MassValue;
            attribute totalPower : PowerValue;
            attribute systemMtbf : TimeValue;

            satisfy Requirements::'Toasting Time';
            satisfy Requirements::'Peak Power';
            satisfy Requirements::'Max Weight';
            satisfy Requirements::'Target MTBF';
            satisfy Requirements::'Thermal Runaway Protection';
        }

        // Action Modeling (Swimlanes)
        action def SmartToastingProcess {
            action userConfigures;
            action toasterHeats;
            action cloudLogs;
            action detectsSmoke;

            first userConfigures then toasterHeats;
            first toasterHeats then cloudLogs;
        }
        
        part def SoS_Implementation {
            part app : MobileApp {
                perform action userConfigures;
            }
            part toaster : ToasterLogical {
                perform action toasterHeats;
                perform action detectsSmoke;
            }
            part cloud : ManufacturerCloud {
                perform action cloudLogs;
            }
        }
    }

    // --- Physical Architecture (Variants) ---
    package PhysicalArchitecture {
        private import LogicalArchitecture::*;

        // Color Variation
        enum def Color {
            enum Red;
            enum Black;
            enum Silver;
        }

        // Heating Element Variation
        variation part def HeatingElement {
            attribute mass : MassValue;
            attribute cost : Real;
            attribute mtbf : TimeValue;
        }

        part def NichromeCoil :> HeatingElement {
            attribute redefines mass = 0.5[kg];
            attribute redefines cost = 15.0;
            attribute redefines mtbf = 5000[h];
        }

        part def InductionHeating :> HeatingElement {
            attribute redefines mass = 0.8[kg];
            attribute redefines cost = 45.0;
            attribute redefines mtbf = 15000[h];
        }

        calc def TotalMassCalc {
            in m1 : MassValue;
            in m2 : MassValue;
            in m3 : MassValue;
            return total : MassValue = m1 + m2 + m3;
        }

        calc def TotalCostCalc {
            in c1 : Real;
            in c2 : Real;
            in c3 : Real;
            return total : Real = c1 + c2 + c3;
        }

        part def PhysicalToaster :> ToasterLogical {
            attribute chassisColor : Color;
            
            part redefines heater : HeatingElement;
            part chassis {
                attribute mass : MassValue = 1.0[kg];
                attribute cost : Real = 20.0;
            }
            part electronics {
                attribute mass : MassValue = 0.5[kg];
                attribute cost : Real = 30.0;
                attribute mtbf : TimeValue = 20000[h];
            }

            // Calculations / Rollups
            calc toasterMassCalc : TotalMassCalc {
                in m1 = heater.mass;
                in m2 = chassis.mass;
                in m3 = electronics.mass;
            }

            bind totalWeight = toasterMassCalc.total;
            bind systemMtbf = heater.mtbf; 
        }

        // Specific configuration
        part premiumToaster : PhysicalToaster {
            attribute redefines chassisColor = Color::Silver;
            part redefines heater : InductionHeating;
        }
    }

    // --- Views ---
    package Views {
        private import Requirements::*;
        private import LogicalArchitecture::*;
        private import PhysicalArchitecture::*;

        view architectureView {
            expose PhysicalArchitecture::PhysicalToaster;
            expose PhysicalArchitecture::premiumToaster;
        }
    }
}
