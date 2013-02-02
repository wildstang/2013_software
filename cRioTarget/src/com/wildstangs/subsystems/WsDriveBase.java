/*
 * Example drive base for the simulation env.
 */
package com.wildstangs.subsystems;

import com.wildstangs.config.DoubleConfigFileParameter;
import com.wildstangs.inputfacade.base.WsInputFacade;
import com.wildstangs.inputfacade.inputs.joystick.driver.WsDriverJoystickButtonEnum;
import com.wildstangs.inputfacade.inputs.joystick.driver.WsDriverJoystickEnum;
import com.wildstangs.outputfacade.base.IOutputEnum;
import com.wildstangs.outputfacade.base.WsOutputFacade;
import com.wildstangs.pid.controller.base.WsPidController;
import com.wildstangs.pid.inputs.WsDriveBaseDistancePidInput;
import com.wildstangs.pid.outputs.WsDriveBaseDistancePidOutput;
import com.wildstangs.subjects.base.BooleanSubject;
import com.wildstangs.subjects.base.IObserver;
import com.wildstangs.subjects.base.Subject;
import com.wildstangs.subsystems.base.WsSubsystem;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Gyro;
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
    private static double TICKS_PER_ROTATION = 360.0;
    //Wheel diameter in inches
    private static double WHEEL_DIAMETER = 4;
    private static double driveBaseThrottleValue = 0.0;
    private static double driveBaseHeadingValue = 0.0;
    private static boolean antiTurboFlag = false;
    private static boolean shifterFlag = false;
    private static boolean quickTurnFlag = false;
    private static Encoder leftDriveEncoder;
    private static Encoder rightDriveEncoder;
    private static Gyro headingGyro;
    private static double leftEncoderValue;
    private static double rightEncoderValue;
    private static WsPidController driveDistancePid;
    private static WsDriveBaseDistancePidInput driveDistancePidInput;
    private static WsDriveBaseDistancePidOutput driveDistancePidOutput;
    private static boolean distancePidEnabled = false;
    private static DoubleConfigFileParameter WHEEL_DIAMETER_config;
    private static DoubleConfigFileParameter TICKS_PER_ROTATION_config;    

    public WsDriveBase(String name) {
        super(name);
        
        WHEEL_DIAMETER_config = new DoubleConfigFileParameter(this.getClass().getName(), "wheel_diameter", 4.0);
        TICKS_PER_ROTATION_config = new DoubleConfigFileParameter(this.getClass().getName(), "ticks_per_roation", 360.0);

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

        //Initialize the drive base encoders
        //@TODO: change the channels to the correct one
        //leftDriveEncoder = new Encoder(1, 2, true, CounterBase.EncodingType.k4X);
        leftDriveEncoder = new Encoder(2, 3, true, EncodingType.k4X);
        leftDriveEncoder.reset();
        leftDriveEncoder.start();
        //rightDriveEncoder = new Encoder(3, 4, false, CounterBase.EncodingType.k4X);
        rightDriveEncoder = new Encoder(4, 5, false, EncodingType.k4X);
        rightDriveEncoder.reset();
        rightDriveEncoder.start();

        //Initialize the gyro
        //headingGyro = new Gyro(1);

        //Initialize the PIDs
        driveDistancePidInput = new WsDriveBaseDistancePidInput();
        driveDistancePidOutput = new WsDriveBaseDistancePidOutput();
        driveDistancePid = new WsPidController(driveDistancePidInput, driveDistancePidOutput, "WsDriveBaseDistancePID");
    }

    public void update() {
        if (false == distancePidEnabled) {

            //Get the inputs for heading and throttle
            //Set headign and throttle values
            double throttleValue = 0.0;
            double headingValue = 0.0;

            throttleValue = ((Double) ((WsInputFacade.getInstance().getOiInput(WsInputFacade.DRIVER_JOYSTICK))).get(WsDriverJoystickEnum.THROTTLE)).doubleValue();
            headingValue = ((Double) ((WsInputFacade.getInstance().getOiInput(WsInputFacade.DRIVER_JOYSTICK))).get(WsDriverJoystickEnum.HEADING)).doubleValue();

            setThrottleValue(throttleValue);
            setHeadingValue(headingValue);

            //Use updated values to update the quickTurnFlag
            checkAutoQuickTurn();

            //Set the drive motor outputs
            updateDriveMotors();

            //Set landing gear output
            SmartDashboard.putNumber("Throttle Value", throttleValue);
            SmartDashboard.putNumber("Heading Value", headingValue);
            SmartDashboard.putBoolean("Shifter State", shifterFlag);
            SmartDashboard.putBoolean("Anti-Turbo Flag", antiTurboFlag);

            //Set gear shift output
            WsOutputFacade.getInstance().getOutput(WsOutputFacade.SHIFTER).set(null, (shifterFlag ? Boolean.TRUE : Boolean.FALSE));
        } else {
            driveDistancePid.Enable();
            driveDistancePid.calcPid();
        }
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
        if (false == distancePidEnabled) {
            double rightMotorSpeed = 0;
            double leftMotorSpeed = 0;
            double angularPower = 0.0;
            if (Math.abs(driveBaseHeadingValue) > 0.05) {
                angularPower = Math.abs(driveBaseThrottleValue) * driveBaseHeadingValue * WS_HEADING_SENSITIVITY;
            }

            rightMotorSpeed = driveBaseThrottleValue - angularPower;
            leftMotorSpeed = driveBaseThrottleValue + angularPower;
            if (true == quickTurnFlag) {
                rightMotorSpeed = 0.0f;
                leftMotorSpeed = 0.0f;
                driveBaseThrottleValue = 0.0f;

                // Quick turn does not take throttle into account
                leftMotorSpeed += driveBaseHeadingValue;
                rightMotorSpeed -= driveBaseHeadingValue;
            } else {
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
            (WsOutputFacade.getInstance().getOutput(WsOutputFacade.LEFT_DRIVE_SPEED)).set((IOutputEnum) null, new Double(leftMotorSpeed));
            (WsOutputFacade.getInstance().getOutput(WsOutputFacade.RIGHT_DRIVE_SPEED)).set((IOutputEnum) null, new Double(rightMotorSpeed));

        }
    }

    public void checkAutoQuickTurn() {
        double throttle = driveBaseThrottleValue;
        double heading = driveBaseHeadingValue;

        throttle = Math.abs(throttle);
        heading = Math.abs(heading);

        if ((throttle < 0.1) && (heading > 0.1)) {
            quickTurnFlag = true;
        } else {
            quickTurnFlag = false;
        }
    }

    /*
     * ENCODER/GYRO STUFF
     */
    public Encoder getLeftEncoder() {
        return leftDriveEncoder;
    }

    public Encoder getRightEncoder() {
        return rightDriveEncoder;
    }

    public double getLeftEncoderValue() {
        return leftDriveEncoder.get();
    }

    public double getRightEncoderValue() {
        return rightDriveEncoder.get();
    }

    public double getLeftDistance() {
        double distance = (leftDriveEncoder.get() / TICKS_PER_ROTATION) * 2.0 * Math.PI * (WHEEL_DIAMETER / 2.0);
        return distance;
    }

    public double getRightDistance() {
        double distance = (rightDriveEncoder.get() / TICKS_PER_ROTATION) * 2.0 * Math.PI * (WHEEL_DIAMETER / 2.0);
        return distance;
    }

    public void setDriveDistancePidSetpoint(double distance) {
        driveDistancePid.setSetPoint(distance);
        driveDistancePid.calcPid();
    }

    public void resetLeftEncoder() {
        leftDriveEncoder.reset();
        leftDriveEncoder.start();
    }

    public void resetRightEncoder() {
        rightDriveEncoder.reset();
        rightDriveEncoder.start();
    }

    public void enableDistancePidControl() {
        distancePidEnabled = true;
        driveDistancePid.Enable();
    }

    public void disableDistancePidControl() {
        distancePidEnabled = false;
    }

    public void resetDistancePid() {
        driveDistancePid.Reset();
    }

    public void notifyConfigChange() {
        WHEEL_DIAMETER = WHEEL_DIAMETER_config.getValue();
        TICKS_PER_ROTATION = TICKS_PER_ROTATION_config.getValue();
    }

    public void acceptNotification(Subject subjectThatCaused) {
        if (subjectThatCaused.getType() == WsDriverJoystickButtonEnum.BUTTON7) {
            antiTurboFlag = ((BooleanSubject) subjectThatCaused).getValue();
        } else if (subjectThatCaused.getType() == WsDriverJoystickButtonEnum.BUTTON6) {
            if (((BooleanSubject) subjectThatCaused).getValue() == true) {
                shifterFlag = !shifterFlag;
            }
        }
    }
}
