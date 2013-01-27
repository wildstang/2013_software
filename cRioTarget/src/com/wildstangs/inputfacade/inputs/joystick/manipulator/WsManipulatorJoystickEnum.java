/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.inputfacade.inputs.joystick.manipulator;

import com.wildstangs.inputfacade.base.IInputEnum;

/**
 *
 * @author Nathan
 */
public class WsManipulatorJoystickEnum implements IInputEnum {
    private int index;
    private String name;

    private WsManipulatorJoystickEnum(int index, String desc) {
        this.index = index;
        this.name = desc;

    }
    public static final WsManipulatorJoystickEnum TURRET_ANGLE = new WsManipulatorJoystickEnum(0, "TURRET_ANGLE");
    public static final WsManipulatorJoystickEnum TURRET_HEADING = new WsManipulatorJoystickEnum(1, "TURRET_HEADING");
    public static final WsManipulatorJoystickEnum D_PAD_UP_DOWN = new WsManipulatorJoystickEnum(2, "D_PAD_UP_DOWN");
    public static final WsManipulatorJoystickEnum D_PAD_LEFT_RIGHT = new WsManipulatorJoystickEnum(3, "D_PAD_LEFT_RIGHT");

    public String toString() {
        return name;
    }

    public int toValue() {
        return index;
    }
}
