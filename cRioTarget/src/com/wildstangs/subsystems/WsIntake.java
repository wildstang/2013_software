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
import edu.wpi.first.wpilibj.DoubleSolenoid;

/**
 *
 * @author Liam Fruzyna
 */
public class WsIntake extends WsSubsystem implements IObserver
{
    private static final boolean controlValveDefaultState = false;
    boolean controlValveState;
    boolean motorForward = false, motorBack = false;

    boolean overrideButtonState;
    public WsIntake (String name)
    {
        super(name);
        
        Subject subject = WsInputFacade.getInstance().getOiInput(WsInputFacade.MANIPULATOR_JOYSTICK).getSubject(WsManipulatorJoystickButtonEnum.BUTTON10);
        subject.attach(this);
        
        subject = WsInputFacade.getInstance().getOiInput(WsInputFacade.MANIPULATOR_JOYSTICK).getSubject(WsManipulatorJoystickButtonEnum.BUTTON5);
        subject.attach(this);
        
        subject = WsInputFacade.getInstance().getOiInput(WsInputFacade.MANIPULATOR_JOYSTICK).getSubject(WsManipulatorJoystickButtonEnum.BUTTON7);
        subject.attach(this);
    }
    
    public void init()
    {
        controlValveState = controlValveDefaultState;
        overrideButtonState = false;
        motorForward = false;
        motorBack = false;
        
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
        
        
        WsFloorPickup pickup = ((WsFloorPickup)(WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_FLOOR_PICKUP)));
        boolean up = pickup.isUp();
        if (motorForward == true && pickup.isUp() && ((WsHopper) WsSubsystemContainer.getInstance()
           .getSubsystem(WsSubsystemContainer.WS_HOPPER)).isHopperUp()) 
        {
            motorForward = false;
        }
        
        if(motorForward) 
        {
            WsOutputFacade.getInstance().getOutput(WsOutputFacade.FUNNELATOR_ROLLER)
                    .set(null, Double.valueOf(1.0));
        }
        else if(motorBack)
        {
            WsOutputFacade.getInstance().getOutput(WsOutputFacade.FUNNELATOR_ROLLER)
                    .set(null, Double.valueOf(-1.0));
        }
        else
        {
            WsOutputFacade.getInstance().getOutput(WsOutputFacade.FUNNELATOR_ROLLER)
                    .set(null, Double.valueOf(0.0));
        }
    }

    public void notifyConfigChange() 
    {
    }

    public void acceptNotification(Subject subjectThatCaused) 
    {
        BooleanSubject button = (BooleanSubject)subjectThatCaused;
        if(subjectThatCaused.getType() == WsManipulatorJoystickButtonEnum.BUTTON10)
        {
            overrideButtonState = button.getValue();
        }
        else if (subjectThatCaused.getType() == WsManipulatorJoystickButtonEnum.BUTTON5) {
            if(button.getValue())
            {
                motorForward = true;
                motorBack = false;
            }
            else
            {
                motorForward = false;
            }
        }
        else if(subjectThatCaused.getType() == WsManipulatorJoystickButtonEnum.BUTTON7)
        {
            if(button.getValue())
            {
                motorForward = false;
                motorBack = true;
            }
            else
            {
                motorBack = false;
            }
        }
    }
}