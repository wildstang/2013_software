/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.programs;

import com.wildstangs.autonomous.WsAutonomousProgram;
import com.wildstangs.autonomous.steps.control.WsAutonomousStepDelay;
import com.wildstangs.autonomous.steps.input.WsAutonomousStepEnableDriveDistancePid;
import com.wildstangs.autonomous.steps.input.WsAutonomousStepSetDriveDistancePidSetpoint;

/**
 *
 * @author Nathan Walters
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
public class WsAutonomousProgramDriveDistance extends WsAutonomousProgram {

    public WsAutonomousProgramDriveDistance() {
        super(2);
    }

    public void defineSteps() {
        programSteps[0] = new WsAutonomousStepEnableDriveDistancePid(0.0);
        programSteps[1] = new WsAutonomousStepSetDriveDistancePidSetpoint(60.0);
    }

    public String toString() {
        return "Test by driving forward for a specified distance using PID control";
    }
}
