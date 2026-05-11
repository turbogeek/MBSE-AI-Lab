package BugDroneModel {
    private import ScalarValues::*;
    private import ISQ::*;
    private import SI::*;
    private import MeasurementReferences::*;

    package Stakeholders {
        part def Stakeholder;
        part def Farmer :> Stakeholder;
        part def EnvironmentalProtector :> Stakeholder;
        part def SystemOperator :> Stakeholder;
        part def SafetyRegulator :> Stakeholder;

        concern EnvironmentConcern {
            doc /* Concern regarding the protection of beneficial insects. */
        }
        concern OperationalConcern {
            doc /* Concern regarding autonomy, endurance, and remote telemetry. */
        }
        concern SafetyConcern {
            doc /* Concern regarding blade strikes, laser blinding, and battery fires. */
        }
    }

    package Requirements {
        private import Stakeholders::*;

        requirement <REQ1> 'Beneficial Insect Protection' {
            frame EnvironmentConcern;
            doc /* The system shall identify and classify insects using computer vision and AI. */
        }
        requirement <REQ2> 'Autonomous Geofencing' {
            frame OperationalConcern;
            doc /* The system shall autonomously navigate via GPS within a geofenced area. */
        }
        requirement <REQ3> 'Pest Elimination' {
            frame OperationalConcern;
            doc /* The system shall eliminate classified pests using a directed laser weapon. */
        }
        requirement <REQ4> 'Solar Endurance' {
            frame OperationalConcern;
            doc /* The system shall land and recharge its battery using solar power. */
        }
        requirement <REQ5> 'Rotor Guard Safety' {
            frame SafetyConcern;
            doc /* The drone's rotor blades must have physical guards or emergency stop mechanisms. */
        }
        requirement <REQ6> 'Battery Thermal Monitoring' {
            frame SafetyConcern;
            doc /* The battery subsystem must include thermal monitoring to prevent fires. */
        }
        requirement <REQ7> 'Laser Eye Safety' {
            frame SafetyConcern;
            doc /* The laser weapon must have safety interlocks to prevent firing toward humans. */
        }
        requirement <REQ8> 'Telemetry Transmission' {
            frame OperationalConcern;
            doc /* The system shall transmit mission logs and telemetry to the base station via a communication link. */
        }
    }

    package SystemContext {
        private import Stakeholders::SystemOperator;
        part def Environment;
        part def InsectHuntingDrone;

        part systemContext {
            part operator : SystemOperator;
            part environment : Environment;
            part drone : InsectHuntingDrone;
        }
    }

    package LogicalArchitecture {
        private import Requirements::*;

        action def PatrolAction;
        action def TargetAction;
        action def FireLaserAction;
        action def SolarChargeAction;
        action def EmergencyStopAction;

        part def NavigationSystem {
            satisfy 'Autonomous Geofencing';
        }

        part def SensorArray {
            satisfy 'Beneficial Insect Protection';
        }

        part def LaserWeapon {
            satisfy 'Pest Elimination';
            satisfy 'Laser Eye Safety';
        }

        part def PowerSubsystem {
            satisfy 'Solar Endurance';
            satisfy 'Battery Thermal Monitoring';
        }

        part def CommunicationSystem {
            satisfy 'Telemetry Transmission';
        }

        part def FlightController {
            satisfy 'Rotor Guard Safety';
            perform action p : PatrolAction;
            perform action e : EmergencyStopAction;
        }

        part def LogicalDrone {
            part nav : NavigationSystem;
            part sensors : SensorArray;
            part laser : LaserWeapon;
            part power : PowerSubsystem;
            part flightCtrl : FlightController;
            part comms : CommunicationSystem;

            state droneStateMachine {
                entry;
                then Patrolling;

                state Patrolling {
                    do action : PatrolAction;
                }

                transition Patrolling_to_Targeting
                    first Patrolling

                    then Targeting;

                state Targeting {
                    do action : TargetAction;
                }

                transition Targeting_to_Firing
                    first Targeting

                    then Firing;

                state Firing {
                    do action : FireLaserAction;
                }

                transition Firing_to_Patrolling
                    first Firing

                    then Patrolling;

                transition Patrolling_to_Charging
                    first Patrolling

                    then Charging;

                state Charging {
                    do action : SolarChargeAction;
                }

                transition Charging_to_Patrolling
                    first Charging

                    then Patrolling;

                transition Any_to_Emergency
                    first Patrolling
                    then EmergencyGrounded;

                state EmergencyGrounded {
                    do action : EmergencyStopAction;
                }
            }
        }
    }

    package PhysicalArchitecture {
        private import LogicalArchitecture::*;

        part def GPSModule :> NavigationSystem {}
        part def AIVisionUnit :> SensorArray {}
        part def DiodeLaser :> LaserWeapon {}
        part def SolarBatteryPack :> PowerSubsystem {}
        part def FlightControllerBoard :> FlightController {}
        part def LTERFAntenna :> CommunicationSystem {}

        part def PhysicalDrone :> LogicalDrone {
            part physicalNav : GPSModule redefines nav;
            part physicalSensors : AIVisionUnit redefines sensors;
            part physicalLaser : DiodeLaser redefines laser;
            part physicalPower : SolarBatteryPack redefines power;
            part physicalFlightCtrl : FlightControllerBoard redefines flightCtrl;
            part physicalComms : LTERFAntenna redefines comms;
        }
    }

    package Views {
        private import LogicalArchitecture::*;
        private import PhysicalArchitecture::*;

        view DroneArchitectureView {
            expose LogicalDrone;
            expose PhysicalDrone;
            expose LogicalDrone::droneStateMachine;
        }
    }
}
