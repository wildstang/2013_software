/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.inputfacade.inputs.joystick.driver;

import com.wildstangs.config.DoubleConfigFileParameter;
import com.wildstangs.inputfacade.base.IInput;
import com.wildstangs.inputfacade.base.IInputEnum;
import com.wildstangs.inputfacade.inputs.joystick.IHardwareJoystick;
import com.wildstangs.inputfacade.inputs.joystick.IJoystick;
import com.wildstangs.subjects.base.BooleanSubject;
import com.wildstangs.subjects.base.DoubleSubject;
import com.wildstangs.subjects.base.ISubjectEnum;
import com.wildstangs.subjects.base.Subject;
import edu.wpi.first.wpilibj.Joystick;

/**
 *
 * @author Nathan
 */
public class WsDriverJoystick implements IInput {

    DoubleSubject throttle;
    DoubleSubject heading;
    final static int numberOfButtons = 12;
    BooleanSubject[] buttons;
    Joystick driverJoystick = null;

    public Subject getSubject(ISubjectEnum subjectEnum) {
        if (subjectEnum == WsDriverJoystickEnum.THROTTLE) {
            return throttle;
        } else if (subjectEnum == WsDriverJoystickEnum.HEADING) {
            return heading;
        } else if (subjectEnum instanceof WsDriverJoystickButtonEnum) {
            return buttons[((WsDriverJoystickButtonEnum) subjectEnum).toValue()];
        } else {
            System.out.println("Subject not supported or incorrect.");
            return null;
        }
    }

    public WsDriverJoystick() {
        throttle = new DoubleSubject("Throttle");
        heading = new DoubleSubject("Heading");
        driverJoystick = (Joystick) new Joystick(1);
        buttons = new BooleanSubject[numberOfButtons];
        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new BooleanSubject(WsDriverJoystickButtonEnum.getEnumFromIndex(i));
        }

    }

    public void set(IInputEnum key, Object value) {
        if (key == WsDriverJoystickEnum.THROTTLE) {
            throttle.setValue(value);
            // this serves no purpose but an example
        } else if (key == WsDriverJoystickEnum.HEADING) {
            heading.setValue(value);
        } else if (key instanceof WsDriverJoystickButtonEnum) {
            buttons[((WsDriverJoystickButtonEnum) key).toValue()].setValue(value);
        } else {
            System.out.println("key not supported or incorrect.");
        }

    }

    public Object get(IInputEnum key) {
        if (key == WsDriverJoystickEnum.THROTTLE) {
            return throttle.getValueAsObject();
        } else if (key == WsDriverJoystickEnum.HEADING) {
            return heading.getValueAsObject();
        } else if (key instanceof WsDriverJoystickButtonEnum) {
            return buttons[((WsDriverJoystickButtonEnum) key).toValue()].getValueAsObject();
        } else {
            return new Double(-100);
        }
    }

    public void update() {
        throttle.updateValue();
        heading.updateValue();
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].updateValue();
        }
    }

    public void pullData() {
        if (driverJoystick instanceof IHardwareJoystick) {
            ((IHardwareJoystick) driverJoystick).pullData();
        }
        throttle.setValue(driverJoystick.getY() * -1);
        heading.setValue(driverJoystick.getZ() * -1);
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setValue(driverJoystick.getRawButton(i + 1));
        }

    }

    public void notifyConfigChange() {
    }
}
