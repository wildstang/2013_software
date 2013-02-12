/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wildstangs.autonomous.steps.shootseven;

import com.wildstangs.autonomous.steps.WsAutonomousParallelStepGroup;
import com.wildstangs.autonomous.steps.drivebase.WsAutonomousStepSetDriveDistancePidSetpoint;
import com.wildstangs.autonomous.steps.shooter.WsAutonomousStepSetShooterPreset;
import com.wildstangs.autonomous.steps.shooter.WsAutonomousStepWaitForShooter;
import com.wildstangs.subsystems.WsShooter;
/**
 *
 * @author Joey
 */
public class WsAutonomousParallelStepGroupSetShooterAndDrive extends WsAutonomousParallelStepGroup
{
    double driveDistance;
    WsShooter.Preset preset;
    
    public WsAutonomousParallelStepGroupSetShooterAndDrive(double driveDist, WsShooter.Preset preset)
    {
        this.addStep(new WsAutonomousStepSetDriveDistancePidSetpoint(driveDist));
        this.addStep(new WsAutonomousStepSetShooterPreset(preset.ENTER_WHEEL_SET_POINT, preset.EXIT_WHEEL_SET_POINT, preset.ANGLE));
        this.addStep(new WsAutonomousStepWaitForShooter());
    }
    
    public String toString()
    {
        return "Setting to drive a distance and the preset of the shooter";
    }
}
