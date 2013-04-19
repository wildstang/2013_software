package com.wildstangs.autonomous.programs.test;

import com.wildstangs.autonomous.WsAutonomousProgram;
import com.wildstangs.autonomous.steps.hopper.WsAutonomousStepMultikick;
import com.wildstangs.autonomous.steps.shooter.WsAutonomousStepSetShooterPreset;
import com.wildstangs.autonomous.steps.shooter.WsAutonomousStepWaitForShooter;
import edu.wpi.first.wpilibj.DoubleSolenoid;

/**
 *
 * @author Nathan
 */
public class WsAutonomousProgramTestMultikick extends WsAutonomousProgram {

    public WsAutonomousProgramTestMultikick() {
        super(3);
    }

    public void defineSteps() {
        programSteps[0] = new WsAutonomousStepSetShooterPreset(2000, 2000, DoubleSolenoid.Value.kOff);
        programSteps[1] = new WsAutonomousStepWaitForShooter();
        programSteps[2] = new WsAutonomousStepMultikick(4);
    }

    public String toString() {
        return "Tests the multikick step group.";
    }
}
