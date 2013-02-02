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
    DoubleConfigFileParameter maxSpeed = new DoubleConfigFileParameter(
            this.getClass().getName(), "maxAccumulatorSpeed", 1.0);

    public WsFloorPickup(String name) {
        super(name);
        Subject subject = WsInputFacade.getInstance().getOiInput(WsInputFacade.DRIVER_JOYSTICK).getSubject(WsDriverJoystickButtonEnum.BUTTON6);
        subject.attach(this);

        subject = WsInputFacade.getInstance().getOiInput(WsInputFacade.DRIVER_JOYSTICK).getSubject(WsDriverJoystickButtonEnum.BUTTON7);
        subject.attach(this);


    }

    public void notifyConfigChange() {
        //Override when extending base class if config is needed.
    }

    public void update() {
        WsOutputFacade.getInstance().getOutput(WsOutputFacade.ACCUMULATOR_SOLENOID_LEFT).set(null, Boolean.valueOf(solenoidState));
        WsOutputFacade.getInstance().getOutput(WsOutputFacade.ACCUMULATOR_SOLENOID_RIGHT).set(null, Boolean.valueOf(solenoidState));
        if (motorState == true) {
            if (solenoidState == false) {
                if (((WsHopper) WsSubsystemContainer.getInstance()
                        .getSubsystem(WsSubsystemContainer.WS_HOPPER)).
                        get_LiftState() == DoubleSolenoid.Value.kReverse) {
                    motorState = false;
                }
            }
        }
    }

    protected void initDefaultCommand() {
    }

    public void acceptNotification(Subject subjectThatCaused) {
        if (subjectThatCaused.getType() == WsDriverJoystickButtonEnum.BUTTON6) {
            solenoidState = !solenoidState;
        } else if (subjectThatCaused.getType() == WsDriverJoystickButtonEnum.BUTTON7) {
            motorState = !motorState;
        }
    }
}