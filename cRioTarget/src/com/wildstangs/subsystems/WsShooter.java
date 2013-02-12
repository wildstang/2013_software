package com.wildstangs.subsystems;

import com.wildstangs.config.BooleanConfigFileParameter;
import com.wildstangs.config.DoubleConfigFileParameter;
import com.wildstangs.inputfacade.base.WsInputFacade;
import com.wildstangs.inputfacade.inputs.joystick.manipulator.WsManipulatorJoystickButtonEnum;
import com.wildstangs.outputfacade.base.IOutputEnum;
import com.wildstangs.outputfacade.base.WsOutputFacade;
import com.wildstangs.outputfacade.outputs.WsVictor;
import com.wildstangs.subjects.base.BooleanSubject;
import com.wildstangs.subjects.base.DoubleSubject;
import com.wildstangs.subjects.base.IObserver;
import com.wildstangs.subjects.base.ISubjectEnum;
import com.wildstangs.subjects.base.Subject;
import com.wildstangs.subsystems.base.WsSubsystem;
import com.wildstangs.subsystems.base.WsSubsystemContainer;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class WsShooter extends WsSubsystem implements IObserver {

    public static class Preset {

        public final int ENTER_WHEEL_SET_POINT;
        public final int EXIT_WHEEL_SET_POINT;
        public final DoubleSolenoid.Value ANGLE;

        public Preset(int enterWheelSetPoint, int exitWheelSetPoint, DoubleSolenoid.Value angle) {
            this.ENTER_WHEEL_SET_POINT = enterWheelSetPoint;
            this.EXIT_WHEEL_SET_POINT = exitWheelSetPoint;
            this.ANGLE = angle;
        }
    }
    
    private Counter counterEnter, counterExit;
    private DoubleConfigFileParameter lowerWheelSpeed = new DoubleConfigFileParameter(
            this.getClass().getName(), "LowerWheelSpeed", 0);
    private DoubleConfigFileParameter lowerVictorSpeed = new DoubleConfigFileParameter(
            this.getClass().getName(), "LowerVictorSpeed", 0);
    private DoubleConfigFileParameter lowerWheelEnterTestSpeed = new DoubleConfigFileParameter(
            this.getClass().getName(), "LowerWheelEnterTestSpeed", 0);
    private DoubleConfigFileParameter upperWheelEnterTestSpeed = new DoubleConfigFileParameter(
            this.getClass().getName(), "UpperWheelEnterTestSpeed", 9000);
    private DoubleConfigFileParameter LowerWheelExitTestSpeed = new DoubleConfigFileParameter(
            this.getClass().getName(), "LowerWheelExitTestSpeed", 0);
    private DoubleConfigFileParameter upperWheelExitTestSpeed = new DoubleConfigFileParameter(
            this.getClass().getName(), "UpperWheelExitTestSpeed", 9000);
    private DoubleConfigFileParameter atSpeedToleranceConfig = new DoubleConfigFileParameter(
            this.getClass().getName(), "AtSpeedTolerance", .05);
    private double wheelEnterSetPoint = 0;
    private double wheelExitSetPoint = 0;
    private double previousTime = 0;
    private double lowWheelSpeed, lowVictorSpeed;
    private double lowWheelEnterTestSpeed, highWheelEnterTestSpeed, lowWheelExitTestSpeed, highWheelExitTestSpeed;
    private double testingEnterKnob = 0.0;
    private double testingExitKnob = 0.0;
    private double atSpeedTolerance;
    private boolean testingCalled = false;
    private boolean atSpeed = false;
    private DoubleSolenoid.Value angleFlag = DoubleSolenoid.Value.kReverse;

    public WsShooter(String name) {
        super(name);
        BooleanConfigFileParameter outputsFor2012 = new BooleanConfigFileParameter(WsOutputFacade.getInstance().getClass().getName(), "2012_Robot", false);
        if(outputsFor2012.getValue())
        {
            counterEnter = new Counter(9);
            counterExit = new Counter(10);
        }
        else
        {
            counterEnter = new Counter(10);
            counterExit = new Counter(11);
        }
        
        
        //Implement this later for testing
        //Subject subject = WsInputFacade.getInstance().getOiInput(WsInputFacade.SHOOTER_SPEED_INPUT).getSubject(null);
        //subject.attach(this);
        Subject subject = WsInputFacade.getInstance().getOiInput(WsInputFacade.MANIPULATOR_JOYSTICK).getSubject(WsManipulatorJoystickButtonEnum.BUTTON2);
        subject.attach(this);
        subject = WsInputFacade.getInstance().getOiInput(WsInputFacade.MANIPULATOR_JOYSTICK).getSubject(WsManipulatorJoystickButtonEnum.BUTTON4);
        subject.attach(this);
        subject = WsInputFacade.getInstance().getOiInput(WsInputFacade.MANIPULATOR_JOYSTICK).getSubject(WsManipulatorJoystickButtonEnum.BUTTON5);
        subject.attach(this);
        subject = WsInputFacade.getInstance().getOiInput(WsInputFacade.MANIPULATOR_JOYSTICK).getSubject(WsManipulatorJoystickButtonEnum.BUTTON7);
        subject.attach(this);
        subject = WsInputFacade.getInstance().getOiInput(WsInputFacade.MANIPULATOR_JOYSTICK).getSubject(WsManipulatorJoystickButtonEnum.BUTTON3);
        subject.attach(this);
        subject = WsInputFacade.getInstance().getOiInput(WsInputFacade.MANIPULATOR_JOYSTICK).getSubject(WsManipulatorJoystickButtonEnum.BUTTON8);
        subject.attach(this);
        subject = WsInputFacade.getInstance().getOiInput(WsInputFacade.ENTER_WHEEL_SHOOTER_SPEED_INPUT).getSubject((ISubjectEnum) null);
        subject.attach(this);
        subject = WsInputFacade.getInstance().getOiInput(WsInputFacade.EXIT_WHEEL_SHOOTER_SPEED_INPUT).getSubject((ISubjectEnum) null);
        subject.attach(this);

        lowWheelSpeed = lowerWheelSpeed.getValue();
        lowVictorSpeed = lowerVictorSpeed.getValue();

        lowWheelEnterTestSpeed = lowerWheelEnterTestSpeed.getValue();
        lowWheelExitTestSpeed = LowerWheelExitTestSpeed.getValue();

        highWheelEnterTestSpeed = upperWheelEnterTestSpeed.getValue();
        highWheelExitTestSpeed = upperWheelExitTestSpeed.getValue();

        atSpeedTolerance = atSpeedToleranceConfig.getValue();
    }

    public void init()
    {
        resetEnterCounter();
        resetExitCounter();
        wheelEnterSetPoint = 0; 
        wheelExitSetPoint = 0 ; 
        angleFlag = DoubleSolenoid.Value.kReverse; 
        WsOutputFacade.getInstance().getOutput(WsOutputFacade.SHOOTER_ANGLE).set(null, new Integer(angleFlag.value));
    }
    
    public void update() 
    {
         double enterKnobValue = ((DoubleSubject)WsInputFacade.getInstance()
               .getOiInput(WsInputFacade.ENTER_WHEEL_SHOOTER_SPEED_INPUT)
               .getSubject((ISubjectEnum)null)).getValue();

         if(enterKnobValue > 3.3) enterKnobValue = 3.3;
         //((currentValue x (maxSetPoint - minSetPoint)) / maxValue) + minSetPoint = wantedSetPoint
         double knobEnterSetPoint = ((enterKnobValue * (highWheelEnterTestSpeed - lowWheelEnterTestSpeed)) / 3.3)
                    + lowWheelEnterTestSpeed;
         
         double exitKnobValue = ((DoubleSubject)WsInputFacade.getInstance()
                    .getOiInput(WsInputFacade.EXIT_WHEEL_SHOOTER_SPEED_INPUT)
                    .getSubject((ISubjectEnum)null)).getValue();

          if(exitKnobValue > 3.3) exitKnobValue = 3.3;
          //((currentValue x (maxSetPoint - minSetPoint)) / maxValue) + minSetPoint = wantedSetPoint
          double knobExitSetPoint = ((enterKnobValue * (highWheelExitTestSpeed - lowWheelExitTestSpeed)) / 3.3)
                    + lowWheelExitTestSpeed;
         
        if(((BooleanSubject)WsInputFacade.getInstance()
                .getOiInput(WsInputFacade.SHOOTER_WHEEL_SPEED_OVERRIDE)
                .getSubject((ISubjectEnum)null)).getValue())
        {
            wheelEnterSetPoint = knobEnterSetPoint;

            wheelExitSetPoint = knobExitSetPoint;
        }
        int enterCounterCount = counterEnter.get();
        int exitCounterCount = counterExit.get();
        double newTime = Timer.getFPGATimestamp();
        double speedEnter = (60.0 / (128 * 3)) * counterEnter.get() / (newTime - previousTime);
        double speedExit = (60.0 / (128 * 3)) * counterExit.get() / (newTime - previousTime);
        
        this.resetEnterCounter();
        this.resetExitCounter();
        
        previousTime = newTime;

        WsVictor victorEnter = (WsVictor) WsOutputFacade.getInstance().getOutput(WsOutputFacade.SHOOTER_VICTOR_ENTER);
        WsVictor victorExit = (WsVictor) WsOutputFacade.getInstance().getOutput(WsOutputFacade.SHOOTER_VICTOR_EXIT);

        if (((speedEnter < lowWheelSpeed) && (speedEnter < wheelEnterSetPoint))) {
            victorEnter.set(null, Double.valueOf(lowVictorSpeed));

        } else if (speedEnter < wheelEnterSetPoint) {
            victorEnter.set(null, Double.valueOf(1.0));
        } else {
            victorEnter.set(null, Double.valueOf(0.0));
        }

        if (((speedExit < lowWheelSpeed) && (speedExit < wheelExitSetPoint))) {
            victorExit.set((IOutputEnum) null, Double.valueOf(lowVictorSpeed));
        }
        else if (speedExit < wheelExitSetPoint) {
            victorExit.set(null, Double.valueOf(1.0));
        } else {
            victorExit.set(null, Double.valueOf(0.0));
        }
        if(((WsHopper) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_HOPPER))
            .getKickerValue() == true)
        {
            victorExit.set(null, Double.valueOf(1.0));
            victorEnter.set(null, Double.valueOf(1.0));
        }
        if(((WsHopper) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_HOPPER))
                .isUpLimitSwitchTriggered() == false)
        {
            victorExit.set(null, Double.valueOf(0.0));
            victorEnter.set(null, Double.valueOf(0.0));
        }
        if (speedExit < wheelExitSetPoint + (wheelExitSetPoint * atSpeedTolerance)
                && speedExit > wheelExitSetPoint - (wheelExitSetPoint * atSpeedTolerance)
                && speedEnter < wheelEnterSetPoint + (wheelEnterSetPoint * atSpeedTolerance)
                && speedEnter > wheelEnterSetPoint - (wheelEnterSetPoint * atSpeedTolerance)) {
            atSpeed = true;
        } else {
            atSpeed = false;
        }
        
        SmartDashboard.putNumber("EnterWheelSpeed", speedEnter);
        SmartDashboard.putNumber("ExitWheelSpeed", speedExit);
        SmartDashboard.putNumber("EnterCounter", enterCounterCount);
        SmartDashboard.putNumber("ExitCounter", exitCounterCount);
        SmartDashboard.putNumber("ExitWheelVictor", ((Double) victorExit.get((IOutputEnum)null)).doubleValue());
        SmartDashboard.putNumber("EnterWheelVictor", ((Double) victorEnter.get((IOutputEnum)null)).doubleValue());
        SmartDashboard.putNumber("EnterWheelSetPoint", wheelEnterSetPoint);
        SmartDashboard.putNumber("ExitWheelSetPoint", wheelExitSetPoint);
        SmartDashboard.putNumber("KnobEnterSetPoint", knobEnterSetPoint);
        SmartDashboard.putNumber("KnobExitSetPoint", knobExitSetPoint);
        //set shooter angle
        WsOutputFacade.getInstance().getOutput(WsOutputFacade.SHOOTER_ANGLE).set(null, new Integer(angleFlag.value));
    }

    public void notifyConfigChange() {
        lowWheelSpeed = lowerWheelSpeed.getValue();
        lowVictorSpeed = lowerVictorSpeed.getValue();

        lowWheelEnterTestSpeed = lowerWheelEnterTestSpeed.getValue();
        lowWheelExitTestSpeed = LowerWheelExitTestSpeed.getValue();

        highWheelEnterTestSpeed = upperWheelEnterTestSpeed.getValue();
        highWheelExitTestSpeed = upperWheelExitTestSpeed.getValue();

        atSpeedTolerance = atSpeedToleranceConfig.getValue();
    }

    public void setWheelEnterSetPoint(int setPoint) {
        wheelEnterSetPoint = setPoint;
    }

    public void setWheelExitSetPoint(int setPoint) {
        wheelExitSetPoint = setPoint;
    }

    public void acceptNotification(Subject subjectThatCaused) {
        if (subjectThatCaused.getType() == WsManipulatorJoystickButtonEnum.BUTTON2) {
            if (((BooleanSubject) subjectThatCaused).getValue() == true) {
                if (angleFlag == DoubleSolenoid.Value.kReverse) {
                    angleFlag = DoubleSolenoid.Value.kForward;
                } else {
                    angleFlag = DoubleSolenoid.Value.kReverse;
                }
            }
        }
        if (subjectThatCaused.getType() == WsManipulatorJoystickButtonEnum.BUTTON4) {
            if (((BooleanSubject) subjectThatCaused).getValue() == true) {
                wheelEnterSetPoint += 100;
                wheelExitSetPoint += 100;
            }
        }
        if (subjectThatCaused.getType() == WsManipulatorJoystickButtonEnum.BUTTON3) {
            if (((BooleanSubject) subjectThatCaused).getValue() == true) {
                wheelEnterSetPoint -= 100;
                wheelExitSetPoint -= 100;
            }
        }
    }
    
     public Counter getEnterCounter() {
        return counterEnter;
    }

    public Counter getExitCounter() {
        return counterExit;
    }

    public Counter getEnterCounterValue() {
        return counterEnter;
    }

    public Counter getExitCounterValue() {
        return counterExit;
    }
    
     public void resetEnterCounter() {
        counterEnter.reset();
        counterEnter.start();
    }

    public void resetExitCounter() {
        counterExit.reset();
        counterExit.start();
    }

    public double getKnobEnterValue() {
        return testingEnterKnob;
    }

    public double getKnobExitValue() {
        return testingExitKnob;
    }

    public void setPresetState(Preset preset) {
        this.wheelEnterSetPoint = preset.ENTER_WHEEL_SET_POINT;
        this.wheelExitSetPoint = preset.EXIT_WHEEL_SET_POINT;
        this.angleFlag = preset.ANGLE;
    }

    public boolean isAtSpeed() {
        return atSpeed;
    }
}
