/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.steps.input;

import com.wildstangs.autonomous.WsAutonomousStep;
import com.wildstangs.subsystems.WsDriveBase;
import com.wildstangs.subsystems.base.WsSubsystemContainer;

/**
 *
 * @author Nathan Walters
 */
public class WsAutonomousStepEnableDriveDistancePid extends WsAutonomousStep {

    public WsAutonomousStepEnableDriveDistancePid() {
    }

    public void initialize() {
        finished = true;
        ((WsDriveBase) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_DRIVE_BASE)).enableDistancePidControl();
        System.out.println("Drive Distance pid is enabled");
    }

    public void update() {
    }

    public String toString() {
        return "Enable the drive distance PID";
    }
}
