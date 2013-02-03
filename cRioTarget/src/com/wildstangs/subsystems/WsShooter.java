package com.wildstangs.subsystems;

import com.wildstangs.config.DoubleConfigFileParameter;
import com.wildstangs.inputfacade.base.IInputEnum;
import com.wildstangs.inputfacade.base.WsInputFacade;
import com.wildstangs.inputfacade.inputs.driverstation.WsDSAnalogInput;
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
import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class WsShooter extends WsSubsystem implements IObserver{

    private Encoder encoderEnter, encoderExit;
    private DoubleConfigFileParameter lowerWheelSpeed = new DoubleConfigFileParameter(
        this.getClass().getName(), "LowerWheelSpeed", 0);
    private DoubleConfigFileParameter lowerVictorSpeed = new DoubleConfigFileParameter(
        this.getClass().getName(), "LowerVictorSpeed", 0);
    private DoubleConfigFileParameter lowerWheelEnterTestSpeed = new DoubleConfigFileParameter(
        this.getClass().getName(), "LowerWheelEnterTestSpeed", 0);
    private DoubleConfigFileParameter upperWheelEnterTestSpeed = new DoubleConfigFileParameter(
        this.getClass().getName(), "UpperWheelEnterTestSpeed", 6000);
    private DoubleConfigFileParameter LowerWheelExitTestSpeed = new DoubleConfigFileParameter(
            this.getClass().getName(), "LowerWheelExitTestSpeed", 0);
    private DoubleConfigFileParameter upperWheelExitTestSpeed = new DoubleConfigFileParameter(
            this.getClass().getName(), "UpperWheelExitTestSpeed", 6000);
    private double wheelEnterSetPoint = 0;
    private double wheelExitSetPoint = 0;
    private double previousTime = 0;
    private double lowWheelSpeed, lowVictorSpeed;
    private double lowWheelEnterTestSpeed, highWheelEnterTestSpeed, lowWheelExitTestSpeed, highWheelExitTestSpeed;
    private double testingEnterKnob = 0.0;
    private double testingExitKnob = 0.0;
    private boolean testingCalled = false;
    private boolean angleFlag = false; //could be true, not sure yet
    public WsShooter (String name) 
    {
        super(name);
        encoderEnter = new Encoder(1, 2, false, CounterBase.EncodingType.k2X);
        encoderExit = new Encoder(3, 4, false, CounterBase.EncodingType.k2X);
        
        //Implement this later for testing
        //Subject subject = WsInputFacade.getInstance().getOiInput(WsInputFacade.SHOOTER_SPEED_INPUT).getSubject(null);
        //subject.attach(this);
        Subject subject = WsInputFacade.getInstance().getOiInput(WsInputFacade.MANIPULATOR_JOYSTICK).getSubject(WsManipulatorJoystickButtonEnum.BUTTON6);
        subject.attach(this);
        subject = WsInputFacade.getInstance().getOiInput(WsInputFacade.MANIPULATOR_JOYSTICK).getSubject(WsManipulatorJoystickButtonEnum.BUTTON5);
        subject.attach(this);
        subject = WsInputFacade.getInstance().getOiInput(WsInputFacade.MANIPULATOR_JOYSTICK).getSubject(WsManipulatorJoystickButtonEnum.BUTTON7);
        subject.attach(this);
        subject = WsInputFacade.getInstance().getOiInput(WsInputFacade.MANIPULATOR_JOYSTICK).getSubject(WsManipulatorJoystickButtonEnum.BUTTON8);
        subject.attach(this);
        subject = WsInputFacade.getInstance().getOiInput(WsInputFacade.MANIPULATOR_JOYSTICK).getSubject(WsManipulatorJoystickButtonEnum.BUTTON9);
        subject.attach(this);
        subject = WsInputFacade.getInstance().getOiInput(WsInputFacade.ENTER_WHEEL_SHOOTER_SPEED_INPUT).getSubject((ISubjectEnum)null); 
        subject.attach(this);
        subject = WsInputFacade.getInstance().getOiInput(WsInputFacade.EXIT_WHEEL_SHOOTER_SPEED_INPUT).getSubject((ISubjectEnum)null); 
        subject.attach(this);
        
        lowWheelSpeed = lowerWheelSpeed.getValue();
        lowVictorSpeed = lowerVictorSpeed.getValue();
        
        lowWheelEnterTestSpeed = lowerWheelEnterTestSpeed.getValue();
        lowWheelExitTestSpeed = LowerWheelExitTestSpeed.getValue();
        
        highWheelEnterTestSpeed = upperWheelEnterTestSpeed.getValue();
        highWheelExitTestSpeed = upperWheelExitTestSpeed.getValue();
    }

    public void init()
    {
        encoderEnter.start();
        encoderExit.start();
    }
    
    public void update() 
    {
        double enterKnobValue = ((DoubleSubject)WsInputFacade.getInstance()
                .getOiInput(WsInputFacade.ENTER_WHEEL_SHOOTER_SPEED_INPUT)
                .getSubject((ISubjectEnum)null)).getValue();
        
        if(enterKnobValue > 3.3) enterKnobValue = 3.3;
        //((currentValue x (maxSetPoint - minSetPoint)) / maxValue) + minSetPoint = wantedSetPoint
        wheelEnterSetPoint = ((enterKnobValue * (highWheelEnterTestSpeed - lowWheelEnterTestSpeed)) / 3.3)
                + lowWheelEnterTestSpeed;
        System.out.println("Knob: " + enterKnobValue);
        
        double exitKnobValue = ((DoubleSubject)WsInputFacade.getInstance()
                .getOiInput(WsInputFacade.EXIT_WHEEL_SHOOTER_SPEED_INPUT)
                .getSubject((ISubjectEnum)null)).getValue();
        
        if(exitKnobValue > 3.3) exitKnobValue = 3.3;
        //((currentValue x (maxSetPoint - minSetPoint)) / maxValue) + minSetPoint = wantedSetPoint
        wheelExitSetPoint = ((enterKnobValue * (highWheelExitTestSpeed - lowWheelExitTestSpeed)) / 3.3)
                + lowWheelExitTestSpeed;
        
        double newTime = System.currentTimeMillis();
        double speedEnter = (60000.0 / 50.0/*Replace with cycles per revolution*/) * encoderEnter.get() / (newTime - previousTime);
        double speedExit = (60000.0 / 50.0/*Replace with cycles per revolution*/) * encoderExit.get() / (newTime - previousTime);
        
        previousTime = newTime;
        
        WsVictor victorEnter = (WsVictor) WsOutputFacade.getInstance().getOutput(WsOutputFacade.SHOOTER_VICTOR_ENTER);
        WsVictor victorExit = (WsVictor) WsOutputFacade.getInstance().getOutput(WsOutputFacade.SHOOTER_VICTOR_EXIT);
        
        if(speedEnter < lowWheelSpeed)
        {
            victorEnter.set(null, Double.valueOf(lowVictorSpeed));
            
        }
        else if(speedEnter < wheelEnterSetPoint)
        {
            victorEnter.set(null, Double.valueOf(1.0));
        }
        else
        {
            victorEnter.set(null, Double.valueOf(0.0));
        }
        
        if(speedExit < lowWheelSpeed)
        {
            victorExit.set((IOutputEnum) null, Double.valueOf(lowVictorSpeed));
        }
        if(speedExit < wheelExitSetPoint)
        {
            victorExit.set(null, Double.valueOf(1.0));
        }
        else
        {
            victorExit.set(null, Double.valueOf(0.0));
        }
        
        SmartDashboard.putNumber("EnterWheelSetPoint", wheelEnterSetPoint);
        SmartDashboard.putNumber("ExitWheelSetPoint", wheelExitSetPoint);
        //set shooter angle
        WsOutputFacade.getInstance().getOutput(WsOutputFacade.SHOOTER_ANGLE).set(null, (angleFlag ? Boolean.TRUE : Boolean.FALSE)); 

    }

    public void notifyConfigChange() 
    {
        lowWheelSpeed = lowerWheelSpeed.getValue();
        lowVictorSpeed = lowerVictorSpeed.getValue();
        
        lowWheelEnterTestSpeed = lowerWheelEnterTestSpeed.getValue();
        lowWheelExitTestSpeed = LowerWheelExitTestSpeed.getValue();
        
        highWheelEnterTestSpeed = upperWheelEnterTestSpeed.getValue();
        highWheelExitTestSpeed = upperWheelExitTestSpeed.getValue();
    }
    
    public void setWheelEnterSetPoint(int setPoint)
    {
        wheelEnterSetPoint = setPoint;
    }
    
    public void setWheelExitSetPoint(int setPoint)
    {
        wheelExitSetPoint = setPoint;
    }

    public void acceptNotification(Subject subjectThatCaused) 
    {
        if (subjectThatCaused.getType() == WsManipulatorJoystickButtonEnum.BUTTON6) 
        {
            if(((BooleanSubject)subjectThatCaused).getValue() == true)
            {
                angleFlag = !angleFlag;
            }
        }
        if (subjectThatCaused.getType() == WsManipulatorJoystickButtonEnum.BUTTON5) 
        {
            if(((BooleanSubject)subjectThatCaused).getValue() == true)
            {
                testingEnterKnob += .1;
                if(testingEnterKnob > 3.3) testingEnterKnob = 3.3;
                ((WsDSAnalogInput)WsInputFacade.getInstance().getOiInput(WsInputFacade.ENTER_WHEEL_SHOOTER_SPEED_INPUT))
                        .set((IInputEnum)null,Double.valueOf(testingEnterKnob));
            }
        }
        if (subjectThatCaused.getType() == WsManipulatorJoystickButtonEnum.BUTTON7) 
        {
            if(((BooleanSubject)subjectThatCaused).getValue() == true)
            {
                testingExitKnob += .1;
                if(testingExitKnob > 3.3) testingExitKnob = 3.3;
                ((WsDSAnalogInput)WsInputFacade.getInstance().getOiInput(WsInputFacade.EXIT_WHEEL_SHOOTER_SPEED_INPUT))
                        .set((IInputEnum)null,Double.valueOf(testingExitKnob));
            }
        }
        if (subjectThatCaused.getType() == WsManipulatorJoystickButtonEnum.BUTTON8) 
        {
            if(((BooleanSubject)subjectThatCaused).getValue() == true)
            {
                testingEnterKnob -= .1;
                if(testingEnterKnob < 0.0) testingEnterKnob = 0.0;
                ((WsDSAnalogInput)WsInputFacade.getInstance().getOiInput(WsInputFacade.ENTER_WHEEL_SHOOTER_SPEED_INPUT))
                        .set((IInputEnum)null, Double.valueOf(testingEnterKnob));
            }
        }
        if (subjectThatCaused.getType() == WsManipulatorJoystickButtonEnum.BUTTON9) 
        {
            if(((BooleanSubject)subjectThatCaused).getValue() == true)
            {
                testingExitKnob -= .1;
                if(testingExitKnob < 0.0) testingExitKnob = 0.0;
                ((WsDSAnalogInput)WsInputFacade.getInstance().getOiInput(WsInputFacade.EXIT_WHEEL_SHOOTER_SPEED_INPUT))
                        .set((IInputEnum)null,Double.valueOf(testingExitKnob));
            }
        }
    }
    
     public Encoder getEnterEncoder() {
        return encoderEnter;
    }

    public Encoder getExitEncoder() {
        return encoderExit;
    }

    public Encoder getEnterEncoderValue() {
        return encoderEnter;
    }

    public Encoder getExitEncoderValue() {
        return encoderExit;
    }
    
     public void resetEnterEncoder() {
        encoderEnter.reset();
        encoderEnter.start();
    }

    public void resetExitEncoder() {
        encoderExit.reset();
        encoderExit.start();
    }
    
    public double getKnobEnterValue()
    {
        return testingEnterKnob;
    }
    
    public double getKnobExitValue()
    {
        return testingExitKnob;
    }
}