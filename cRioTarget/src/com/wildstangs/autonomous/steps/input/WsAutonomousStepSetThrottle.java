/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wildstangs.autonomous.steps.input;

import com.wildstangs.autonomous.WsAutonomousStep;
import com.wildstangs.inputfacade.base.WsInputFacade;
import com.wildstangs.inputfacade.inputs.joystick.driver.WsDriverJoystickEnum;
import com.wildstangs.logger.Logger;

/**
 *
 * @author coder65535
 */
public class WsAutonomousStepSetThrottle extends WsAutonomousStep 
{
    double value;
    
    public WsAutonomousStepSetThrottle(double value)
    {
        this.value = value;
    }

    public void initialize()
    {
        finished = true;
        Logger.getLogger().debug(toString(), "Checking if run", "It ran");
        WsInputFacade.getInstance().getOiInput(WsInputFacade.DRIVER_JOYSTICK).set(WsDriverJoystickEnum.THROTTLE, new Double(value));
    }

    public void update()
    {
        
    }

//    public int hashCode()
//    {
//        int hash = 5;
//        hash = 67 * hash + (int) (Double.doubleToLongBits(this.value) ^ (Double.doubleToLongBits(this.value) >>> 32));
//        return hash;
//    }
//
//    public boolean equals(Object o)
//    {
//        if (o instanceof WsAutonomousStepSetThrottle)
//        {
//            WsAutonomousStepSetThrottle obj = (WsAutonomousStepSetThrottle)o;
//            return obj.value == this.value;
//        }
//        return false;
//    }

    public String toString()
    {
        return "Set throttle to " + value;
    }
}
