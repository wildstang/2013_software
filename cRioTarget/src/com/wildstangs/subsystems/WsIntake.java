package com.wildstangs.subsystems;

import com.wildstangs.config.BooleanConfigFileParameter;
import com.wildstangs.config.DoubleConfigFileParameter;
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
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author Liam Fruzyna
 */
public class WsIntake extends WsSubsystem implements IObserver {

    private DoubleConfigFileParameter switchDelay = new DoubleConfigFileParameter(
            this.getClass().getName(), "FingerDelayFromAccumulatorSwitch", 3.5);
    private BooleanConfigFileParameter useDelay = new BooleanConfigFileParameter(
            this.getClass().getName(), "UseTimeDelay", true);
    private double switchDelayTime;
    private boolean useTimeDelay = true;
    
    private static final boolean fingerValveDefaultState = false;
    private boolean fingerValveState;
    private boolean motorForward = false, motorBack = false;
    private boolean rightAccumulatorLimitSwitch = false, leftAccumulatorLimitSwitch = false,
            funnelatorLimitSwitch = false;
    private boolean latchAccumulatorSwitches = false;
    private boolean fingerDownOverrideButtonState;
    private boolean fingerUpOverrideButtonState;
    private boolean counting = false;
    private double countTo = 0;
    private int numLatchedDiscs = 0;

    public WsIntake(String name) {
        super(name);

        //Finger down override button
        Subject subject = WsInputFacade.getInstance().getOiInput(WsInputFacade.MANIPULATOR_JOYSTICK).getSubject(WsManipulatorJoystickButtonEnum.BUTTON10);
        subject.attach(this);
        
        //Finger up override button
        subject = WsInputFacade.getInstance().getOiInput(WsInputFacade.MANIPULATOR_JOYSTICK).getSubject(WsManipulatorJoystickButtonEnum.BUTTON9);
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

        switchDelayTime = switchDelay.getValue();
        useTimeDelay = useDelay.getValue();
    }

    public void init() {
        fingerValveState = fingerValveDefaultState;
        fingerDownOverrideButtonState = false;
        motorForward = false;
        motorBack = false;
        countTo = 0;
        counting = false;
        latchAccumulatorSwitches=false; 
    }

    public void update() {
        //If this is true, the driver just brought the accumulator up and we have locked the switch states
        if (true == latchAccumulatorSwitches) {
            //Once the left limit switch has transitioned to false, it is safe to let the second frisbee through
            if (false == leftAccumulatorLimitSwitch) {
                if (useTimeDelay && !counting) {
                    counting = true;
                    countTo = Timer.getFPGATimestamp() + switchDelayTime;
                }
            } //Otherwise if the right switch is still true, leave up the finger for now
            else {
                fingerValveState = true;
            }
        } //We should always default to the finger being down
        else {
            fingerValveState = false;
        }

        if (true == fingerDownOverrideButtonState && false == fingerUpOverrideButtonState) {
            //Override the finger down
            fingerValveState = false;
        } else if (false == fingerDownOverrideButtonState && true == fingerUpOverrideButtonState) {
            //Override the finger up
            fingerValveState = true;
        }

        //Set the finger state in the output facade
        WsOutputFacade.getInstance().getOutput(WsOutputFacade.FRISBIE_CONTROL).set((IOutputEnum) null, new Boolean(fingerValveState));

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

        if (useTimeDelay && counting) {
            if (Timer.getFPGATimestamp() >= countTo) {
                //Unlatch the button states and bring down the funnelator finger
                latchAccumulatorSwitches = false;
                fingerValveState = false;
                counting = false;
            }
        }

        SmartDashboard.putBoolean("RightAccumulatorLimitSwitch: ", rightAccumulatorLimitSwitch);
        SmartDashboard.putBoolean("LeftAccumulatorLimitSwitch: ", leftAccumulatorLimitSwitch);
        SmartDashboard.putBoolean("FunnelatorLimitSwitch: ", funnelatorLimitSwitch);
    }

    public void notifyConfigChange() {
        switchDelayTime = switchDelay.getValue();
        useTimeDelay = useDelay.getValue();
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
    
    public boolean getFingerDownOverrideButtonState()
    {
        return fingerDownOverrideButtonState;
    }

    public void latchNumDiscs() {
        if (true == rightAccumulatorLimitSwitch && true == leftAccumulatorLimitSwitch) {
            numLatchedDiscs = 2;
        } else if (true == rightAccumulatorLimitSwitch && false == leftAccumulatorLimitSwitch) {
            numLatchedDiscs = 1;
        } else if (false == rightAccumulatorLimitSwitch && true == leftAccumulatorLimitSwitch) {
            numLatchedDiscs = 1;
        } else {
            numLatchedDiscs = 0;
        }
    }

    public int getNumLatchedDiscs() {
        return numLatchedDiscs;
    }

    public void acceptNotification(Subject subjectThatCaused) {
        BooleanSubject button = (BooleanSubject) subjectThatCaused;
        if (subjectThatCaused.getType() == WsManipulatorJoystickButtonEnum.BUTTON10) {
            fingerDownOverrideButtonState = button.getValue();
        } else if (subjectThatCaused.getType() == WsManipulatorJoystickButtonEnum.BUTTON9) {
            fingerUpOverrideButtonState = button.getValue();
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
            if(funnelatorLimitSwitch == false)
            {
                if(!useTimeDelay && fingerValveState && !leftAccumulatorLimitSwitch)
                {
                    //Unlatch the button states and bring down the funnelator finger
                    latchAccumulatorSwitches = false;
                    fingerValveState = false;
                }
                ((WsHopper)WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_HOPPER)).addDisk();
            }
        }
    }
}
