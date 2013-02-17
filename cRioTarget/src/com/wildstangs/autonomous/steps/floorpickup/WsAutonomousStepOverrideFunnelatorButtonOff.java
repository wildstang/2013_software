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
public class WsAutonomousStepOverrideFunnelatorButtonOff extends WsAutonomousStep 
{
    public void initialize()
    {
        Subject subject = WsInputFacade.getInstance().getOiInput(WsInputFacade.MANIPULATOR_JOYSTICK).getSubject(WsManipulatorJoystickButtonEnum.BUTTON10);
        BooleanSubject backButton = (BooleanSubject)subject;
        
        backButton.setValue(false);
        finished = true;
    }
    public void update()
    {
        
    }
    public String toString()
    {
        return "Turn on the funnelator presets";
    }
    
}
