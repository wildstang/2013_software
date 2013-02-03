/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.steps.input;

import com.wildstangs.autonomous.WsAutonomousStep;
import com.wildstangs.inputfacade.base.WsInputFacade;
import com.wildstangs.inputfacade.inputs.joystick.manipulator.WsManipulatorJoystickButtonEnum;
import com.wildstangs.subjects.base.BooleanSubject;
import com.wildstangs.subjects.base.Subject;
import com.wildstangs.subsystems.WsHopper;
import com.wildstangs.subsystems.base.WsSubsystemContainer;
import edu.wpi.first.wpilibj.DoubleSolenoid;

/**
 *
 * @author Batman
 */
public class WsAutonomousStepRaiseHopper extends WsAutonomousStep
{

    public void initialize() 
    {
        WsHopper subsystem = (WsHopper)(WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_HOPPER));
        Subject subject = WsInputFacade.getInstance().getOiInput(WsInputFacade.MANIPULATOR_JOYSTICK).getSubject(WsManipulatorJoystickButtonEnum.BUTTON3);
        BooleanSubject button = (BooleanSubject)subject;
        
        if(subsystem.getLiftValueEquals(DoubleSolenoid.Value.kReverse))
            button.setValue(true);
        finished = true;
    }

    public void update() 
    {
    }

    public String toString() 
    {
        return "Raising the hopper to use the kicker.";
    }
    
}
