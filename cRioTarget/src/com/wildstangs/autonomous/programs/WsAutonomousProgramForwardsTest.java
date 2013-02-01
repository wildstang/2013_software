/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wildstangs.autonomous.programs;

import com.wildstangs.autonomous.WsAutonomousProgram;
import com.wildstangs.autonomous.steps.control.WsAutonomousStepDelay;
import com.wildstangs.autonomous.steps.input.WsAutonomousStepSetThrottle;

/**
 *
 * @author coder65535
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
public class WsAutonomousProgramForwardsTest extends WsAutonomousProgram
{
    public WsAutonomousProgramForwardsTest()
    {
        super(3);
    }
    
    public void defineSteps()
    {
        programSteps[0] = new WsAutonomousStepSetThrottle(1.0);
        programSteps[1] = new WsAutonomousStepDelay(500);
        programSteps[2] = new WsAutonomousStepSetThrottle(0.0);
    }
    
    public String toString()
    {
        return "Test by driving forwards for 1 second";
    }
}

