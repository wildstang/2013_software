/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.steps.shooter;

import com.wildstangs.autonomous.WsAutonomousStep;
import com.wildstangs.subsystems.WsShooter;
import com.wildstangs.subsystems.base.WsSubsystemContainer;

/**
 *
 * @author Nathan
 */
public class WsAutonomousStepWaitForShooter extends WsAutonomousStep {

    public WsAutonomousStepWaitForShooter() {
    }

    public void initialize() {
        System.out.println("Wait initialized");
    }

    public void update() {
        finished = ((WsShooter) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_SHOOTER)).isAtSpeed();
        if(finished == true) {
            System.out.println("Wait finished!");
        }
    }

    public String toString() {
        return "Wait for Shooter flywheels to get up to speed";
    }
}
