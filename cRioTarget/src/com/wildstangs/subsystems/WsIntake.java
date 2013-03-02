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
public class WsIntake extends WsSubsystem implements IObserver {

    private static final boolean controlValveDefaultState = false;
    boolean controlValveState;
    boolean motorForward = false, motorBack = false;
    boolean rightAccumulatorLimitSwitch = false, leftAccumulatorLimitSwitch = false,
            funnelatorLimitSwitch = false;
    boolean latchAccumulatorSwitches = false;
    boolean overrideButtonState;

    public WsIntake(String name) {
        super(name);

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

    public void init() {
        controlValveState = controlValveDefaultState;
        overrideButtonState = false;
        motorForward = false;
        motorBack = false;
    }

    public void update() {
        //If this is true, the driver just brought the accumulator up and we have locked the switch states
        if (true == latchAccumulatorSwitches) {
            //Once the left limit switch has transitioned to false, it is safe to let the second frisbee through
            if (false == leftAccumulatorLimitSwitch) {
                //Unlatch the button states and bring down the funnelator finger
                latchAccumulatorSwitches = false;
                controlValveState = false;
            } //Otherwise if the right switch is still true, leave up the finger for now
            else {
                controlValveState = true;
            }
        } //We should always default to the finger being down
        else {
            controlValveState = false;
        }
        //If the override button is pressed, bring down the finger
        if (true == overrideButtonState) {
            controlValveState = false;
        }

        //Set the finger state in the output facade
        WsOutputFacade.getInstance().getOutput(WsOutputFacade.FRISBIE_CONTROL).set((IOutputEnum) null, new Boolean(controlValveState));


        WsFloorPickup pickup = ((WsFloorPickup) (WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_FLOOR_PICKUP)));
        boolean up = pickup.isUp();
        if (motorForward == true && pickup.isUp() && false == ((WsHopper) WsSubsystemContainer.getInstance()
                .getSubsystem(WsSubsystemContainer.WS_HOPPER)).isDownLimitSwitchTriggered()) {
            motorForward = false;
        }

        if (motorForward && ((WsHopper) WsSubsystemContainer.getInstance()
                .getSubsystem(WsSubsystemContainer.WS_HOPPER)).isDownLimitSwitchTriggered()) {
            WsOutputFacade.getInstance().getOutput(WsOutputFacade.FUNNELATOR_ROLLER)
                    .set(null, Double.valueOf(1.0));
            SmartDashboard.putNumber("Funnelator roller", 1.0);
        } else if (motorBack) {
            WsOutputFacade.getInstance().getOutput(WsOutputFacade.FUNNELATOR_ROLLER)
                    .set(null, Double.valueOf(-1.0));
            SmartDashboard.putNumber("Funnelator roller", -1.0);

        } else {
            WsOutputFacade.getInstance().getOutput(WsOutputFacade.FUNNELATOR_ROLLER)
                    .set(null, Double.valueOf(0.0));
            SmartDashboard.putNumber("Funnelator roller", 0.0);
        }

        SmartDashboard.putBoolean("RightAccumulatorLimitSwitch: ", rightAccumulatorLimitSwitch);
        SmartDashboard.putBoolean("LeftAccumulatorLimitSwitch: ", leftAccumulatorLimitSwitch);
        SmartDashboard.putBoolean("FunnelatorLimitSwitch: ", funnelatorLimitSwitch);
    }

    public void notifyConfigChange() {
    }

    public boolean getFunnelatorLimitSwitch() {
        return funnelatorLimitSwitch;
    }

    public boolean getLeftAccumulatorLimitSwitch() {
        return leftAccumulatorLimitSwitch;
    }

    public boolean getRightAccumulatorLimitSwitch() {
        return rightAccumulatorLimitSwitch;
    }

    public void latchAccumulatorSwitches() {
        //Only latch the switch states if both are pressed
        if (true == rightAccumulatorLimitSwitch && true == leftAccumulatorLimitSwitch) {
            latchAccumulatorSwitches = true;
        }
    }

    public void acceptNotification(Subject subjectThatCaused) {
        BooleanSubject button = (BooleanSubject) subjectThatCaused;
        if (subjectThatCaused.getType() == WsManipulatorJoystickButtonEnum.BUTTON10) {
            overrideButtonState = button.getValue();
        } else if (subjectThatCaused.getType() == WsManipulatorJoystickButtonEnum.BUTTON5) {
            if (button.getValue()) {
                motorForward = true;
                motorBack = false;
            } else {
                motorForward = false;
            }
        } else if (subjectThatCaused.getType() == WsManipulatorJoystickButtonEnum.BUTTON7) {
            if (button.getValue()) {
                motorForward = false;
                motorBack = true;
            } else {
                motorBack = false;
            }
        } else if (subjectThatCaused.equals(WsInputFacade.getInstance().
                getSensorInput(WsInputFacade.LEFT_ACCUMULATOR_LIMIT_SWITCH).
                getSubject((ISubjectEnum) null))) {
            leftAccumulatorLimitSwitch = ((BooleanSubject) subjectThatCaused).getValue();
        } else if (subjectThatCaused.equals(WsInputFacade.getInstance().
                getSensorInput(WsInputFacade.RIGHT_ACCUMULATOR_LIMIT_SWITCH).
                getSubject((ISubjectEnum) null))) {
            rightAccumulatorLimitSwitch = ((BooleanSubject) subjectThatCaused).getValue();
        } else if (subjectThatCaused.equals(WsInputFacade.getInstance().
                getSensorInput(WsInputFacade.FUNNELATOR_LIMIT_SWITCH).getSubject((ISubjectEnum) null))) {
            funnelatorLimitSwitch = ((BooleanSubject) subjectThatCaused).getValue();
        }
    }
}