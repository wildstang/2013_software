/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.simulation.hopper;

import com.wildstangs.inputfacade.base.WsInputFacade;
import com.wildstangs.outputfacade.base.IOutputEnum;
import com.wildstangs.outputfacade.base.WsOutputFacade;
import com.wildstangs.simulation.WsDigitalInputContainer;
import com.wildstangs.subjects.base.ISubjectEnum;
import com.wildstangs.subsystems.WsHopper;
import com.wildstangs.subsystems.base.WsSubsystemContainer;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;

/**
 *
 * @author chadschmidt
 */
public class HopperLimitSwitches {

    private static int UP_LIMIT_SWITCH_CHANNEL = 12-1;
    private static int DOWN_LIMIT_SWITCH_CHANNEL = 13-1;
    public HopperLimitSwitches() {
    }
    
    public void update(){
        
        //Get the lift value 
        DoubleSolenoid.Value solValue = ((DoubleSolenoid.Value) ((WsHopper) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_HOPPER)).get_LiftState()); 
        
        //Forward is up
        if (solValue == DoubleSolenoid.Value.kForward){
            //Set the limit switches based on that value
            WsDigitalInputContainer.getInstance().inputs[UP_LIMIT_SWITCH_CHANNEL].set(true);
            WsDigitalInputContainer.getInstance().inputs[DOWN_LIMIT_SWITCH_CHANNEL].set(false);
            
        } else { 
            //Set the limit switches based on that value
            WsDigitalInputContainer.getInstance().inputs[UP_LIMIT_SWITCH_CHANNEL].set(false);
            WsDigitalInputContainer.getInstance().inputs[DOWN_LIMIT_SWITCH_CHANNEL].set(true);
            
        }
        
    }
}
