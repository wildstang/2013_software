/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.subsystems;

import com.wildstangs.config.DoubleConfigFileParameter;
import com.wildstangs.inputfacade.base.WsInputFacade;
import com.wildstangs.inputfacade.inputs.joystick.manipulator.WsManipulatorJoystickButtonEnum;
import com.wildstangs.outputfacade.base.WsOutputFacade;
import com.wildstangs.outputfacade.outputs.WsServo;
import com.wildstangs.subjects.base.BooleanSubject;
import com.wildstangs.subjects.base.IObserver;
import com.wildstangs.subjects.base.Subject;
import com.wildstangs.subsystems.base.WsSubsystem;

/**
 *
 * @author User
 */
public class WsLoadingRamp extends WsSubsystem implements IObserver
{
    private Double angle;
    
    private static Double ANGLE_UP = new Double(90.0);
    private static Double ANGLE_DOWN = new Double(60.0);
    private DoubleConfigFileParameter AngleUp = new DoubleConfigFileParameter(
            this.getClass().getName(), "AngleUp", 0);
    private DoubleConfigFileParameter AngleDown = new DoubleConfigFileParameter(
            this.getClass().getName(), "AngleDown", 0);
    
    public WsLoadingRamp(String name)
    {
        super(name);
        
        Subject subject = WsInputFacade.getInstance().getOiInput(WsInputFacade.MANIPULATOR_JOYSTICK).getSubject(WsManipulatorJoystickButtonEnum.BUTTON10);
        subject.attach(this);
        
        init();
        
        ANGLE_UP = new Double(AngleUp.getValue());
        ANGLE_DOWN = new Double(AngleDown.getValue());
    }
    
    public void init()
    {
        angle = ANGLE_UP;
    }
    
    public void update()
    {
        WsServo servo = (WsServo)(WsOutputFacade.getInstance().getOutput(WsOutputFacade.LOADING_RAMP));
        servo.setAngle(null, angle);
    }
    
    public void notifyConfigChange(){
        ANGLE_UP = new Double(AngleUp.getValue());
        ANGLE_DOWN = new Double(AngleDown.getValue());
    }
    
    public void acceptNotification(Subject subjectThatCaused) {
        BooleanSubject button = (BooleanSubject)subjectThatCaused;
        
        if(button.getValue())
        {
            angle = ANGLE_DOWN;
        }
        else
        {
            angle = ANGLE_UP;
        }   
    }
}
