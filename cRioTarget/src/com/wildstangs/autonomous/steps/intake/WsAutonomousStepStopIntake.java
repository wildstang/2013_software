/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wildstangs.autonomous.steps.intake;

import com.wildstangs.autonomous.WsAutonomousStep;
import com.wildstangs.inputfacade.base.WsInputFacade;
import com.wildstangs.inputfacade.inputs.joystick.driver.WsDriverJoystickButtonEnum;
import com.wildstangs.subjects.base.BooleanSubject;
import com.wildstangs.subjects.base.Subject;


/**
 *
 * @author Joshua Gustafson
 */
public class WsAutonomousStepStopIntake extends WsAutonomousStep 
{
 
    public void initialize()
    {
        Subject subject = WsInputFacade.getInstance().getOiInput(WsInputFacade.DRIVER_JOYSTICK).getSubject(WsDriverJoystickButtonEnum.BUTTON5);
        BooleanSubject button = (BooleanSubject)subject;
        button.setValue(false);
        finished = true;
    }

    public void update()
    {
        
    }

    public String toString()
    {
        return "Turn Funnelator Motor Off";
    }
}
