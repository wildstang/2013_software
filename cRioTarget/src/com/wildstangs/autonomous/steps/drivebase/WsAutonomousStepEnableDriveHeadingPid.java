/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.steps.drivebase;

import com.wildstangs.autonomous.WsAutonomousStep;
import com.wildstangs.subsystems.WsDriveBase;
import com.wildstangs.subsystems.base.WsSubsystemContainer;

/**
 *
 * @author Nathan Walters
 */
public class WsAutonomousStepEnableDriveHeadingPid extends WsAutonomousStep {

    public WsAutonomousStepEnableDriveHeadingPid() {
    }

    public void initialize() {
        ((WsDriveBase) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_DRIVE_BASE)).enableHeadingPidControl();
        System.out.println("Drive Heading pid is enabled");
        finished = true;
    }

    public void update() {
    }

    public String toString() {
        return "Enable the drive heading PID";
    }
}