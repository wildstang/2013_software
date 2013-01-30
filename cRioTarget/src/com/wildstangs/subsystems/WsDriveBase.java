/*
 * Example drive base for the simulation env.
 */
package com.wildstangs.subsystems;

import com.wildstangs.inputfacade.base.WsInputFacade;
import com.wildstangs.inputfacade.inputs.joystick.driver.WsDriverJoystickButtonEnum;
import com.wildstangs.inputfacade.inputs.joystick.driver.WsDriverJoystickEnum;
import com.wildstangs.outputfacade.base.IOutputEnum;
import com.wildstangs.outputfacade.base.WsOutputFacade;
import com.wildstangs.subjects.base.BooleanSubject;
import com.wildstangs.subjects.base.IObserver;
import com.wildstangs.subjects.base.Subject;
import com.wildstangs.subsystems.base.WsSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author Smitty
 */
public class WsDriveBase extends WsSubsystem implements IObserver {

    private static final double MAX_INPUT_THROTTLE_VALUE = 1.0;
    private static final double MAX_NEG_INPUT_THROTTLE_VALUE = -1.0;
    private static final double MAX_INPUT_HEADING_VALUE = 1.0;
    private static final double MAX_NEG_INPUT_HEADING_VALUE = -1.0;
    private static final double WS_HEADING_SENSITIVITY = 1.8;
    private static final double WS_MAX_MOTOR_OUTPUT = 1.0;
    private static final double WS_NEG_MAX_MOTOR_OUTPUT = -1.0;
    private static final double WS_ANTI_TURBO_MAX_DEFLECTION = 0.500;
    private static final double WS_THROTTLE_ACCEL_FACTOR = 0.250;
    private static final double WS_HEADING_ACCEL_FACTOR = 0.500;
    private static double driveBaseThrottleValue = 0.0;
    private static double driveBaseHeadingValue = 0.0;
    private static boolean antiTurboFlag = false;
    private static boolean shifterFlag = false;
    private static boolean quickTurnFlag = false;

    public WsDriveBase(String name) {
        super(name);
        init();
        
        Subject subject = WsInputFacade.getInstance().getOiInput(WsInputFacade.DRIVER_JOYSTICK).getSubject(WsDriverJoystickEnum.HEADING);
        subject.attach(this);
        subject = WsInputFacade.getInstance().getOiInput(WsInputFacade.DRIVER_JOYSTICK).getSubject(WsDriverJoystickEnum.THROTTLE);
        subject.attach(this);
        //Anti-Turbo button
        subject = WsInputFacade.getInstance().getOiInput(WsInputFacade.DRIVER_JOYSTICK).getSubject(WsDriverJoystickButtonEnum.BUTTON7);
        subject.attach(this);
        //Shifter Button
        subject = WsInputFacade.getInstance().getOiInput(WsInputFacade.DRIVER_JOYSTICK).getSubject(WsDriverJoystickButtonEnum.BUTTON6);
        subject.attach(this);
    }
    
    public void init()
    {
        driveBaseThrottleValue = 0.0;
        driveBaseHeadingValue = 0.0;
        antiTurboFlag = false;
        shifterFlag = false;
        quickTurnFlag = false;
    }

    public void update() {

        //Get the inputs for heading and throttle
        //Set headign and throttle values
        double throttleValue = 0.0;
        double headingValue = 0.0;

        throttleValue = ((Double) ((WsInputFacade.getInstance().getOiInput(WsInputFacade.DRIVER_JOYSTICK))).get(WsDriverJoystickEnum.THROTTLE)).doubleValue();
        headingValue = ((Double) ((WsInputFacade.getInstance().getOiInput(WsInputFacade.DRIVER_JOYSTICK))).get(WsDriverJoystickEnum.HEADING)).doubleValue();

        setThrottleValue(throttleValue);
        setHeadingValue(headingValue);

        //Use updated values to update the quickTurnFlag

        //Set the drive motor outputs
        updateDriveMotors();

        //Set landing gear output
        SmartDashboard.putNumber("Throttle Value", throttleValue);
        SmartDashboard.putNumber("Heading Value", headingValue);
        SmartDashboard.putBoolean("Shifter State", shifterFlag);
        SmartDashboard.putBoolean("Anti-Turbo Flag", antiTurboFlag);
        
        //Set gear shift output
        WsOutputFacade.getInstance().getOutput(WsOutputFacade.SHIFTER).set(null, (shifterFlag ? Boolean.TRUE : Boolean.FALSE)); 
    }

    public void setThrottleValue(double tValue) {

        // Taking into account Anti-Turbo
        double new_throttle = tValue;
        if (true == antiTurboFlag) {
            new_throttle *= WS_ANTI_TURBO_MAX_DEFLECTION;

            if (new_throttle > WS_ANTI_TURBO_MAX_DEFLECTION) {
                new_throttle = WS_ANTI_TURBO_MAX_DEFLECTION;
            }
            if (new_throttle < -WS_ANTI_TURBO_MAX_DEFLECTION) {
                new_throttle = -WS_ANTI_TURBO_MAX_DEFLECTION;
            }
        }

        //Setting throttle
        driveBaseThrottleValue = driveBaseThrottleValue + (new_throttle - driveBaseThrottleValue) * WS_THROTTLE_ACCEL_FACTOR;

        if (driveBaseThrottleValue > MAX_INPUT_THROTTLE_VALUE) {
            driveBaseThrottleValue = MAX_INPUT_THROTTLE_VALUE;
        } else if (driveBaseThrottleValue < MAX_NEG_INPUT_THROTTLE_VALUE) {
            driveBaseThrottleValue = MAX_NEG_INPUT_THROTTLE_VALUE;
        }
    }

    public void setHeadingValue(double hValue) {

        // Taking into account Anti-Turbo
        double new_heading = hValue;
        if (true == antiTurboFlag) {
            new_heading *= WS_ANTI_TURBO_MAX_DEFLECTION;

            if (new_heading > WS_ANTI_TURBO_MAX_DEFLECTION) {
                new_heading = WS_ANTI_TURBO_MAX_DEFLECTION;
            }
            if (new_heading < -WS_ANTI_TURBO_MAX_DEFLECTION) {
                new_heading = -WS_ANTI_TURBO_MAX_DEFLECTION;
            }
        }

        //Setting heading
        driveBaseHeadingValue = driveBaseHeadingValue + (new_heading - driveBaseHeadingValue) * WS_HEADING_ACCEL_FACTOR;

        if (driveBaseHeadingValue > MAX_INPUT_HEADING_VALUE) {
            driveBaseHeadingValue = MAX_INPUT_HEADING_VALUE;
        } else if (driveBaseHeadingValue < MAX_NEG_INPUT_HEADING_VALUE) {
            driveBaseHeadingValue = MAX_NEG_INPUT_HEADING_VALUE;
        }
    }

    public void updateDriveMotors() {
        double rightMotorSpeed = 0;
        double leftMotorSpeed = 0;
        double angularPower = 0.0;
        if (Math.abs(driveBaseHeadingValue) > 0.05) {
            angularPower = Math.abs(driveBaseThrottleValue) * driveBaseHeadingValue * WS_HEADING_SENSITIVITY;
        }

        rightMotorSpeed = driveBaseThrottleValue - angularPower;
        leftMotorSpeed = driveBaseThrottleValue + angularPower;
        if (true == quickTurnFlag)
        {
            rightMotorSpeed = 0.0f;
            leftMotorSpeed = 0.0f;
            driveBaseThrottleValue = 0.0f;

            // Quick turn does not take throttle into account
            leftMotorSpeed += driveBaseHeadingValue;
            rightMotorSpeed -= driveBaseHeadingValue;
        }
        else
        {
            if (driveBaseThrottleValue >= 0) {
                if (rightMotorSpeed < 0) {
                    rightMotorSpeed = 0;
                }
                if (leftMotorSpeed < 0) {
                    leftMotorSpeed = 0;
                }
            } else {
                if (rightMotorSpeed >= 0) {
                    rightMotorSpeed = 0;
                }
                if (leftMotorSpeed >= 0) {
                    leftMotorSpeed = 0;
                }
            }

            if (rightMotorSpeed > WS_MAX_MOTOR_OUTPUT) {
                rightMotorSpeed = WS_MAX_MOTOR_OUTPUT;
            }
            if (leftMotorSpeed > WS_MAX_MOTOR_OUTPUT) {
                leftMotorSpeed = WS_MAX_MOTOR_OUTPUT;
            }
            if (rightMotorSpeed < WS_NEG_MAX_MOTOR_OUTPUT) {
                rightMotorSpeed = WS_NEG_MAX_MOTOR_OUTPUT;
            }
            if (leftMotorSpeed < WS_NEG_MAX_MOTOR_OUTPUT) {
                leftMotorSpeed = WS_NEG_MAX_MOTOR_OUTPUT;
            }
        }

        //Update Output Facade.
        (WsOutputFacade.getInstance().getOutput(WsOutputFacade.LEFT_DRIVE_SPEED)).set((IOutputEnum)null, new Double(leftMotorSpeed));
        (WsOutputFacade.getInstance().getOutput(WsOutputFacade.RIGHT_DRIVE_SPEED)).set((IOutputEnum)null, new Double(rightMotorSpeed));

    }

    public void checkAutoQuickTurn() 
    {
        double throttle = driveBaseThrottleValue;
        double heading = driveBaseHeadingValue;
    
        throttle = Math.abs(throttle);
        heading = Math.abs(heading);
    
        if ((throttle < 0.1) && (heading > 0.1))
        {
            quickTurnFlag = true;
        }
        else
        {
            quickTurnFlag = false;
        }
    }

    public void notifyConfigChange() {
    }

    public void acceptNotification(Subject subjectThatCaused) {
        if (subjectThatCaused.getType() == WsDriverJoystickButtonEnum.BUTTON7) 
        {
            antiTurboFlag = ((BooleanSubject)subjectThatCaused).getValue();
        }
        else if (subjectThatCaused.getType() == WsDriverJoystickButtonEnum.BUTTON6) 
        {
            if(((BooleanSubject)subjectThatCaused).getValue() == true)
            {
                shifterFlag = !shifterFlag;
            }
        }
    }
}
