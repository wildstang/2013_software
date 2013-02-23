
package com.wildstangs.simulation.encoders;

import com.wildstangs.logger.Logger;
import com.wildstangs.outputfacade.base.IOutputEnum;
import com.wildstangs.outputfacade.base.WsOutputFacade;
import com.wildstangs.subsystems.WsDriveBase;
import com.wildstangs.subsystems.base.WsSubsystemContainer;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;


public class DriveBaseEncoders {
    private double desired_left_drive_speed = 0.0;
    private double desired_right_drive_speed = 0.0;
    private double actual_left_drive_speed = 0.0;
    private double actual_right_drive_speed = 0.0;
    private double previousTime = 0.0;
    
    //Set top speed to 19.2 ft/ second = 230.4 inches / second = 4.608 inches/ 20 ms 
    private static final double MAX_SPEED_INCHES_HIGHGEAR = 4.608; 
    //Set low gear top speed to 8.5 ft/ second = 102 inches / second = 2.04 inches/ 20 ms 
    private static final double MAX_SPEED_INCHES_LOWGEAR = 2.04; 
    private static final double MAX_SPEED_INCHES = MAX_SPEED_INCHES_LOWGEAR; 
    private static final double WHEEL_DIAMETER = 6.0; 
    private static final double GEAR_RATIO = 7.5; 
    private static final double ENCODER_TICKS_PER_INCH = ((360.0*GEAR_RATIO)/(WHEEL_DIAMETER*Math.PI)); 
    
    //Following are inches per 20 ms
    private static final double VICTOR_BRAKES_EFFECT_DECELERATION = ((50.0/2000)*20);; 
    //Say it takes two seconds to get to top speed
    private static final double ACCELERATION_PER_FRAME = 600.0; 

    private static final boolean doModelWithCappedAcceleration = false; 
        DoubleGraph actual_left_speed;
        DoubleGraph actual_right_speed;
        DoubleGraph left_distance;
        DoubleGraph right_distance ;
        DoubleGraph measuredVelocity ;
        DoubleGraph measuredAccel ;
        
    public DriveBaseEncoders() {
        
        desired_left_drive_speed = 0.0;
        desired_right_drive_speed = 0.0;
        actual_left_drive_speed = 0.0;
        actual_right_drive_speed = 0.0;
        
        actual_left_speed = new DoubleGraph("Left Velocity", 820,0); 
        actual_right_speed = new DoubleGraph("Right Velocity", 1130,0); 
        left_distance = new DoubleGraph("Left Distance", 820,400); 
        right_distance = new DoubleGraph("Right Distance", 1130,400); 
        measuredVelocity = new DoubleGraph("Measured Velocity", 1430,0); 
        measuredAccel = new DoubleGraph("Measured Accel", 1430,400); 
        
        //Create graphs for 
    }
    public void update (){ 
        desired_left_drive_speed = ((Double) WsOutputFacade.getInstance().getOutput(WsOutputFacade.LEFT_DRIVE_SPEED).get((IOutputEnum) null));
        desired_right_drive_speed = ((Double) WsOutputFacade.getInstance().getOutput(WsOutputFacade.RIGHT_DRIVE_SPEED).get((IOutputEnum) null));

        //Convert victor -1 to 1 to -2.04 inches per 20 ms to 2.04
        desired_left_drive_speed *= MAX_SPEED_INCHES;
        desired_right_drive_speed *= MAX_SPEED_INCHES;
        
        if (doModelWithCappedAcceleration){
            
            double diff = desired_left_drive_speed - actual_left_drive_speed; 
            //Handle brakes
            if ((desired_left_drive_speed > -0.01) && (desired_left_drive_speed < 0.01)){
                if (actual_left_drive_speed > 0.0){
                    actual_left_drive_speed -= VICTOR_BRAKES_EFFECT_DECELERATION; 
                    if (actual_left_drive_speed < 0){
                        actual_left_drive_speed = 0 ; 
                    }
                } else { 
                    actual_left_drive_speed += VICTOR_BRAKES_EFFECT_DECELERATION; 
                    if (actual_left_drive_speed > 0){
                        actual_left_drive_speed = 0 ; 
                    }
                }
            }
            else if (diff > 0.05  ){
                actual_left_drive_speed += ACCELERATION_PER_FRAME; 
                if (actual_left_drive_speed > MAX_SPEED_INCHES){
                    actual_left_drive_speed = MAX_SPEED_INCHES; 
                }
            } else if (diff < -0.05  ){
                actual_left_drive_speed -= ACCELERATION_PER_FRAME; 
                if (actual_left_drive_speed < (-1*MAX_SPEED_INCHES)){
                    actual_left_drive_speed = (-1*MAX_SPEED_INCHES); 
                }
            } else {
            }

            //Handle Right side
            double diffRight = desired_right_drive_speed - actual_right_drive_speed; 
            //Handle brakes
            if ((desired_right_drive_speed > -0.01) && (desired_right_drive_speed < 0.01)){
                if (actual_right_drive_speed > 0.0){
                    actual_right_drive_speed -= VICTOR_BRAKES_EFFECT_DECELERATION; 
                    if (actual_right_drive_speed < 0){
                        actual_right_drive_speed = 0 ; 
                    }
                } else { 
                    actual_right_drive_speed += VICTOR_BRAKES_EFFECT_DECELERATION; 
                    if (actual_right_drive_speed > 0){
                        actual_right_drive_speed = 0 ; 
                    }
                }
            }
            else if (diffRight > 0.05  ){
                actual_right_drive_speed += ACCELERATION_PER_FRAME;
                if (actual_right_drive_speed > MAX_SPEED_INCHES){
                    actual_right_drive_speed = MAX_SPEED_INCHES; 
                }
            } else if (diffRight < -0.05  ){
                actual_right_drive_speed -= ACCELERATION_PER_FRAME; 
                if (actual_right_drive_speed< (-1*MAX_SPEED_INCHES)){
                    actual_right_drive_speed = (-1*MAX_SPEED_INCHES); 
                }
            } else {
            }

        } else { 
            //Perfect controller matches desired speed
            actual_right_drive_speed = desired_right_drive_speed; 
            actual_left_drive_speed = desired_left_drive_speed; 

        }
        
        
        int left_encoder = ((Encoder) ((WsDriveBase) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_DRIVE_BASE)).getLeftEncoder()).get(); 
        int right_encoder = ((Encoder) ((WsDriveBase) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_DRIVE_BASE)).getRightEncoder()).get(); 
        //Compensate for variable period
        int left_encoder_increment = 0 ; 
        int right_encoder_increment = 0; 
        double currTime = Timer.getFPGATimestamp(); 
        double deltaTime = currTime - previousTime; 
        if (Math.abs(actual_right_drive_speed) > 0.001){
            right_encoder_increment = (int)Math.ceil((actual_right_drive_speed *ENCODER_TICKS_PER_INCH * deltaTime) / 0.020);
            right_encoder+= right_encoder_increment;
        }
        if (Math.abs(actual_left_drive_speed) > 0.001){
            left_encoder_increment = (int)Math.ceil((actual_left_drive_speed *ENCODER_TICKS_PER_INCH * deltaTime) / 0.020);
            left_encoder+= left_encoder_increment;
        }
        previousTime = currTime; 
        
        ((Encoder) ((WsDriveBase) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_DRIVE_BASE)).getLeftEncoder()).set(left_encoder);
        ((Encoder) ((WsDriveBase) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_DRIVE_BASE)).getRightEncoder()).set(right_encoder);

        if (Math.abs(actual_left_drive_speed) > 0.01){
            Logger.getLogger().debug(this.getClass().getName(), "KinematicsSimulation", "lDS: " + actual_left_drive_speed + " rDS: " + actual_right_drive_speed + " dle: " + left_encoder_increment + " dre: " + right_encoder_increment);
        }

        actual_left_speed.updateWithValue(actual_left_drive_speed, MAX_SPEED_INCHES); 
        actual_right_speed.updateWithValue(actual_right_drive_speed, MAX_SPEED_INCHES); 
        measuredVelocity.updateWithValue(((WsDriveBase) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_DRIVE_BASE)).getVelocity(), ((MAX_SPEED_INCHES * 50)) );
        measuredAccel.updateWithValue(((WsDriveBase) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_DRIVE_BASE)).getAcceleration(), ((ACCELERATION_PER_FRAME *50*50)));
        
        left_distance.updateWithValue(((WsDriveBase) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_DRIVE_BASE)).getLeftDistance(), 100); 
        right_distance.updateWithValue(((WsDriveBase) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_DRIVE_BASE)).getRightDistance(), 100);
        
        
    }
    
    

}
