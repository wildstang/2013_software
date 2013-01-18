/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.steps;

import java.util.Arrays;
import com.wildstangs.autonomous.WsAutonomousStep;
/**
 *
 * @author coder65535
 */
public abstract class WsAutonomousStepGroup extends WsAutonomousStep
{
    protected final WsAutonomousStep[] steps;
    protected int currentStep, errorCount;
    protected boolean finishedStep;
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
                failedStep(step);
            }
        }
    }
    protected void cleanup()
    {
        for (int i = 0; i < steps.length; i++)
        {
            steps[i] = null;
        }
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

    public int hashCode()
    {
        int hash = 7;
        hash = 59 * hash + Arrays.deepHashCode(this.steps);
        return hash;
    }
    public boolean equals(Object o)
    {
        if (o instanceof WsAutonomousStepGroup)
        {
            WsAutonomousStepGroup obj = (WsAutonomousStepGroup)o;
            return Arrays.deepEquals(obj.steps, steps);
        }
        return false;
    }
    
    
    
}
