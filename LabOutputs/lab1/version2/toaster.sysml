package ToasterModel {
    private import ScalarValues::*;
    private import ISQ::*;
    private import SI::*;
    private import MeasurementReferences::*;

    package Stakeholders {
        part def Stakeholder;
        part def HomeUser :> Stakeholder;
        part def SafetyRegulator :> Stakeholder;
        part def Manufacturer :> Stakeholder;

        concern SafetyConcern {
            doc /* Concern regarding electrical and fire safety. */
        }
        concern PerformanceConcern {
            doc /* Concern regarding the speed and evenness of toasting. */
        }
        concern UsabilityConcern {
            doc /* Concern regarding ease of use and adjustable browning. */
        }
    }

    package Requirements {
        private import Stakeholders::*;

        requirement <REQ1> 'Dual Slot Heating' {
            frame PerformanceConcern;
            doc /* The system shall heat 2 slots simultaneously. */
        }
        requirement <REQ2> 'Pop Up Mechanism' {
            frame UsabilityConcern;
            doc /* The system shall pop up the bread when the toasting cycle is complete. */
        }
        requirement <REQ3> 'Adjustable Timer' {
            frame UsabilityConcern;
            doc /* The system shall allow the user to adjust the toasting time. */
        }
        requirement <REQ4> 'Cool Exterior' {
            frame SafetyConcern;
            doc /* The system's exterior shall remain cool to the touch (under 40°C) during operation. */
        }
    }

    package SystemContext {
        private import Stakeholders::HomeUser;
        part def Environment;
        part def ToasterSystem;

        part systemContext {
            part user : HomeUser;
            part environment : Environment;
            part toaster : ToasterSystem;
        }
    }

    package LogicalArchitecture {
        private import Requirements::*;

        part def HeatingSubsystem {
            satisfy 'Dual Slot Heating';
        }
        part def ControlSubsystem {
            satisfy 'Adjustable Timer';
        }
        part def MechanicalSubsystem {
            satisfy 'Pop Up Mechanism';
        }

        part def LogicalToaster {
            part heater : HeatingSubsystem;
            part controller : ControlSubsystem;
            part mechanism : MechanicalSubsystem;
            satisfy 'Cool Exterior';
        }
    }

    package PhysicalArchitecture {
        private import LogicalArchitecture::*;

        part def NichromeWire :> HeatingSubsystem {}
        part def PCBTimer :> ControlSubsystem {}
        part def SpringCarriage :> MechanicalSubsystem {}

        part def PhysicalToaster :> LogicalToaster {
            part physicalHeater : NichromeWire redefines heater;
            part physicalController : PCBTimer redefines controller;
            part physicalMechanism : SpringCarriage redefines mechanism;
        }
    }

    package Views {
        private import LogicalArchitecture::*;
        private import PhysicalArchitecture::*;

        view ToasterArchitectureView {
            expose LogicalToaster;
            expose PhysicalToaster;
        }
    }
}
