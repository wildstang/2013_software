package com.wildstangs.subsystems;

import com.wildstangs.config.DoubleConfigFileParameter;
import com.wildstangs.inputfacade.base.WsInputFacade;
import com.wildstangs.inputfacade.inputs.joystick.driver.WsDriverJoystickButtonEnum;
import com.wildstangs.outputfacade.base.WsOutputFacade;
import com.wildstangs.subjects.base.IObserver;
import com.wildstangs.subjects.base.Subject;
import com.wildstangs.subsystems.base.WsSubsystem;
import com.wildstangs.subsystems.base.WsSubsystemContainer;
import edu.wpi.first.wpilibj.DoubleSolenoid;


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
    DoubleConfigFileParameter maxSpeed = new DoubleConfigFileParameter(
            this.getClass().getName(), "maxAccumulatorSpeed", 1.0);

    public WsFloorPickup(String name) {
        super(name);
        Subject subject = WsInputFacade.getInstance().getOiInput(WsInputFacade.DRIVER_JOYSTICK).getSubject(WsDriverJoystickButtonEnum.BUTTON4);
        subject.attach(this);
        
        subject = WsInputFacade.getInstance().getOiInput(WsInputFacade.DRIVER_JOYSTICK).getSubject(WsDriverJoystickButtonEnum.BUTTON3);
        subject.attach(this);

        subject = WsInputFacade.getInstance().getOiInput(WsInputFacade.DRIVER_JOYSTICK).getSubject(WsDriverJoystickButtonEnum.BUTTON5);
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
        WsOutputFacade.getInstance().getOutput(WsOutputFacade.ACCUMULATOR_SOLENOID).set(null, Boolean.valueOf(solenoidState));
        
        if (motorForward == true && solenoidState == false && ((WsHopper) WsSubsystemContainer.getInstance()
           .getSubsystem(WsSubsystemContainer.WS_HOPPER)).get_LiftState() != DoubleSolenoid.Value.kReverse) 
        {
            motorForward = false;
        }
        
        if(motorForward) 
        {
            WsOutputFacade.getInstance().getOutput(WsOutputFacade.ACCUMULATOR_VICTOR)
                    .set(null, Double.valueOf(maxVictorSpeed));
        }
        else if(motorBack)
        {
            WsOutputFacade.getInstance().getOutput(WsOutputFacade.ACCUMULATOR_VICTOR)
                    .set(null, Double.valueOf(-maxVictorSpeed));
        }
        else
        {
            WsOutputFacade.getInstance().getOutput(WsOutputFacade.ACCUMULATOR_VICTOR)
                    .set(null, Double.valueOf(0.0));
        }
    }

    protected void initDefaultCommand() {
    }
    
    public boolean getSolenoidState()
    {
        return solenoidState;
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
        if (subjectThatCaused.getType() == WsDriverJoystickButtonEnum.BUTTON4) {
            solenoidState = !solenoidState;
        } else if (subjectThatCaused.getType() == WsDriverJoystickButtonEnum.BUTTON5) {
            motorForward = !motorForward;
            motorBack = false;
        }
        else if(subjectThatCaused.getType() == WsDriverJoystickButtonEnum.BUTTON3)
        {
            motorBack = !motorBack;
            motorForward = false;
        }
    }
    public boolean isUp()
    {
        return !solenoidState;
    }
}