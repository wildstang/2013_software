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
import com.wildstangs.autonomous.steps.intake.WsAutonomousStepIntakeIfFunnelatorTripped;
import com.wildstangs.autonomous.steps.intake.WsAutonomousStepWaitForDiscsLatchedThroughFunnelator;
import com.wildstangs.autonomous.steps.intake.WsAutonomousStepWaitForFunnelatorLimitSwitchTrueToFalse;
import com.wildstangs.autonomous.steps.shooter.WsAutonomousStepSetShooterPreset;
import com.wildstangs.autonomous.steps.shooter.WsAutonomousStepWaitForShooter;
import com.wildstangs.config.BooleanConfigFileParameter;
import com.wildstangs.config.DoubleConfigFileParameter;
import com.wildstangs.config.IntegerConfigFileParameter;
import com.wildstangs.subsystems.WsShooter;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public class WsAutonomousProgramShootFive extends WsAutonomousProgram {

    private DoubleConfigFileParameter FirstDrive;
    private DoubleConfigFileParameter SecondDrive;
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
    private WsShooter.Preset firstShooterPreset, secondShooterPreset;

    private void defineConfigValues() {
        FirstDrive = new DoubleConfigFileParameter(this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".FirstDrive", 50.0);
        SecondDrive = new DoubleConfigFileParameter(this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".SecondDrive", -50.0);
        FirstEnterWheelSetPoint = new IntegerConfigFileParameter(this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".FirstEnterWheelSetPoint", 2800);
        FirstExitWheelSetPoint = new IntegerConfigFileParameter(this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".FirstExitWheelSetPoint", 3550);
        FirstShooterAngle = new BooleanConfigFileParameter(this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".FirstShooterAngle", false);
        SecondEnterWheelSetPoint = new IntegerConfigFileParameter(this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".SecondEnterWheelSetPoint", 2800);
        SecondExitWheelSetPoint = new IntegerConfigFileParameter(this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".SecondExitWheelSetPoint", 3550);
        SecondShooterAngle = new BooleanConfigFileParameter(this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".SecondShooterAngle", false);
        RaiseAccumulatorDelay = new IntegerConfigFileParameter(this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".RaiseAccumulatorDelay", 3000);
        LowerAccumulatorDelay = new IntegerConfigFileParameter(this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".LowerAccumulatorDelay", 1500);
        FunnelatorLoadDelay = new IntegerConfigFileParameter(this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".FunnelatorLoadDelay", 6000);
        ThirdFrisbeeDelay = new IntegerConfigFileParameter(this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".ThirdFrisbeeDelay", 100);

        firstShooterPreset = new WsShooter.Preset(FirstEnterWheelSetPoint.getValue(),
                FirstExitWheelSetPoint.getValue(),
                FirstShooterAngle.getValue()
                ? DoubleSolenoid.Value.kForward : DoubleSolenoid.Value.kReverse);
        secondShooterPreset = new WsShooter.Preset(SecondEnterWheelSetPoint.getValue(),
                SecondExitWheelSetPoint.getValue(),
                SecondShooterAngle.getValue()
                ? DoubleSolenoid.Value.kForward : DoubleSolenoid.Value.kReverse);
    }

    public WsAutonomousProgramShootFive() {
        super(17);
    }

    public void defineSteps() {
        defineConfigValues();
        WsAutonomousParallelStepGroup pg1 = new WsAutonomousParallelStepGroup("Drive and Set shooter");
        programSteps[0] = pg1;
            pg1.addStep(new WsAutonomousStepSetShooterPreset(firstShooterPreset.ENTER_WHEEL_SET_POINT, firstShooterPreset.EXIT_WHEEL_SET_POINT, firstShooterPreset.ANGLE));
            pg1.addStep(new WsAutonomousStepLowerAccumulator());
        programSteps[1] = new WsAutonomousStepWaitForShooter();
        WsAutonomousSerialStepContainer ssc1 = new WsAutonomousSerialStepContainer("Kick twice, delay, then kick again");
        programSteps[2] = ssc1;
            ssc1.addStep(new WsAutonomousStepMultikick(2));
            ssc1.addStep(new WsAutonomousStepDelay(ThirdFrisbeeDelay.getValue()));
            ssc1.addStep(new WsAutonomousStepMultikick(1));
        programSteps[3] = new WsAutonomousStepLowerHopper();
        programSteps[4] = new WsAutonomousStepIntakeMotorPullFrisbeesIn();
        programSteps[5] = new WsAutonomousStepStartDriveUsingMotionProfile(FirstDrive.getValue(), 0.0);
        programSteps[6] = new WsAutonomousStepWaitForDriveMotionProfile(); 
        programSteps[7] = new WsAutonomousStepStopDriveUsingMotionProfile();
        programSteps[8] = new WsAutonomousStepIntakeMotorStop();
        WsAutonomousParallelStepGroup pg2 = new WsAutonomousParallelStepGroup("Raise accumulator and wait for it");
        programSteps[9] = pg2;
            pg2.addStep(new WsAutonomousStepRaiseAccumulator());
            pg2.addStep(new WsAutonomousStepWaitForAccumulatorUp());
        WsAutonomousSerialStepContainer ssc2 = new WsAutonomousSerialStepContainer("Intake two frisbees");
        programSteps[10] = ssc2;
            ssc2.addStep(new WsAutonomousStepIntakeMotorPullFrisbeesIn());
        WsAutonomousParallelFinishedOnAnyStepGroup pfa1 = new WsAutonomousParallelFinishedOnAnyStepGroup("Time out or take in a frisbee 1");
        WsAutonomousSerialStepContainer ssc3 = new WsAutonomousSerialStepContainer("Two frisbees on limit switch 2");
            ssc3.addStep(new WsAutonomousStepOverrideFunnelatorUpButtonOn());    
            ssc3.addStep(new WsAutonomousStepWaitForFunnelatorLimitSwitchTrueToFalse());
            ssc3.addStep(new WsAutonomousStepOverrideFunnelatorUpButtonOff());
            ssc3.addStep(new WsAutonomousStepOverrideFunnelatorButtonOn());
            ssc3.addStep(new WsAutonomousStepWaitForFunnelatorLimitSwitchTrueToFalse());
            ssc3.addStep(new WsAutonomousStepOverrideFunnelatorButtonOff());
        WsAutonomousSerialStepContainer sscfunnel = new WsAutonomousSerialStepContainer("Funnel Delay");
            sscfunnel.addStep(new WsAutonomousStepDelay(FunnelatorLoadDelay.getValue()/2));
            sscfunnel.addStep(new WsAutonomousStepOverrideFunnelatorUpButtonOff());
            sscfunnel.addStep(new WsAutonomousStepOverrideFunnelatorButtonOn());
            sscfunnel.addStep(new WsAutonomousStepDelay(FunnelatorLoadDelay.getValue()/2));
            sscfunnel.addStep(new WsAutonomousStepOverrideFunnelatorButtonOff());
                pfa1.addStep(sscfunnel);
                pfa1.addStep(ssc3);
            ssc2.addStep(pfa1);
            ssc2.addStep(new WsAutonomousStepDelay(500));
            ssc2.addStep(new WsAutonomousStepIntakeIfFunnelatorTripped());
            ssc2.addStep(new WsAutonomousStepIntakeMotorStop());
                programSteps[11] = new WsAutonomousStepRaiseHopper();
        WsAutonomousParallelStepGroup pg3 = new WsAutonomousParallelStepGroup("5 Drive and shooter set up");
        programSteps[12] = pg3;
            pg3.addStep(new WsAutonomousStepStartDriveUsingMotionProfile(SecondDrive.getValue(), 0.0));
            pg3.addStep(new WsAutonomousStepSetShooterPreset(secondShooterPreset.ENTER_WHEEL_SET_POINT, secondShooterPreset.EXIT_WHEEL_SET_POINT, secondShooterPreset.ANGLE));
        WsAutonomousParallelStepGroup pg4 = new WsAutonomousParallelStepGroup("5 Wait for shooter and drive");
        programSteps[13] = pg4;
            pg4.addStep(new WsAutonomousStepWaitForShooter());
            pg4.addStep(new WsAutonomousStepWaitForDriveMotionProfile());
        programSteps[14] = new WsAutonomousStepStopDriveUsingMotionProfile();
        programSteps[15] = new WsAutonomousStepMultikick(2);
        programSteps[16] = new WsAutonomousStepSetShooterPreset(0, 0, DoubleSolenoid.Value.kReverse);
    }

    public String toString() {
        return "Shoot Five Frisbees";
    }
}
