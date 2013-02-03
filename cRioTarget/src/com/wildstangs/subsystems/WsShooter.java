package com.wildstangs.subsystems;

import com.wildstangs.config.DoubleConfigFileParameter;
import com.wildstangs.inputfacade.base.WsInputFacade;
import com.wildstangs.inputfacade.inputs.joystick.manipulator.WsManipulatorJoystickButtonEnum;
import com.wildstangs.outputfacade.base.IOutputEnum;
import com.wildstangs.outputfacade.base.WsOutputFacade;
import com.wildstangs.outputfacade.outputs.WsVictor;
import com.wildstangs.subjects.base.BooleanSubject;
import com.wildstangs.subjects.base.IObserver;
import com.wildstangs.subjects.base.ISubjectEnum;
import com.wildstangs.subjects.base.Subject;
import com.wildstangs.subsystems.base.WsSubsystem;
import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;

public class WsShooter extends WsSubsystem implements IObserver{

    
    private Encoder encoderEnter, encoderExit;
    private DoubleConfigFileParameter lowerWheelSpeed = new DoubleConfigFileParameter(
            this.getClass().getName(), "LowerWheelSpeed", 0);
    private DoubleConfigFileParameter lowerVictorSpeed = new DoubleConfigFileParameter(
            this.getClass().getName(), "LowerVictorSpeed", 0);
    private double wheelEnterSetPoint = 0;
    private double wheelExitSetPoint = 0;
    private double previousTime = 0;
    private double lowWheelSpeed, lowVictorSpeed;
    private boolean angleFlag = false; //could be true, not sure yet
    public WsShooter (String name) 
    {
        super(name);
        encoderEnter = new Encoder(10, 11, false, CounterBase.EncodingType.k2X);
        encoderExit = new Encoder(12, 13, false, CounterBase.EncodingType.k2X);
        
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
    }

    public void init()
    {
        encoderEnter.start();
        encoderExit.start();
    }
    
    public void update() 
    {
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
       System.out.println("Enter wheel set point: " + wheelEnterSetPoint);
        System.out.println("Exit wheel set point: " + wheelExitSetPoint);
        //set shooter angle
        WsOutputFacade.getInstance().getOutput(WsOutputFacade.SHOOTER_ANGLE).set(null, (angleFlag ? Boolean.TRUE : Boolean.FALSE)); 

    }

    public void notifyConfigChange() 
    {
        lowWheelSpeed = lowerWheelSpeed.getValue();
        lowVictorSpeed = lowerVictorSpeed.getValue();
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
                wheelEnterSetPoint += 250;
            }
        }
        if (subjectThatCaused.getType() == WsManipulatorJoystickButtonEnum.BUTTON7) 
        {
            if(((BooleanSubject)subjectThatCaused).getValue() == true)
            {
                wheelExitSetPoint += 250;
            }
        }
        if (subjectThatCaused.getType() == WsManipulatorJoystickButtonEnum.BUTTON8) 
        {
            if(((BooleanSubject)subjectThatCaused).getValue() == true)
            {
                wheelEnterSetPoint -= 250;
            }
        }
        if (subjectThatCaused.getType() == WsManipulatorJoystickButtonEnum.BUTTON9) 
        {
            if(((BooleanSubject)subjectThatCaused).getValue() == true)
            {
                wheelExitSetPoint -= 250;
            }
        }
        if (subjectThatCaused.getType() == WsInputFacade.getInstance().getOiInput(WsInputFacade.ENTER_WHEEL_SHOOTER_SPEED_INPUT).getSubject((ISubjectEnum)null))
        {
            
        }
        if (subjectThatCaused.getType() == WsInputFacade.getInstance().getOiInput(WsInputFacade.EXIT_WHEEL_SHOOTER_SPEED_INPUT).getSubject((ISubjectEnum)null))
        {
            
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

}