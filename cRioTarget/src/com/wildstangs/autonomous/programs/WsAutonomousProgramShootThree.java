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

public class WsAutonomousProgramShootThree extends WsAutonomousProgram {

    private DoubleConfigFileParameter StartDrive;
    private DoubleConfigFileParameter SecondDrive;
    private IntegerConfigFileParameter FirstEnterWheelSetPoint;
    private IntegerConfigFileParameter FirstExitWheelSetPoint;
    private BooleanConfigFileParameter FirstShooterAngle;
    private IntegerConfigFileParameter ThirdFrisbeeDelay;
    private IntegerConfigFileParameter LowerAccumulatorDelay;
    private WsShooter.Preset startPreset;

    private void defineConfigValues() {
        StartDrive = new DoubleConfigFileParameter(this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".StartDrive", 60.5);
        SecondDrive = new DoubleConfigFileParameter(this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".SecondDrive", 60.5);
        FirstEnterWheelSetPoint = new IntegerConfigFileParameter(this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".FirstEnterWheelSetPoint", 5000);
        FirstExitWheelSetPoint = new IntegerConfigFileParameter(this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".FirstExitWheelSetPoint", 4500);
        FirstShooterAngle = new BooleanConfigFileParameter(this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".FirstShooterAngle", false);
        ThirdFrisbeeDelay = new IntegerConfigFileParameter(this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".ThirdFrisbeeDelay", 100);
        LowerAccumulatorDelay = new IntegerConfigFileParameter(this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + ".LowerAccumulatorDelay", 2000);

        startPreset = new WsShooter.Preset(FirstEnterWheelSetPoint.getValue(),
                FirstExitWheelSetPoint.getValue(),
                FirstShooterAngle.getValue()
                ? DoubleSolenoid.Value.kForward : DoubleSolenoid.Value.kReverse);
    }

    public WsAutonomousProgramShootThree() {
        super(14);
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
        programSteps[6] = new WsAutonomousStepMultikick(2);
        programSteps[7] = new WsAutonomousStepDelay(ThirdFrisbeeDelay.getValue());
        programSteps[8] = new WsAutonomousStepMultikick(1);
        programSteps[9] = new WsAutonomousStepLowerAccumulator();
        programSteps[10] = new WsAutonomousStepDelay(LowerAccumulatorDelay.getValue());
        programSteps[11] = new WsAutonomousStepSetShooterPreset(0, 0, DoubleSolenoid.Value.kReverse); 
        programSteps[12] = new WsAutonomousStepDelay(LowerAccumulatorDelay.getValue());
        programSteps[13] = new WsAutonomousStepRaiseAccumulator();
        
    }

    public String toString() {
        return "Shooting Three Frisbees";
    }
}
