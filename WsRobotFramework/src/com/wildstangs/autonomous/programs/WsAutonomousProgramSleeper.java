/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wildstangs.autonomous.programs;

import com.wildstangs.autonomous.WsAutonomousProgram;
import com.wildstangs.autonomous.steps.control.WsAutonomousStepStop;

/**
 *
 * @author coder65535
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
public class WsAutonomousProgramSleeper extends WsAutonomousProgram
{
    public WsAutonomousProgramSleeper()
    {
        super(1);
    }
    public void defineSteps()
    {
        programSteps[0] = new WsAutonomousStepStop();
    }
    public String toString()
    {
        return "Sleeper";
    }
}
