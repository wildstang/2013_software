/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.steps.control;

import com.wildstangs.autonomous.WsAutonomousStep;
import com.wildstangs.logger.Logger;

/**
 *
 * @author coder65535
 */
public class WsAutonomousStepDelay extends WsAutonomousStep
/* This step delays testing for the specified number of cycles.
 * Note: If used in a parallel step group, it insures that the group waits for at least the specified number of cycles, instead.
 */
{

    private int count;
    protected final int originalCount;

    public WsAutonomousStepDelay(int delay)
    {
        count = delay - 1;
        originalCount = delay;
        if (delay <= 0)
        {
            pass = false;
            errorInfo = "Negative delay";
        }
    }
    
    public WsAutonomousStepDelay()
    {
        this(50);
    }

    public void initialize() // Do nothing, as the variables have been initialised in the constructor.
    {
    }

    public void update()
    {
        if (count-- <= 0) //Preventing stupid errors that could occur by passing a negative value into the constructor.
        {
            finished = true;
        }
    }

    public String toString()
    {
        return "Delay for " + originalCount + " frames";
    }

//    public int hashCode()
//    {
//        int hash = 5;
//        hash = 53 * hash + this.originalCount;
//        return hash;
//    }
//
//    public boolean equals(Object o)
//    {
//        if (o instanceof WsAutonomousStepDelay)
//        {
//            WsAutonomousStepDelay step = (WsAutonomousStepDelay) o;
//            if (step.originalCount == this.originalCount)
//            {
//                return true;
//            }
//        }
//        return false;
//    }

}
