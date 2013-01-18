/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wildstangs.autonomous.steps.control;

import com.wildstangs.autonomous.WsAutonomousStep;

/**
 *
 * @author coder65535
 */
public class WsAutonomousStepNoOp extends WsAutonomousStep 
{
    
    public WsAutonomousStepNoOp()
    {
        
    }

    public void initialize()
    {
        finished = true; //This step does nothing, and finishes immediately.
    }

    public void update()
    {
        
    }

    public boolean equals(Object o)
    {
        if (o instanceof WsAutonomousStepNoOp)
        {
            WsAutonomousStepNoOp obj = (WsAutonomousStepNoOp)o;
            return true;
        }
        return false;
    }

    public String toString()
    {
        return "No-Op";
    }

    public int hashCode()
    {
        int hash = 7;
        return hash;
    }
}
