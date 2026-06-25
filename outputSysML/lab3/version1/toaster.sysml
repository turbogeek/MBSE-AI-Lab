package ToasterModel_v2 {
    package Stakeholders {
        part def Stakeholder;
        
        part consumer : Stakeholder {
            concern safety_concern;
            concern performance_concern;
        }
        
        part manufacturer : Stakeholder {
            concern cost_concern;
            concern compliance_concern;
        }
        
        part regulator : Stakeholder {
            concern safety_standards_concern;
        }
        
        part artisanBakery : Stakeholder {
            concern brand_representation_concern;
        }
        
        part repairabilityPartner : Stakeholder {
            concern modular_design_concern;
        }

        part governmentRegulator : Stakeholder {
            concern safety_prevention_concern;
        }

        part governmentEnvironmentalist : Stakeholder {
            concern energy_efficiency_concern;
            concern recyclability_concern;
        }
    }
    
    package Requirements {
        requirement def <REQ1> 'Heating Requirement' {
            doc /* The toaster shall provide heating elements capable of reaching toasting temperatures. */
            frame Stakeholders::consumer.performance_concern;
        }
        
        requirement def <REQ2> 'Power Consumption Requirement' {
            doc /* The toaster shall operate on standard 120V AC and consume no more than 1500W. */
            frame Stakeholders::manufacturer.compliance_concern;
        }
        
        requirement def <REQ3> 'Timer Requirement' {
            doc /* The toaster shall include an adjustable timer to control the duration of the toasting cycle. */
            frame Stakeholders::consumer.performance_concern;
        }
        
        requirement def <REQ4> 'User Safety Requirement' {
            doc /* The toaster shall maintain its exterior surface temperature below a safe threshold to prevent burns. */
            frame Stakeholders::consumer.safety_concern;
            frame Stakeholders::regulator.safety_standards_concern;
        }
        
        requirement def <REQ5> 'Pop-up Mechanism Requirement' {
            doc /* The toaster shall automatically eject the bread and terminate heating when the timer expires. */
            frame Stakeholders::consumer.performance_concern;
            frame Stakeholders::consumer.safety_concern;
        }

        requirement def <REQ6> 'Optical Sensing Requirement' {
            doc /* The toaster shall include an optical sensor to monitor the browning level of the bread and automatically stop heating when the target shade is reached. */
            frame Stakeholders::consumer.performance_concern;
        }
        
        requirement def <REQ7> 'Connectivity Requirement' {
            doc /* The toaster shall include Wi-Fi connectivity to download specialized toasting profiles from partner bakeries. */
            frame Stakeholders::artisanBakery.brand_representation_concern;
        }
        
        requirement def <REQ8> 'Touchscreen UI Requirement' {
            doc /* The toaster shall feature a touchscreen interface for users to select bread types, browning levels, and custom profiles. */
            frame Stakeholders::consumer.performance_concern;
        }
        
        requirement def <REQ9> 'Repairability Requirement' {
            doc /* The toaster shall be designed with modular, easily replaceable heating elements and control boards to facilitate end-user repair. */
            frame Stakeholders::repairabilityPartner.modular_design_concern;
        }

        requirement def <REQ10> 'Energy Efficiency Requirement' {
            doc /* The toaster shall be energy efficient and consume no more than 1500W. */
            frame Stakeholders::governmentEnvironmentalist.energy_efficiency_concern;
        }

        requirement def <REQ11> 'Fire Safety Requirement' {
            doc /* The toaster shall be safe to use and not pose a fire hazard. */
            frame Stakeholders::governmentRegulator.safety_prevention_concern;
        }

        requirement def <REQ12> 'Durability Requirement' {
            doc /* The toaster shall be durable and long-lasting. */
            frame Stakeholders::consumer.performance_concern;
        }

        requirement def <REQ13> 'Ease of Use Requirement' {
            doc /* The toaster shall be easy to use and understand. */
            frame Stakeholders::consumer.performance_concern;
        }

        requirement def <REQ14> 'Aesthetics Requirement' {
            doc /* The toaster shall be aesthetically pleasing and fit in well with other kitchen appliances. */
            frame Stakeholders::consumer.performance_concern;
        }

        requirement <REQ1> heatingReq : 'Heating Requirement';
        requirement <REQ2> powerReq : 'Power Consumption Requirement';
        requirement <REQ3> timerReq : 'Timer Requirement';
        requirement <REQ4> safetyReq : 'User Safety Requirement';
        requirement <REQ5> popupReq : 'Pop-up Mechanism Requirement';
        requirement <REQ6> opticalReq : 'Optical Sensing Requirement';
        requirement <REQ7> connectivityReq : 'Connectivity Requirement';
        requirement <REQ8> touchscreenReq : 'Touchscreen UI Requirement';
        requirement <REQ9> repairabilityReq : 'Repairability Requirement';
        requirement <REQ10> energyEfficiencyReq : 'Energy Efficiency Requirement';
        requirement <REQ11> fireSafetyReq : 'Fire Safety Requirement';
        requirement <REQ12> durabilityReq : 'Durability Requirement';
        requirement <REQ13> easeOfUseReq : 'Ease of Use Requirement';
        requirement <REQ14> aestheticsReq : 'Aesthetics Requirement';
    }
    
    package LogicalArchitecture {
        part def PowerSubsystem {
            satisfy Requirements::powerReq;
            satisfy Requirements::energyEfficiencyReq;
        }
        
        part def ControlSubsystem {
            satisfy Requirements::timerReq;
            satisfy Requirements::touchscreenReq;
            satisfy Requirements::easeOfUseReq;
        }
        
        part def HeatingSubsystem {
            satisfy Requirements::heatingReq;
        }
        
        part def MechanicalSubsystem {
            satisfy Requirements::popupReq;
            satisfy Requirements::repairabilityReq;
            satisfy Requirements::durabilityReq;
        }

        part def SafetySubsystem {
            satisfy Requirements::safetyReq;
            satisfy Requirements::fireSafetyReq;
        }

        part def OpticalSensingSubsystem {
            satisfy Requirements::opticalReq;
        }

        part def ConnectivitySubsystem {
            satisfy Requirements::connectivityReq;
        }

        part def Toaster {
            satisfy Requirements::aestheticsReq;
            
            part powerSubsystem : PowerSubsystem;
            part controlSubsystem : ControlSubsystem;
            part heatingSubsystem : HeatingSubsystem;
            part mechanicalSubsystem : MechanicalSubsystem;
            part safetySubsystem : SafetySubsystem;
            part opticalSensingSubsystem : OpticalSensingSubsystem;
            part connectivitySubsystem : ConnectivitySubsystem;
        }
    }

    package SystemContext {
        part def ToasterContext;
        part def User;
        part def PowerSource;
        part def Bread;
        
        part toasterContext : ToasterContext {
            part user : User;
            part powerSource : PowerSource;
            part bread : Bread;
            part toaster : LogicalArchitecture::Toaster;
        }
    }
    
    package PhysicalArchitecture {
        part def ToasterProduct {
            part casing;
            part heatingElements;
            part circuitBoard;
            part carriageMechanism;
        }
    }
}
