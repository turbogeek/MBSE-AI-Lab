package BugDroneModel {
	public import ISQ::*;
	public import SI::*;
	public import ScalarValues::*;
	
	package Stakeholders {
		part def AgriculturalManager;
		part def EnvironmentalRegulator;
		part def SafetyAuthority;
		part def SystemMaintainer;
		
		concern <C1> 'Pest Reduction' {
			doc /* The drone must effectively reduce pest populations. */
		}
		concern <C2> 'Safety' {
			doc /* The drone must operate safely without harming humans or environment. */
		}
		concern <C3> 'Cost and Efficiency' {
			doc /* The drone must be cost-effective and have adequate flight time. */
		}
		concern <C4> 'Weight and Power' {
			doc /* The drone must manage weight and power consumption efficiently. */
		}
	}
	
	package Requirements {
		public import Stakeholders::*;
		
		requirement <REQ1> 'Autonomous Navigation' {
			doc /* The system shall navigate autonomously along its planned trajectory within a tolerance of +/- 2.0 meters horizontally and +/- 0.5 meters vertically. */
			frame C1;
		}
		
		requirement <REQ2> 'Geofence Enforcement' {
			doc /* The system shall automatically halt horizontal progression within 5.0 meters of the boundary of a predefined 1x1 mile geofence, and shall not exceed a maximum altitude of 25.0 meters AGL. */
			frame C2;
		}
		
		requirement <REQ3> 'Computer Vision Identification' {
			doc /* The system shall identify and classify insects as "pest" or "beneficial" with a minimum accuracy of 95% under nominal daylight conditions. */
			frame C1;
		}
		
		requirement <REQ4> 'Pest Elimination' {
			doc /* Upon positive identification of a pest, the system shall engage the target using its laser weapon, achieving a confirmed elimination rate of at least 90% per attempted engagement. */
			frame C1;
		}
		
		requirement <REQ5> 'Target Discrimination' {
			doc /* The system shall exhibit a false-positive engagement rate (engaging a beneficial insect or non-insect) of less than 0.1%. */
			frame C1;
		}
		
		requirement <REQ6> 'Autonomous Recharge' {
			doc /* Upon detecting battery capacity below 20%, the system shall autonomously land within a 1.0 x 1.0 meter safe zone and recharge its batteries to 100% capacity using integrated solar panels within 6 hours of optimal sunlight. */
			frame C3;
		}
		
		requirement <REQ7> 'Telemetry Reporting' {
			doc /* The system shall transmit a telemetry and status report (including battery level, location, and kill count) to the human operator interface at an interval not exceeding 5 minutes. */
			frame C3;
		}
		
		requirement <REQ8> 'Battery Thermal Safety' {
			doc /* The system shall continuously monitor battery temperature and autonomously sever the main power circuit within 500 milliseconds if the temperature exceeds 60 degC. */
			frame C2;
		}
		
		requirement <REQ9> 'Laser Interlock Safety' {
			doc /* The system shall disable the laser firing circuit within 50 milliseconds if the drone's pitch or roll exceeds 30 degrees, or if a human is detected in the field of view. */
			frame C2;
		}
		
		requirement <REQ10> 'Rotor Safety' {
			doc /* The system's rotor assemblies shall feature physical guards that prevent a 1-inch diameter spherical probe from contacting the spinning blades from any lateral direction. */
			frame C2;
		}
		
		requirement <REQ11> 'Flight Time' {
			doc /* Continuous operational flight duration on a full charge. */
			frame C3;
			attribute flightTime : TimeValue;
			require constraint { flightTime >= 45.0 [min] }
		}
		
		requirement <REQ12> 'Weight Limit' {
			doc /* Total takeoff weight including payload. */
			frame C4;
			attribute totalWeight : MassValue;
			require constraint { totalWeight < 5.0 [kg] }
		}
	}
	
	package SystemContext {
		public import Stakeholders::*;
		public import LogicalArchitecture::*;
		
		part def HumanOperator;
		part def Environment;
		
		part def DroneContext {
			part drone : InsectHuntingDrone;
			part operator : HumanOperator;
			part environment : Environment;
		}
	}
	
	package LogicalArchitecture {
		part def NavigationSystem {
			attribute power : PowerValue := 5.0 [W];
			attribute weight : MassValue := 0.2 [kg];
		}
		part def LaserWeapon {
			attribute power : PowerValue := 50.0 [W];
			attribute weight : MassValue := 0.5 [kg];
		}
		part def SensorArray {
			attribute power : PowerValue := 30.0 [W];
			attribute weight : MassValue := 0.4 [kg];
		}
		part def PowerSubsystem {
			attribute power : PowerValue := 10.0 [W];
			attribute weight : MassValue := 1.0 [kg];
		}
		part def FlightController {
			attribute power : PowerValue := 150.0 [W]; // Motors and controller
			attribute weight : MassValue := 1.5 [kg];
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
				
			exhibit state behavior : DroneBehavior;
		}
	}
	
	package PhysicalArchitecture {
		public import LogicalArchitecture::*;
		
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
		
		part def PhysicalBugDrone :> InsectHuntingDrone {
			part redefines navigation : GPSModule;
			part redefines laser : BlueLaserModule;
			part redefines sensors : RGBAndIRCamera;
			part redefines powerSys : LiPoBatterySolar;
			part redefines flightCtrl : QuadCopterFrame;
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
		
		view 'State Machine View' {
			expose LogicalArchitecture::DroneBehavior;
		}
	}
}
