package ToasterModel {
	
	package Stakeholders {
		part def Consumer;
		part def Manufacturer;
		part def SafetyRegulator;
		
		concern <C1> 'Toast Bread Reliably' {
			doc /* The toaster must heat sliced bread reliably and evenly. */
		}
		
		concern <C2> 'Ensure Safety' {
			doc /* The toaster must be safe from electrical and thermal hazards. */
		}
		
		concern <C3> 'Ease of Use' {
			doc /* The toaster must provide an intuitive interface. */
		}
	}
	
	package Requirements {
		public import Stakeholders::*;
		
		requirement <REQ1> 'Two Slot Capacity' {
			doc /* The system shall accommodate up to two standard slices of bread simultaneously. */
			frame C1;
		}
		
		requirement <REQ2> 'Resistive Heating' {
			doc /* The system shall heat the bread using resistive heating elements. */
			frame C1;
		}
		
		requirement <REQ3> 'Auto Eject' {
			doc /* The system shall automatically eject the bread upon completion of the toasting cycle. */
			frame C1;
		}
		
		requirement <REQ4> 'Manual Cancel' {
			doc /* The system shall provide a manual cancel function to abort the toasting cycle. */
			frame C3;
		}
		
		requirement <REQ5> 'Power Consumption' {
			doc /* The system's maximum power consumption shall not exceed 900 Watts. */
			frame C2;
		}
		
		requirement <REQ6> 'Thermal Safety' {
			doc /* The system shall prevent access to live electrical components and limit exterior temperature. */
			frame C2;
		}
	}
	
	package SystemContext {
		public import Stakeholders::*;
		public import LogicalArchitecture::*;
		public import PhysicalArchitecture::*;
		
		part def ToasterContext {
			part toaster : Toaster;
			part user : Consumer;
			part powerGrid;
			part breadSlice[1..2];
		}
	}
	
	package LogicalArchitecture {
		part def HeatingSubsystem;
		part def ControlSubsystem;
		part def MechanicalSubsystem;
		part def EnclosureSubsystem;
		
		part def Toaster {
			part heatingSubsystem : HeatingSubsystem;
			part controlSubsystem : ControlSubsystem;
			part mechanicalSubsystem : MechanicalSubsystem;
			part enclosureSubsystem : EnclosureSubsystem;
		}
	}
	
	package PhysicalArchitecture {
		public import LogicalArchitecture::*;
		
		part def ToasterPhysical {
			part resistiveNichromeWire : HeatingSubsystem;
			part timerCircuit : ControlSubsystem;
			part springCarriage : MechanicalSubsystem;
			part housing : EnclosureSubsystem;
		}
	}
	
	package Views {
		public import Stakeholders::*;
		public import Requirements::*;
		public import SystemContext::*;
		public import LogicalArchitecture::*;
		public import PhysicalArchitecture::*;
		
		view 'Requirements View' {
			expose Requirements::**;
		}
		
		view 'Logical Architecture View' {
			expose LogicalArchitecture::**;
		}
		
		view 'Physical Architecture View' {
			expose PhysicalArchitecture::**;
		}
	}
}
