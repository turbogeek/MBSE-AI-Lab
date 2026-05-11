package ToasterModel {
    private import ScalarValues::*;
    private import ISQ::*;
    private import SI::*;

    // -------------------------------------------------------------
    // STAKEHOLDERS & CONCERNS
    // -------------------------------------------------------------
    package Stakeholders {
        part def Person;
        part def Organization;

        part consumer : Person;
        part manufacturer : Organization;
        part regulatoryAgency : Organization;

        concern def SafetyConcern {
            doc /* Concern regarding burn prevention and electrical safety. */
        }
        concern def PerformanceConcern {
            doc /* Concern regarding even toasting and duration. */
        }
        concern def PowerConcern {
            doc /* Concern regarding energy efficiency and limits. */
        }

        concern safetyConcern : SafetyConcern;
        concern performanceConcern : PerformanceConcern;
        concern powerConcern : PowerConcern;
    }

    // -------------------------------------------------------------
    // REQUIREMENTS
    // -------------------------------------------------------------
    package Requirements {
        private import Stakeholders::*;

        requirement def FunctionalRequirement;
        requirement def SafetyRequirement;
        requirement def PerformanceRequirement;

        requirement <'REQ-01'> 'Two Bread Slots' : FunctionalRequirement {
            doc /* The system must provide two standard-sized bread slots. */
        }

        requirement <'REQ-02'> 'Even Heating' : PerformanceRequirement {
            doc /* The system must apply heat using electrical heating elements to toast bread evenly. */
            frame concern performanceConcern;
        }

        requirement <'REQ-03'> 'Adjustable Timer' : FunctionalRequirement {
            doc /* The system must include a timer mechanism adjustable by the user. */
            frame concern performanceConcern;
        }

        requirement <'REQ-04'> 'Pop-up Mechanism' : FunctionalRequirement {
            doc /* The system must include a pop-up mechanism to eject the bread. */
        }

        requirement <'REQ-05'> 'Manual Cancel' : FunctionalRequirement {
            doc /* The system must include a manual cancel function. */
            frame concern safetyConcern;
        }

        requirement <'REQ-06'> 'Max Power Consumption' : PerformanceRequirement {
            doc /* Power consumption should not exceed 1000 watts. */
            frame concern powerConcern;
            attribute maxPower : PowerValue = 1000.0[W];
        }

        requirement <'REQ-07'> 'Surface Temperature Limit' : SafetyRequirement {
            doc /* Exterior housing must remain cool to the touch (under 50 degrees Celsius). */
            frame concern safetyConcern;
            // Using kelvin as the base SI unit, 50 C = 323.15 K
            attribute maxTemp : TemperatureValue = 323.15[K];
        }
    }

    // -------------------------------------------------------------
    // SYSTEM CONTEXT
    // -------------------------------------------------------------
    package SystemContext {
        private import Stakeholders::*;

        part def Environment;
        part def ToasterSystem;

        part context {
            part user : Person;
            part kitchen : Environment;
            part toaster : ToasterSystem;

            connection userInteraction {
                end user;
                end toaster;
            }
        }
    }

    // -------------------------------------------------------------
    // LOGICAL ARCHITECTURE
    // -------------------------------------------------------------
    package LogicalArchitecture {
        private import SystemContext::ToasterSystem;
        private import Requirements::*;

        part def HeatingSystem;
        part def ControlSystem;
        part def EjectionSystem;
        part def PowerSystem;

        part def LogicalToaster :> ToasterSystem {
            part heatingSys : HeatingSystem;
            part controlSys : ControlSystem;
            part ejectionSys : EjectionSystem;
            part pwrSys : PowerSystem;

            satisfy requirement 'Two Bread Slots' by ejectionSys;
            satisfy requirement 'Even Heating' by heatingSys;
            satisfy requirement 'Adjustable Timer' by controlSys;
            satisfy requirement 'Pop-up Mechanism' by ejectionSys;
            satisfy requirement 'Manual Cancel' by controlSys;
            satisfy requirement 'Max Power Consumption' by heatingSys;
        }
    }

    // -------------------------------------------------------------
    // PHYSICAL ARCHITECTURE
    // -------------------------------------------------------------
    package PhysicalArchitecture {
        private import LogicalArchitecture::*;

        part def NichromeElements :> HeatingSystem;
        part def TimerDial :> ControlSystem;
        part def SpringMechanism :> EjectionSystem;
        part def PowerCord :> PowerSystem;

        part def PhysicalToaster :> LogicalToaster {
            part redefines heatingSys : NichromeElements;
            part redefines controlSys : TimerDial;
            part redefines ejectionSys : SpringMechanism;
            part redefines pwrSys : PowerCord;
        }
    }

    // -------------------------------------------------------------
    // VIEWS
    // -------------------------------------------------------------
    package ToasterViews {
        private import Stakeholders::*;
        private import Requirements::*;
        private import LogicalArchitecture::*;
        private import PhysicalArchitecture::*;

        view stakeholderView {
            expose Stakeholders::**;
        }

        view requirementsView {
            expose Requirements::**;
        }

        view logicalArchitectureView {
            expose LogicalArchitecture::**;
        }

        view physicalArchitectureView {
            expose PhysicalArchitecture::**;
        }
    }
}
