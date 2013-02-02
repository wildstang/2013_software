/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.programs;

import com.wildstangs.autonomous.WsAutonomousProgram;
import com.wildstangs.autonomous.steps.input.WsAutonomousStepEnableDriveDistancePid;
import com.wildstangs.autonomous.steps.input.WsAutonomousStepSetDriveDistancePidSetpoint;
import com.wildstangs.autonomous.steps.input.WsAutonomousStepWaitForDriveDistancePid;

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
        super(3);
    }

    public void defineSteps() {
        programSteps[0] = new WsAutonomousStepEnableDriveDistancePid();
        programSteps[1] = new WsAutonomousStepSetDriveDistancePidSetpoint(60.0);
        programSteps[2] = new WsAutonomousStepWaitForDriveDistancePid();
    }

    public String toString() {
        return "Test by driving forward for a specified distance using PID control";
    }
}
