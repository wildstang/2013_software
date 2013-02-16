/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wildstangs.autonomous.programs;

import com.wildstangs.autonomous.WsAutonomousManager;
import com.wildstangs.autonomous.WsAutonomousProgram;
import com.wildstangs.autonomous.steps.WsAutonomousParallelStepGroup;
import com.wildstangs.autonomous.steps.control.WsAutonomousStepDelay;
import com.wildstangs.autonomous.steps.control.WsAutonomousStepStopAutonomous;
import com.wildstangs.autonomous.steps.drivebase.*;
import com.wildstangs.autonomous.steps.floorpickup.*;
import com.wildstangs.autonomous.steps.hopper.*;
import com.wildstangs.autonomous.steps.shooter.WsAutonomousStepSetShooterPreset;
import com.wildstangs.autonomous.steps.shooter.WsAutonomousStepWaitForShooter;
import com.wildstangs.config.BooleanConfigFileParameter;
import com.wildstangs.config.DoubleConfigFileParameter;
import com.wildstangs.config.IntegerConfigFileParameter;
import com.wildstangs.logger.Logger;
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
    private IntegerConfigFileParameter ForceStopAtStep;
    
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
        FunnelatorLoadDelay = new IntegerConfigFileParameter(this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".FunnelatorLoadDelay", 2000);
        ForceStopAtStep = new IntegerConfigFileParameter(this.getClass().getName(), "ForceStopAtStep", 0);

        
        startPreset = new WsShooter.Preset(FirstEnterWheelSetPoint.getValue(),
                FirstExitWheelSetPoint.getValue(),
                FirstShooterAngle.getValue() ?
                DoubleSolenoid.Value.kReverse : DoubleSolenoid.Value.kForward);
        secondShooterPreset = new WsShooter.Preset(SecondEnterWheelSetPoint.getValue(),
                SecondExitWheelSetPoint.getValue(),
                SecondShooterAngle.getValue() ?
                DoubleSolenoid.Value.kReverse : DoubleSolenoid.Value.kForward);
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
        programSteps[1] = new WsAutonomousStepEnableDriveDistancePid();
        WsAutonomousParallelStepGroup pg2 = new WsAutonomousParallelStepGroup("Wait for shooter and drive");
        programSteps[2] = pg2;
            pg2.addStep(new WsAutonomousStepWaitForShooter());
            pg2.addStep(new WsAutonomousStepWaitForDriveDistancePid());
        programSteps[3] = new WsAutonomousStepSetDriveHeadingPidSetpoint(AngleTurn.getValue());
        programSteps[4] = new WsAutonomousStepEnableDriveHeadingPid();
        programSteps[5] = new WsAutonomousStepWaitForDriveHeadingPid();
        programSteps[7] = new WsAutonomousStepEnableDriveDistancePid();
        programSteps[6] = new WsAutonomousStepSetDriveDistancePidSetpoint(SecondDrive.getValue());
        programSteps[8] = new WsAutonomousStepWaitForDriveDistancePid();
        
        programSteps[9] = new WsAutonomousStepMultikick(3);
        WsAutonomousParallelStepGroup pg3 = new WsAutonomousParallelStepGroup("Set up for intake");
        programSteps[10] = pg3;
            pg3.addStep(new WsAutonomousStepLowerHopper());
            pg3.addStep(new WsAutonomousStepLowerAccumulator());
            pg3.addStep(new WsAutonomousStepIntakeMotorBackwards());
        programSteps[11] = new WsAutonomousStepSetDriveDistancePidSetpoint(ThirdDrive.getValue());
        programSteps[12] = new WsAutonomousStepEnableDriveDistancePid();
        programSteps[13] = new WsAutonomousStepWaitForDriveDistancePid();
        programSteps[14] = new WsAutonomousStepIntakeMotorStop();
        programSteps[15] = new WsAutonomousStepRaiseAccumulator();
        programSteps[16] = new WsAutonomousStepIntakeMotorBackwards();
        programSteps[17] = new WsAutonomousStepDelay(FunnelatorLoadDelay.getValue());
        WsAutonomousParallelStepGroup pg4 = new WsAutonomousParallelStepGroup("Set up for intake");
        programSteps[18] = pg4;
            pg4.addStep(new WsAutonomousStepLowerHopper());
            pg4.addStep(new WsAutonomousStepLowerAccumulator());
            pg4.addStep(new WsAutonomousStepIntakeMotorBackwards());
        programSteps[19] = new WsAutonomousStepSetDriveDistancePidSetpoint(FourthDrive.getValue());
        programSteps[20] = new WsAutonomousStepEnableDriveDistancePid();
        programSteps[21] = new WsAutonomousStepWaitForDriveDistancePid();
        programSteps[22] = new WsAutonomousStepIntakeMotorStop();
        programSteps[23] = new WsAutonomousStepRaiseAccumulator();
        programSteps[24] = new WsAutonomousStepIntakeMotorBackwards();
        programSteps[25] = new WsAutonomousStepDelay(FunnelatorLoadDelay.getValue());
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

