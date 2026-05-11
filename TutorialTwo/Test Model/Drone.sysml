package Drone {
    package DroneFlyHunter {
        doc /* Autonomous drone that patrols a user-defined area, detects flies, and eliminates them with a low-power eye-safe laser. SysMLv2 model organized by Concept / UseCases / Requirements / Structure / Behavior.*/
        package Concept {
            doc /* Stakeholder-facing view: mission statement, high-level system as a black box, key concept attributes.*/
            part Mission {
                doc /* Automate fly elimination in enclosed spaces (homes, restaurants, food processing) with minimal human intervention and zero chemical use.*/
            }
            part DroneSystem_Concept {
                doc /* The Drone system as viewed from outside: a thing that patrols and hunts.*/
                attribute patrolArea_m2 {
                    doc /* User-configured patrol area in square meters (max 100).*/
                }
                attribute maxFlightTime_min {
                    doc /* Minimum guaranteed flight time per charge.*/
                }
                attribute laserRange_m {
                    doc /* Effective elimination range.*/
                }
                attribute detectionAccuracy {
                    doc /* Fly-detection accuracy (fraction).*/
                }
                attribute rechargeTime_min {
                    doc /* Time from empty to full charge.*/
                }
                attribute eyeSafetyClass {
                    doc /* Laser eye-safety class (Class 1M default).*/
                }
            }
        }
        package UseCases {
            doc /* Mission threads — nominal and corner cases.*/
            use case UC_PatrolArea {
                doc /* Autonomously patrol the configured rectangular area.*/
            }
            use case UC_DetectFly {
                doc /* Identify a fly in the camera frame with >=95% confidence.*/
            }
            use case UC_EngageFly {
                doc /* Track and eliminate a detected fly with the laser.*/
            }
            use case UC_ReturnToBase {
                doc /* Navigate to the charging station on command or low battery.*/
            }
            use case UC_Recharge {
                doc /* Dock, negotiate power, charge, and resume.*/
            }
            use case UC_ConfigurePatrolArea {
                doc /* Operator defines a rectangular 2D patrol boundary.*/
            }
            use case UC_ConfigureLaser {
                doc /* Operator sets laser power/duration within safety limits.*/
            }
            use case UC_EmergencyAbort {
                doc /* Immediate land-and-disarm on operator kill-switch.*/
            }
            use case UC_HumanSafety {
                doc /* Abort laser firing when a human is within 5 m.*/
            }
            use case UC_ObstacleAvoidance {
                doc /* Deflect path to maintain >=1 m from obstacles.*/
            }
            use case UC_LowBatteryResponse {
                doc /* Return to base when battery <=20%.*/
            }
            use case UC_MultipleTargets {
                doc /* Prioritize closest fly when several are in view.*/
            }
            use case UC_GPSLoss {
                doc /* Hover at last known position on GPS fix loss.*/
            }
            use case UC_PropellerFailure {
                doc /* Safe descent on rotor/propeller fault.*/
            }
            use case UC_WeatherAbort {
                doc /* Refuse takeoff / return home when wind > 20 km/h.*/
            }
        }
        package Requirements {
            doc /* All requirements, bucketed.*/
            package Functional {
                doc /* What the drone does.*/
                requirement R_DetectFly {
                    doc /* The drone shall detect flies with >=95% accuracy within 3 m.*/
                }
                requirement R_TrackFly {
                    doc /* The drone shall track a moving fly up to 5 m/s with the laser.*/
                }
                requirement R_EliminateFly {
                    doc /* The drone shall eliminate a targeted fly within 0.5 s of lock.*/
                }
                requirement R_PatrolArea {
                    doc /* The drone shall patrol a user-defined rectangular area.*/
                }
                requirement R_HoverAccuracy {
                    doc /* The drone shall maintain hover within 10 cm of a waypoint.*/
                }
                requirement R_MultiTarget {
                    doc /* When multiple flies are visible, the drone shall engage the closest first.*/
                }
                requirement R_ConfigArea {
                    doc /* The operator shall configure the patrol area via a bounded-rectangle input.*/
                }
                requirement R_KillSwitch {
                    doc /* The drone shall accept an operator emergency-stop via a dedicated radio command.*/
                }
            }
            package Performance {
                doc /* How well and how fast.*/
                requirement R_FlightTime {
                    doc /* The drone shall operate >=30 minutes per full charge.*/
                }
                requirement R_RechargeTime {
                    doc /* The drone shall fully recharge in <=60 minutes.*/
                }
                requirement R_LaserRange {
                    doc /* Laser effective range shall be 0.5 m to 5 m.*/
                }
                requirement R_MaxPatrolArea {
                    doc /* The drone shall cover a patrol area up to 100 m^2.*/
                }
                requirement R_PositioningAcc {
                    doc /* Positioning accuracy shall be <=10 cm (indoor) / <=1 m (outdoor).*/
                }
                requirement R_ResponseTime {
                    doc /* From fly detection to laser fire: <=1.0 s.*/
                }
            }
            package Safety {
                doc /* Non-negotiable constraints.*/
                requirement R_HumanDetection {
                    doc /* The drone shall detect humans within 5 m and inhibit laser firing.*/
                }
                requirement R_LowBatteryRTB {
                    doc /* The drone shall return to base when battery <=20%.*/
                }
                requirement R_EyeSafeLaser {
                    doc /* Laser output shall be Class 1M eye-safe when not aimed at a target.*/
                }
                requirement R_ObstacleDistance {
                    doc /* The drone shall maintain >=1 m from walls and static obstacles.*/
                }
                requirement R_Geofence {
                    doc /* The drone shall not operate outside the configured patrol area.*/
                }
                requirement R_WindLimit {
                    doc /* The drone shall not operate when sustained wind exceeds 20 km/h.*/
                }
                requirement R_DisarmOnLand {
                    doc /* The drone shall disarm the laser within 0.2 s of landing.*/
                }
            }
            package CornerCases {
                doc /* Off-nominal conditions.*/
                requirement R_GPSLoss {
                    doc /* On GPS fix loss, hover at last known position and request operator.*/
                }
                requirement R_PropellerFailure {
                    doc /* On propeller/motor fault, enter safe-descent mode to nearest clear floor.*/
                }
                requirement R_TempLimit {
                    doc /* Above 40 °C ambient, reduce flight time by 20% and warn operator.*/
                }
                requirement R_Darkness {
                    doc /* In ambient light <10 lux, enable onboard LED illumination for detection.*/
                }
                requirement R_SwarmConflict {
                    doc /* When another drone is detected within 2 m, yield and re-plan path.*/
                }
                requirement R_SurfaceReflect {
                    doc /* On a reflective surface (mirror/glass) within 2 m of target, inhibit laser.*/
                }
                requirement R_UnresponsiveOp {
                    doc /* If operator heartbeat is lost for >=30 s, return to base.*/
                }
            }
        }
        package Structure {
            doc /* Parts and their decomposition (logical design).*/
            view 'Level Two A' : DS_Views::SymbolicViews::gv {
                expose Structure::*;
            }
            part Drone {
                doc /* The fly-hunting drone as a logical part; aggregates all subsystems.*/
                ref part FlightController {
                    doc /* Runs flight control loops, path planning, telemetry.*/
                    ref satisfy requirement s_flightCtl_PatrolArea ::> Requirements::Functional::R_PatrolArea;
                    ref satisfy requirement s_flightCtl_HoverAccuracy ::> Requirements::Functional::R_HoverAccuracy;
                    ref satisfy requirement s_flightCtl_MaxPatrolArea ::> Requirements::Performance::R_MaxPatrolArea;
                    ref satisfy requirement s_flightCtl_PositioningAcc ::> Requirements::Performance::R_PositioningAcc;
                }
                ref part PropulsionSystem {
                    doc /* 4 motor+propeller assemblies; thrust and yaw control.*/
                    ref part Motor_1;
                    ref part Motor_2;
                    ref part Motor_3;
                    ref part Motor_4;
                    ref part Propeller_1;
                    ref part Propeller_2;
                    ref part Propeller_3;
                    ref part Propeller_4;
                    ref part ESC_Board {
                        doc /* Electronic speed controller board.*/
                    }
                    ref satisfy requirement s_propulsion_PropellerFailure ::> Requirements::CornerCases::R_PropellerFailure;
                }
                ref part PowerSystem {
                    doc /* Battery, power management, recharge interface.*/
                    ref part Battery {
                        doc /* LiPo or Li-ion pack; primary energy store.*/
                        attribute capacity_Wh {
                            doc /* Nominal battery capacity in watt-hours.*/
                        }
                        attribute cycles_remaining {
                            doc /* Remaining charge cycles before capacity fade threshold.*/
                        }
                    }
                    ref part PowerManagement {
                        doc /* Voltage regulation, cell balancing, charge control.*/
                    }
                    ref part ChargePort {
                        doc /* Dock-based contactless or pin charging interface.*/
                    }
                    ref satisfy requirement s_power_FlightTime ::> Requirements::Performance::R_FlightTime;
                    ref satisfy requirement s_power_RechargeTime ::> Requirements::Performance::R_RechargeTime;
                    ref satisfy requirement s_power_LowBatteryRTB ::> Requirements::Safety::R_LowBatteryRTB;
                    ref satisfy requirement s_power_TempLimit ::> Requirements::CornerCases::R_TempLimit;
                }
                ref part VisionSystem {
                    doc /* Camera + image-processing pipeline for detection.*/
                    ref part Camera {
                        doc /* RGB + IR for low-light detection.*/
                    }
                    ref part FlyDetectorAlgo {
                        doc /* ML model identifying Musca domestica class targets.*/
                    }
                    ref part ImageProcessor {
                        doc /* Preprocessing: deinterlace, stabilize, threshold.*/
                    }
                    ref satisfy requirement s_vision_DetectFly ::> Requirements::Functional::R_DetectFly;
                    ref satisfy requirement s_vision_TrackFly ::> Requirements::Functional::R_TrackFly;
                    ref satisfy requirement s_vision_MultiTarget ::> Requirements::Functional::R_MultiTarget;
                    ref satisfy requirement s_vision_ResponseTime ::> Requirements::Performance::R_ResponseTime;
                    ref satisfy requirement s_vision_Darkness ::> Requirements::CornerCases::R_Darkness;
                }
                ref part LaserTargetingSystem {
                    doc /* Laser, gimbal, range-finder; eye-safe default.*/
                    ref part Laser {
                        doc /* Low-power continuous-wave laser, <=1 W.*/
                        attribute outputPower_W {
                            doc /* Laser output power (max 1.0 W in Class 1M default).*/
                        }
                        attribute wavelength_nm {
                            doc /* Laser wavelength in nm.*/
                        }
                    }
                    ref part Gimbal {
                        doc /* 2-axis gimbal for laser pointing.*/
                        attribute maxSlewRate_deg_s {
                            doc /* Maximum gimbal slew rate.*/
                        }
                    }
                    ref part LaserRangeFinder {
                        doc /* Time-of-flight rangefinder for target distance.*/
                    }
                    ref satisfy requirement s_laser_EliminateFly ::> Requirements::Functional::R_EliminateFly;
                    ref satisfy requirement s_laser_LaserRange ::> Requirements::Performance::R_LaserRange;
                    ref satisfy requirement s_laser_EyeSafe ::> Requirements::Safety::R_EyeSafeLaser;
                    ref satisfy requirement s_laser_DisarmOnLand ::> Requirements::Safety::R_DisarmOnLand;
                    ref satisfy requirement s_laser_SurfaceReflect ::> Requirements::CornerCases::R_SurfaceReflect;
                }
                ref part SafetySystem {
                    doc /* Human/obstacle detection, geofence, emergency stop.*/
                    ref part HumanDetector {
                        doc /* Separate ML model flagging human silhouettes.*/
                    }
                    ref part ObstacleDetector {
                        doc /* Ultrasonic + optical-flow obstacle sensing.*/
                    }
                    ref part EmergencyStopReceiver {
                        doc /* Dedicated 433 MHz receiver for operator kill switch.*/
                    }
                    ref part GeofenceModule {
                        doc /* Compares current position to configured boundary.*/
                    }
                    ref satisfy requirement s_safety_KillSwitch ::> Requirements::Functional::R_KillSwitch;
                    ref satisfy requirement s_safety_HumanDetection ::> Requirements::Safety::R_HumanDetection;
                    ref satisfy requirement s_safety_ObstacleDistance ::> Requirements::Safety::R_ObstacleDistance;
                    ref satisfy requirement s_safety_Geofence ::> Requirements::Safety::R_Geofence;
                    ref satisfy requirement s_safety_WindLimit ::> Requirements::Safety::R_WindLimit;
                    ref satisfy requirement s_safety_SwarmConflict ::> Requirements::CornerCases::R_SwarmConflict;
                    ref satisfy requirement s_safety_UnresponsiveOp ::> Requirements::CornerCases::R_UnresponsiveOp;
                }
                ref part CommunicationSystem {
                    doc /* Radio link to operator; GPS receiver.*/
                    ref part RadioModule {
                        doc /* 2.4 GHz bi-directional telemetry + command.*/
                    }
                    ref part GPSReceiver {
                        doc /* GNSS receiver — disabled indoors.*/
                    }
                    ref satisfy requirement s_comms_ConfigArea ::> Requirements::Functional::R_ConfigArea;
                    ref satisfy requirement s_comms_GPSLoss ::> Requirements::CornerCases::R_GPSLoss;
                }
                ref part ChassisFrame {
                    doc /* Structural frame, mounting points, landing gear.*/
                }
                view DroneView : DS_Views::SymbolicViews::iv;
            }
        }
        package Behavior {
            doc /* States + Actions (logical design).*/
            package States {
                doc /* Lifecycle + mission states.*/
                state S_Off {
                    doc /* Power off, idle on charger.*/
                }
                state S_BootingUp {
                    doc /* Firmware POST, sensor init.*/
                }
                state S_Idle {
                    doc /* Ready on pad awaiting command.*/
                }
                state S_Patrolling {
                    doc /* Flying patrol waypoints, scanning.*/
                }
                state S_Detecting {
                    doc /* Fly candidate identified; verifying.*/
                }
                state S_Tracking {
                    doc /* Target locked; maintaining aim.*/
                }
                state S_Engaging {
                    doc /* Laser firing — bounded duration.*/
                }
                state S_ReturningToBase {
                    doc /* Navigating back to charger.*/
                }
                state S_Charging {
                    doc /* Docked and charging.*/
                }
                state S_EmergencyAbort {
                    doc /* Kill-switch received; descending.*/
                }
                state S_SafeDescent {
                    doc /* Propeller fault; controlled fall.*/
                }
                state S_Error {
                    doc /* Unrecoverable fault; operator attention required.*/
                }
            }
            package Actions {
                doc /* Mission actions performed by subsystems.*/
                action A_BootUp;
                action A_ScanArea {
                    doc /* Fly the patrol pattern while the camera scans for flies.*/
                }
                action A_DetectFly {
                    doc /* Run the fly-detector algorithm on each camera frame.*/
                }
                action A_TrackFly {
                    doc /* Keep the fly centered in frame; update gimbal.*/
                }
                action A_CheckHumanSafety {
                    doc /* Run human-detector; if positive, inhibit laser.*/
                }
                action A_CheckRange {
                    doc /* Query the range-finder; proceed only within 0.5–5 m.*/
                }
                action A_AimLaser;
                action A_FireLaser {
                    doc /* Emit for bounded duration; cease on loss of lock.*/
                }
                action A_CheckBattery;
                action A_AvoidObstacle;
                action A_ReturnHome;
                action A_LandOnCharger;
                action A_Recharge;
                action A_EmergencyShutdown;
                action A_SafeDescent;
            }
        }
    }
}
