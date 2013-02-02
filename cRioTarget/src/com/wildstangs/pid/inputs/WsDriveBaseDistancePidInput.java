package com.wildstangs.pid.inputs;

import com.wildstangs.pid.inputs.base.IPidInput;
import com.wildstangs.subsystems.WsDriveBase;
import com.wildstangs.subsystems.base.WsSubsystemContainer;

/**
 *
 * @author Nathan
 */
public class WsDriveBaseDistancePidInput implements IPidInput {
    
    public WsDriveBaseDistancePidInput() {
        //Nothing to do here
    }

    public double pidRead() {
        double left_encoder_value, right_encoder_value;
        left_encoder_value = ((WsDriveBase) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_DRIVE_BASE)).getLeftDistance();
        right_encoder_value = ((WsDriveBase) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_DRIVE_BASE)).getRightDistance();
        return (left_encoder_value + right_encoder_value) / 2;
    }
    
}
