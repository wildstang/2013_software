/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wildstangs.autonomous.programs;

import com.wildstangs.autonomous.WsAutonomousProgram;
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
    private IntegerConfigFileParameter toPyramidEnterWheelSetPoint = new IntegerConfigFileParameter(
            this.getClass().getName(), "ToPyramidEnterWheelSetPoint", 5000);
    private IntegerConfigFileParameter toPyramidExitWheelSetPoint = new IntegerConfigFileParameter(
            this.getClass().getName(), "ToPyramidExitWheelSetPoint", 4500);
    private BooleanConfigFileParameter toPyramidShooterAngle = new BooleanConfigFileParameter(
            this.getClass().getName(), "ToPyramidShooterAngle", false);
    
    private WsShooter.Preset toPyramidStartPreset;
    
    public WsAutonomousProgramShootSeven()
    {
        super(6);
        toPyramidStartPreset = new WsShooter.Preset(toPyramidEnterWheelSetPoint.getValue(),
                toPyramidExitWheelSetPoint.getValue(),
                toPyramidShooterAngle.getValue() ?
                DoubleSolenoid.Value.kReverse : DoubleSolenoid.Value.kForward);
    }
    public void defineSteps()
    {
        programSteps[0] = new WsAutonomousStepGroupDriveToPyramidFrisbees(toPyramidStartDrive.getValue()
                , toPyramidAngleTurn.getValue(), toPyramidSecondDrive.getValue()
                , toPyramidStartPreset);
    }
    
    public String toString()
    {
        return "Shooting Seven Frisbees";
    }
}

