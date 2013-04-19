package com.wildstangs.subsystems;

import com.wildstangs.inputfacade.base.WsInputFacade;
import com.wildstangs.inputfacade.inputs.joystick.driver.WsDriverJoystickButtonEnum;
import com.wildstangs.inputfacade.inputs.joystick.driver.WsDriverJoystickEnum;
import com.wildstangs.outputfacade.base.IOutputEnum;
import com.wildstangs.outputfacade.base.WsOutputFacade;
import com.wildstangs.subjects.base.BooleanSubject;
import com.wildstangs.subjects.base.DoubleSubject;
import com.wildstangs.subjects.base.IObserver;
import com.wildstangs.subjects.base.Subject;
import com.wildstangs.subsystems.base.WsSubsystem;
import edu.wpi.first.wpilibj.Relay;

/**
 *
 * @author Joey
 */
public class WsLightCannon extends WsSubsystem implements IObserver
{
    private Relay.Value relayState = Relay.Value.kOff;
    
    public WsLightCannon(String name)
    {
        super(name);
        
        Subject subject = WsInputFacade.getInstance().getOiInput(WsInputFacade.DRIVER_JOYSTICK).getSubject(WsDriverJoystickEnum.D_PAD_UP_DOWN);
        subject.attach(this);
    }

    public void acceptNotification(Subject subjectThatCaused)
    {
        double value = ((DoubleSubject)subjectThatCaused).getValue();
        if(value > 0.5 || value < -0.5)
        {
            relayState = Relay.Value.kOn;
        }
        else
        {
            relayState = Relay.Value.kOff;
        }
        WsOutputFacade.getInstance().getOutput(WsOutputFacade.LIGHT_CANNON_RELAY).set((IOutputEnum) null, relayState);
    }
}
