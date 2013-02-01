package com.wildstangs.subsystems;

import com.wildstangs.config.DoubleConfigFileParameter;
import com.wildstangs.outputfacade.base.IOutputEnum;
import com.wildstangs.outputfacade.base.WsOutputFacade;
import com.wildstangs.outputfacade.outputs.WsVictor;
import com.wildstangs.subjects.base.IObserver;
import com.wildstangs.subjects.base.Subject;
import com.wildstangs.subsystems.base.WsSubsystem;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;

public class WsShooter extends WsSubsystem implements IObserver{

    
    private Encoder encoderEnter, encoderExit;
    private DoubleConfigFileParameter lowerWheelSpeed = new DoubleConfigFileParameter(
            this.getClass().getName(), "LowerWheelSpeed", 0);
    private DoubleConfigFileParameter lowerVictorSpeed = new DoubleConfigFileParameter(
            this.getClass().getName(), "LowerVictorSpeed", 0);
    private double wheelEnterSpeed = 0;
    private double wheelExitSpeed = 0;
    private double previousTime = 0;
    public WsShooter (String name) 
    {
        super(name);
        encoderEnter = new Encoder(1, 1, 2, 2, false);
        encoderExit = new Encoder(3, 3, 4, 4, false);
        
        //Implement this later for testing
        //Subject subject = WsInputFacade.getInstance().getOiInput(WsInputFacade.SHOOTER_SPEED_INPUT).getSubject(null);
        //subject.attach(this);
    }

    public void init()
    {
        encoderEnter.start();
        encoderExit.start();
    }
    
    public void update() 
    {
        double newTime = Timer.getFPGATimestamp();
        double speedEnter = (60.0 / 1.0/*Replace with cycles per revolution*/) * encoderEnter.get() / (newTime - previousTime);
        double speedExit = (60.0 / 1.0/*Replace with cycles per revolution*/) * encoderExit.get() / (newTime - previousTime);
        
        previousTime = newTime;
        
        WsVictor victorEnter = (WsVictor) WsOutputFacade.getInstance().getOutput(WsOutputFacade.SHOOTER_VICTOR_ENTER);
        WsVictor victorExit = (WsVictor) WsOutputFacade.getInstance().getOutput(WsOutputFacade.SHOOTER_VICTOR_EXIT);
        
        if(speedEnter < lowerWheelSpeed.getValue())
        {
            victorEnter.set(null, Double.valueOf(lowerVictorSpeed.getValue()));
        }
        else if(speedEnter < wheelEnterSpeed)
        {
            victorEnter.set(null, Double.valueOf(1.0)); 
        }
        else
        {
            victorEnter.set(null, Double.valueOf(0.0));
        }
        
        if(speedExit < lowerWheelSpeed.getValue())
        {
            victorExit.set((IOutputEnum) null, Double.valueOf(lowerVictorSpeed.getValue()));
        }
        if(speedExit < wheelExitSpeed)
        {
            victorExit.set(null, Double.valueOf(1.0));
        }
        else
        {
            victorExit.set(null, Double.valueOf(0.0));
        }
    }

    public void notifyConfigChange() 
    {
        
    }
    
    public void setWheelEnterSetPoint(int setPoint)
    {
        wheelEnterSpeed = setPoint;
    }
    
    public void setWheelExitSetPoint(int setPoint)
    {
        wheelExitSpeed = setPoint;
    }

    public void acceptNotification(Subject subjectThatCaused) 
    {
        
    }
}