package com.wildstangs.subsystems;

import com.wildstangs.config.DoubleConfigFileParameter;
import edu.wpi.first.wpilibj.command.Subsystem;
import com.wildstangs.inputfacade.base.WsInputFacade;
import com.wildstangs.inputfacade.inputs.joystick.driver.WsDriverJoystickButtonEnum;
import com.wildstangs.outputfacade.base.WsOutputFacade;
import com.wildstangs.subjects.base.IObserver;
import com.wildstangs.subjects.base.Subject;
import com.wildstangs.subsystems.base.WsSubsystemContainer;
import edu.wpi.first.wpilibj.DoubleSolenoid;


/**
 *
 * @author Adam
 */
public class WsFloorPickup extends Subsystem implements IObserver {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    boolean solenoidState = false;
    boolean motorState = false;
    double maxVictorSpeed;
    DoubleConfigFileParameter maxSpeed = new DoubleConfigFileParameter(
            this.getClass().getName(), "maxAccumulatorSpeed", 1.0);

    public WsFloorPickup(String name) {
        super(name);
        Subject subject = WsInputFacade.getInstance().getOiInput(WsInputFacade.DRIVER_JOYSTICK).getSubject(WsDriverJoystickButtonEnum.BUTTON4);
        subject.attach(this);

        subject = WsInputFacade.getInstance().getOiInput(WsInputFacade.DRIVER_JOYSTICK).getSubject(WsDriverJoystickButtonEnum.BUTTON5);
        subject.attach(this);

        maxVictorSpeed = maxSpeed.getValue();
    }

    public void init()
    {
        solenoidState = false;
        motorState = false;
        
        WsOutputFacade.getInstance().getOutput(WsOutputFacade.ACCUMULATOR_SOLENOID_LEFT).set(null, Boolean.valueOf(solenoidState));
        WsOutputFacade.getInstance().getOutput(WsOutputFacade.ACCUMULATOR_SOLENOID_RIGHT).set(null, Boolean.valueOf(solenoidState));
        WsOutputFacade.getInstance().getOutput(WsOutputFacade.ACCUMULATOR_VICTOR).set(null, Double.valueOf(0.0));
    }
    
    public void notifyConfigChange() {
        maxVictorSpeed = maxSpeed.getValue();
    }

    public void update() {
        WsOutputFacade.getInstance().getOutput(WsOutputFacade.ACCUMULATOR_SOLENOID_LEFT).set(null, Boolean.valueOf(solenoidState));
        WsOutputFacade.getInstance().getOutput(WsOutputFacade.ACCUMULATOR_SOLENOID_RIGHT).set(null, Boolean.valueOf(solenoidState));
        
        if (motorState == true && solenoidState == false && ((WsHopper) WsSubsystemContainer.getInstance()
           .getSubsystem(WsSubsystemContainer.WS_HOPPER)).get_LiftState() != DoubleSolenoid.Value.kReverse) 
        {
            motorState = false;
        }
        
        if(motorState) 
        {
            WsOutputFacade.getInstance().getOutput(WsOutputFacade.ACCUMULATOR_VICTOR)
                    .set(null, Double.valueOf(maxVictorSpeed));
        }
        else
        {
            WsOutputFacade.getInstance().getOutput(WsOutputFacade.ACCUMULATOR_VICTOR)
                    .set(null, Double.valueOf(0.0));
        }
    }

    protected void initDefaultCommand() {
    }

    public void acceptNotification(Subject subjectThatCaused) {
        if (subjectThatCaused.getType() == WsDriverJoystickButtonEnum.BUTTON4) {
            solenoidState = !solenoidState;
        } else if (subjectThatCaused.getType() == WsDriverJoystickButtonEnum.BUTTON5) {
            motorState = !motorState;
        }
    }
}