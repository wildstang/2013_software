/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.programs;

import com.wildstangs.autonomous.WsAutonomousProgram;
import com.wildstangs.autonomous.steps.drivebase.WsAutonomousStepEnableDriveDistancePid;
import com.wildstangs.autonomous.steps.drivebase.WsAutonomousStepSetDriveDistancePidSetpoint;
import com.wildstangs.autonomous.steps.drivebase.WsAutonomousStepStartDriveUsingMotionProfile;
import com.wildstangs.autonomous.steps.drivebase.WsAutonomousStepStopDriveUsingMotionProfile;
import com.wildstangs.autonomous.steps.drivebase.WsAutonomousStepWaitForDriveDistancePid;
import com.wildstangs.autonomous.steps.drivebase.WsAutonomousStepWaitForDriveMotionProfile;
import com.wildstangs.config.DoubleConfigFileParameter;

/**
 *
 * @author Nathan Walters
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
public class WsAutonomousProgramDriveDistanceMotionProfile extends WsAutonomousProgram {

    private DoubleConfigFileParameter distance;

    public WsAutonomousProgramDriveDistanceMotionProfile() {
        super(3);
        distance = new DoubleConfigFileParameter(this.getClass().getName(), "distance", 10.0);
    }

    public void defineSteps() {
        programSteps[0] = new WsAutonomousStepStartDriveUsingMotionProfile(distance.getValue(), 0.0);
        programSteps[1] = new WsAutonomousStepWaitForDriveMotionProfile(); 
        programSteps[2] = new WsAutonomousStepStopDriveUsingMotionProfile();
//        programSteps[3] = new WsAutonomousStepEnableDriveDistancePid();
//        programSteps[4] = new WsAutonomousStepSetDriveDistancePidSetpoint(distance.getValue());
//        programSteps[5] = new WsAutonomousStepWaitForDriveDistancePid();
//        programSteps[6] = new WsAutonomousStepStartDriveUsingMotionProfile(distance.getValue(), 0.0);
//        programSteps[7] = new WsAutonomousStepWaitForDriveMotionProfile(); 
//        programSteps[8] = new WsAutonomousStepStopDriveUsingMotionProfile();
    }

    public String toString() {
        return "Test by driving forward for a specified distance using motion profile";
    }
}
