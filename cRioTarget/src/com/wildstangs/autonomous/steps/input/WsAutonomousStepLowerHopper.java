/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.steps.input;

import com.wildstangs.autonomous.WsAutonomousStep;
import com.wildstangs.subsystems.WsHopper;
import com.wildstangs.subsystems.base.WsSubsystemContainer;
import edu.wpi.first.wpilibj.DoubleSolenoid;

/**
 *
 * @author Batman
 */
public class WsAutonomousStepLowerHopper extends WsAutonomousStep
{

    public void initialize() 
    {
        WsHopper subsystem = (WsHopper)(WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_HOPPER));
        subsystem.setLiftValue(DoubleSolenoid.Value.kReverse);
        finished = true;
    }

    public void update() 
    {
    }

    public String toString() 
    {
        return "Lowering the hopper.";
    }
    
}
