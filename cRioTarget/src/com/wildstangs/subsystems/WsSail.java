package com.wildstangs.subsystems;

import com.wildstangs.inputfacade.base.WsInputFacade;
import com.wildstangs.inputfacade.inputs.joystick.driver.WsDriverJoystickButtonEnum;
import com.wildstangs.outputfacade.base.IOutputEnum;
import com.wildstangs.outputfacade.base.WsOutputFacade;
import com.wildstangs.subjects.base.BooleanSubject;
import com.wildstangs.subjects.base.IObserver;
import com.wildstangs.subjects.base.Subject;
import com.wildstangs.subsystems.base.WsSubsystem;

/**
 *
 * @author Joey
 */
public class WsSail extends WsSubsystem implements IObserver
{
    private boolean state = false;
    
    public WsSail(String name)
    {
        super(name);
        
        Subject subject = WsInputFacade.getInstance().getOiInput(WsInputFacade.DRIVER_JOYSTICK).getSubject(WsDriverJoystickButtonEnum.BUTTON9);
        subject.attach(this);
    }
    
    public void update()
    {
        WsOutputFacade.getInstance().getOutput(WsOutputFacade.SAIL_SOLENOID).set((IOutputEnum) null, new Boolean(state));
    }
    
    public void acceptNotification(Subject subjectThatCaused)
    {
        if(((BooleanSubject)subjectThatCaused).getValue() == true)
        {
            state = !state;
        }
    }
}
