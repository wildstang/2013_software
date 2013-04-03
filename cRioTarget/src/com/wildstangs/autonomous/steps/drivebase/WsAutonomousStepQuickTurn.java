/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wildstangs.autonomous.steps.drivebase;

import com.wildstangs.autonomous.WsAutonomousStep;
import com.wildstangs.inputfacade.base.WsInputFacade;
import com.wildstangs.inputfacade.inputs.joystick.driver.WsDriverJoystickEnum;
import com.wildstangs.subsystems.WsDriveBase;
import com.wildstangs.subsystems.base.WsSubsystemContainer;

/**
 *
 * @author Joey
 */
public class WsAutonomousStepQuickTurn extends WsAutonomousStep 
{
    private double value, angle;
    public WsAutonomousStepQuickTurn(double relativeAngle)
    {
        this.value = relativeAngle;
    }

    public void initialize()
    {
        
        angle = ((WsDriveBase) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_DRIVE_BASE)).getGyroAngle() + value;
        ((WsDriveBase) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_DRIVE_BASE)).setThrottleValue(0);
        ((WsDriveBase) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_DRIVE_BASE)).setHeadingValue(value < 0 ? 0.8 : -0.8);
        
        WsInputFacade.getInstance().getOiInput(WsInputFacade.DRIVER_JOYSTICK).set(WsDriverJoystickEnum.THROTTLE, new Double(0.0));
        WsInputFacade.getInstance().getOiInput(WsInputFacade.DRIVER_JOYSTICK).set(WsDriverJoystickEnum.HEADING, new Double(value < 0 ? 0.8 : -0.8));
 
    }

    public void update()
    {
        double gyroAngle = ((WsDriveBase) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_DRIVE_BASE)).getGyroAngle();
        if(value < 0)
        {
            if(angle > gyroAngle)
            {
                ((WsDriveBase) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_DRIVE_BASE)).setHeadingValue(0.0);
                WsInputFacade.getInstance().getOiInput(WsInputFacade.DRIVER_JOYSTICK).set(WsDriverJoystickEnum.HEADING, new Double(0.0));
                finished = true;
            }
        }
        else
        {
            if(angle < gyroAngle)
            {
                ((WsDriveBase) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_DRIVE_BASE)).setHeadingValue(0.0);
                WsInputFacade.getInstance().getOiInput(WsInputFacade.DRIVER_JOYSTICK).set(WsDriverJoystickEnum.HEADING, new Double(0.0));
                finished = true;
            }
        }
    }

    public String toString()
    {
        return "Turning using quickturn with a relative angle";
    }
}
