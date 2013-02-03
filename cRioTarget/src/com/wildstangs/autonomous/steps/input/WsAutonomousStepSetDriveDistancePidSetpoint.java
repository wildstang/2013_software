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
public class WsAutonomousStepSetDriveDistancePidSetpoint extends WsAutonomousStep {

    double value;

    public WsAutonomousStepSetDriveDistancePidSetpoint(double value) {
        this.value = value;
    }

    public void initialize() {
        ((WsDriveBase) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_DRIVE_BASE)).setDriveDistancePidSetpoint(value);
    }

    public void update() {
    }

    public String toString() {
        return "Enable the drive distance PID";
    }
}