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
import com.wildstangs.autonomous.steps.intake.WsAutonomousStepWaitForDiscsLatchedThroughFunnelator;
import com.wildstangs.autonomous.steps.shooter.WsAutonomousStepSetShooterPreset;
import com.wildstangs.autonomous.steps.shooter.WsAutonomousStepWaitForShooter;
import com.wildstangs.config.BooleanConfigFileParameter;
import com.wildstangs.config.DoubleConfigFileParameter;
import com.wildstangs.config.IntegerConfigFileParameter;
import com.wildstangs.subsystems.WsShooter;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public class WsAutonomousProgramShootSevenActiveAccumulator extends WsAutonomousProgram {

    private DoubleConfigFileParameter firstDrive;
    private DoubleConfigFileParameter secondDrive;
    private DoubleConfigFileParameter thirdDrive;
    private IntegerConfigFileParameter firstEnterWheelSetPoint;
    private IntegerConfigFileParameter firstExitWheelSetPoint;
    private BooleanConfigFileParameter firstShooterAngle;
    private IntegerConfigFileParameter secondEnterWheelSetPoint;
    private IntegerConfigFileParameter secondExitWheelSetPoint;
    private BooleanConfigFileParameter secondShooterAngle;
    private IntegerConfigFileParameter thirdFrisbeeDelay;
    private WsShooter.Preset startPreset, secondShooterPreset;

    private void defineConfigValues() {
        firstDrive = new DoubleConfigFileParameter(this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".FirstDrive", 36);
        secondDrive = new DoubleConfigFileParameter(this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".SecondDrive", 100);
        thirdDrive = new DoubleConfigFileParameter(this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".ThirdDrive", 46);
        firstEnterWheelSetPoint = new IntegerConfigFileParameter(this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".FirstEnterWheelSetPoint", 2800);
        firstExitWheelSetPoint = new IntegerConfigFileParameter(this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".FirstExitWheelSetPoint", 3550);
        firstShooterAngle = new BooleanConfigFileParameter(this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".FirstShooterAngle", false);
        secondEnterWheelSetPoint = new IntegerConfigFileParameter(this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".SecondEnterWheelSetPoint", 2200);
        secondExitWheelSetPoint = new IntegerConfigFileParameter(this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".SecondExitWheelSetPoint", 2850);
        secondShooterAngle = new BooleanConfigFileParameter(this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".SecondShooterAngle", true);
        thirdFrisbeeDelay = new IntegerConfigFileParameter(this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".ThirdFrisbeeDelay", 100);

        startPreset = new WsShooter.Preset(firstEnterWheelSetPoint.getValue(),
                firstExitWheelSetPoint.getValue(),
                firstShooterAngle.getValue()
                ? DoubleSolenoid.Value.kForward : DoubleSolenoid.Value.kReverse);
        secondShooterPreset = new WsShooter.Preset(secondEnterWheelSetPoint.getValue(),
                secondExitWheelSetPoint.getValue(),
                secondShooterAngle.getValue()
                ? DoubleSolenoid.Value.kForward : DoubleSolenoid.Value.kReverse);
    }

    public WsAutonomousProgramShootSevenActiveAccumulator() {
        super(24);
    }

    public void defineSteps() {
        defineConfigValues();
        WsAutonomousParallelStepGroup pg1 = new WsAutonomousParallelStepGroup("Set shooter, drop accumulator");
        programSteps[0] = pg1;
            pg1.addStep(new WsAutonomousStepSetShooterPreset(startPreset.ENTER_WHEEL_SET_POINT, startPreset.EXIT_WHEEL_SET_POINT, startPreset.ANGLE));
            pg1.addStep(new WsAutonomousStepLowerAccumulator());
            pg1.addStep(new WsAutonomousStepWaitForShooter());
        programSteps[1] = new WsAutonomousStepMultikick(2);  
        programSteps[2] = new WsAutonomousStepDelay(thirdFrisbeeDelay.getValue());
        programSteps[3] = new WsAutonomousStepMultikick(1);
        WsAutonomousParallelStepGroup pg2 = new WsAutonomousParallelStepGroup("Drop hopper and turn on accum");
        programSteps[4] = pg2;
            pg2.addStep(new WsAutonomousStepLowerHopper());
            pg2.addStep(new WsAutonomousStepIntakeMotorPullFrisbeesIn());
            pg2.addStep(new WsAutonomousStepStartDriveUsingMotionProfile(firstDrive.getValue(), 0.0));
        programSteps[5] = new WsAutonomousStepWaitForDriveMotionProfile(); 
        programSteps[6] = new WsAutonomousStepStopDriveUsingMotionProfile();
        programSteps[7] = new WsAutonomousStepIntakeMotorStop();
        WsAutonomousParallelStepGroup pg3 = new WsAutonomousParallelStepGroup("Raise accumulator and wait for it");
        programSteps[8] = pg3;
            pg3.addStep(new WsAutonomousStepRaiseAccumulator());
            pg3.addStep(new WsAutonomousStepWaitForAccumulatorUp());
        programSteps[9] = new WsAutonomousStepIntakeMotorPullFrisbeesIn();
        WsAutonomousParallelStepGroup pgIntake = new WsAutonomousParallelStepGroup("Wait for intake");
        programSteps[10] = pgIntake;
            pgIntake.addStep(new WsAutonomousStepWaitForDiscsLatchedThroughFunnelator());            
            pgIntake.addStep(new WsAutonomousStepDelay(1000));  //Min delay since it is not "finished on any"
       
        WsAutonomousParallelStepGroup pg4 = new WsAutonomousParallelStepGroup("Set up for intake");
        programSteps[11] = pg4;
            pg4.addStep(new WsAutonomousStepLowerHopper());
            pg4.addStep(new WsAutonomousStepLowerAccumulator());
            pg4.addStep(new WsAutonomousStepIntakeMotorPullFrisbeesIn());
            pg4.addStep(new WsAutonomousStepStartDriveUsingMotionProfile(secondDrive.getValue(), 0.0));
        programSteps[12] = new WsAutonomousStepWaitForDriveMotionProfile(); 
        programSteps[13] = new WsAutonomousStepStopDriveUsingMotionProfile();
        programSteps[14] = new WsAutonomousStepIntakeMotorStop();
        WsAutonomousParallelStepGroup pg5 = new WsAutonomousParallelStepGroup("Raise accumulator and wait for it");
        programSteps[15] = pg5;
            pg5.addStep(new WsAutonomousStepRaiseAccumulator());
            pg5.addStep(new WsAutonomousStepWaitForAccumulatorUp());
        WsAutonomousParallelStepGroup pg6 = new WsAutonomousParallelStepGroup("Intake and drive");
        WsAutonomousParallelStepGroup pg7 = new WsAutonomousParallelStepGroup("5 Drive and shooter set up");
        programSteps[16] = pg7;
            pg7.addStep(new WsAutonomousStepStartDriveUsingMotionProfile(thirdDrive.getValue(), 0.0));
            pg7.addStep(new WsAutonomousStepSetShooterPreset(secondShooterPreset.ENTER_WHEEL_SET_POINT, secondShooterPreset.EXIT_WHEEL_SET_POINT, startPreset.ANGLE));
            pg7.addStep(new WsAutonomousStepIntakeMotorPullFrisbeesIn());
        WsAutonomousParallelStepGroup pgIntakeDrive = new WsAutonomousParallelStepGroup("Intake and drive");
        WsAutonomousParallelStepGroup pgIntake2 = new WsAutonomousParallelStepGroup("Wait for intake");
        WsAutonomousSerialStepContainer pssDrive = new WsAutonomousSerialStepContainer("Wait for Drive and stop");
        WsAutonomousSerialStepContainer pssIntakeThenRaise = new WsAutonomousSerialStepContainer("Wait for Drive and stop");
        programSteps[17] = pgIntakeDrive;
            pgIntakeDrive.addStep(pssIntakeThenRaise);
                    pssIntakeThenRaise.addStep(pgIntake2);
                        pgIntake2.addStep(new WsAutonomousStepWaitForDiscsLatchedThroughFunnelator());            
                        pgIntake2.addStep(new WsAutonomousStepDelay(1000)); //Min delay since it is not "finished on any"
                    pssIntakeThenRaise.addStep(new WsAutonomousStepIntakeMotorStop());
                    pssIntakeThenRaise.addStep(new WsAutonomousStepSetShooterPreset(secondShooterPreset.ENTER_WHEEL_SET_POINT, secondShooterPreset.EXIT_WHEEL_SET_POINT, secondShooterPreset.ANGLE));
                
            pgIntakeDrive.addStep(pssDrive);
                pssDrive.addStep(new WsAutonomousStepWaitForDriveMotionProfile());    
                pssDrive.addStep( new WsAutonomousStepStopDriveUsingMotionProfile());   
       

        programSteps[18] = new WsAutonomousStepRaiseHopper();
        programSteps[19] = new WsAutonomousStepWaitForHopperUp();
        programSteps[20] = new WsAutonomousStepWaitForShooter(); 
        programSteps[21] = new WsAutonomousStepDelay(200);
        programSteps[22] = new WsAutonomousStepMultikick(4);
        programSteps[23] = new WsAutonomousStepSetShooterPreset(0, 0, DoubleSolenoid.Value.kReverse);
    }

    public String toString() {
        return "Shooting Seven Frisbees with active accumulator optimizations";
    }
}
