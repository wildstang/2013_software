package com.wildstangs.subsystems;

import com.wildstangs.config.DoubleConfigFileParameter;
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

/**
 *
 * @author Joey
 */
public class WsFrisbeeHolder extends WsSubsystem implements IObserver 
{
    private DoubleConfigFileParameter servoOutValue = new DoubleConfigFileParameter(
            this.getClass().getName(), "ServoOutValue", 75);
    private DoubleConfigFileParameter servoInValue = new DoubleConfigFileParameter(
            this.getClass().getName(), "ServoInValue", 0);
    private boolean outLimitSwitch = false;
    private boolean servoOut = true;
    private double outValue;
    private double inValue;
    
    public WsFrisbeeHolder(String name)
    {
        super(name);
        
        Subject subject = WsInputFacade.getInstance().getOiInput(WsInputFacade.MANIPULATOR_JOYSTICK).getSubject(WsManipulatorJoystickButtonEnum.BUTTON1);
        subject.attach(this);
        
        subject = WsInputFacade.getInstance().getOiInput(WsInputFacade.FRISBEE_HOLDER_OUT_LIMIT_SWITCH).getSubject((ISubjectEnum) null);
        subject.attach(this);
        
        outValue = servoOutValue.getValue();
        inValue = servoInValue.getValue();
    }
    
    public void init()
    {
        servoOut = true;
        outLimitSwitch = false;
        ((WsServo) WsOutputFacade.getInstance().getOutput(WsOutputFacade.FRISBEE_HOLDER_SERVO))
                .setAngle((IOutputEnum) null, new Double(servoOut ? outValue : inValue));
    }
    
    public void notifyConfigChange() 
    {
        outValue = servoOutValue.getValue();
        inValue = servoInValue.getValue();
    }
    
    public void acceptNotification(Subject subjectThatCaused)
    {
        if(subjectThatCaused == WsInputFacade.getInstance().getOiInput(WsInputFacade.MANIPULATOR_JOYSTICK).
                getSubject(WsManipulatorJoystickButtonEnum.BUTTON1))
        {
            servoOut = ((BooleanSubject) subjectThatCaused).getValue();
            ((WsServo) WsOutputFacade.getInstance().getOutput(WsOutputFacade.FRISBEE_HOLDER_SERVO))
                .setAngle((IOutputEnum) null, new Double(servoOut ? outValue : inValue));
        }
        else
        {
            outLimitSwitch = ((BooleanSubject) subjectThatCaused).getValue();
        }
    }
}
