/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wildstangs.autonomous.steps.drivebase;

import com.wildstangs.autonomous.WsAutonomousStep;
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
        ((WsDriveBase) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_DRIVE_BASE)).setHeadingValue(value < 0 ? -1.0 : 1.0);
    }

    public void update()
    {
        double gyroAngle = ((WsDriveBase) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_DRIVE_BASE)).getGyroAngle();
        if(value < 0)
        {
            if(angle > gyroAngle)
            {
                finished = true;
            }
        }
        else
        {
            if(angle < gyroAngle)
            {
                finished = true;
            }
        }
    }

    public String toString()
    {
        return "Turning using quickturn with a relative angle";
    }
}
