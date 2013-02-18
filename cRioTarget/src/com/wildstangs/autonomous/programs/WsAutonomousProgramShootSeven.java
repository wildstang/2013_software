/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wildstangs.autonomous.programs;

import com.wildstangs.autonomous.WsAutonomousManager;
import com.wildstangs.autonomous.WsAutonomousProgram;
import com.wildstangs.autonomous.steps.WsAutonomousParallelStepGroup;
import com.wildstangs.autonomous.steps.control.WsAutonomousStepDelay;
import com.wildstangs.autonomous.steps.control.WsAutonomousStepNoOp;
import com.wildstangs.autonomous.steps.drivebase.*;
import com.wildstangs.autonomous.steps.floorpickup.*;
import com.wildstangs.autonomous.steps.hopper.*;
import com.wildstangs.autonomous.steps.shooter.WsAutonomousStepSetShooterPreset;
import com.wildstangs.autonomous.steps.shooter.WsAutonomousStepWaitForShooter;
import com.wildstangs.config.BooleanConfigFileParameter;
import com.wildstangs.config.DoubleConfigFileParameter;
import com.wildstangs.config.IntegerConfigFileParameter;
import com.wildstangs.subsystems.WsShooter;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public class WsAutonomousProgramShootSeven extends WsAutonomousProgram
{
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
    
    private WsShooter.Preset startPreset, secondShooterPreset;
    
    private void defineConfigValues(){ 
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

        
        startPreset = new WsShooter.Preset(FirstEnterWheelSetPoint.getValue(),
                FirstExitWheelSetPoint.getValue(),
                FirstShooterAngle.getValue() ?
                DoubleSolenoid.Value.kForward : DoubleSolenoid.Value.kReverse);
        secondShooterPreset = new WsShooter.Preset(SecondEnterWheelSetPoint.getValue(),
                SecondExitWheelSetPoint.getValue(),
                SecondShooterAngle.getValue() ?
                DoubleSolenoid.Value.kForward : DoubleSolenoid.Value.kReverse);
    }
    
    public WsAutonomousProgramShootSeven()
    {
        super(32);
    }
    public void defineSteps()
    {
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
        programSteps[3] = new WsAutonomousStepNoOp();
        programSteps[4] = new WsAutonomousStepNoOp();
        programSteps[5] = new WsAutonomousStepNoOp();
        programSteps[6] = new WsAutonomousStepSetDriveDistancePidSetpoint(SecondDrive.getValue());
        programSteps[7] = new WsAutonomousStepEnableDriveDistancePid();
        programSteps[8] = new WsAutonomousStepWaitForDriveDistancePid();
        
        programSteps[9] = new WsAutonomousStepMultikick(3);
        WsAutonomousParallelStepGroup pg3 = new WsAutonomousParallelStepGroup("Set up for intake");
        programSteps[10] = pg3;
            pg3.addStep(new WsAutonomousStepLowerHopper());
            pg3.addStep(new WsAutonomousStepDelay(LowerAccumulatorDelay.getValue()));
        programSteps[11] = new WsAutonomousStepIntakeMotorPullFrisbeesIn();
        programSteps[12] = new WsAutonomousStepSetDriveDistancePidSetpoint(ThirdDrive.getValue());
        programSteps[13] = new WsAutonomousStepEnableDriveDistancePid();
        programSteps[14] = new WsAutonomousStepWaitForDriveDistancePid();
        programSteps[15] = new WsAutonomousStepIntakeMotorStop();
        WsAutonomousParallelStepGroup pg7 = new WsAutonomousParallelStepGroup("Raise accumulator and wait for it");
        programSteps[16] = pg7;
            pg7.addStep(new WsAutonomousStepRaiseAccumulator());
            pg7.addStep(new WsAutonomousStepDelay(RaiseAccumulatorDelay.getValue()));
        programSteps[17] = new WsAutonomousStepGroupIntakeTwoFrisbees(FunnelatorLoadDelay.getValue());
        WsAutonomousParallelStepGroup pg4 = new WsAutonomousParallelStepGroup("Set up for intake");
        programSteps[18] = pg4;
            pg4.addStep(new WsAutonomousStepLowerHopper());
            pg4.addStep(new WsAutonomousStepLowerAccumulator());
            pg4.addStep(new WsAutonomousStepDelay(LowerAccumulatorDelay.getValue()));
        programSteps[19] = new WsAutonomousStepIntakeMotorPullFrisbeesIn();
        programSteps[20] = new WsAutonomousStepSetDriveDistancePidSetpoint(FourthDrive.getValue());
        programSteps[21] = new WsAutonomousStepEnableDriveDistancePid();
        programSteps[22] = new WsAutonomousStepWaitForDriveDistancePid();
        programSteps[23] = new WsAutonomousStepIntakeMotorStop();
        WsAutonomousParallelStepGroup pg8 = new WsAutonomousParallelStepGroup("Raise accumulator and wait for it");
        programSteps[24] = pg8;
            pg8.addStep(new WsAutonomousStepRaiseAccumulator());
            pg8.addStep(new WsAutonomousStepDelay(RaiseAccumulatorDelay.getValue()));
        programSteps[25] = new WsAutonomousStepGroupIntakeTwoFrisbees(FunnelatorLoadDelay.getValue());
        programSteps[26] = new WsAutonomousStepIntakeMotorStop();
        programSteps[27] = new WsAutonomousStepRaiseHopper();
        WsAutonomousParallelStepGroup pg5 = new WsAutonomousParallelStepGroup("5 Drive and shooter set up");
        programSteps[28] = pg5;
            pg5.addStep(new WsAutonomousStepSetDriveDistancePidSetpoint(FifthDrive.getValue()));
            pg5.addStep(new WsAutonomousStepSetShooterPreset(secondShooterPreset.ENTER_WHEEL_SET_POINT, secondShooterPreset.EXIT_WHEEL_SET_POINT, secondShooterPreset.ANGLE));
        programSteps[29] = new WsAutonomousStepEnableDriveDistancePid();
        WsAutonomousParallelStepGroup pg6 = new WsAutonomousParallelStepGroup("5 Wait for shooter and drive");
        programSteps[30] = pg6;
            pg6.addStep(new WsAutonomousStepWaitForShooter());
            pg6.addStep(new WsAutonomousStepWaitForDriveDistancePid());
        programSteps[31] = new WsAutonomousStepMultikick(4);
    }
    
    public String toString()
    {
        return "Shooting Seven Frisbees";
    }
}

