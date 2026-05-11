package InsectHuntingDroneModel {
    private import ScalarValues::*;
    private import ISQ::*;
    private import SI::*;

    // -------------------------------------------------------------
    // STAKEHOLDERS & CONCERNS
    // -------------------------------------------------------------
    package Stakeholders {
        part def Person;
        part def Organization;

        part farmer : Person;
        part operator : Person;
        part regulatoryAgency : Organization;

        concern def CostConcern {
            doc /* Concern regarding the estimated cost per unit. */
        }
        concern def WeightConcern {
            doc /* Concern regarding the total weight of the drone. */
        }
        concern def PowerConsumptionConcern {
            doc /* Concern regarding the power consumption and flight time. */
        }
        concern def SafetyConcern {
            doc /* Concern regarding human and environmental safety. */
        }

        concern costConcern : CostConcern;
        concern weightConcern : WeightConcern;
        concern powerConsumptionConcern : PowerConsumptionConcern;
        concern safetyConcern : SafetyConcern;
    }

    // -------------------------------------------------------------
    // REQUIREMENTS
    // -------------------------------------------------------------
    package Requirements {
        private import Stakeholders::*;

        requirement def PerformanceRequirement;
        requirement def SafetyRequirement;
        requirement def PhysicalRequirement;

        requirement <'REQ-01'> 'Autonomous Solar Operation' : PerformanceRequirement {
            doc /* Powered by solar energy. Must land, charge via solar, and take off autonomously. */
            frame concern powerConsumptionConcern;
            attribute flightTime_target : DurationValue = 2[h];
        }

        requirement <'REQ-02'> 'Pest Elimination' : PerformanceRequirement {
            doc /* Equipped with a laser weapon and AI to identify and kill pest insects while preserving beneficial ones. */
            frame concern powerConsumptionConcern;
            attribute bugsKilled_target : Real = 1000.0;
        }

        requirement <'REQ-03'> 'Navigation and Communication' : PerformanceRequirement {
            doc /* Equipped with GPS for navigation and a communication system to report to human operator. */
        }

        requirement <'REQ-04'> 'Weather Resistance' : PhysicalRequirement {
            doc /* Capable of operating in varied weather, including moderate rain. */
        }

        requirement <'REQ-05'> 'Payload Capacity' : PhysicalRequirement {
            doc /* Payload capacity up to 1 kg. */
            attribute payload_target : MassValue = 1.0[kg];
        }

        requirement <'REQ-06'> 'Operating Limits' : PerformanceRequirement {
            doc /* Max altitude 25m, within a geofenced area 1 mile x 1 mile. */
        }

        requirement <'REQ-07'> 'Safety Protocols' : SafetyRequirement {
            doc /* Rotor guards, emergency stop, battery thermal monitoring, and laser safety interlocks (eye-safe, tilt disable). */
            frame concern safetyConcern;
        }

        requirement <'REQ-08'> 'Cost Limit' : PhysicalRequirement {
            doc /* Estimated cost per unit. */
            frame concern costConcern;
            attribute cost_target : Real = 500.0;
        }

        requirement <'REQ-09'> 'Weight Limit' : PhysicalRequirement {
            doc /* Total system weight limit. */
            frame concern weightConcern;
            attribute weight_target : MassValue = 5.0[kg];
        }
    }

    // -------------------------------------------------------------
    // SYSTEM CONTEXT
    // -------------------------------------------------------------
    package SystemContext {
        private import Stakeholders::*;

        part def Environment;
        part def InsectHuntingDrone;

        part context {
            part operator : Person;
            part environment : Environment;
            part drone : InsectHuntingDrone;

            connection comms {
                end operator;
                end drone;
            }
        }
    }

    // -------------------------------------------------------------
    // LOGICAL ARCHITECTURE
    // -------------------------------------------------------------
    package LogicalArchitecture {
        private import SystemContext::InsectHuntingDrone;
        private import Requirements::*;

        // Subsystem Definitions
        part def NavigationSystem {
            attribute powerConsumption : PowerValue;
            attribute mass : MassValue;
        }
        part def LaserWeapon {
            attribute powerConsumption : PowerValue;
            attribute mass : MassValue;
        }
        part def SensorArray {
            attribute powerConsumption : PowerValue;
            attribute mass : MassValue;
        }
        part def PowerSubsystem {
            attribute powerConsumption : PowerValue;
            attribute mass : MassValue;
        }
        part def FlightController {
            attribute powerConsumption : PowerValue;
            attribute mass : MassValue;
        }

        part def LogicalDrone :> InsectHuntingDrone {
            part navSys : NavigationSystem;
            part laserSys : LaserWeapon;
            part sensorSys : SensorArray;
            part pwrSys : PowerSubsystem;
            part flightCtrl : FlightController;

            attribute totalPower : PowerValue;
            attribute totalMass : MassValue;

            assert constraint {
                totalPower == navSys.powerConsumption + laserSys.powerConsumption + sensorSys.powerConsumption + pwrSys.powerConsumption + flightCtrl.powerConsumption
            }
            assert constraint {
                totalMass == navSys.mass + laserSys.mass + sensorSys.mass + pwrSys.mass + flightCtrl.mass
            }

            // Events
            item def InsectDetectedEvent;
            item def TargetLockedEvent;
            item def InsectKilledEvent;
            item def LowBatteryEvent;
            item def FullyChargedEvent;
            item def EmergencyEvent;

            // Actions for state transitions
            action targetBug;
            action fireLaser;
            action resumePatrol;
            action returnToBase;
            action halt;

            // State Machine
            state def FlightStates {
                entry; then patrolling;

                state patrolling;
                state targeting;
                state firingLaser;
                state returningToCharge;
                state emergencyStop;

                transition t1 
                    first patrolling 
                    accept e1 : InsectDetectedEvent 
                    do action t_bug :> targetBug 
                    then targeting;

                transition t2 
                    first targeting 
                    accept e2 : TargetLockedEvent 
                    do action f_laser :> fireLaser 
                    then firingLaser;

                transition t3 
                    first firingLaser 
                    accept e3 : InsectKilledEvent 
                    do action r_patrol1 :> resumePatrol 
                    then patrolling;

                transition t4 
                    first patrolling 
                    accept e4 : LowBatteryEvent 
                    do action r_base :> returnToBase 
                    then returningToCharge;

                transition t5 
                    first returningToCharge 
                    accept e5 : FullyChargedEvent 
                    do action r_patrol2 :> resumePatrol 
                    then patrolling;

                transition t6 
                    first patrolling 
                    accept e6 : EmergencyEvent 
                    do action h_stop :> halt 
                    then emergencyStop;
            }

            exhibit state operatingState : FlightStates;

            satisfy requirement 'Autonomous Solar Operation' by pwrSys;
            satisfy requirement 'Pest Elimination' by laserSys;
            satisfy requirement 'Pest Elimination' by sensorSys;
            satisfy requirement 'Navigation and Communication' by navSys;
            satisfy requirement 'Safety Protocols' by flightCtrl;
        }
    }

    // -------------------------------------------------------------
    // PHYSICAL ARCHITECTURE
    // -------------------------------------------------------------
    package PhysicalArchitecture {
        private import LogicalArchitecture::*;

        part def GPSModule :> NavigationSystem {
            attribute redefines powerConsumption = 2.0[W];
            attribute redefines mass = 0.1[kg];
        }
        part def LaserEmitter :> LaserWeapon {
            attribute redefines powerConsumption = 50.0[W];
            attribute redefines mass = 0.5[kg];
        }
        part def VisionCamera :> SensorArray {
            attribute redefines powerConsumption = 5.0[W];
            attribute redefines mass = 0.2[kg];
        }
        part def SolarBatteryPack :> PowerSubsystem {
            attribute redefines powerConsumption = 0.0[W]; // acts as source
            attribute redefines mass = 2.0[kg];
        }
        part def Microcontroller :> FlightController {
            attribute redefines powerConsumption = 3.0[W];
            attribute redefines mass = 0.2[kg];
        }

        part def PhysicalDrone :> LogicalDrone {
            part redefines navSys : GPSModule;
            part redefines laserSys : LaserEmitter;
            part redefines sensorSys : VisionCamera;
            part redefines pwrSys : SolarBatteryPack;
            part redefines flightCtrl : Microcontroller;
        }

        part physicalDroneInstance : PhysicalDrone;
    }

    // -------------------------------------------------------------
    // VIEWS
    // -------------------------------------------------------------
    package DroneViews {
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
