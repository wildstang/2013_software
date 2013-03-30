package com.wildstangs.subsystems;

import com.wildstangs.config.DoubleConfigFileParameter;
import com.wildstangs.config.IntegerConfigFileParameter;
import com.wildstangs.inputfacade.base.WsInputFacade;
import com.wildstangs.inputfacade.inputs.joystick.manipulator.WsManipulatorJoystickButtonEnum;
import com.wildstangs.outputfacade.base.IOutputEnum;
import com.wildstangs.outputfacade.base.WsOutputFacade;
import com.wildstangs.outputfacade.outputs.WsServo;
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
    private DoubleConfigFileParameter servoUpValue = new DoubleConfigFileParameter(
            this.getClass().getName(), "AngleUp", 75);
    private DoubleConfigFileParameter servoDownValue = new DoubleConfigFileParameter(
            this.getClass().getName(), "AngleDown", 0);
    private boolean servoUp = true;
    private double upValue;
    private double downValue;

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
        
        subject = WsInputFacade.getInstance().getOiInput(WsInputFacade.MANIPULATOR_JOYSTICK).getSubject(WsManipulatorJoystickButtonEnum.BUTTON1);
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
        upValue = servoUpValue.getValue();
        downValue = servoDownValue.getValue();
        servoUp = true;
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
                if (kickerButtonPressed && (this.isUpLimitSwitchTriggered()||
                    ((WsIntake) WsSubsystemContainer.getInstance().getSubsystem(
                        WsSubsystemContainer.WS_INTAKE)).getFingerDownOverrideButtonState())) {
                    goingForward = true;
                    kickerValue = true;
                    if(disks > 0)
                    {
                        disks--;
                    }
                }
            }
        }
        
        
        WsOutputFacade.getInstance().getOutput(WsOutputFacade.KICKER).set((IOutputEnum) null, new Boolean(kickerValue));
        ((WsServo) WsOutputFacade.getInstance().getOutput(WsOutputFacade.FRISBEE_HOLDER_SERVO))
                .setAngle((IOutputEnum) null, new Double(servoUp ? upValue : downValue));
        WsOutputFacade.getInstance().getOutput(WsOutputFacade.LIFT).set((IOutputEnum) null, new Integer(liftValue.value));

        SmartDashboard.putBoolean("Kicker value", kickerValue);
        SmartDashboard.putNumber("Lift Value", liftValue.value);
        SmartDashboard.putBoolean("KickerReady", goingForward || goingBack);
        SmartDashboard.putBoolean("Up limit switch", upLimitSwitchValue);
        SmartDashboard.putBoolean("Down limit switch", downLimitSwitchValue);
        SmartDashboard.putBoolean("Frisbee Servo Position", servoUp);
        SmartDashboard.putNumber("Num disks", disks);
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
                    
//                    WsIntake intakeSubsystem = (WsIntake) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_INTAKE);
//                    boolean funnelatorLimitSwitch = intakeSubsystem.getFunnelatorLimitSwitch();
//                //Only allow the hopper to go up if the funnelator switch is NOT tripped (prevent jam-ups in autonomous)
//                    if (false == funnelatorLimitSwitch) {
                        liftValue = DoubleSolenoid.Value.kForward;
                        //Also lift the frisbee holder
                        servoUp = true;
//                    }
                    
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
        else if(subjectThatCaused == WsInputFacade.getInstance().getOiInput(WsInputFacade.MANIPULATOR_JOYSTICK).
                getSubject(WsManipulatorJoystickButtonEnum.BUTTON1))
        {
            if (button.getValue() == true && (button.getPreviousValue() == false)) {
                servoUp = !servoUp;
            }
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
    
    public boolean isFrisbeeHolderUp() {
        return servoUp;
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
