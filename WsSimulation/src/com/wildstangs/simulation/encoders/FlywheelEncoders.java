/* ****************************************************************************
 *
 * Module Name     : DriveBaseEncoders.java
 *
 * Copyright © 2010, Incredible Technologies, Inc.  All Rights Reserved
 *
 * Author          : ChadS
 *
 * Creation Date   : Feb 15, 2013
 *
 * Status          : Not Submitted
 *
 * Submission ID   : SID.xx.yy
 *
 * RESERVED        :
 *
 * Purpose         : 
 *
 * Edit History    : YOUR_INITIALS Date: 0-00-0000, SID.xx.yy, DESCRIPTION_OF_WHAT_CHANGED
 *
 * ****************************************************************************/

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wildstangs.simulation.encoders;

import com.wildstangs.outputfacade.base.IOutputEnum;
import com.wildstangs.outputfacade.base.WsOutputFacade;
import com.wildstangs.subsystems.WsShooter;
import com.wildstangs.subsystems.base.WsSubsystemContainer;
import edu.wpi.first.wpilibj.Counter;


public class FlywheelEncoders {
    private int exit_wheel_encoder = 0;
    private int enter_wheel_encoder = 0;
    private double enter_wheel_speed = 0.0;
    private double exit_wheel_speed = 0.0;
    
    private static int AMOUNT_TO_CHANGE = 50; 

    public FlywheelEncoders() {
        
        exit_wheel_encoder = 0;
        enter_wheel_encoder = 0;
        enter_wheel_speed = 0.0;
        exit_wheel_speed = 0.0;
    }
    public void update (){ 
        enter_wheel_speed = ((Double) WsOutputFacade.getInstance().getOutput(WsOutputFacade.SHOOTER_VICTOR_ENTER).get((IOutputEnum) null));
        exit_wheel_speed = ((Double) WsOutputFacade.getInstance().getOutput(WsOutputFacade.SHOOTER_VICTOR_EXIT).get((IOutputEnum) null));

        
        
        //Use instanteous speed to update encoders
        if (enter_wheel_speed > 0) {
            enter_wheel_encoder+= AMOUNT_TO_CHANGE;
        } else {
            enter_wheel_encoder-= AMOUNT_TO_CHANGE;
            if (enter_wheel_encoder < 0){
                enter_wheel_encoder = 0 ;
            }
        }
        if (exit_wheel_speed > 0) {
            exit_wheel_encoder+= AMOUNT_TO_CHANGE;
        } else {
            exit_wheel_encoder-= AMOUNT_TO_CHANGE;
            if (exit_wheel_encoder < 0){
                exit_wheel_encoder = 0 ;
            }
        }
        
        ((Counter) ((WsShooter) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_SHOOTER)).getEnterCounter()).set(enter_wheel_encoder);
        ((Counter) ((WsShooter) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_SHOOTER)).getExitCounter()).set(exit_wheel_encoder);
    }
    
    

}
