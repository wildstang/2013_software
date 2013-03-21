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
import com.wildstangs.subsystems.base.WsSubsystemContainer;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author Liam Fruzyna
 */
public class WsHopper extends WsSubsystem implements IObserver {

    private static final boolean KICKER_DEFAULT_VALUE = false;
    private static final DoubleSolenoid.Value LIFT_DEFAULT_VALUE = DoubleSolenoid.Value.kForward;
    private IntegerConfigFileParameter forwardCycleConfig = new IntegerConfigFileParameter(this.getClass().getName(), "forwardCycles", 15);
    private IntegerConfigFileParameter backwardCycleConfig = new IntegerConfigFileParameter(this.getClass().getName(), "backwardCycles", 15);
    private int backwardCycles;
    private int forwardCycles;
    private int cycle;
    private boolean goingForward = false, goingBack = false;
    private boolean upLimitSwitchValue = false, downLimitSwitchValue = false;
    private boolean kickerButtonPressed = false;
    private boolean kickerValue;
    private DoubleSolenoid.Value liftValue;
    private int disks = 0;

    public WsHopper(String name) {
        super(name);
        init();

        Subject subject = WsInputFacade.getInstance().getOiInput(WsInputFacade.MANIPULATOR_JOYSTICK).getSubject(WsManipulatorJoystickButtonEnum.BUTTON6);
        subject.attach(this);

        subject = WsInputFacade.getInstance().getOiInput(WsInputFacade.MANIPULATOR_JOYSTICK).getSubject(WsManipulatorJoystickButtonEnum.BUTTON8);
        subject.attach(this);

        subject = WsInputFacade.getInstance().getSensorInput(WsInputFacade.HOPPER_DOWN_LIMIT_SWITCH).getSubject((ISubjectEnum) null);
        subject.attach(this);

        subject = WsInputFacade.getInstance().getSensorInput(WsInputFacade.HOPPER_UP_LIMIT_SWITCH).getSubject((ISubjectEnum) null);
        subject.attach(this);
    }

    public void init() {
        forwardCycles = forwardCycleConfig.getValue();
        backwardCycles = backwardCycleConfig.getValue();
        kickerValue = KICKER_DEFAULT_VALUE;
        liftValue = LIFT_DEFAULT_VALUE;
        cycle = 0;
        kickerButtonPressed = false;
        disks = 0;
    }

    public void update() {
        if (goingForward) {
            cycle++;
            if (cycle >= forwardCycles) {
                goingForward = false;
                goingBack = true;
                kickerValue = false;
                cycle = 0;
            }
        } else if (goingBack) {
            cycle++;
            if (cycle >= backwardCycles) {
                goingBack = false;
                cycle = 0;
                if (kickerButtonPressed && this.isUpLimitSwitchTriggered()) {
                    goingForward = true;
                    kickerValue = true;
                    if(disks > 0)
                    {
                        disks--;
                    }
                }
            }
        }
        
        //If the funnelator limit switch is pressed, leave the hopper down
        WsIntake intakeSubsystem = (WsIntake) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_INTAKE);
        boolean funnelatorLimitSwitch = intakeSubsystem.getFunnelatorLimitSwitch();
        if (true == funnelatorLimitSwitch) {
            liftValue = DoubleSolenoid.Value.kReverse;
        }
        
        WsOutputFacade.getInstance().getOutput(WsOutputFacade.KICKER).set((IOutputEnum) null, new Boolean(kickerValue));
        WsOutputFacade.getInstance().getOutput(WsOutputFacade.LIFT).set((IOutputEnum) null, new Integer(liftValue.value));

        SmartDashboard.putBoolean("Kicker value", kickerValue);
        SmartDashboard.putNumber("Lift Value", liftValue.value);
        SmartDashboard.putBoolean("KickerReady", goingForward || goingBack);
        SmartDashboard.putBoolean("Up limit switch", upLimitSwitchValue);
        SmartDashboard.putBoolean("Down limit switch", downLimitSwitchValue);
    }

    public void notifyConfigChange() {
    }

    public boolean getLiftValueEquals(DoubleSolenoid.Value value) {
        return liftValue.equals(value);
    }

    public boolean getKickerValue() {
        return kickerValue;
    }

    public void acceptNotification(Subject subjectThatCaused) {
        BooleanSubject button = (BooleanSubject) subjectThatCaused;

        if (subjectThatCaused.getType() == WsManipulatorJoystickButtonEnum.BUTTON6) {
            kickerButtonPressed = button.getValue();
            if (button.getValue()) {
                if (!goingForward && !goingBack && (this.isUpLimitSwitchTriggered() ||
                    ((WsIntake) WsSubsystemContainer.getInstance().getSubsystem(
                        WsSubsystemContainer.WS_INTAKE)).getFingerDownOverrideButtonState()))
                {
                    goingForward = true;
                    cycle = 0;
                    kickerValue = true;
                    if(disks > 0)
                    {
                        disks--;
                    }
                }
            }
        } else if (subjectThatCaused.getType() == WsManipulatorJoystickButtonEnum.BUTTON8) {
            if (button.getValue() == true && (button.getPreviousValue() == false)) {
                if (liftValue == DoubleSolenoid.Value.kReverse) {
                    liftValue = DoubleSolenoid.Value.kForward;
                } else {
                    liftValue = DoubleSolenoid.Value.kReverse;
                }
            }
        } else if (subjectThatCaused.equals(WsInputFacade.getInstance().
                getSensorInput(WsInputFacade.HOPPER_DOWN_LIMIT_SWITCH).
                getSubject((ISubjectEnum) null))) {
            downLimitSwitchValue = ((BooleanSubject) WsInputFacade.getInstance()
                    .getSensorInput(WsInputFacade.HOPPER_DOWN_LIMIT_SWITCH)
                    .getSubject(((ISubjectEnum) null))).getValue();
        } else if (subjectThatCaused.equals(WsInputFacade.getInstance().
                getSensorInput(WsInputFacade.HOPPER_UP_LIMIT_SWITCH).
                getSubject((ISubjectEnum) null))) {
            upLimitSwitchValue = ((BooleanSubject) WsInputFacade.getInstance()
                    .getSensorInput(WsInputFacade.HOPPER_UP_LIMIT_SWITCH)
                    .getSubject(((ISubjectEnum) null))).getValue();
        }
    }

    public DoubleSolenoid.Value getLiftState() {
        return liftValue;
    }

    public boolean isDownLimitSwitchTriggered() {
        return downLimitSwitchValue;
    }

    public boolean isUpLimitSwitchTriggered() {
        return upLimitSwitchValue;
    }

    public boolean isHopperUp() {
        return (liftValue == DoubleSolenoid.Value.kForward);
    }
    
    public void addDisk()
    {
        disks++;
    }
    
    public int getDisks()
    {
        return disks;
    }
}
