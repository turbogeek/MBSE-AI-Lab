package BugDroneModel {
	private import ISQ::*;
	private import SI::*;
	private import ScalarValues::*;
	
	/* ================= Stakeholders & Concerns ================= */
	package Stakeholders {
		doc /* ================= Stakeholders & Concerns ================= */
		private import ISQ::*;
		private import SI::*;
		private import ScalarValues::*;
		
		part def AgriculturalManager;
		part def EnvironmentalRegulator;
		part def SafetyAuthority;
		part def SystemMaintainer;
		
		concern <C1> 'Pest Reduction' {
			doc /* The drone must effectively reduce pest populations. */
		}
		concern <C2> 'Safety' {
			doc /* The drone must operate safely without harming humans or the environment. */
		}
		concern <C3> 'Cost and Efficiency' {
			doc /* The drone must be cost-effective and have adequate flight time. */
		}
		concern <C4> 'Weight and Power' {
			doc /* The drone must manage weight and power consumption efficiently. */
		}
	}
	
	/* ================= Requirements ================= */
	package Requirements {
		doc /* ================= Requirements ================= */
		private import ISQ::*;
		private import SI::*;
		private import ScalarValues::*;
		private import Stakeholders::*;
		
		requirement <REQ1> REQ1_AutonomousNavigation {
			doc /* The system shall navigate autonomously along its planned trajectory within a tolerance of +/- 2.0 meters horizontally and +/- 0.5 meters vertically. */
			frame C1;
		}
		
		requirement <REQ2> REQ2_GeofenceEnforcement {
			doc /* The system shall automatically halt horizontal progression within 5.0 meters of the boundary of a predefined 1x1 mile geofence, and shall not exceed a maximum altitude of 25.0 meters AGL. */
			frame C2;
		}
		
		requirement <REQ3> REQ3_CVIdentification {
			doc /* The system shall identify and classify insects as "pest" or "beneficial" with a minimum accuracy of 95% under nominal daylight conditions. */
			frame C1;
		}
		
		requirement <REQ4> REQ4_PestElimination {
			doc /* Upon positive identification of a pest, the system shall engage the target using its laser weapon, achieving a confirmed elimination rate of at least 90% per attempted engagement. */
			frame C1;
		}
		
		requirement <REQ5> REQ5_TargetDiscrimination {
			doc /* The system shall exhibit a false-positive engagement rate (engaging a beneficial insect or non-insect) of less than 0.1%. */
			frame C1;
		}
		
		requirement <REQ6> REQ6_AutonomousRecharge {
			doc /* Upon detecting battery capacity below 20%, the system shall autonomously land within a 1.0 x 1.0 meter safe zone and recharge its batteries to 100% capacity using integrated solar panels within 6 hours of optimal sunlight. */
			frame C3;
		}
		
		requirement <REQ7> REQ7_TelemetryReporting {
			doc /* The system shall transmit a telemetry and status report (including battery level, location, and kill count) to the human operator interface at an interval not exceeding 5 minutes. */
			frame C3;
		}
		
		requirement <REQ8> REQ8_BatteryThermalSafety {
			doc /* The system shall continuously monitor battery temperature and autonomously sever the main power circuit within 500 milliseconds if the temperature exceeds 60°C. */
			frame C2;
		}
		
		requirement <REQ9> REQ9_LaserInterlockSafety {
			doc /* The system shall disable the laser firing circuit within 50 milliseconds if the drone's pitch or roll exceeds 30 degrees, or if a human is detected in the field of view. */
			frame C2;
		}
		
		requirement <REQ10> REQ10_RotorSafety {
			doc /* The system's rotor assemblies shall feature physical guards that prevent a 1-inch diameter spherical probe from contacting the spinning blades from any lateral direction. */
			frame C2;
		}
		
		requirement <REQ11> REQ11_FlightTime {
			doc /* The system shall maintain an operational flight duration of at least 45 minutes on a full battery charge. */
			frame C3;
			attribute flightTime : TimeValue;
			require constraint { flightTime >= 45.0 [min] }
		}
		
		requirement <REQ12> REQ12_WeightLimit {
			doc /* The system takeoff weight, including all subsystems and a maximum payload capacity of 1 kg, shall be less than 5.0 kg. */
			frame C4;
			attribute totalWeight : MassValue;
			require constraint { totalWeight < 5.0 [kg] }
		}
	}
	
	/* ================= System Context ================= */
	package SystemContext {
		doc /* ================= System Context ================= */
		private import ISQ::*;
		private import SI::*;
		private import ScalarValues::*;
		private import Stakeholders::*;
		private import LogicalArchitecture::*;
		
		part def HumanOperator;
		part def Environment;
		
		part def DroneContext {
			part drone : InsectHuntingDrone;
			part operator : HumanOperator;
			part environment : Environment;
		}
	}
	
	/* ================= Logical Architecture ================= */
	package LogicalArchitecture {
		doc /* ================= Logical Architecture ================= */
		private import ISQ::*;
		private import SI::*;
		private import ScalarValues::*;
		private import Requirements::*;
		
		part def NavigationSystem {
			attribute power : PowerValue := 5.0 [W];
			attribute weight : MassValue := 0.2 [kg];
			satisfy REQ1_AutonomousNavigation;
			satisfy REQ2_GeofenceEnforcement;
			satisfy REQ7_TelemetryReporting;
		}
		part def LaserWeapon {
			attribute power : PowerValue := 50.0 [W];
			attribute weight : MassValue := 0.5 [kg];
			satisfy REQ4_PestElimination;
			satisfy REQ9_LaserInterlockSafety;
		}
		part def SensorArray {
			attribute power : PowerValue := 30.0 [W];
			attribute weight : MassValue := 0.4 [kg];
			satisfy REQ3_CVIdentification;
			satisfy REQ5_TargetDiscrimination;
		}
		part def PowerSubsystem {
			attribute power : PowerValue := 10.0 [W];
			attribute weight : MassValue := 1.0 [kg];
			satisfy REQ6_AutonomousRecharge;
			satisfy REQ8_BatteryThermalSafety;
		}
		part def FlightController {
			attribute power : PowerValue := 150.0 [W];
			attribute weight : MassValue := 1.5 [kg];
			satisfy REQ10_RotorSafety;
		}
		
		item def batteryFullEvent;
		item def targetDetectedEvent;
		item def lowBatteryEvent;
		item def emergencyEvent;
		item def pestConfirmedEvent;
		item def beneficialConfirmedEvent;
		item def targetDestroyedEvent;
		
		state def DroneBehavior {
			entry; then Charging;
			
			state Charging;
			accept batteryFullEvent
				then Patrolling;
				
			state Patrolling;
			accept targetDetectedEvent
				then Targeting;
			accept lowBatteryEvent
				then Charging;
			accept emergencyEvent
				then EmergencyHalt;
				
			state Targeting;
			accept pestConfirmedEvent
				then Engaging;
			accept beneficialConfirmedEvent
				then Patrolling;
			accept emergencyEvent
				then EmergencyHalt;
				
			state Engaging;
			accept targetDestroyedEvent
				then Patrolling;
			accept emergencyEvent
				then EmergencyHalt;
				
			state EmergencyHalt;
		}
		
		part def InsectHuntingDrone {
			part navigation : NavigationSystem;
			part laser : LaserWeapon;
			part sensors : SensorArray;
			part powerSys : PowerSubsystem;
			part flightCtrl : FlightController;
			
			attribute totalPower : PowerValue := 
				navigation.power + laser.power + sensors.power + powerSys.power + flightCtrl.power;
			
			attribute totalWeight : MassValue := 
				navigation.weight + laser.weight + sensors.weight + powerSys.weight + flightCtrl.weight;
				
			exhibit state droneBehavior : DroneBehavior;
			
			satisfy REQ11_FlightTime;
			satisfy REQ12_WeightLimit;
		}
	}
	
	/* ================= Physical Architecture ================= */
	package PhysicalArchitecture {
		doc /* ================= Physical Architecture ================= */
		private import ISQ::*;
		private import SI::*;
		private import ScalarValues::*;
		private import LogicalArchitecture::*;
		
		part def GPSModule :> NavigationSystem {
			attribute redefines power := 4.0 [W];
			attribute redefines weight := 0.1 [kg];
		}
		
		part def BlueLaserModule :> LaserWeapon {
			attribute redefines power := 45.0 [W];
			attribute redefines weight := 0.4 [kg];
		}
		
		part def RGBAndIRCamera :> SensorArray {
			attribute redefines power := 25.0 [W];
			attribute redefines weight := 0.3 [kg];
		}
		
		part def LiPoBatterySolar :> PowerSubsystem {
			attribute redefines power := 8.0 [W];
			attribute redefines weight := 1.2 [kg];
		}
		
		part def QuadCopterFrame :> FlightController {
			attribute redefines power := 160.0 [W];
			attribute redefines weight := 1.8 [kg];
		}
		
		part def ESC {
			attribute power : PowerValue := 10.0 [W];
			attribute weight : MassValue := 0.15 [kg];
		}
		
		part def PX4FlightController :> FlightController {
			attribute redefines power := 5.0 [W];
			attribute redefines weight := 0.1 [kg];
		}
		
		part def RCReceiver :> NavigationSystem {
			attribute redefines power := 2.0 [W];
			attribute redefines weight := 0.05 [kg];
		}
		
		part def PhysicalBugDrone :> InsectHuntingDrone {
			part redefines navigation : GPSModule;
			part redefines laser : BlueLaserModule;
			part redefines sensors : RGBAndIRCamera;
			part redefines powerSys : LiPoBatterySolar;
			part redefines flightCtrl : QuadCopterFrame;
			
			part esc : ESC;
			part px4FlightCtrl : PX4FlightController;
			part rcReceiver : RCReceiver;
			
			attribute redefines totalPower := 
				navigation.power + laser.power + sensors.power + powerSys.power + flightCtrl.power + esc.power + px4FlightCtrl.power + rcReceiver.power;
				
			attribute redefines totalWeight := 
				navigation.weight + laser.weight + sensors.weight + powerSys.weight + flightCtrl.weight + esc.weight + px4FlightCtrl.weight + rcReceiver.weight;
		}
	}
	
	/* ================= Allocations ================= */
	package Allocations {
		doc /* ================= Allocations ================= */
		private import ISQ::*;
		private import SI::*;
		private import ScalarValues::*;
		private import LogicalArchitecture::*;
		private import PhysicalArchitecture::*;
		
		part myLogical : InsectHuntingDrone;
		part myPhysical : PhysicalBugDrone;
		
		allocate myPhysical.navigation to myLogical.navigation;
		allocate myPhysical.laser to myLogical.laser;
		allocate myPhysical.sensors to myLogical.sensors;
		allocate myPhysical.powerSys to myLogical.powerSys;
		allocate myPhysical.flightCtrl to myLogical.flightCtrl;
		
		allocate myPhysical.esc to myLogical.flightCtrl;
		allocate myPhysical.px4FlightCtrl to myLogical.flightCtrl;
		allocate myPhysical.rcReceiver to myLogical.navigation;
	}
	
	/* ================= Views ================= */
	package Views {
		doc /* ================= Views ================= */
		private import ISQ::*;
		private import SI::*;
		private import ScalarValues::*;
		private import Stakeholders::*;
		private import Requirements::*;
		private import SystemContext::*;
		private import LogicalArchitecture::*;
		private import PhysicalArchitecture::*;
		private import Allocations::*;
		private import DS_Views::TabularViews::*;
		
		view concernsTable : gt {
			expose Stakeholders::C1;
			expose Stakeholders::C2;
			expose Stakeholders::C3;
			expose Stakeholders::C4;
		}
		
		view stakeholdersTable : gt {
			expose Stakeholders::AgriculturalManager;
			expose Stakeholders::EnvironmentalRegulator;
			expose Stakeholders::SafetyAuthority;
			expose Stakeholders::SystemMaintainer;
		}
		
		view statesTable : gt {
			expose LogicalArchitecture::DroneBehavior;
		}
		
		view partsTable : gt {
			expose LogicalArchitecture::InsectHuntingDrone;
			expose PhysicalArchitecture::PhysicalBugDrone;
		}
		
		view requirementsTable : rt {
			expose Requirements::**;
		}
		
		view attributesTable : gt {
			expose Requirements::REQ11_FlightTime::flightTime;
			expose Requirements::REQ12_WeightLimit::totalWeight;
		}
	}
}
