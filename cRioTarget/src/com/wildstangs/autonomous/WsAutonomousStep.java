/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous;

/**
 *
 * @author coder65535
 */
public abstract class WsAutonomousStep
{

    protected boolean finished, pass, fatal;
    public String errorInfo;

    public WsAutonomousStep()
    {
        //initialize variables
        finished = false; //A step can't finish before it starts.
        pass = true; //Most steps pass automatically, as they issue a command or check a value.
        fatal = false; //This is only used to "fail safe". Most steps don't need to interrupt the sub-program.
        errorInfo = "Passed";
    }

    public abstract void initialize();

    public abstract void update();

    public boolean isFinished()
    {
        return finished;//The abort flag means that the robot must halt immediately, thus the step is always finished when the abort flag is triggered.
    }

    public boolean isPassed()
    {
        return pass;//The abort flag overrides the pass flag. The abort flag always means a fatal error occured.
    }

    public boolean isFatal()
    {
        if (isPassed()) //No test can both return a fatal error and pass, this is a catch to prevent stupid errors.
        {
            return false;
        }
        else
        {
            return fatal;//The abort flag overrides the pass flag. The abort flag always means a fatal error occured.
        }
    }

    public abstract String toString();

    public abstract int hashCode();

    public abstract boolean equals(Object o);
}
