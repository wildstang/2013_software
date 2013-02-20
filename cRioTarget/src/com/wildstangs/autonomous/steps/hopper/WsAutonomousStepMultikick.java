/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.steps.hopper;

import com.wildstangs.autonomous.steps.WsAutonomousSerialStepGroup;

/**
 *
 * @author coder65535
 */
public class WsAutonomousStepMultikick extends WsAutonomousSerialStepGroup {

    private int count;

    public WsAutonomousStepMultikick(int count) {
        super(count);
        this.count = count;
        defineSteps();
    }

    public void defineSteps() {
        for (int i = 0; i < count; i++) {
            steps[i] = new WsAutonomousStepKick();
        }
    }

    public String toString() {
        return "Kick " + count + " frisbees";
    }
}
