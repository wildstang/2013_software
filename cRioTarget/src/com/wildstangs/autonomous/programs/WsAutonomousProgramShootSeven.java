/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wildstangs.autonomous.programs;

import com.wildstangs.autonomous.WsAutonomousManager;
import com.wildstangs.autonomous.WsAutonomousProgram;
import com.wildstangs.autonomous.steps.WsAutonomousParallelStepGroup;
import com.wildstangs.autonomous.steps.control.WsAutonomousStepDelay;
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
    private DoubleConfigFileParameter StartDrive = new DoubleConfigFileParameter(
            this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".StartDrive", 60.5);
    private DoubleConfigFileParameter AngleTurn = new DoubleConfigFileParameter(
            this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".AngleTurn", 90);
    private DoubleConfigFileParameter SecondDrive = new DoubleConfigFileParameter(
            this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".SecondDrive", 60.5);
    private DoubleConfigFileParameter ThirdDrive = new DoubleConfigFileParameter(
            this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".ThirdDrive", 60.5);
    private DoubleConfigFileParameter FourthDrive = new DoubleConfigFileParameter(
            this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".FourthDrive", 60.5);
    private DoubleConfigFileParameter FifthDrive = new DoubleConfigFileParameter(
            this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".FifthDrive", 60.5);
    private IntegerConfigFileParameter FirstEnterWheelSetPoint = new IntegerConfigFileParameter(
            this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".EnterWheelSetPoint", 5000);
    private IntegerConfigFileParameter FirstExitWheelSetPoint = new IntegerConfigFileParameter(
            this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".ExitWheelSetPoint", 4500);
    private BooleanConfigFileParameter FirstShooterAngle = new BooleanConfigFileParameter(
            this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".ShooterAngle", false);
    private IntegerConfigFileParameter SecondEnterWheelSetPoint = new IntegerConfigFileParameter(
            this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".EnterWheelSetPoint", 5000);
    private IntegerConfigFileParameter SecondExitWheelSetPoint = new IntegerConfigFileParameter(
            this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".ExitWheelSetPoint", 4500);
    private BooleanConfigFileParameter SecondShooterAngle = new BooleanConfigFileParameter(
            this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".ShooterAngle", false);
    private IntegerConfigFileParameter funnelatorLoadDelay = new IntegerConfigFileParameter(
            this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".funnelatorLoadDelay", 2000);
    
    private WsShooter.Preset StartPreset, SecondShooterPreset;
    
    public WsAutonomousProgramShootSeven()
    {
        super(6);
        StartPreset = new WsShooter.Preset(FirstEnterWheelSetPoint.getValue(),
                FirstExitWheelSetPoint.getValue(),
                FirstShooterAngle.getValue() ?
                DoubleSolenoid.Value.kReverse : DoubleSolenoid.Value.kForward);
        SecondShooterPreset = new WsShooter.Preset(SecondEnterWheelSetPoint.getValue(),
                SecondExitWheelSetPoint.getValue(),
                SecondShooterAngle.getValue() ?
                DoubleSolenoid.Value.kReverse : DoubleSolenoid.Value.kForward);
    }
    public void defineSteps()
    {
        WsAutonomousParallelStepGroup pg1 = new WsAutonomousParallelStepGroup();
        programSteps[0] = pg1;
        pg1.addStep(new WsAutonomousStepSetDriveDistancePidSetpoint(StartDrive.getValue()));
        pg1.addStep(new WsAutonomousStepSetShooterPreset(StartPreset.ENTER_WHEEL_SET_POINT, StartPreset.EXIT_WHEEL_SET_POINT, StartPreset.ANGLE));
        programSteps[1] = new WsAutonomousStepEnableDriveDistancePid();
        WsAutonomousParallelStepGroup pg2 = new WsAutonomousParallelStepGroup();
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
        WsAutonomousParallelStepGroup pg3 = new WsAutonomousParallelStepGroup();
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
        programSteps[17] = new WsAutonomousStepDelay(funnelatorLoadDelay.getValue());
        WsAutonomousParallelStepGroup pg4 = new WsAutonomousParallelStepGroup();
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
        programSteps[25] = new WsAutonomousStepDelay(funnelatorLoadDelay.getValue());
        programSteps[26] = new WsAutonomousStepRaiseHopper();
        WsAutonomousParallelStepGroup pg5 = new WsAutonomousParallelStepGroup();
        programSteps[27] = pg5;
        pg5.addStep(new WsAutonomousStepSetDriveDistancePidSetpoint(FifthDrive.getValue()));
        pg5.addStep(new WsAutonomousStepSetShooterPreset(SecondShooterPreset.ENTER_WHEEL_SET_POINT, SecondShooterPreset.EXIT_WHEEL_SET_POINT, SecondShooterPreset.ANGLE));
        programSteps[28] = new WsAutonomousStepEnableDriveDistancePid();
        WsAutonomousParallelStepGroup pg6 = new WsAutonomousParallelStepGroup();
        programSteps[29] = pg6;
        pg6.addStep(new WsAutonomousStepWaitForShooter());
        pg6.addStep(new WsAutonomousStepWaitForDriveDistancePid());
        programSteps[30] = new WsAutonomousStepMultikick(4);
        
    }
    
    public String toString()
    {
        return "Shooting Seven Frisbees";
    }
}

