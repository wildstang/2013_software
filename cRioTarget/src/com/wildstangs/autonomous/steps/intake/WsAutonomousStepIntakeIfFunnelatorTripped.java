package com.wildstangs.autonomous.steps.intake;

import com.wildstangs.autonomous.WsAutonomousStep;
import com.wildstangs.inputfacade.base.WsInputFacade;
import com.wildstangs.inputfacade.inputs.joystick.manipulator.WsManipulatorJoystickButtonEnum;
import com.wildstangs.subjects.base.BooleanSubject;
import com.wildstangs.subjects.base.IObserver;
import com.wildstangs.subjects.base.ISubjectEnum;
import com.wildstangs.subjects.base.Subject;
import com.wildstangs.subsystems.WsFloorPickup;
import com.wildstangs.subsystems.base.WsSubsystemContainer;

/**
 *
 * @author Nathan
 */
public class WsAutonomousStepIntakeIfFunnelatorTripped extends WsAutonomousStep implements IObserver {

    boolean funnelatorSwitch;

    public WsAutonomousStepIntakeIfFunnelatorTripped() {
        Subject subject = WsInputFacade.getInstance().getSensorInput(WsInputFacade.FUNNELATOR_LIMIT_SWITCH).getSubject((ISubjectEnum) null);
        subject.attach(this);
    }

    public void initialize() {
        if (false == funnelatorSwitch) {
            finished = true;
        } else {
            WsFloorPickup subsystem = (WsFloorPickup) (WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_FLOOR_PICKUP));
            Subject subject = WsInputFacade.getInstance().getOiInput(WsInputFacade.MANIPULATOR_JOYSTICK).getSubject(WsManipulatorJoystickButtonEnum.BUTTON5);
            BooleanSubject backButton = (BooleanSubject) subject;

            if (subsystem.getMotorBack() == false) {
                backButton.setValue(true);
            }
        }
    }

    public void update() {
        if (true == funnelatorSwitch) {
            Subject subject = WsInputFacade.getInstance().getOiInput(WsInputFacade.MANIPULATOR_JOYSTICK).getSubject(WsManipulatorJoystickButtonEnum.BUTTON5);
            BooleanSubject backButton = (BooleanSubject) subject;
            backButton.setValue(false);
            finished = true;
        }
    }

    public String toString() {
        return "Inatakes accumulator motor if funnelator is tripped; keep intaking til switch goes false";
    }

    public void acceptNotification(Subject subjectThatCaused) {
        funnelatorSwitch = ((BooleanSubject) subjectThatCaused).getValue();
    }
}
