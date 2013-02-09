/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous;

import com.wildstangs.autonomous.programs.*;
import com.wildstangs.inputfacade.base.WsInputFacade;
import com.wildstangs.subjects.base.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author coder65535
 */
public class WsAutonomousManager implements IObserver {

    private WsAutonomousProgram[] programs;
    private int currentProgram, lockedProgram;
    private float selectorSwitch;
    private WsAutonomousProgram runningProgram;
    private boolean programFinished, programRunning, lockInSwitch;
    private static WsAutonomousManager instance = null;

    private WsAutonomousManager() {
        definePrograms();
        WsInputFacade.getInstance().getOiInput(WsInputFacade.AUTO_PROGRAM_SELECTOR).getSubject((ISubjectEnum) null).attach(this);
        WsInputFacade.getInstance().getOiInput(WsInputFacade.LOCK_IN_SWITCH).getSubject((ISubjectEnum) null).attach(this);
        selectorSwitch = 0;
        lockInSwitch = false;
    }

    public void update() {
        if (programFinished) {
            runningProgram.cleanup();
            programFinished = false;
            lockedProgram = 0;
            startCurrentProgram();
        }
        runningProgram.update();
        if (runningProgram.isFinished()) {
            programFinished = true;
        }
    }

    public void startCurrentProgram() {
        runningProgram = programs[lockedProgram];
        runningProgram.initialize();
        SmartDashboard.putString("Running Autonomous Program", runningProgram.toString());
    }

    public void clear() {
        programFinished = false;
        programRunning = false;
        if (runningProgram != null) {
            runningProgram.cleanup();
        }
        runningProgram = programs[0];
        lockedProgram = 0;
        SmartDashboard.putString("Running Autonomous Program", "No Program Running");
        SmartDashboard.putString("Locked Autonomous Program", programs[lockedProgram].toString());
        SmartDashboard.putString("Current Autonomous Program", programs[currentProgram].toString());
    }

    public WsAutonomousProgram getRunningProgram() {
        if (programRunning) {
            return runningProgram;
        } else {
            return (WsAutonomousProgram) null;
        }
    }

    public String getRunningProgramName() {
        return runningProgram.toString();
    }

    public String getSelectedProgramName() {
        return programs[currentProgram].toString();
    }

    public String getLockedProgramName() {
        return programs[lockedProgram].toString();
    }

    public void acceptNotification(Subject cause) {
        if (cause instanceof DoubleSubject) {
            selectorSwitch = (float) ((DoubleSubject) cause).getValue();
            if (selectorSwitch >= 3.3) {
                selectorSwitch = 3.3f;
            }
            currentProgram = (int) (Math.floor((selectorSwitch / 3.4) * programs.length));
            SmartDashboard.putString("Current Autonomous Program", programs[currentProgram].toString());
        } else if (cause instanceof BooleanSubject) {
            lockInSwitch = ((BooleanSubject) cause).getValue();
            lockedProgram = lockInSwitch ? currentProgram : 0;
            SmartDashboard.putString("Locked Autonomous Program", programs[lockedProgram].toString());
        }
    }

    public static WsAutonomousManager getInstance() {
        if (WsAutonomousManager.instance == null) {
            WsAutonomousManager.instance = new WsAutonomousManager();
        }
        return WsAutonomousManager.instance;
    }

    private void definePrograms() {
        programs = new WsAutonomousProgram[6];
        programs[0] = new WsAutonomousProgramSleeper(); //Always leave Sleeper as 0. Other parts of the code assume 0 is Sleeper.
        programs[1] = new WsAutonomousProgramForwardsTest();
        programs[2] = new WsAutonomousProgramDriveDistance();
        programs[3] = new WsAutonomousProgramHopperTest();
        programs[4] = new WsAutonomousProgramTestParallel();
        programs[5] = new WsAutonomousProgramTestIntake();
    }
}
