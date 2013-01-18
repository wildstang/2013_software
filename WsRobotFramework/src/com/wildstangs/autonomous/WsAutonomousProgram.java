/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous;

import java.util.Arrays;

/**
 *
 * @author coder65535
 */
public abstract class WsAutonomousProgram
{

    protected final WsAutonomousStep[] programSteps;
    protected int currentStep;
    protected boolean finishedStep, finished;

    public WsAutonomousProgram(int size)
    {
        programSteps = new WsAutonomousStep[size];
        currentStep = -1;
        finishedStep = true; //This is set to true to make the update code start the first task
        finished = false;
    }

    protected abstract void defineSteps();
    
    public void initialize()
    {
        defineSteps();
    }
    
    public void cleanup()
    {
        for (int i = 0; i < programSteps.length; i++)
        {
            programSteps[i] = null;
        }
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
            if (currentStep >= programSteps.length)
            {
                finished = true;
                return;
            }
            else
            {
                programSteps[currentStep].initialize();
            }
        }
        WsAutonomousStep step = programSteps[currentStep]; //Prevent errors caused by mistyping.
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

    protected final void failedStep(WsAutonomousStep step)
    {
        if (step.isFatal())
        {
            finished = true;
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

    protected void handleError(WsAutonomousStep step)
    {
        // TODO: add error handling/logging
    }

    public void logResults()
    {
        // TODO: add result logging
    }

    public boolean isFinished()
    {
        return finished;
    }

    public int hashCode()
    {
        int hash = 7;
        hash = 97 * hash + Arrays.deepHashCode(this.programSteps);
        return hash;
    }

    public boolean equals(Object o)
    {
        if (o instanceof WsAutonomousProgram)
        {
            WsAutonomousProgram obj = (WsAutonomousProgram) o;
            return Arrays.equals(obj.programSteps, programSteps);
        }
        return false;
    }
}
