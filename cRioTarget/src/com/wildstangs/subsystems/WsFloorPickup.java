package com.wildstangs.subsystems;

import com.wildstangs.config.DoubleConfigFileParameter;
import com.wildstangs.inputfacade.base.IInputEnum;
import com.wildstangs.inputfacade.base.WsInputFacade;
import com.wildstangs.inputfacade.inputs.WsDigitalInput;
import com.wildstangs.inputfacade.inputs.joystick.driver.WsDriverJoystickButtonEnum;
import com.wildstangs.inputfacade.inputs.joystick.manipulator.WsManipulatorJoystickButtonEnum;
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
 * @author Adam
 */
public class WsFloorPickup extends WsSubsystem implements IObserver {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    boolean solenoidState = false;
    boolean motorForward = false, motorBack = false;
    double maxVictorSpeed;
    boolean accumulatorUpLimitSwitch = false;
    DoubleConfigFileParameter maxSpeed = new DoubleConfigFileParameter(
            this.getClass().getName(), "maxAccumulatorSpeed", 1.0);

    public WsFloorPickup(String name) {
        super(name);
        Subject subject = WsInputFacade.getInstance().getOiInput(WsInputFacade.DRIVER_JOYSTICK).getSubject(WsDriverJoystickButtonEnum.BUTTON5);
        subject.attach(this);
        
        subject = WsInputFacade.getInstance().getOiInput(WsInputFacade.MANIPULATOR_JOYSTICK).getSubject(WsManipulatorJoystickButtonEnum.BUTTON7);
        subject.attach(this);

        subject = WsInputFacade.getInstance().getOiInput(WsInputFacade.MANIPULATOR_JOYSTICK).getSubject(WsManipulatorJoystickButtonEnum.BUTTON5);
        subject.attach(this);
        
        subject = WsInputFacade.getInstance().getSensorInput(WsInputFacade.ACCUMULATOR_UP_LIMIT_SWITCH).getSubject((ISubjectEnum) null);
        subject.attach(this);
        
        maxVictorSpeed = maxSpeed.getValue();
    }

    public void init()
    {
        solenoidState = false;
        motorForward = false;
        motorBack = false;
        
        WsOutputFacade.getInstance().getOutput(WsOutputFacade.ACCUMULATOR_SOLENOID).set(null, Boolean.valueOf(solenoidState));
        WsOutputFacade.getInstance().getOutput(WsOutputFacade.ACCUMULATOR_VICTOR).set(null, Double.valueOf(0.0));
    }
    
    public void notifyConfigChange() {
        maxVictorSpeed = maxSpeed.getValue();
    }

    public void update() {
        WsDigitalInput upSwitch = (WsDigitalInput)(WsInputFacade.getInstance().getSensorInput(WsInputFacade.ACCUMULATOR_UP_LIMIT_SWITCH));
        boolean switchState = ((Boolean)(upSwitch.get((IInputEnum)null))).booleanValue();
        SmartDashboard.putBoolean("Accumulator Up Limit Switch", switchState);
        WsOutputFacade.getInstance().getOutput(WsOutputFacade.ACCUMULATOR_SOLENOID).set(null, Boolean.valueOf(solenoidState));
        
        if (motorForward == true && solenoidState == false && (false == ((WsHopper) WsSubsystemContainer.getInstance()
           .getSubsystem(WsSubsystemContainer.WS_HOPPER)).isDownLimitSwitchTriggered())) 
        {
            motorForward = false;
        }
        
        if(motorForward) 
        {
            WsOutputFacade.getInstance().getOutput(WsOutputFacade.ACCUMULATOR_VICTOR)
                    .set(null, Double.valueOf(maxVictorSpeed));
            SmartDashboard.putNumber("Accumulator motor", maxVictorSpeed);
        }
        else if(motorBack)
        {
            WsOutputFacade.getInstance().getOutput(WsOutputFacade.ACCUMULATOR_VICTOR)
                    .set(null, Double.valueOf(-maxVictorSpeed));
            SmartDashboard.putNumber("Accumulator motor", -maxVictorSpeed);

        }
        else
        {
            WsOutputFacade.getInstance().getOutput(WsOutputFacade.ACCUMULATOR_VICTOR)
                    .set(null, Double.valueOf(0.0));
            SmartDashboard.putNumber("Accumulator motor", 0.0);

        }
    }

    protected void initDefaultCommand() {
    }
    
    public boolean getMotorBack()
    {
        return motorBack;
    }
    
    public boolean getMotorForward()
    {
        return motorForward;
    }
    
    
    public void acceptNotification(Subject subjectThatCaused) {
        if (subjectThatCaused.getType() == WsDriverJoystickButtonEnum.BUTTON5) 
        {
            BooleanSubject button = (BooleanSubject)subjectThatCaused;
            solenoidState = button.getValue();
        } else if (subjectThatCaused.getType() == WsManipulatorJoystickButtonEnum.BUTTON5) {
            BooleanSubject button = (BooleanSubject)subjectThatCaused;
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
            BooleanSubject button = (BooleanSubject)subjectThatCaused;
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
        else if(subjectThatCaused.equals(WsInputFacade.getInstance().getSensorInput(WsInputFacade.ACCUMULATOR_UP_LIMIT_SWITCH).getSubject((ISubjectEnum) null)))
        {
            accumulatorUpLimitSwitch = ((BooleanSubject) subjectThatCaused).getValue();
        }
    }
    public boolean isUp()
    {
        return accumulatorUpLimitSwitch;
    }
}
