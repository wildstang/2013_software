/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous;

import com.wildstangs.autonomous.steps.WsAutonomousStepGroup;
import com.wildstangs.logger.Logger;

/**
 *
 * @author coder65535
 */
public abstract class WsAutonomousProgram implements IStepContainer {

    protected final WsAutonomousStep[] programSteps;
    protected int currentStep;
    protected boolean finishedStep, finished, lastStepError;

    public WsAutonomousProgram(int size) {
        programSteps = new WsAutonomousStep[size];
    }

    protected abstract void defineSteps();

    public void initialize() {
        defineSteps();
        currentStep = 0;
        finishedStep = false;
        finished = false;
        lastStepError = false;
        programSteps[0].initialize();
    }

    public void cleanup() {
        for (int i = 0; i < programSteps.length; i++) {
            programSteps[i] = null;
        }
    }

    public void update() {
        if (finished) {
            return;
        }
        if (finishedStep) {
            finishedStep = false;
            currentStep++;
            if (currentStep >= programSteps.length) {
                finished = true;
                return;
            } else {
                programSteps[currentStep].initialize();
            }
        }
        WsAutonomousStep step = programSteps[currentStep]; //Prevent errors caused by mistyping.
        step.update();
        if (step.isFinished()) {
            finishedStep = true;
            if (!step.isPassed()) {
                lastStepError = true;
                failedStep(step);
            } else {
                lastStepError = false;
            }
        }
    }

    public WsAutonomousStep getCurrentStep() {
        return programSteps[currentStep];
    }

    public WsAutonomousStep getNextStep() {
        if (currentStep + 1 < programSteps.length) {
            return programSteps[currentStep + 1];
        } else {
            return null;
        }
    }

    public void setNextStep(WsAutonomousStep newStep) {
        if (programSteps[currentStep] instanceof WsAutonomousStepGroup) {
            if (((WsAutonomousStepGroup) programSteps[currentStep]).getNextStep() != null) {
                ((WsAutonomousStepGroup) programSteps[currentStep]).setNextStep(newStep);
            } else {
                programSteps[currentStep + 1] = newStep;
            }
        } else if (currentStep + 1 < programSteps.length) {
            programSteps[currentStep + 1] = newStep;
        }
    }

    public void setStep(WsAutonomousStep newStep, int stepNumber) {
        if (currentStep != stepNumber && stepNumber >= 0 && stepNumber < programSteps.length) {
            programSteps[stepNumber] = newStep;
        }
    }

    protected final void failedStep(WsAutonomousStep step) {
        if (step.isFatal()) {
            finished = true;
            fatalError(step);
        } else {
            handleError(step);
        }
    }

    protected void fatalError(WsAutonomousStep step) //Separate method for easy overrides.
    {
        handleError(step);
    }

    protected void handleError(WsAutonomousStep step) {
        if (step.errorInfo.trim().length() != 0) {
            return;
        }
        Logger.getLogger().error("Autonomous step " + currentStep + " (" + step.toString() + ")", "Auto Step", step.errorInfo);
    }

    public boolean isFinished() {
        return finished;
    }

    public boolean lastStepHadError() {
        if (programSteps[currentStep] instanceof WsAutonomousStepGroup) {
            return ((WsAutonomousStepGroup) programSteps[currentStep]).lastStepHadError();
        }
        return lastStepError;
    }

//    public int hashCode()
//    {
//        int hash = 7;
//        hash = 97 * hash + Arrays.deepHashCode(this.programSteps);
//        return hash;
//    }
//
//    public boolean equals(Object o)
//    {
//        if (o instanceof WsAutonomousProgram)
//        {
//            WsAutonomousProgram obj = (WsAutonomousProgram) o;
//            return Arrays.equals(obj.programSteps, programSteps);
//        }
//        return false;
//    }
    public abstract String toString();
}
