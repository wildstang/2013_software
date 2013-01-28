/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.steps;

import com.wildstangs.autonomous.IStepContainer;
import com.wildstangs.autonomous.WsAutonomousStep;
/**
 *
 * @author coder65535
 */
public abstract class WsAutonomousStepGroup extends WsAutonomousStep implements IStepContainer
{
    protected final WsAutonomousStep[] steps;
    protected int currentStep, errorCount;
    protected boolean finishedStep, lastStepError;
    public WsAutonomousStepGroup(int stepCount)
    {
        steps = new WsAutonomousStep[stepCount];
        finishedStep = false;
        currentStep = 0;
        errorCount = 0;
    }
    protected abstract void defineSteps(WsAutonomousStep[] steps);
    public void initialize()
    {
        defineSteps(steps);
        steps[0].initialize();
    }
    public void update()
    {
        if (finished)
        {
            return;
        }
        if (finishedStep)
        {
            finishedStep = false;
            currentStep++;
            if (currentStep >= steps.length)
            {
                finished = true;
                cleanup();
                return;
            }
            else
            {
                steps[currentStep].initialize();
            }
        }
        WsAutonomousStep step = steps[currentStep]; //Prevent errors caused by mistyping.
        step.update();
        if (step.isFinished())
        {
            finishedStep = true;
            if (!step.isPassed())
            {
                lastStepError = true;
                failedStep(step);
            }
            else
            {
                lastStepError = false;
            }
        }
    }
    
    public WsAutonomousStep getCurrentStep()
    {
        return steps[currentStep];
    }

    public WsAutonomousStep getNextStep()
    {
        if (currentStep + 1 < steps.length)
        {
            return steps[currentStep+1];
        }
        else
        {
            return null;
        }
    }
    
    public void setNextStep(WsAutonomousStep newStep)
    {
        if (steps[currentStep] instanceof WsAutonomousStepGroup)
        {
            if (((WsAutonomousStepGroup)steps[currentStep]).getNextStep() != null)
            {
                ((WsAutonomousStepGroup)steps[currentStep]).setNextStep(newStep);
            }
            else
            {
                steps[currentStep+1] = newStep;
            }
        }
        else if (currentStep + 1 < steps.length)
        {
            steps[currentStep+1] = newStep;
        }
    }
    
    public void setStep(WsAutonomousStep newStep, int stepNumber)
    {
        if (currentStep != stepNumber && stepNumber>=0 && stepNumber <steps.length)
        {
            steps[stepNumber] = newStep;
        }
    }
    protected void cleanup()
    {
        for (int i = 0; i < steps.length; i++)
        {
            steps[i] = null;
        }
    }
    public void finishGroup()
    {
        finished = true;
    }
    protected final void failedStep(WsAutonomousStep step)
    {
        if (step.isFatal())
        {
            finished = true;
            fatal = true;
            fatalError(step);
        }
        else
        {
            handleError(step);
        }
    }
    protected void fatalError(WsAutonomousStep step) //Separate method for easy overrides.
    {
        handleError(step);
    }
    protected void handleError(WsAutonomousStep step) //Separate method for easy overrides.
    {
        pass = false;
        if (errorInfo.equals("Passed"))
        {
            errorInfo = "";
        }
        errorInfo += ++errorCount + ": Error in substep "+ currentStep +"(" + step.toString() + "): "/* + "\n"*/ + step.errorInfo + ", "/* + "\n"*/;
        // I am unsure whether this looks better as one line or as multiple.
    }
    
    public boolean lastStepHadError()
    {
        if (steps[currentStep] instanceof WsAutonomousStepGroup)
        {
            return ((WsAutonomousStepGroup)steps[currentStep]).lastStepHadError();
        }
        return lastStepError;
    }

//    public int hashCode()
//    {
//        int hash = 7;
//        hash = 59 * hash + Arrays.deepHashCode(this.steps);
//        return hash;
//    }
//    public boolean equals(Object o)
//    {
//        if (o instanceof WsAutonomousStepGroup)
//        {
//            WsAutonomousStepGroup obj = (WsAutonomousStepGroup)o;
//            return Arrays.deepEquals(obj.steps, steps);
//        }
//        return false;
//    }
    
    
    
}
