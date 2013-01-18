/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous;

import com.wildstangs.autonomous.programs.WsAutonomousProgramSleeper;


/**
 *
 * @author coder65535
 */
public class WsAutonomousManager
{

    private WsAutonomousProgram[] programs;
    private int currentProgram;
    private boolean programFinished, finished;

    public WsAutonomousManager()
    {
        definePrograms();
        currentProgram = -1;
        finished = false;
        programFinished = true;
    }

    public void update()
    {
        if (finished)
        {
            return;
        }
        if (programFinished)
        {
            programs[currentProgram].cleanup();
            programFinished = false;
            currentProgram++;
            if (currentProgram >= programs.length)
            {
                finished = true;
                return;
            }
            programs[currentProgram].initialize();
        }
        WsAutonomousProgram program = programs[currentProgram]; //Prevent errors caused by mistyping.
        program.update();
        if (program.isFinished())
        {
            program.logResults();
            programFinished = true;
        }
    }

    private void definePrograms()
    {
        //TODO: implement reading of program list from config file
        programs = new WsAutonomousProgram[1];
        programs[0] = new WsAutonomousProgramSleeper();
    }
}
