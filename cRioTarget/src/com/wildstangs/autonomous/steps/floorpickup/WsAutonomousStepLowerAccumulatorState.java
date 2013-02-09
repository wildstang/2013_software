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
public class WsAutonomousStepLowerAccumulatorState extends WsAutonomousStep 
{
    public void initialize()
    {
        WsFloorPickup subsystem = (WsFloorPickup)(WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_FLOOR_PICKUP));
        Subject subject = WsInputFacade.getInstance().getOiInput(WsInputFacade.DRIVER_JOYSTICK).getSubject(WsDriverJoystickButtonEnum.BUTTON4);
        BooleanSubject button = (BooleanSubject)subject;
        
        if(subsystem.isUp()) {
            button.setValue(true);
        }
    }
    public void update()
    {
        WsFloorPickup subsystem = (WsFloorPickup)(WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_FLOOR_PICKUP));
        if(!subsystem.isUp())
        {
            Subject subject = WsInputFacade.getInstance().getOiInput(WsInputFacade.DRIVER_JOYSTICK).getSubject(WsDriverJoystickButtonEnum.BUTTON4);
            BooleanSubject button = (BooleanSubject)subject;
            button.setValue(false);
            finished = true;
        }
    }
    public String toString()
    {
        return "doing something with the solenoid";
    }
    
}