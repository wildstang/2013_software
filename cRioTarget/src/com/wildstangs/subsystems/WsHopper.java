package com.wildstangs.subsystems;

import com.wildstangs.subsystems.base.WsSubsystem;
import com.wildstangs.inputfacade.base.WsInputFacade;
import com.wildstangs.inputfacade.inputs.joystick.manipulator.WsManipulatorJoystickButtonEnum;
import com.wildstangs.outputfacade.base.IOutputEnum;
import com.wildstangs.outputfacade.base.WsOutputFacade;
import com.wildstangs.subjects.base.Subject;
import com.wildstangs.subjects.base.BooleanSubject;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import com.wildstangs.subjects.base.IObserver;



/**
 *
 * @author Liam Fruzyna
 */

public class WsHopper extends WsSubsystem implements IObserver
{
    private static final boolean kicker_default_value = false;
    private static final DoubleSolenoid.Value lift_default_value = DoubleSolenoid.Value.kReverse;
    private boolean kicker_value;
    private DoubleSolenoid.Value lift_value;
    
    public WsHopper (String name)
    {
        super(name);
        init();
        
        Subject subject = WsInputFacade.getInstance().getOiInput(WsInputFacade.MANIPULATOR_JOYSTICK).getSubject(WsManipulatorJoystickButtonEnum.BUTTON2);
        subject.attach(this);
        
        subject = WsInputFacade.getInstance().getOiInput(WsInputFacade.MANIPULATOR_JOYSTICK).getSubject(WsManipulatorJoystickButtonEnum.BUTTON3);
        subject.attach(this);
    }
    
    public void init()
    {
        kicker_value = kicker_default_value;
        lift_value = lift_default_value;
    }

    public void update() 
    {
        WsOutputFacade.getInstance().getOutput(WsOutputFacade.KICKER).set((IOutputEnum)null, new Boolean(kicker_value));
        WsOutputFacade.getInstance().getOutput(WsOutputFacade.LIFT).set((IOutputEnum)null, lift_value);
    }

    public void notifyConfigChange() 
    {
    }

    public void acceptNotification(Subject subjectThatCaused) 
    {
        BooleanSubject button = (BooleanSubject)subjectThatCaused;
        
        if(subjectThatCaused.getType() == WsManipulatorJoystickButtonEnum.BUTTON2)
        {
            kicker_value = button.getValue();
            if(lift_value.value == DoubleSolenoid.Value.kReverse_val)
            {
                kicker_value = false;
            }
        }
        else if(subjectThatCaused.getType() == WsManipulatorJoystickButtonEnum.BUTTON3)
        {
            if(button.getValue() == true && (button.getPreviousValue() == false))
            {
                if(lift_value.value == DoubleSolenoid.Value.kReverse_val)
                {
                    lift_value = DoubleSolenoid.Value.kForward;
                }
                else
                {
                    lift_value = DoubleSolenoid.Value.kReverse;
                }
            }
        }
    }
}