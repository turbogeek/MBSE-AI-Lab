package ToasterModel {
    private import ScalarValues::*;
    private import ISQ::*;
    private import SI::*;
    private import MeasurementReferences::*;

    package Requirements {
        requirement HeatingReq {
            doc /* The system shall heat the bread using resistive heating elements. */
        }
        requirement CapacityReq {
            doc /* The system shall accommodate up to two standard slices of bread simultaneously. */
        }
        requirement EjectReq {
            doc /* The system shall automatically eject the bread upon completion of the toasting cycle. */
        }
        requirement CancelReq {
            doc /* The system shall provide a manual cancel function to abort the toasting cycle. */
        }
        requirement PowerReq {
            doc /* The system's maximum power consumption shall not exceed 900 Watts. */
        }
    }

    package LogicalArchitecture {
        private import Requirements::*;

        part def HeatingSubsystem {
            satisfy HeatingReq;
        }

        part def ControlSubsystem {
            satisfy CancelReq;
        }

        part def MechanicalSubsystem {
            satisfy CapacityReq;
            satisfy EjectReq;
        }

        part def EnclosureSubsystem {
        }

        part def Toaster {
            part heating : HeatingSubsystem;
            part control : ControlSubsystem;
            part mechanism : MechanicalSubsystem;
            part enclosure : EnclosureSubsystem;

            attribute powerConsumption : PowerValue [1];
            satisfy PowerReq;
        }
    }

    package PhysicalArchitecture {
        private import LogicalArchitecture::*;

        part def NichromeWire :> HeatingSubsystem {
        }

        part def ElectronicTimer :> ControlSubsystem {
        }

        part def SpringCarriage :> MechanicalSubsystem {
        }

        part def PlasticHousing :> EnclosureSubsystem {
        }

        part def PhysicalToaster :> Toaster {
            part physicalHeating : NichromeWire redefines heating;
            part physicalControl : ElectronicTimer redefines control;
            part physicalMechanism : SpringCarriage redefines mechanism;
            part physicalEnclosure : PlasticHousing redefines enclosure;
        }
    }
}
