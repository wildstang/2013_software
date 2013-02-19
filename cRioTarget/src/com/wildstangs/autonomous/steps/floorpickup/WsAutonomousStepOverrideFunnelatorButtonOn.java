package com.wildstangs.autonomous.steps.floorpickup;

import com.wildstangs.autonomous.WsAutonomousStep;
import com.wildstangs.inputfacade.base.WsInputFacade;
import com.wildstangs.inputfacade.inputs.joystick.manipulator.WsManipulatorJoystickButtonEnum;
import com.wildstangs.subjects.base.BooleanSubject;
import com.wildstangs.subjects.base.Subject;

/**
 *
 * @author Liam Fruzyna
 */
public class WsAutonomousStepOverrideFunnelatorButtonOn extends WsAutonomousStep 
{
    public void initialize()
    {
        Subject subject = WsInputFacade.getInstance().getOiInput(WsInputFacade.MANIPULATOR_JOYSTICK).getSubject(WsManipulatorJoystickButtonEnum.BUTTON10);
        BooleanSubject backButton = (BooleanSubject)subject;
        
        backButton.setValue(true);
        finished = true;
    }
    public void update()
    {
        
    }
    public String toString()
    {
        return "Turn off the funnelator gate";
    }
    
}