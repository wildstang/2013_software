/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.steps.hopper;

import com.wildstangs.autonomous.WsAutonomousStep;
import com.wildstangs.inputfacade.base.WsInputFacade;
import com.wildstangs.inputfacade.inputs.joystick.manipulator.WsManipulatorJoystickButtonEnum;
import com.wildstangs.subjects.base.BooleanSubject;
import com.wildstangs.subjects.base.Subject;
import com.wildstangs.subsystems.WsHopper;
import com.wildstangs.subsystems.base.WsSubsystemContainer;

/**
 *
 * @author User
 */
public class WsAutonomousStepKick extends WsAutonomousStep
{
    private boolean firstUpdate;
    public void initialize() 
    {
        firstUpdate = true;
        Subject subject = WsInputFacade.getInstance().getOiInput(WsInputFacade.MANIPULATOR_JOYSTICK).getSubject(WsManipulatorJoystickButtonEnum.BUTTON6);
        BooleanSubject button = (BooleanSubject)subject;
        button.setValue(true);
    }

    public void update() 
    {
        if (!firstUpdate)
        {
            WsHopper subsystem = (WsHopper)(WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_HOPPER));
            if(subsystem.getKickerValue() == false)
            {
                Subject subject = WsInputFacade.getInstance().getOiInput(WsInputFacade.MANIPULATOR_JOYSTICK).getSubject(WsManipulatorJoystickButtonEnum.BUTTON6);
                BooleanSubject button = (BooleanSubject)subject;
                button.setValue(false);
                finished = true;
            }
        } else {
            firstUpdate = false;
        }
    }

    public String toString() 
    {
        return "Start the kicker cycle";
    }
    
}
