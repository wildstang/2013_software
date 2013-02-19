package com.wildstangs.subsystems;

import com.wildstangs.inputfacade.base.WsInputFacade;
import com.wildstangs.inputfacade.inputs.joystick.manipulator.WsManipulatorJoystickButtonEnum;
import com.wildstangs.outputfacade.base.IOutputEnum;
import com.wildstangs.outputfacade.base.WsOutputFacade;
import com.wildstangs.subjects.base.BooleanSubject;
import com.wildstangs.subjects.base.IObserver;
import com.wildstangs.subjects.base.ISubjectEnum;
import com.wildstangs.subjects.base.Subject;
import com.wildstangs.subsystems.base.WsSubsystem;
import com.wildstangs.subsystems.base.WsSubsystemContainer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author Liam Fruzyna
 */
public class WsIntake extends WsSubsystem implements IObserver
{
    private static final boolean controlValveDefaultState = false;
    boolean controlValveState;
    boolean motorForward = false, motorBack = false;
    boolean rightAccumulatorLimitSwitch = false, leftAccumulatorLimitSwitch = false,
            funnelatorLimitSwitch = false;

    boolean overrideButtonState;
    public WsIntake (String name)
    {
        super(name);
        
        
        //Funnelator override commented out, button 10 now used for loading ramp
        Subject subject = WsInputFacade.getInstance().getOiInput(WsInputFacade.MANIPULATOR_JOYSTICK).getSubject(WsManipulatorJoystickButtonEnum.BUTTON10);
        subject.attach(this);
        
        subject = WsInputFacade.getInstance().getOiInput(WsInputFacade.MANIPULATOR_JOYSTICK).getSubject(WsManipulatorJoystickButtonEnum.BUTTON5);
        subject.attach(this);
        
        subject = WsInputFacade.getInstance().getOiInput(WsInputFacade.MANIPULATOR_JOYSTICK).getSubject(WsManipulatorJoystickButtonEnum.BUTTON7);
        subject.attach(this);
        
        subject = WsInputFacade.getInstance().getSensorInput(WsInputFacade.LEFT_ACCUMULATOR_LIMIT_SWITCH).getSubject((ISubjectEnum) null);
        subject.attach(this);
        
        subject = WsInputFacade.getInstance().getSensorInput(WsInputFacade.RIGHT_ACCUMULATOR_LIMIT_SWITCH).getSubject((ISubjectEnum) null);
        subject.attach(this);
        
        subject = WsInputFacade.getInstance().getSensorInput(WsInputFacade.FUNNELATOR_LIMIT_SWITCH).getSubject((ISubjectEnum) null);
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
        if(leftAccumulatorLimitSwitch && rightAccumulatorLimitSwitch
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
        if (motorForward == true && pickup.isUp() && false == ((WsHopper) WsSubsystemContainer.getInstance()
           .getSubsystem(WsSubsystemContainer.WS_HOPPER)).isDownLimitSwitchTriggered()) 
        {
            motorForward = false;
        }
        
        if(motorForward && ((WsHopper) WsSubsystemContainer.getInstance()
           .getSubsystem(WsSubsystemContainer.WS_HOPPER)).isDownLimitSwitchTriggered()) 
        {
            WsOutputFacade.getInstance().getOutput(WsOutputFacade.FUNNELATOR_ROLLER)
                    .set(null, Double.valueOf(1.0));
            SmartDashboard.putNumber("Funnelator roller", 1.0);
        }
        else if(motorBack)
        {
            WsOutputFacade.getInstance().getOutput(WsOutputFacade.FUNNELATOR_ROLLER)
                    .set(null, Double.valueOf(-1.0));
            SmartDashboard.putNumber("Funnelator roller", -1.0);

        }
        else
        {
            WsOutputFacade.getInstance().getOutput(WsOutputFacade.FUNNELATOR_ROLLER)
                    .set(null, Double.valueOf(0.0));
            SmartDashboard.putNumber("Funnelator roller", 0.0);
        }
        
        SmartDashboard.putBoolean("RightAccumulatorLimitSwitch: ", rightAccumulatorLimitSwitch);
        SmartDashboard.putBoolean("LeftAccumulatorLimitSwitch: ", leftAccumulatorLimitSwitch);
        SmartDashboard.putBoolean("FunnelatorLimitSwitch: ", funnelatorLimitSwitch);
    }

    public void notifyConfigChange() 
    {
    }
    
    public boolean getFunnelatorLimitSwitch()
    {
        return funnelatorLimitSwitch;
    }
    
    public boolean getLeftAccumulatorLimitSwitch()
    {
        return leftAccumulatorLimitSwitch;
    }
    
    public boolean getRightAccumulatorLimitSwitch()
    {
        return rightAccumulatorLimitSwitch;
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
        else if(subjectThatCaused.equals(WsInputFacade.getInstance().
                getSensorInput(WsInputFacade.LEFT_ACCUMULATOR_LIMIT_SWITCH).
                getSubject((ISubjectEnum) null)))
        {
            leftAccumulatorLimitSwitch = ((BooleanSubject) subjectThatCaused).getValue();
        }
        else if(subjectThatCaused.equals(WsInputFacade.getInstance().
                getSensorInput(WsInputFacade.RIGHT_ACCUMULATOR_LIMIT_SWITCH).
                getSubject((ISubjectEnum) null)))
        {
            rightAccumulatorLimitSwitch = ((BooleanSubject) subjectThatCaused).getValue();
        }
        else if(subjectThatCaused.equals(WsInputFacade.getInstance().
                getSensorInput(WsInputFacade.FUNNELATOR_LIMIT_SWITCH).getSubject((ISubjectEnum) null)))
        {
            funnelatorLimitSwitch = ((BooleanSubject) subjectThatCaused).getValue();
        }
    }
}