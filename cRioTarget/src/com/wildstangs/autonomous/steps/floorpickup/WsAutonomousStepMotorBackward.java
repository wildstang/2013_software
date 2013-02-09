package com.wildstangs.autonomous.steps.floorpickup;

import com.wildstangs.autonomous.WsAutonomousStep;
import com.wildstangs.inputfacade.base.WsInputFacade;
import com.wildstangs.inputfacade.inputs.joystick.driver.WsDriverJoystickButtonEnum;
import com.wildstangs.subjects.base.BooleanSubject;
import com.wildstangs.subjects.base.Subject;
import com.wildstangs.subsystems.WsFloorPickup;
import com.wildstangs.subsystems.base.WsSubsystemContainer;

/**
 *
 * @author Liam Fruzyna
 */
public class WsAutonomousStepMotorBackward extends WsAutonomousStep 
{
    public void initialize()
    {
        WsFloorPickup subsystem = (WsFloorPickup)(WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_FLOOR_PICKUP));
        Subject subject = WsInputFacade.getInstance().getOiInput(WsInputFacade.DRIVER_JOYSTICK).getSubject(WsDriverJoystickButtonEnum.BUTTON3);
        BooleanSubject backButton = (BooleanSubject)subject;
        
        if(subsystem.getMotorBack() == false)
            backButton.setValue(true);
    }
    public void update()
    {
        
    }
    public String toString()
    {
        return "moving floor pickup motor backward";
    }
    
}
