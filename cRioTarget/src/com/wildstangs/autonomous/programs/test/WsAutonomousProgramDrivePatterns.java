/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wildstangs.autonomous.programs.test;

import com.wildstangs.autonomous.WsAutonomousManager;
import com.wildstangs.autonomous.WsAutonomousProgram;
import com.wildstangs.autonomous.steps.drivebase.WsAutonomousStepQuickTurn;
import com.wildstangs.autonomous.steps.drivebase.WsAutonomousStepSetDriveHeadingPidRelativeSetpoint;
import com.wildstangs.autonomous.steps.drivebase.WsAutonomousStepStartDriveUsingMotionProfile;
import com.wildstangs.autonomous.steps.drivebase.WsAutonomousStepWaitForDriveMotionProfile;
import com.wildstangs.config.DoubleConfigFileParameter;

/**
 *
 * @author Joey
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
public class WsAutonomousProgramDrivePatterns extends WsAutonomousProgram
{
    private DoubleConfigFileParameter firstAngle = new DoubleConfigFileParameter(
               this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + "FirstRelativeAngle", 45);
    private DoubleConfigFileParameter secondAngle = new DoubleConfigFileParameter(
               this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + "SecondRelativeAngle", 45);
    private DoubleConfigFileParameter firstDriveDistance = new DoubleConfigFileParameter(
               this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + "FirstDriveDistance", 100);
    private DoubleConfigFileParameter firstDriveVelocity = new DoubleConfigFileParameter(
               this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + "FirstDriveVelocity", 1.0);
    private DoubleConfigFileParameter secondDriveDistance = new DoubleConfigFileParameter(
               this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + "SecondDriveDistance", 30);
    private DoubleConfigFileParameter secondDriveVelocity = new DoubleConfigFileParameter(
               this.getClass().getName(), WsAutonomousManager.getInstance().getStartPosition().toConfigString() + "SecondDriveVelocity", 1.0);
    public WsAutonomousProgramDrivePatterns()
    {
        super(7);
    }
    
    public void defineSteps()
    {
        programSteps[0] = new WsAutonomousStepSetDriveHeadingPidRelativeSetpoint(firstAngle.getValue());
        programSteps[1] = new WsAutonomousStepStartDriveUsingMotionProfile(firstDriveDistance.getValue(), firstDriveVelocity.getValue());
        programSteps[2] = new WsAutonomousStepWaitForDriveMotionProfile();
        programSteps[3] = new WsAutonomousStepQuickTurn(secondAngle.getValue());
        programSteps[4] = new WsAutonomousStepSetDriveHeadingPidRelativeSetpoint(0);
        programSteps[5] = new WsAutonomousStepStartDriveUsingMotionProfile(secondDriveDistance.getValue(), secondDriveVelocity.getValue());
        programSteps[6] = new WsAutonomousStepWaitForDriveMotionProfile();
    }
    
    public String toString()
    {
        return "Testing drive patterns for after shoot 5";
    }
}

