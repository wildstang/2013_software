/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wildstangs.autonomous.programs;

import com.wildstangs.autonomous.WsAutonomousProgram;
import com.wildstangs.autonomous.steps.WsAutonomousParallelStepGroup;
import com.wildstangs.autonomous.steps.control.WsAutonomousStepDelay;
import com.wildstangs.autonomous.steps.drivebase.*;
import com.wildstangs.autonomous.steps.floorpickup.*;
import com.wildstangs.autonomous.steps.hopper.*;
import com.wildstangs.autonomous.steps.shooter.WsAutonomousStepSetShooterPreset;
import com.wildstangs.autonomous.steps.shooter.WsAutonomousStepWaitForShooter;
import com.wildstangs.autonomous.steps.shootseven.WsAutonomousStepGroupDriveToPyramidFrisbees;
import com.wildstangs.config.BooleanConfigFileParameter;
import com.wildstangs.config.DoubleConfigFileParameter;
import com.wildstangs.config.IntegerConfigFileParameter;
import com.wildstangs.subsystems.WsShooter;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public class WsAutonomousProgramShootSeven extends WsAutonomousProgram
{
    private DoubleConfigFileParameter toPyramidStartDrive = new DoubleConfigFileParameter(
            this.getClass().getName(), "ToPyramidStartDrive", 60.5);
    private DoubleConfigFileParameter toPyramidAngleTurn = new DoubleConfigFileParameter(
            this.getClass().getName(), "ToPyramidAngleTurn", 90);
    private DoubleConfigFileParameter toPyramidSecondDrive = new DoubleConfigFileParameter(
            this.getClass().getName(), "ToPyramidSecondDrive", 60.5);
    private DoubleConfigFileParameter toPyramidThirdDrive = new DoubleConfigFileParameter(
            this.getClass().getName(), "ToPyramidThirdDrive", 60.5);
    private DoubleConfigFileParameter toPyramidFourthDrive = new DoubleConfigFileParameter(
            this.getClass().getName(), "ToPyramidFourthDrive", 60.5);
    private DoubleConfigFileParameter toPyramidFifthDrive = new DoubleConfigFileParameter(
            this.getClass().getName(), "ToPyramidFifthDrive", 60.5);
    private IntegerConfigFileParameter toPyramidFirstEnterWheelSetPoint = new IntegerConfigFileParameter(
            this.getClass().getName(), "ToPyramidEnterWheelSetPoint", 5000);
    private IntegerConfigFileParameter toPyramidFirstExitWheelSetPoint = new IntegerConfigFileParameter(
            this.getClass().getName(), "ToPyramidExitWheelSetPoint", 4500);
    private BooleanConfigFileParameter toPyramidFirstShooterAngle = new BooleanConfigFileParameter(
            this.getClass().getName(), "ToPyramidShooterAngle", false);
    private IntegerConfigFileParameter toPyramidSecondEnterWheelSetPoint = new IntegerConfigFileParameter(
            this.getClass().getName(), "ToPyramidEnterWheelSetPoint", 5000);
    private IntegerConfigFileParameter toPyramidSecondExitWheelSetPoint = new IntegerConfigFileParameter(
            this.getClass().getName(), "ToPyramidExitWheelSetPoint", 4500);
    private BooleanConfigFileParameter toPyramidSecondShooterAngle = new BooleanConfigFileParameter(
            this.getClass().getName(), "ToPyramidShooterAngle", false);
    private IntegerConfigFileParameter funnelatorLoadDelay = new IntegerConfigFileParameter(
            this.getClass().getName(), "funnelatorLoadDelay", 2000);
    
    private WsShooter.Preset toPyramidStartPreset, toPyramidSecondShooterPreset;
    
    public WsAutonomousProgramShootSeven()
    {
        super(6);
        toPyramidStartPreset = new WsShooter.Preset(toPyramidFirstEnterWheelSetPoint.getValue(),
                toPyramidFirstExitWheelSetPoint.getValue(),
                toPyramidFirstShooterAngle.getValue() ?
                DoubleSolenoid.Value.kReverse : DoubleSolenoid.Value.kForward);
        toPyramidSecondShooterPreset = new WsShooter.Preset(toPyramidSecondEnterWheelSetPoint.getValue(),
                toPyramidSecondExitWheelSetPoint.getValue(),
                toPyramidSecondShooterAngle.getValue() ?
                DoubleSolenoid.Value.kReverse : DoubleSolenoid.Value.kForward);
    }
    public void defineSteps()
    {
        programSteps[0] = new WsAutonomousStepGroupDriveToPyramidFrisbees(toPyramidStartDrive.getValue()
                , toPyramidAngleTurn.getValue(), toPyramidSecondDrive.getValue()
                , toPyramidStartPreset);
        programSteps[0] = new WsAutonomousStepEnableDriveDistancePid();
        WsAutonomousParallelStepGroup pg1 = new WsAutonomousParallelStepGroup();
        programSteps[1] = pg1;
        pg1.addStep(new WsAutonomousStepSetDriveDistancePidSetpoint(toPyramidStartDrive.getValue()));
        pg1.addStep(new WsAutonomousStepSetShooterPreset(toPyramidStartPreset.ENTER_WHEEL_SET_POINT, toPyramidStartPreset.EXIT_WHEEL_SET_POINT, toPyramidStartPreset.ANGLE));
        WsAutonomousParallelStepGroup pg2 = new WsAutonomousParallelStepGroup();
        programSteps[2] = pg2;
        pg2.addStep(new WsAutonomousStepWaitForShooter());
        pg2.addStep(new WsAutonomousStepWaitForDriveDistancePid());
        programSteps[3] = new WsAutonomousStepEnableDriveHeadingPid();
        programSteps[4] = new WsAutonomousStepSetDriveHeadingPidSetpoint(toPyramidAngleTurn.getValue());
        programSteps[5] = new WsAutonomousStepWaitForDriveHeadingPid();
        programSteps[6] = new WsAutonomousStepEnableDriveDistancePid();
        programSteps[7] = new WsAutonomousStepSetDriveDistancePidSetpoint(toPyramidSecondDrive.getValue());
        programSteps[8] = new WsAutonomousStepWaitForDriveDistancePid();
        
        programSteps[9] = new WsAutonomousStepMultikick(3);
        WsAutonomousParallelStepGroup pg3 = new WsAutonomousParallelStepGroup();
        programSteps[10] = pg3;
        pg3.addStep(new WsAutonomousStepLowerHopper());
        pg3.addStep(new WsAutonomousStepLowerAccumulator());
        pg3.addStep(new WsAutonomousStepIntakeMotorBackwards());
        programSteps[11] = new WsAutonomousStepEnableDriveDistancePid();
        programSteps[12] = new WsAutonomousStepSetDriveDistancePidSetpoint(toPyramidThirdDrive.getValue());
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
        programSteps[19] = new WsAutonomousStepEnableDriveDistancePid();
        programSteps[20] = new WsAutonomousStepSetDriveDistancePidSetpoint(toPyramidFourthDrive.getValue());
        programSteps[21] = new WsAutonomousStepWaitForDriveDistancePid();
        programSteps[22] = new WsAutonomousStepIntakeMotorStop();
        programSteps[23] = new WsAutonomousStepRaiseAccumulator();
        programSteps[24] = new WsAutonomousStepIntakeMotorBackwards();
        programSteps[25] = new WsAutonomousStepDelay(funnelatorLoadDelay.getValue());
        programSteps[26] = new WsAutonomousStepRaiseHopper();
        programSteps[27] = new WsAutonomousStepEnableDriveDistancePid();
        WsAutonomousParallelStepGroup pg5 = new WsAutonomousParallelStepGroup();
        programSteps[28] = pg5;
        pg5.addStep(new WsAutonomousStepSetDriveDistancePidSetpoint(toPyramidFifthDrive.getValue()));
        pg5.addStep(new WsAutonomousStepSetShooterPreset(toPyramidSecondShooterPreset.ENTER_WHEEL_SET_POINT, toPyramidSecondShooterPreset.EXIT_WHEEL_SET_POINT, toPyramidSecondShooterPreset.ANGLE));
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

