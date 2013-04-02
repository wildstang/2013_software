/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.simulation;

import com.wildstangs.outputfacade.base.WsOutputFacade;

/**
 *
 * @author chadschmidt
 */
public class AccumulatorLimitSwitch {

    private static int UP_LIMIT_SWITCH_CHANNEL = 8;
    public AccumulatorLimitSwitch() {
    }
    
    public void update(){
        
        //Get the solenoid value 
        Boolean solState = ((Boolean)WsOutputFacade.getInstance().getOutput(WsOutputFacade.ACCUMULATOR_SOLENOID).get(null)); 
        
        //Forward is up
        if (false== solState){
            //Set the limit switches based on that value
            WsDigitalInputContainer.getInstance().inputs[UP_LIMIT_SWITCH_CHANNEL].set(true);
            
        } else { 
            //Set the limit switches based on that value
            WsDigitalInputContainer.getInstance().inputs[UP_LIMIT_SWITCH_CHANNEL].set(false);
            
        }
        
    }
}
