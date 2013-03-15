/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.programs.test;

import com.wildstangs.autonomous.WsAutonomousProgram;
import com.wildstangs.autonomous.steps.control.WsAutonomousStepDelay;
import com.wildstangs.autonomous.steps.intake.WsAutonomousStepStartIntake;
import com.wildstangs.autonomous.steps.intake.WsAutonomousStepStopIntake;

//@author Joshua Gustafson
public class WsAutonomousProgramTestIntake extends WsAutonomousProgram {

    public WsAutonomousProgramTestIntake() {
        super(3);
    }

    public void defineSteps() {
        programSteps[0] = new WsAutonomousStepStartIntake();
        programSteps[1] = new WsAutonomousStepDelay();
        programSteps[2] = new WsAutonomousStepStopIntake();
    }

    public String toString() {
        return "Start Funnalator motor for a second, then stop";
    }
}
