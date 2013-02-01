/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous;

import com.wildstangs.autonomous.programs.WsAutonomousProgramSleeper;
import com.wildstangs.inputfacade.base.WsInputFacade;
import com.wildstangs.subjects.base.*;

/**
 *
 * @author coder65535
 */
public class WsAutonomousManager implements IObserver
{

    private WsAutonomousProgram[] programs;
    private int currentProgram;
    private float selectorSwitch;
    private WsAutonomousProgram runningProgram;
    private boolean programFinished, programRunning, lockInSwitch;
    private static WsAutonomousManager instance = null;

    private WsAutonomousManager()
    {
        definePrograms();
        currentProgram = -1;
        programFinished = true;
        programRunning = false;
        runningProgram = programs[0];
        WsInputFacade.getInstance().getOiInput(WsInputFacade.AUTO_PROGRAM_SELECTOR).getSubject((ISubjectEnum)null).attach(this);
        WsInputFacade.getInstance().getOiInput(WsInputFacade.LOCK_IN_SWITCH).getSubject((ISubjectEnum)null).attach(this);
        selectorSwitch = 0;
        lockInSwitch = false;
    }

    public void update()

    {
        if (programFinished)
        {
            runningProgram.cleanup();
            programFinished = false;
            programs[0].initialize();
        }
        if (programRunning)
        {
            runningProgram.update();
            if (runningProgram.isFinished())
            {
                runningProgram.logResults();
                programFinished = true;
            }
        }
        else
        {
            programs[0].update();
        }
    }
    
    public void startCurrentProgram()
    {
        if (lockInSwitch)
        {
            runningProgram = programs[currentProgram];
        }
        else
        {
            runningProgram = programs[0];
        }
        runningProgram.initialize();
    }

    public WsAutonomousProgram getRunningProgram()
    {
        if (programRunning)
        {
            return runningProgram;
        }
        else
        {
            return (WsAutonomousProgram)null;
        }
    }

    public String getRunningProgramName()
    {
        return runningProgram.toString();
    }
    
    public String getSelectedProgramName()
    {
        return programs[currentProgram].toString();
    }
    
    public void acceptNotification(Subject cause)
    {
        if (cause instanceof DoubleSubject)
        {
            selectorSwitch = (float)((DoubleSubject)cause).getValue();
            currentProgram = (int)(Math.floor((selectorSwitch/3.33)*programs.length));
        }
        else if (cause instanceof BooleanSubject)
        {
            lockInSwitch = ((BooleanSubject)cause).getValue();
        }
    }
    
    public static WsAutonomousManager getInstance()
    {
        if (WsAutonomousManager.instance == null)
        {
            WsAutonomousManager.instance = new WsAutonomousManager();
        }
        return WsAutonomousManager.instance;
    }

    private void definePrograms()
    {
        //TODO: implement reading of program list from config file
        programs = new WsAutonomousProgram[1];
        programs[0] = new WsAutonomousProgramSleeper(); //Always leave Sleeper as 0. Other parts of the code assume 0 is Sleeper
    }
}