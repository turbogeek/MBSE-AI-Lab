package BugDroneModel {
    private import ScalarValues::*;
    private import ISQ::*;
    private import SI::*;
    private import MeasurementReferences::*;

    package Requirements {
        requirement GeofenceReq {
            doc /* The system shall autonomously navigate using GPS within a defined 1 mile by 1 mile geofence. */
        }
        requirement ClassificationReq {
            doc /* The system shall use computer vision and AI to classify insects as pest or beneficial. */
        }
        requirement EliminationReq {
            doc /* The system shall eliminate identified pests using a directed laser weapon. */
        }
        requirement SolarChargeReq {
            doc /* The system shall land and recharge its internal batteries using integrated solar panels. */
        }
        requirement CommReq {
            doc /* The system shall transmit telemetry and mission logs to a human operator via a communication link. */
        }
        requirement AltitudeReq {
            doc /* The system shall have a maximum operating altitude of 25 meters. */
        }
        requirement PayloadReq {
            doc /* The system shall carry a functional payload of up to 1 kg. */
        }
    }

    package SystemContext {
        private import Requirements::*;

        part def HumanOperator {
        }
        part def Environment {
            attribute weatherConditions : String;
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

        part def FlightController {
            satisfy AltitudeReq;
            satisfy PayloadReq;
        }

        part def NavigationSystem {
            satisfy GeofenceReq;
        }

        part def SensorArray {
            satisfy ClassificationReq;
        }

        part def WeaponSystem {
            satisfy EliminationReq;
        }

        part def PowerSubsystem {
            satisfy SolarChargeReq;
        }

        part def CommunicationSystem {
            satisfy CommReq;
        }

        part def LogicalDrone {
            part flightCtrl : FlightController;
            part navSystem : NavigationSystem;
            part sensors : SensorArray;
            part weapon : WeaponSystem;
            part power : PowerSubsystem;
            part comms : CommunicationSystem;
        }
    }

    package PhysicalArchitecture {
        private import LogicalArchitecture::*;

        part def QuadRotorChassis :> FlightController {
        }

        part def GPSModule :> NavigationSystem {
        }

        part def CameraAndAI :> SensorArray {
        }

        part def LaserDiode :> WeaponSystem {
        }

        part def SolarBatteryPack :> PowerSubsystem {
        }

        part def RFTransceiver :> CommunicationSystem {
        }

        part def PhysicalDrone :> LogicalDrone {
            part physicalFlightCtrl : QuadRotorChassis redefines flightCtrl;
            part physicalNavSystem : GPSModule redefines navSystem;
            part physicalSensors : CameraAndAI redefines sensors;
            part physicalWeapon : LaserDiode redefines weapon;
            part physicalPower : SolarBatteryPack redefines power;
            part physicalComms : RFTransceiver redefines comms;
        }
    }
}
