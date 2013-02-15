
package com.wildstangs.simulation.encoders;

import com.wildstangs.outputfacade.base.IOutputEnum;
import com.wildstangs.outputfacade.base.WsOutputFacade;
import com.wildstangs.subsystems.WsDriveBase;
import com.wildstangs.subsystems.base.WsSubsystemContainer;
import edu.wpi.first.wpilibj.Encoder;


public class DriveBaseEncoders {
    private double left_drive_speed = 0.0;
    private double right_drive_speed = 0.0;
    
    //Set top speed to 20 ft/ second = 240 inches / second = 4.8 inches/ 20 ms 
    // 4.8 inches / 7.5 wheel diameter * 360 encoder ticks
    private static final double MAX_SPEED = 230.4; 

    public DriveBaseEncoders() {
        
        left_drive_speed = 0.0;
        right_drive_speed = 0.0;
    }
    public void update (){ 
        left_drive_speed = ((Double) WsOutputFacade.getInstance().getOutput(WsOutputFacade.LEFT_DRIVE_SPEED).get((IOutputEnum) null));
        right_drive_speed = ((Double) WsOutputFacade.getInstance().getOutput(WsOutputFacade.RIGHT_DRIVE_SPEED).get((IOutputEnum) null));

        int left_encoder = ((Encoder) ((WsDriveBase) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_DRIVE_BASE)).getLeftEncoder()).get(); 
        int right_encoder = ((Encoder) ((WsDriveBase) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_DRIVE_BASE)).getRightEncoder()).get(); 
        
        left_encoder+= Math.ceil(left_drive_speed*MAX_SPEED);
        right_encoder+= Math.ceil(right_drive_speed*MAX_SPEED);
        
        ((Encoder) ((WsDriveBase) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_DRIVE_BASE)).getLeftEncoder()).set(left_encoder);
        ((Encoder) ((WsDriveBase) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_DRIVE_BASE)).getRightEncoder()).set(right_encoder);
    }
    
    

}
