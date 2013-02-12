/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wildstangs.autonomous.steps.shootseven;

import com.wildstangs.autonomous.steps.WsAutonomousSerialStepGroup;
import com.wildstangs.autonomous.steps.drivebase.WsAutonomousStepDisableDriveDistancePid;
import com.wildstangs.autonomous.steps.drivebase.WsAutonomousStepDisableDriveHeadingPid;
import com.wildstangs.autonomous.steps.drivebase.WsAutonomousStepEnableDriveDistancePid;
import com.wildstangs.autonomous.steps.drivebase.WsAutonomousStepEnableDriveHeadingPid;
import com.wildstangs.autonomous.steps.drivebase.WsAutonomousStepSetDriveHeadingPidSetpoint;
import com.wildstangs.autonomous.steps.drivebase.WsAutonomousStepWaitForDriveHeadingPid;
import com.wildstangs.subsystems.WsShooter;
/**
 *
 * @author Joey
 */
public class WsAutonomousStepGroupDriveToPyramidFrisbees extends WsAutonomousSerialStepGroup
{
    double startDrive, angle, secondDrive;
    WsShooter.Preset shooterPreset;
    
    public WsAutonomousStepGroupDriveToPyramidFrisbees(double startDrive, double angle, double secondDrive, WsShooter.Preset shooterPreset)
    {
        super(7);
        this.startDrive = startDrive;
        this.angle = angle;
        this.secondDrive = secondDrive;
        this.shooterPreset = shooterPreset;
    }
    public void defineSteps()
    {
        steps[0] = new WsAutonomousStepEnableDriveDistancePid();
        steps[1] = new WsAutonomousParallelStepGroupSetShooterAndDrive(startDrive, shooterPreset);
        steps[2] = new WsAutonomousStepDisableDriveDistancePid();
        steps[3] = new WsAutonomousStepEnableDriveHeadingPid();
        steps[4] = new WsAutonomousStepSetDriveHeadingPidSetpoint(angle);
        steps[5] = new WsAutonomousStepWaitForDriveHeadingPid();
        steps[6] = new WsAutonomousStepDisableDriveHeadingPid();
    }
    public String toString()
    {
        return "Driving to Frisbees under the pyramid";
    }
}
