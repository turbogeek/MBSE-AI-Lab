package BugDroneModel {
    private import ScalarValues::*;
    private import ISQ::*;
    private import SI::*;
    private import MeasurementReferences::*;

    package Requirements {
        requirement GeofenceReq {
            doc /* The system shall autonomously navigate via GPS within a geofenced area of 1 sq mile. */
        }
        requirement ClassificationReq {
            doc /* The system shall identify and classify insects using computer vision and AI. */
        }
        requirement EliminationReq {
            doc /* The system shall eliminate classified pests using a directed laser weapon. */
        }
        requirement SolarChargeReq {
            doc /* The system shall land and recharge its battery using solar power. */
        }
        requirement CommReq {
            doc /* The system shall transmit mission logs and video/telemetry to the base station via a communication link. */
        }
        requirement WeatherReq {
            doc /* The system shall operate safely in moderate rain. */
        }
        requirement AltitudeReq {
            doc /* The system shall not exceed a maximum altitude of 25 meters. */
        }
        requirement PayloadReq {
            doc /* The system shall support a payload capacity of up to 1 kg. */
        }
    }

    package SystemContext {
        private import Requirements::*;

        part def HumanOperator {
        }
        part def Environment {
        }
        part def InsectHuntingDrone {
        }

        part systemContext {
            part operator : HumanOperator;
            part environment : Environment;
            part drone : InsectHuntingDrone;
        }
    }

    package LogicalArchitecture {
        private import Requirements::*;

        part def NavigationSystem {
            satisfy GeofenceReq;
        }

        part def SensorArray {
            satisfy ClassificationReq;
        }

        part def LaserWeapon {
            satisfy EliminationReq;
        }

        part def FlightController {
            satisfy AltitudeReq;
            satisfy PayloadReq;
            satisfy WeatherReq;
        }

        part def CommunicationSystem {
            satisfy CommReq;
        }

        part def PowerSubsystem {
            satisfy SolarChargeReq;
        }

        part def LogicalDrone {
            part nav : NavigationSystem;
            part sensors : SensorArray;
            part laser : LaserWeapon;
            part flightCtrl : FlightController;
            part comms : CommunicationSystem;
            part power : PowerSubsystem;
        }
    }

    package PhysicalArchitecture {
        private import LogicalArchitecture::*;

        part def GPSAndIMU :> NavigationSystem {
        }

        part def AIVisionProcessingUnit :> SensorArray {
        }

        part def BlueDiodeLaser :> LaserWeapon {
        }

        part def FlightControllerBoard :> FlightController {
        }

        part def LTEAntenna :> CommunicationSystem {
        }

        part def SolarBatteryPack :> PowerSubsystem {
        }

        part def PhysicalDrone :> LogicalDrone {
            part physicalNav : GPSAndIMU redefines nav;
            part physicalSensors : AIVisionProcessingUnit redefines sensors;
            part physicalLaser : BlueDiodeLaser redefines laser;
            part physicalFlightCtrl : FlightControllerBoard redefines flightCtrl;
            part physicalComms : LTEAntenna redefines comms;
            part physicalPower : SolarBatteryPack redefines power;
        }
    }
}
