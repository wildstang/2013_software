/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.programs;

import com.wildstangs.autonomous.WsAutonomousProgram;
import com.wildstangs.autonomous.steps.drivebase.WsAutonomousStepEnableDriveHeadingPid;
import com.wildstangs.autonomous.steps.drivebase.WsAutonomousStepSetDriveHeadingPidSetpoint;
import com.wildstangs.autonomous.steps.drivebase.WsAutonomousStepWaitForDriveHeadingPid;

/**
 *
 * @author Nathan Walters
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
public class WsAutonomousProgramDriveHeading extends WsAutonomousProgram {

    public WsAutonomousProgramDriveHeading() {
        super(3);
    }

    public void defineSteps() {
        programSteps[0] = new WsAutonomousStepEnableDriveHeadingPid();
        programSteps[1] = new WsAutonomousStepSetDriveHeadingPidSetpoint(30.0);
        programSteps[2] = new WsAutonomousStepWaitForDriveHeadingPid();
    }

    public String toString() {
        return "Test by driving to a specified heading using PID control";
    }
}
