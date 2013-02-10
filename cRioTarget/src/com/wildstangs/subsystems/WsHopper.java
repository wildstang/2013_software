package com.wildstangs.subsystems;

import com.wildstangs.config.IntegerConfigFileParameter;
import com.wildstangs.inputfacade.base.WsInputFacade;
import com.wildstangs.inputfacade.inputs.joystick.manipulator.WsManipulatorJoystickButtonEnum;
import com.wildstangs.outputfacade.base.IOutputEnum;
import com.wildstangs.outputfacade.base.WsOutputFacade;
import com.wildstangs.subjects.base.BooleanSubject;
import com.wildstangs.subjects.base.IObserver;
import com.wildstangs.subjects.base.ISubjectEnum;
import com.wildstangs.subjects.base.Subject;
import com.wildstangs.subsystems.base.WsSubsystem;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


/**
 *
 * @author Liam Fruzyna
 */

public class WsHopper extends WsSubsystem implements IObserver
{
    private static final boolean KICKER_DEFAULT_VALUE = false;
    private static final DoubleSolenoid.Value LIFT_DEFAULT_VALUE = DoubleSolenoid.Value.kReverse;
    private IntegerConfigFileParameter forwardCycleConfig = new IntegerConfigFileParameter(this.getClass().getName(), "forwardCycles", 30);
    private int forwardCycles;
    private int cycle;
    private boolean upLimitSwitchValue = false, downLimitSwitchValue = false;
    
    private boolean kickerValue;
    private DoubleSolenoid.Value liftValue;
    
    
    
    public WsHopper (String name)
    {
        super(name);
        init();
        
        Subject subject = WsInputFacade.getInstance().getOiInput(WsInputFacade.MANIPULATOR_JOYSTICK).getSubject(WsManipulatorJoystickButtonEnum.BUTTON6);
        subject.attach(this);
        
        subject = WsInputFacade.getInstance().getOiInput(WsInputFacade.MANIPULATOR_JOYSTICK).getSubject(WsManipulatorJoystickButtonEnum.BUTTON8);
        subject.attach(this);
        
        subject = WsInputFacade.getInstance().getSensorInput(WsInputFacade.HOPPER_DOWN_LIMIT_SWITCH).getSubject((ISubjectEnum)null);
        subject.attach(this);
        
        subject = WsInputFacade.getInstance().getSensorInput(WsInputFacade.HOPPER_UP_LIMIT_SWITCH).getSubject((ISubjectEnum)null);
        subject.attach(this);
    }
    
    public void init()
    {
        forwardCycles = forwardCycleConfig.getValue();
        kickerValue = KICKER_DEFAULT_VALUE;
        liftValue = LIFT_DEFAULT_VALUE;
        cycle = 0;
    }

    public void update() 
    {
        SmartDashboard.putBoolean("Kicker value", kickerValue);
        SmartDashboard.putNumber("Lift Value", liftValue.value);
        cycle++;
        if(cycle >= forwardCycles)
        {
            kickerValue = false;
        }
        WsOutputFacade.getInstance().getOutput(WsOutputFacade.KICKER).set((IOutputEnum)null, new Boolean(kickerValue));
        WsOutputFacade.getInstance().getOutput(WsOutputFacade.LIFT).set((IOutputEnum)null, new Integer(liftValue.value));
    }

    public void notifyConfigChange() 
    {
    }
    
    public boolean getLiftValueEquals(DoubleSolenoid.Value value)
    {
        return liftValue.equals(value);
    }
    
    public boolean getKickerValue()
    {
        return kickerValue;
    }
    
    public void acceptNotification(Subject subjectThatCaused) 
    {
        BooleanSubject button = (BooleanSubject)subjectThatCaused;
        
        if(subjectThatCaused.getType() == WsManipulatorJoystickButtonEnum.BUTTON6)
        {
            if(button.getValue() == true)
                kickerValue = true;
            if(liftValue.value == DoubleSolenoid.Value.kReverse_val)
            {
                kickerValue = false;
            }
            if(kickerValue == true && (button.getPreviousValue() == false))
            {
                cycle = 0;
            }
        }
        else if(subjectThatCaused.getType() == WsManipulatorJoystickButtonEnum.BUTTON8)
        {
            if(button.getValue() == true && (button.getPreviousValue() == false))
            {
                if(liftValue == DoubleSolenoid.Value.kReverse)
                {
                    liftValue = DoubleSolenoid.Value.kForward;
                }
                else
                {
                    liftValue = DoubleSolenoid.Value.kReverse;
                }
            }
        }
        else if(subjectThatCaused.equals(WsInputFacade.getInstance().
                getSensorInput(WsInputFacade.HOPPER_DOWN_LIMIT_SWITCH).
                getSubject((ISubjectEnum)null)))
        {
             downLimitSwitchValue = ((BooleanSubject)WsInputFacade.getInstance()
                      .getSensorInput(WsInputFacade.HOPPER_UP_LIMIT_SWITCH)
                      .getSubject(((ISubjectEnum)null))).getValue();
        }
        else if(subjectThatCaused.equals(WsInputFacade.getInstance().
            getSensorInput(WsInputFacade.HOPPER_UP_LIMIT_SWITCH).
            getSubject((ISubjectEnum)null)))
        {
            upLimitSwitchValue = ((BooleanSubject)WsInputFacade.getInstance()
                     .getSensorInput(WsInputFacade.HOPPER_UP_LIMIT_SWITCH)
                     .getSubject(((ISubjectEnum)null))).getValue();
        }
    }
    public DoubleSolenoid.Value get_LiftState (){
        return liftValue;     
    }
    
    public boolean isDownLimitSwitchTriggered()
    {
        return downLimitSwitchValue;
    }
    
    public boolean isUpLimitSwitchTriggered()
    {
        return upLimitSwitchValue;
    }
}