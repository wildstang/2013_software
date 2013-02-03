/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.programs;

import com.wildstangs.autonomous.WsAutonomousProgram;
import com.wildstangs.autonomous.steps.input.WsAutonomousStepKick;
import com.wildstangs.autonomous.steps.input.WsAutonomousStepLowerHopper;
import com.wildstangs.autonomous.steps.input.WsAutonomousStepRaiseHopper;

/**
 *
 * @author Batman
 */
public class WsAutonomousProgramHopperTest extends WsAutonomousProgram
{
    public WsAutonomousProgramHopperTest()
    {
        super(3);
    }
    protected void defineSteps() 
    {
        programSteps[0] = new WsAutonomousStepRaiseHopper();
        programSteps[1] = new WsAutonomousStepKick();
        programSteps[2] = new WsAutonomousStepLowerHopper();
    }

    public String toString() 
    {
        return "Raise the lift, kick the kicker, and lower the lift.";
    }
    
}
