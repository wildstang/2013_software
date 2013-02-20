/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.steps.floorpickup;

import com.wildstangs.autonomous.WsAutonomousStep;
import com.wildstangs.inputfacade.base.WsInputFacade;
import com.wildstangs.subjects.base.BooleanSubject;
import com.wildstangs.subjects.base.ISubjectEnum;

/**
 *
 * @author Joshua Gustafson
 */
public class WsAutonomousStepWaitForAccumulatorUp extends WsAutonomousStep {

    public WsAutonomousStepWaitForAccumulatorUp() {
    }

    public void initialize() {
    }

    public void update() {
        boolean upSwitchState = ((BooleanSubject) WsInputFacade.getInstance().getSensorInput(WsInputFacade.ACCUMULATOR_UP_LIMIT_SWITCH).getSubject((ISubjectEnum) null)).getValue();
        if (true == upSwitchState) {
            finished = true;
        }
    }

    public String toString() {
        return "Wait for the accumulator to raise.";
    }
}
