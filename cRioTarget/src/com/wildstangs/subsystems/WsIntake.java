package com.wildstangs.subsystems;

import com.wildstangs.inputfacade.base.IInputEnum;
import com.wildstangs.inputfacade.base.WsInputFacade;
import com.wildstangs.inputfacade.inputs.WsDigitalInput;
import com.wildstangs.inputfacade.inputs.joystick.driver.WsDriverJoystickButtonEnum;
import com.wildstangs.inputfacade.inputs.joystick.manipulator.WsManipulatorJoystickButtonEnum;
import com.wildstangs.outputfacade.base.IOutputEnum;
import com.wildstangs.outputfacade.base.WsOutputFacade;
import com.wildstangs.subjects.base.BooleanSubject;
import com.wildstangs.subjects.base.IObserver;
import com.wildstangs.subjects.base.Subject;
import com.wildstangs.subsystems.base.WsSubsystem;
import com.wildstangs.subsystems.base.WsSubsystemContainer;

/**
 *
 * @author Liam Fruzyna
 */
public class WsIntake extends WsSubsystem implements IObserver
{
    private static final boolean controlValveDefaultState = false;
    boolean controlValveState;
    boolean overrideButtonState;
    public WsIntake (String name)
    {
        super(name);
        
        Subject subject = WsInputFacade.getInstance().getOiInput(WsInputFacade.MANIPULATOR_JOYSTICK).getSubject(WsManipulatorJoystickButtonEnum.BUTTON4);
        subject.attach(this);
        
        subject = WsInputFacade.getInstance().getOiInput(WsInputFacade.DRIVER_JOYSTICK).getSubject(WsManipulatorJoystickButtonEnum.BUTTON5);
        subject.attach(this);
    }
    
    public void init()
    {
        controlValveState = controlValveDefaultState;
        overrideButtonState = false;
    }

    public void update() 
    {
        // only close gate if both limit switches pressed and override button state is false
        WsDigitalInput leftSwitch = (WsDigitalInput)(WsInputFacade.getInstance().getSensorInput(WsInputFacade.LEFT_FUNNELATOR_LIMIT_SWITCH));
        WsDigitalInput rightSwitch = (WsDigitalInput)(WsInputFacade.getInstance().getSensorInput(WsInputFacade.RIGHT_FUNNELATOR_LIMIT_SWITCH));
        
        Boolean leftState = (Boolean)(leftSwitch.get((IInputEnum)null));
        Boolean rightState = (Boolean)(rightSwitch.get((IInputEnum)null));
        
        if(leftState.booleanValue() && rightState.booleanValue()
                && (overrideButtonState == false))
        {
            controlValveState = true;
        }
        else
        {
            controlValveState = false;
        }
        
        WsOutputFacade.getInstance().getOutput(WsOutputFacade.FRISBIE_CONTROL).set((IOutputEnum)null, new Boolean(controlValveState));
    }

    public void notifyConfigChange() 
    {
    }

    public void acceptNotification(Subject subjectThatCaused) 
    {
        BooleanSubject button = (BooleanSubject)subjectThatCaused;
        if(subjectThatCaused.getType() == WsManipulatorJoystickButtonEnum.BUTTON4)
        {
            overrideButtonState = button.getValue();
        }
        else if(subjectThatCaused.getType() == WsDriverJoystickButtonEnum.BUTTON5)
        {
            WsFloorPickup pickup = (WsFloorPickup)(WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_FLOOR_PICKUP));
            boolean up = pickup.isUp();
            if(up && button.getValue())
            {
                WsOutputFacade.getInstance().getOutput(WsOutputFacade.FUNNELATOR_ROLLER).set((IOutputEnum)null, new Double(1.0));
            }
            else
            {
                WsOutputFacade.getInstance().getOutput(WsOutputFacade.FUNNELATOR_ROLLER).set((IOutputEnum)null, new Double(0));
            }
        }
        else if(subjectThatCaused.getType() == WsDriverJoystickButtonEnum.BUTTON3)
        {
                WsOutputFacade.getInstance().getOutput(WsOutputFacade.FUNNELATOR_ROLLER).set((IOutputEnum)null, new Double(-1.0));
        }
    }
}