package com.wildstangs.pid.inputs;

import com.wildstangs.pid.inputs.base.IPidInput;
import com.wildstangs.subsystems.WsDriveBase;
import com.wildstangs.subsystems.base.WsSubsystemContainer;

/**
 *
 * @author Nathan
 */
public class WsDriveBasePidRightInput implements IPidInput {
    
    public WsDriveBasePidRightInput() {
        //Nothing to do here
    }

    public double pidRead() {
        double encoder_value;
        encoder_value = ((WsDriveBase) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_DRIVE_BASE)).getRightDistance();
        return encoder_value;
    }
    
}
