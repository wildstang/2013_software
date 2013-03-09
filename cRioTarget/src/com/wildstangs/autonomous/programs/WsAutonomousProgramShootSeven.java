/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous.programs;

import com.wildstangs.autonomous.WsAutonomousManager;
import com.wildstangs.autonomous.WsAutonomousProgram;
import com.wildstangs.autonomous.steps.WsAutonomousParallelFinishedOnAnyStepGroup;
import com.wildstangs.autonomous.steps.WsAutonomousParallelStepGroup;
import com.wildstangs.autonomous.steps.WsAutonomousSerialStepContainer;
import com.wildstangs.autonomous.steps.control.WsAutonomousStepDelay;
import com.wildstangs.autonomous.steps.drivebase.*;
import com.wildstangs.autonomous.steps.floorpickup.*;
import com.wildstangs.autonomous.steps.hopper.*;
import com.wildstangs.autonomous.steps.intake.WsAutonomousStepWaitForFunnelatorLimitSwitchTrueToFalse;
import com.wildstangs.autonomous.steps.shooter.WsAutonomousStepSetShooterPreset;
import com.wildstangs.autonomous.steps.shooter.WsAutonomousStepWaitForShooter;
import com.wildstangs.config.BooleanConfigFileParameter;
import com.wildstangs.config.DoubleConfigFileParameter;
import com.wildstangs.config.IntegerConfigFileParameter;
import com.wildstangs.subsystems.WsShooter;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public class WsAutonomousProgramShootSeven extends WsAutonomousProgram {

    private DoubleConfigFileParameter StartDrive;
    private DoubleConfigFileParameter AngleTurn;
    private DoubleConfigFileParameter SecondDrive;
    private DoubleConfigFileParameter ThirdDrive;
    private DoubleConfigFileParameter FourthDrive;
    private DoubleConfigFileParameter FifthDrive;
    private IntegerConfigFileParameter FirstEnterWheelSetPoint;
    private IntegerConfigFileParameter FirstExitWheelSetPoint;
    private BooleanConfigFileParameter FirstShooterAngle;
    private IntegerConfigFileParameter SecondEnterWheelSetPoint;
    private IntegerConfigFileParameter SecondExitWheelSetPoint;
    private BooleanConfigFileParameter SecondShooterAngle;
    private IntegerConfigFileParameter FunnelatorLoadDelay;
    private IntegerConfigFileParameter RaiseAccumulatorDelay;
    private IntegerConfigFileParameter LowerAccumulatorDelay;
    private IntegerConfigFileParameter ThirdFrisbeeDelay;
    private WsShooter.Preset startPreset, secondShooterPreset;

    private void defineConfigValues() {
        StartDrive = new DoubleConfigFileParameter(this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".StartDrive", 60.5);
        AngleTurn = new DoubleConfigFileParameter(this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".AngleTurn", 90);
        SecondDrive = new DoubleConfigFileParameter(this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".SecondDrive", 60.5);
        ThirdDrive = new DoubleConfigFileParameter(this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".ThirdDrive", 60.5);
        FourthDrive = new DoubleConfigFileParameter(this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".FourthDrive", 60.5);
        FifthDrive = new DoubleConfigFileParameter(this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".FifthDrive", 60.5);
        FirstEnterWheelSetPoint = new IntegerConfigFileParameter(this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".FirstEnterWheelSetPoint", 5000);
        FirstExitWheelSetPoint = new IntegerConfigFileParameter(this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".FirstExitWheelSetPoint", 4500);
        FirstShooterAngle = new BooleanConfigFileParameter(this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".FirstShooterAngle", false);
        SecondEnterWheelSetPoint = new IntegerConfigFileParameter(this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".SecondEnterWheelSetPoint", 5000);
        SecondExitWheelSetPoint = new IntegerConfigFileParameter(this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".SecondExitWheelSetPoint", 4500);
        SecondShooterAngle = new BooleanConfigFileParameter(this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".SecondShooterAngle", false);
        RaiseAccumulatorDelay = new IntegerConfigFileParameter(this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".RaiseAccumulatorDelay", 2000);
        LowerAccumulatorDelay = new IntegerConfigFileParameter(this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".LowerAccumulatorDelay", 2000);
        FunnelatorLoadDelay = new IntegerConfigFileParameter(this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".FunnelatorLoadDelay", 2000);
        ThirdFrisbeeDelay = new IntegerConfigFileParameter(this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".ThirdFrisbeeDelay", 100);

        startPreset = new WsShooter.Preset(FirstEnterWheelSetPoint.getValue(),
                FirstExitWheelSetPoint.getValue(),
                FirstShooterAngle.getValue()
                ? DoubleSolenoid.Value.kForward : DoubleSolenoid.Value.kReverse);
        secondShooterPreset = new WsShooter.Preset(SecondEnterWheelSetPoint.getValue(),
                SecondExitWheelSetPoint.getValue(),
                SecondShooterAngle.getValue()
                ? DoubleSolenoid.Value.kForward : DoubleSolenoid.Value.kReverse);
    }

    public WsAutonomousProgramShootSeven() {
        super(36);
    }

    public void defineSteps() {
        defineConfigValues();
        WsAutonomousParallelStepGroup pg1 = new WsAutonomousParallelStepGroup("Drive and Set shooter");
        programSteps[0] = pg1;
            pg1.addStep(new WsAutonomousStepSetDriveDistancePidSetpoint(StartDrive.getValue()));
            pg1.addStep(new WsAutonomousStepSetShooterPreset(startPreset.ENTER_WHEEL_SET_POINT, startPreset.EXIT_WHEEL_SET_POINT, startPreset.ANGLE));
            pg1.addStep(new WsAutonomousStepLowerAccumulator());
        programSteps[1] = new WsAutonomousStepEnableDriveDistancePid();
        WsAutonomousParallelStepGroup pg2 = new WsAutonomousParallelStepGroup("Wait for shooter and drive");
        programSteps[2] = pg2;
            pg2.addStep(new WsAutonomousStepWaitForShooter());
            pg2.addStep(new WsAutonomousStepWaitForDriveDistancePid());
        programSteps[3] = new WsAutonomousStepSetDriveDistancePidSetpoint(SecondDrive.getValue());
        programSteps[4] = new WsAutonomousStepEnableDriveDistancePid();
        programSteps[5] = new WsAutonomousStepWaitForDriveDistancePid();
        WsAutonomousSerialStepContainer ssc0 = new WsAutonomousSerialStepContainer("Kick twice, delay, then kick again");
        programSteps[6] = ssc0;
            ssc0.addStep(new WsAutonomousStepMultikick(2));
            ssc0.addStep(new WsAutonomousStepDelay(ThirdFrisbeeDelay.getValue()));
            ssc0.addStep(new WsAutonomousStepMultikick(1));
        programSteps[7] = new WsAutonomousStepLowerHopper();
        programSteps[8] = new WsAutonomousStepIntakeMotorPullFrisbeesIn();
        programSteps[9] = new WsAutonomousStepStartDriveUsingMotionProfile(ThirdDrive.getValue(), 0.0);
        programSteps[10] = new WsAutonomousStepWaitForDriveMotionProfile(); 
        programSteps[11] = new WsAutonomousStepStopDriveUsingMotionProfile();
        programSteps[12] = new WsAutonomousStepIntakeMotorStop();
        WsAutonomousParallelStepGroup pg7 = new WsAutonomousParallelStepGroup("Raise accumulator and wait for it");
        programSteps[13] = pg7;
            pg7.addStep(new WsAutonomousStepRaiseAccumulator());
            pg7.addStep(new WsAutonomousStepWaitForAccumulatorUp());
        WsAutonomousSerialStepContainer ssc1 = new WsAutonomousSerialStepContainer("Intake two frisbees");
        programSteps[14] = ssc1;
        ssc1.addStep(new WsAutonomousStepIntakeMotorPullFrisbeesIn());
        WsAutonomousParallelFinishedOnAnyStepGroup pfa1 = new WsAutonomousParallelFinishedOnAnyStepGroup("Time out or take in a frisbee 1");
        WsAutonomousSerialStepContainer ssc1_5 = new WsAutonomousSerialStepContainer("Two frisbees on limit switch 2");
            ssc1_5.addStep(new WsAutonomousStepWaitForFunnelatorLimitSwitchTrueToFalse());
            ssc1_5.addStep(new WsAutonomousStepWaitForFunnelatorLimitSwitchTrueToFalse());
                pfa1.addStep(new WsAutonomousStepDelay(FunnelatorLoadDelay.getValue()));
                pfa1.addStep(ssc1_5);
            ssc1.addStep(pfa1);
            ssc1.addStep(new WsAutonomousStepIntakeMotorStop());
        WsAutonomousParallelStepGroup pg4 = new WsAutonomousParallelStepGroup("Set up for intake");
        programSteps[15] = pg4;
            pg4.addStep(new WsAutonomousStepLowerHopper());
            pg4.addStep(new WsAutonomousStepLowerAccumulator());
            pg4.addStep(new WsAutonomousStepDelay(LowerAccumulatorDelay.getValue()));
        programSteps[16] = new WsAutonomousStepIntakeMotorPullFrisbeesIn();
        programSteps[17] = new WsAutonomousStepStartDriveUsingMotionProfile(FourthDrive.getValue(), 0.0);
        programSteps[18] = new WsAutonomousStepWaitForDriveMotionProfile(); 
        programSteps[19] = new WsAutonomousStepStopDriveUsingMotionProfile();
        programSteps[20] = new WsAutonomousStepIntakeMotorStop();
        WsAutonomousParallelStepGroup pg8 = new WsAutonomousParallelStepGroup("Raise accumulator and wait for it");
        programSteps[21] = pg8;
            pg8.addStep(new WsAutonomousStepRaiseAccumulator());
            pg8.addStep(new WsAutonomousStepWaitForAccumulatorUp());
        WsAutonomousSerialStepContainer ssc2 = new WsAutonomousSerialStepContainer("Intake two frisbees");
        programSteps[22] = ssc2;
            ssc2.addStep(new WsAutonomousStepIntakeMotorPullFrisbeesIn());
            WsAutonomousParallelFinishedOnAnyStepGroup pfa2 = new WsAutonomousParallelFinishedOnAnyStepGroup("Time out or take in a frisbee 1");
            WsAutonomousSerialStepContainer ssc2_5 = new WsAutonomousSerialStepContainer("Two frisbees on limit switch 2");
            ssc2_5.addStep(new WsAutonomousStepWaitForFunnelatorLimitSwitchTrueToFalse());
            ssc2_5.addStep(new WsAutonomousStepWaitForFunnelatorLimitSwitchTrueToFalse());
                pfa2.addStep(new WsAutonomousStepDelay(FunnelatorLoadDelay.getValue()));
                pfa2.addStep(ssc2_5);
            ssc2.addStep(pfa2);
        programSteps[23] = new WsAutonomousStepIntakeMotorStop();
        programSteps[24] = new WsAutonomousStepRaiseHopper();
        WsAutonomousParallelStepGroup pg5 = new WsAutonomousParallelStepGroup("5 Drive and shooter set up");
        programSteps[25] = pg5;
            pg5.addStep(new WsAutonomousStepStartDriveUsingMotionProfile(FifthDrive.getValue(), 0.0));
            pg5.addStep(new WsAutonomousStepDriveManual(0, 0.8));
            pg5.addStep(new WsAutonomousStepSetShooterPreset(secondShooterPreset.ENTER_WHEEL_SET_POINT, secondShooterPreset.EXIT_WHEEL_SET_POINT, secondShooterPreset.ANGLE));
        WsAutonomousParallelStepGroup pg6 = new WsAutonomousParallelStepGroup("5 Wait for shooter and drive");
        programSteps[26] = pg6;
            pg6.addStep(new WsAutonomousStepWaitForShooter());
            pg6.addStep(new WsAutonomousStepWaitForDriveMotionProfile());
        programSteps[27] = new WsAutonomousStepStopDriveUsingMotionProfile();
        programSteps[28] = new WsAutonomousStepDriveManual(0, 0);
        programSteps[29] = new WsAutonomousStepMultikick(4);
        programSteps[30] = new WsAutonomousStepLowerAccumulator();
        programSteps[31] = new WsAutonomousStepDelay(LowerAccumulatorDelay.getValue());
        programSteps[32] = new WsAutonomousStepSetShooterPreset(0, 0, DoubleSolenoid.Value.kReverse); 
        programSteps[33] = new WsAutonomousStepDelay(LowerAccumulatorDelay.getValue());
        programSteps[34] = new WsAutonomousStepRaiseAccumulator();
        programSteps[35] = new WsAutonomousStepDelay(LowerAccumulatorDelay.getValue());
    }

    public String toString() {
        return "Shooting Seven Frisbees";
    }
}
