/* ****************************************************************************
 *
 * Module Name     : Joystick.java
 *
 * Copyright 2010, Incredible Technologies, Inc.  All Rights Reserved
 *
 * Author          : ChadS
 *
 * Creation Date   : Nov 5, 2012
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

package edu.wpi.first.wpilibj;

import com.wildstangs.inputfacade.inputs.joystick.IHardwareJoystick;
import com.wildstangs.joystick.OnscreenJoystick;
import com.wildstangs.inputfacade.inputs.joystick.IJoystick;
import com.wildstangs.joystick.WsHardwareJoystick;


public class Joystick implements IJoystick, IHardwareJoystick{
    IJoystick joystick; 
    public Joystick(int channel){ 
        WsHardwareJoystick hardwareJoystick = new WsHardwareJoystick(); 
        if (hardwareJoystick.initializeJoystick()){
            joystick = hardwareJoystick;
        }else { 
            joystick = new OnscreenJoystick(channel);
            
        }

    }

    public boolean getRawButton(int but) {
        return joystick.getRawButton(but);
    }

    public boolean getTrigger() {
        return joystick.getTrigger(); 
    }

    public double getX() {
        return joystick.getX();
    }

    public double getY() {
        return joystick.getY();
    }

    public double getZ() {
        return joystick.getZ();
    }
    
    public double getTwist() {
        return joystick.getTwist();
    }
    
    public double getThrottle() {
        return joystick.getThrottle();
    }

    @Override
    public void pullData() {
        if (joystick instanceof IHardwareJoystick){
            ((IHardwareJoystick)joystick).pullData();
        }
    }

}
