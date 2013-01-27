/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.inputfacade.inputs.joystick.driver;

import com.wildstangs.inputfacade.base.IInputEnum;

/**
 *
 * @author Nathan
 */
public class WsDriverJoystickEnum implements IInputEnum {
    private int index;
  private String name;
 
private WsDriverJoystickEnum(int index, String desc){
    this.index = index; 
    this.name = desc; 
    
} 

/**
 * Throttle enum type.
 */
public static final WsDriverJoystickEnum THROTTLE = new WsDriverJoystickEnum(0, "THROTTLE"); 
/**
 * Heading enum type.
 */
public static final WsDriverJoystickEnum HEADING = new WsDriverJoystickEnum(1, "HEADING"); 


/**
 * Converts the enum type to a String. 
 * 
 * @return A string representing the enum.
 */
    public String toString() {
        return name;
    }
    
/** 
 * Converts the enum type to a numeric value.
 * 
 * @return An integer representing the enum.
 */
    public int toValue() {
        return index;
    }
}
