/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wildstangs.autonomous.steps.control;

import com.wildstangs.autonomous.*;
import com.wildstangs.autonomous.steps.WsAutonomousStepGroup;

/**
 *
 * @author coder65535
 */
public class WsAutonomousStepFinishGroupIfError extends WsAutonomousStep 
{
    
    public WsAutonomousStepFinishGroupIfError()
    {
        //Nothing to set.
    }

    public void initialize()
    {
        //All the work is done in update().
    }

    public void update()
    {
        finished = true;
        boolean bottomOfTree = false;
        IStepContainer container = WsAutonomousManager.getInstance().getRunningProgram();
        while (!bottomOfTree)
        {
            WsAutonomousStep currStep = container.getCurrentStep();
            if (currStep instanceof WsAutonomousStepGroup)
            {
                container = (WsAutonomousStepGroup)currStep;
            }
            else
            {
                bottomOfTree = true;
            }
        }
        if (container.lastStepHadError())
        {
            errorInfo = "";
            pass = false;
            container.setNextStep(new WsAutonomousStepFinishGroup());
        }
    }

    public boolean equals(Object o)
    {
        if (o instanceof WsAutonomousStepFinishGroupIfError)
        {
            return true;
        }
        return false;
    }

    public String toString()
    {
        return "Skips next step if last step had error";
    }

    public int hashCode()
    {
        int hash = 3;
        return hash;
    }
}
