/*
 * Example drive base for the simulation env.
 */
package com.wildstangs.subsystems;

import com.wildstangs.config.DoubleConfigFileParameter;
import com.wildstangs.inputfacade.base.WsInputFacade;
import com.wildstangs.inputfacade.inputs.joystick.driver.WsDriverJoystickButtonEnum;
import com.wildstangs.inputfacade.inputs.joystick.driver.WsDriverJoystickEnum;
import com.wildstangs.logger.Logger;
import com.wildstangs.outputfacade.base.IOutputEnum;
import com.wildstangs.outputfacade.base.WsOutputFacade;
import com.wildstangs.pid.controller.base.WsPidController;
import com.wildstangs.pid.controller.base.WsPidStateType;
import com.wildstangs.pid.inputs.WsDriveBaseDistancePidInput;
import com.wildstangs.pid.inputs.WsDriveBaseHeadingPidInput;
import com.wildstangs.pid.outputs.WsDriveBaseDistancePidOutput;
import com.wildstangs.pid.outputs.WsDriveBaseHeadingPidOutput;
import com.wildstangs.subjects.base.BooleanSubject;
import com.wildstangs.subjects.base.IObserver;
import com.wildstangs.subjects.base.Subject;
import com.wildstangs.subsystems.base.WsSubsystem;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.DoubleSolenoid;
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
    private static final double HEADING_SENSITIVITY = 1.8;
    private static final double MAX_MOTOR_OUTPUT = 1.0;
    private static final double NEG_MAX_MOTOR_OUTPUT = -1.0;
    private static final double ANTI_TURBO_MAX_DEFLECTION = 0.500;
    private static double THROTTLE_LOW_GEAR_ACCEL_FACTOR = 0.250;
    private static double HEADING_LOW_GEAR_ACCEL_FACTOR = 0.500;
    private static double THROTTLE_HIGH_GEAR_ACCEL_FACTOR = 0.125;
    private static double HEADING_HIGH_GEAR_ACCEL_FACTOR = 0.250;
    private static double TICKS_PER_ROTATION = 360.0;
    private static double WHEEL_DIAMETER = 7.5;
    private static double MAX_HIGH_GEAR_PERCENT = 0.80;
    private static double driveBaseThrottleValue = 0.0;
    private static double driveBaseHeadingValue = 0.0;
    private static double pidThrottleValue = 0.0;
    private static double pidHeadingValue = 0.0;
    private static boolean antiTurboFlag = false;
    private static boolean turboFlag = false;
    private static DoubleSolenoid.Value shifterFlag = DoubleSolenoid.Value.kForward; //Default to low gear
    private static boolean quickTurnFlag = false;
    private static Encoder leftDriveEncoder;
    private static Encoder rightDriveEncoder;
    private static Gyro driveHeadingGyro;
    private static WsPidController driveHeadingPid;
    private static WsDriveBaseHeadingPidInput driveHeadingPidInput;
    private static WsDriveBaseHeadingPidOutput driveHeadingPidOutput;
    private static double gyroValue;
    private static boolean driveHeadingPidEnabled = false;
    private static WsPidController driveDistancePid;
    private static WsDriveBaseDistancePidInput driveDistancePidInput;
    private static WsDriveBaseDistancePidOutput driveDistancePidOutput;
    private static double leftEncoderValue;
    private static double rightEncoderValue;
    private static boolean driveDistancePidEnabled = false;
    private static DoubleConfigFileParameter WHEEL_DIAMETER_config;
    private static DoubleConfigFileParameter TICKS_PER_ROTATION_config;
    private static DoubleConfigFileParameter THROTTLE_LOW_GEAR_ACCEL_FACTOR_config;
    private static DoubleConfigFileParameter HEADING_LOW_GEAR_ACCEL_FACTOR_config;
    private static DoubleConfigFileParameter THROTTLE_HIGH_GEAR_ACCEL_FACTOR_config;
    private static DoubleConfigFileParameter HEADING_HIGH_GEAR_ACCEL_FACTOR_config;
    private static DoubleConfigFileParameter MAX_HIGH_GEAR_PERCENT_config;

    public WsDriveBase(String name) {
        super(name);

        WHEEL_DIAMETER_config = new DoubleConfigFileParameter(this.getClass().getName(), "wheel_diameter", 7.5);
        TICKS_PER_ROTATION_config = new DoubleConfigFileParameter(this.getClass().getName(), "ticks_per_rotation", 360.0);
        THROTTLE_LOW_GEAR_ACCEL_FACTOR_config = new DoubleConfigFileParameter(this.getClass().getName(), "throttle_low_gear_accel_factor", 0.250);
        HEADING_LOW_GEAR_ACCEL_FACTOR_config = new DoubleConfigFileParameter(this.getClass().getName(), "heading_low_gear_accel_factor", 0.500);
        THROTTLE_HIGH_GEAR_ACCEL_FACTOR_config = new DoubleConfigFileParameter(this.getClass().getName(), "throttle_high_gear_accel_factor", 0.125);
        HEADING_HIGH_GEAR_ACCEL_FACTOR_config = new DoubleConfigFileParameter(this.getClass().getName(), "heading_high_gear_accel_factor", 0.250);
        MAX_HIGH_GEAR_PERCENT_config = new DoubleConfigFileParameter(this.getClass().getName(), "max_high_gear_percent", 0.80);

        //Anti-Turbo button
        Subject subject = WsInputFacade.getInstance().getOiInput(WsInputFacade.DRIVER_JOYSTICK).getSubject(WsDriverJoystickButtonEnum.BUTTON8);
        subject.attach(this);
        //Turbo button
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
        //@TODO: Get the correct port
        driveHeadingGyro = new Gyro(1);

        //Initialize the PIDs
        driveDistancePidInput = new WsDriveBaseDistancePidInput();
        driveDistancePidOutput = new WsDriveBaseDistancePidOutput();
        driveDistancePid = new WsPidController(driveDistancePidInput, driveDistancePidOutput, "WsDriveBaseDistancePid");

        driveHeadingPidInput = new WsDriveBaseHeadingPidInput();
        driveHeadingPidOutput = new WsDriveBaseHeadingPidOutput();
        driveHeadingPid = new WsPidController(driveHeadingPidInput, driveHeadingPidOutput, "WsDriveBaseHeadingPid");
        init();
    }

    public void init() {
        driveBaseThrottleValue = 0.0;
        driveBaseHeadingValue = 0.0;
        antiTurboFlag = false;
        turboFlag = false;
        shifterFlag = DoubleSolenoid.Value.kForward;
        quickTurnFlag = false;
        this.disableDistancePidControl();
        this.disableHeadingPidControl();
        Logger.getLogger().always(this.getClass().getName(), "init", "Drive Base init");
    }

    public void update() {
        if (true == driveDistancePidEnabled) {
            //We are driving by distance under PID control
            enableDistancePidControl();
            driveDistancePid.calcPid();
            setThrottleValue(pidThrottleValue);
            setHeadingValue(0);
            updateDriveMotors();
            SmartDashboard.putNumber("PID Throttle Value", pidThrottleValue);
        } else if (true == driveHeadingPidEnabled) {
            //We are driving by heading under PID control
            enableHeadingPidControl();
            driveHeadingPid.calcPid();
            quickTurnFlag = true;
            setThrottleValue(0);
            setHeadingValue(pidHeadingValue);
            updateDriveMotors();
            SmartDashboard.putNumber("PID Heading Value", pidHeadingValue);
        } else if (true == driveDistancePidEnabled && true == driveHeadingPidEnabled) {
            //This isn't good...
            //Disable both the PIDs and tell the logger we have a problem
            disableDistancePidControl();
            disableHeadingPidControl();
            Logger.getLogger().error(this.getClass().getName(), "update", "Both PIDS are enabled. Disabling both.");
        } else if (false == driveDistancePidEnabled && false == driveHeadingPidEnabled) {
            //We are in manual control
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

            SmartDashboard.putNumber("Throttle Value", driveBaseThrottleValue);
            SmartDashboard.putNumber("Heading Value", driveBaseHeadingValue);
            SmartDashboard.putBoolean("Shifter State", shifterFlag.equals(DoubleSolenoid.Value.kReverse));
            SmartDashboard.putBoolean("Anti-Turbo Flag", antiTurboFlag);

            //Set gear shift output
            WsOutputFacade.getInstance().getOutput(WsOutputFacade.SHIFTER).set(null, new Integer(shifterFlag.value));
        } else {
        }
    }

    public void setThrottleValue(double tValue) {

        // Taking into account Anti-Turbo
        double new_throttle = tValue;
        if (true == antiTurboFlag) {
            new_throttle *= ANTI_TURBO_MAX_DEFLECTION;

            //Cap the throttle at the maximum deflection allowed for anti-turbo
            if (new_throttle > ANTI_TURBO_MAX_DEFLECTION) {
                new_throttle = ANTI_TURBO_MAX_DEFLECTION;
            }
            if (new_throttle < -ANTI_TURBO_MAX_DEFLECTION) {
                new_throttle = -ANTI_TURBO_MAX_DEFLECTION;
            }
        }

        if (shifterFlag == DoubleSolenoid.Value.kReverse) {
            //We are in high gear, see if the turbo button is pressed
            if (turboFlag == true) {
                //We are in turbo mode, don't cap the output
            } else {
                //We aren't in turbo mode, cap the output at the max percent for high gear
                if (new_throttle > MAX_MOTOR_OUTPUT * MAX_HIGH_GEAR_PERCENT) {
                    new_throttle = MAX_MOTOR_OUTPUT * MAX_HIGH_GEAR_PERCENT;
                }
                if (new_throttle < NEG_MAX_MOTOR_OUTPUT * MAX_HIGH_GEAR_PERCENT) {
                    new_throttle = NEG_MAX_MOTOR_OUTPUT * MAX_HIGH_GEAR_PERCENT;
                }
            }

        } else {
            //We are in low gear, don't modify the throttle
        }

        //Use the acceleration factor based on the current shifter state
        if (shifterFlag == DoubleSolenoid.Value.kForward) {
            //We are in low gear, use that acceleration factor
            driveBaseThrottleValue = driveBaseThrottleValue + (new_throttle - driveBaseThrottleValue) * THROTTLE_LOW_GEAR_ACCEL_FACTOR;
        } else if (shifterFlag == DoubleSolenoid.Value.kReverse) {
            //We are in high gear, use that acceleration factor
            driveBaseThrottleValue = driveBaseThrottleValue + (new_throttle - driveBaseThrottleValue) * THROTTLE_HIGH_GEAR_ACCEL_FACTOR;
        } else {
            //This is bad...
            //If we get here we have a problem
        }

        if (driveBaseThrottleValue > MAX_INPUT_THROTTLE_VALUE) {
            driveBaseThrottleValue = MAX_INPUT_THROTTLE_VALUE;
        } else if (driveBaseThrottleValue < MAX_NEG_INPUT_THROTTLE_VALUE) {
            driveBaseThrottleValue = MAX_NEG_INPUT_THROTTLE_VALUE;
        }
    }

    public void setHeadingValue(double hValue) {

        // Taking into account anti-turbo
        double new_heading = hValue;
        if (true == antiTurboFlag) {
            new_heading *= ANTI_TURBO_MAX_DEFLECTION;

            //Cap the heading at the maximum deflection allowed for anti-turbo
            if (new_heading > ANTI_TURBO_MAX_DEFLECTION) {
                new_heading = ANTI_TURBO_MAX_DEFLECTION;
            }
            if (new_heading < -ANTI_TURBO_MAX_DEFLECTION) {
                new_heading = -ANTI_TURBO_MAX_DEFLECTION;
            }
        }

        //Use the acceleration factor based on the current shifter state
        if (shifterFlag == DoubleSolenoid.Value.kForward) {
            //We are in low gear, use that acceleration factor
            driveBaseHeadingValue = driveBaseHeadingValue + (new_heading - driveBaseHeadingValue) * HEADING_LOW_GEAR_ACCEL_FACTOR;
        } else if (shifterFlag == DoubleSolenoid.Value.kReverse) {
            //We are in high gear, use that acceleration factor
            driveBaseHeadingValue = driveBaseHeadingValue + (new_heading - driveBaseHeadingValue) * HEADING_HIGH_GEAR_ACCEL_FACTOR;
        } else {
            //This is bad...
            //If we get here we have a problem
        }

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
            angularPower = Math.abs(driveBaseThrottleValue) * driveBaseHeadingValue * HEADING_SENSITIVITY;
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

            if (rightMotorSpeed > MAX_MOTOR_OUTPUT) {
                rightMotorSpeed = MAX_MOTOR_OUTPUT;
            }
            if (leftMotorSpeed > MAX_MOTOR_OUTPUT) {
                leftMotorSpeed = MAX_MOTOR_OUTPUT;
            }
            if (rightMotorSpeed < NEG_MAX_MOTOR_OUTPUT) {
                rightMotorSpeed = NEG_MAX_MOTOR_OUTPUT;
            }
            if (leftMotorSpeed < NEG_MAX_MOTOR_OUTPUT) {
                leftMotorSpeed = NEG_MAX_MOTOR_OUTPUT;
            }
        }

        //Update Output Facade.
        (WsOutputFacade.getInstance().getOutput(WsOutputFacade.LEFT_DRIVE_SPEED)).set((IOutputEnum) null, new Double(leftMotorSpeed));
        (WsOutputFacade.getInstance().getOutput(WsOutputFacade.RIGHT_DRIVE_SPEED)).set((IOutputEnum) null, new Double(rightMotorSpeed));
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
        driveDistancePidEnabled = true;
        driveDistancePid.enable();
    }

    public void disableDistancePidControl() {
        driveDistancePidEnabled = false;
        driveDistancePid.disable();
        resetDistancePid();
        Logger.getLogger().debug(this.getClass().getName(), "disableDistancePidControl", "Distance PID is disabled");
    }

    public void resetDistancePid() {
        driveDistancePid.reset();
        resetLeftEncoder();
        resetRightEncoder();
    }

    public WsPidStateType getDistancePidState() {
        return driveDistancePid.getState();
    }

    public void setPidThrottleValue(double pidThrottle) {
        pidThrottleValue = pidThrottle;
    }

    public Gyro getLeftGyro() {
        return driveHeadingGyro;
    }

    public double getGyroAngle() {
        return driveHeadingGyro.getAngle();
    }

    public void setDriveHeadingPidSetpoint(double distance) {
        driveHeadingPid.setSetPoint(distance);
        driveHeadingPid.calcPid();
    }

    public void resetGyro() {
        driveHeadingGyro.reset();
    }

    public void enableHeadingPidControl() {
        driveHeadingPidEnabled = true;
        driveHeadingPid.enable();
    }

    public void disableHeadingPidControl() {
        driveHeadingPidEnabled = false;
        driveHeadingPid.disable();
        resetHeadingPid();
        Logger.getLogger().debug(this.getClass().getName(), "disableHeadingPidControl", "Heading PID is disabled");
    }

    public void resetHeadingPid() {
        driveHeadingPid.reset();
        resetGyro();
    }

    public WsPidStateType getHeadingPidState() {
        return driveHeadingPid.getState();
    }

    public void setPidHeadingValue(double pidHeading) {
        pidHeadingValue = pidHeading;
        Logger.getLogger().debug(this.getClass().getName(), "setPidHeadingValue", "Heading PID value set: " + pidHeading);
    }

    public void notifyConfigChange() {
        WHEEL_DIAMETER = WHEEL_DIAMETER_config.getValue();
        TICKS_PER_ROTATION = TICKS_PER_ROTATION_config.getValue();
        THROTTLE_LOW_GEAR_ACCEL_FACTOR = THROTTLE_LOW_GEAR_ACCEL_FACTOR_config.getValue();
        HEADING_LOW_GEAR_ACCEL_FACTOR = HEADING_LOW_GEAR_ACCEL_FACTOR_config.getValue();
        THROTTLE_HIGH_GEAR_ACCEL_FACTOR = THROTTLE_HIGH_GEAR_ACCEL_FACTOR_config.getValue();
        HEADING_HIGH_GEAR_ACCEL_FACTOR = HEADING_HIGH_GEAR_ACCEL_FACTOR_config.getValue();
        MAX_HIGH_GEAR_PERCENT = MAX_HIGH_GEAR_PERCENT_config.getValue();
        driveDistancePid.notifyConfigChange();
        driveHeadingPid.notifyConfigChange();
    }

    public void acceptNotification(Subject subjectThatCaused) {
        if (subjectThatCaused.getType() == WsDriverJoystickButtonEnum.BUTTON8) {
            antiTurboFlag = ((BooleanSubject) subjectThatCaused).getValue();
        } else if (subjectThatCaused.getType() == WsDriverJoystickButtonEnum.BUTTON6) {
            if (((BooleanSubject) subjectThatCaused).getValue() == true) {
                shifterFlag = shifterFlag.equals(DoubleSolenoid.Value.kForward)
                        ? DoubleSolenoid.Value.kReverse : DoubleSolenoid.Value.kForward;
            }
        } else if (subjectThatCaused.getType() == WsDriverJoystickButtonEnum.BUTTON7) {
            turboFlag = ((BooleanSubject) subjectThatCaused).getValue();
        }
    }
}
