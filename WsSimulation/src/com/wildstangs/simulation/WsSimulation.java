/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.simulation;

import com.wildstangs.autonomous.WsAutonomousManager;
import com.wildstangs.autonomous.WsAutonomousProgram;
import com.wildstangs.configfacade.WsConfigFacade;
import com.wildstangs.configfacade.WsConfigFacadeException;
import com.wildstangs.inputfacade.base.WsInputFacade;
import com.wildstangs.logger.*;
import com.wildstangs.logviewer.LogViewer;
import com.wildstangs.outputfacade.base.IOutputEnum;
import com.wildstangs.outputfacade.base.WsOutputFacade;
import com.wildstangs.outputfacade.outputs.WsDriveSpeed;
import com.wildstangs.outputfacade.outputs.WsVictor;
import com.wildstangs.profiling.WsProfilingTimer;
import com.wildstangs.subjects.base.Subject;
import com.wildstangs.subsystems.WsDriveBase;
import com.wildstangs.subsystems.base.WsSubsystemContainer;
import edu.wpi.first.wpilibj.*;

/**
 *
 * @author ChadS
 */
public class WsSimulation {

    static String c = "WsSimulation";
    
    static boolean autonomousRun = false;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        WsProfilingTimer durationTimer = new WsProfilingTimer("Sim method duration", 50);
        WsProfilingTimer periodTimer = new WsProfilingTimer("Sim method period", 50);

        //Instantiate the Facades and Containers

        //start the log viewer.
        (new Thread(new LogViewer())).start();

        try {
            WsConfigFacade.getInstance().setFileName("/Config/ws_config.txt");
            WsConfigFacade.getInstance().readConfig();
            //System.out.println(WsConfigFacade.getInstance().getConfigParamByName("com.wildstangs.WsInputFacade.WsDriverJoystick.trim"));
        } catch (WsConfigFacadeException wscfe) {
            System.out.println(wscfe.toString());
        }
        WsConfigFacade.getInstance().dumpConfigData();

        Logger logger = Logger.getLogger();

        //System.out.println(WsConfigFacade.getInstance().getConfigItemName("com.wildstangs.WsInputFacade.WsDriverJoystick.trim"));
        //System.out.println(WsConfigFacade.getInstance().dumpConfigData());
        logger.always(c, "sim_startup", "Simulation starting.");
        //logger.setLogLevel(Level.ALL);
        //logger.fatal(c, "fatal_test", "fatal");
        //logger.error(c, "erro_test", "error");
        //logger.notice(c, "notice_test", "notice");
        //logger.info(c, "info_test", "info");
        //logger.debug(c, "debug_test", "debug");
        //logger.warning(c, "warning_test", "warning");
        //logger.always(c, "always_test", "always");
        WsInputFacade.getInstance();
        WsOutputFacade.getInstance();
        WsSubsystemContainer.getInstance();

        Subject subject = ((WsDriveSpeed) WsOutputFacade.getInstance().getOutput(WsOutputFacade.LEFT_DRIVE_SPEED)).getSubject(null);
        DoubleSubjectGraph leftDriveSpeed = new DoubleSubjectGraph("Left Drive Speed", subject);

        subject = ((WsDriveSpeed) WsOutputFacade.getInstance().getOutput(WsOutputFacade.RIGHT_DRIVE_SPEED)).getSubject(null);
        DoubleSubjectGraph rightDriveSpeed = new DoubleSubjectGraph("Right Drive Speed", subject);

        subject = ((WsVictor) WsOutputFacade.getInstance().getOutput(WsOutputFacade.ACCUMULATOR_VICTOR)).getSubject(null);
        DoubleSubjectGraph accumulatorSpeed = new DoubleSubjectGraph("Accumulator Speed", subject);
        
        subject = ((WsVictor) WsOutputFacade.getInstance().getOutput(WsOutputFacade.FUNNELATOR_ROLLER)).getSubject(null);
        DoubleSubjectGraph funnelatorSpeed = new DoubleSubjectGraph("Funnelator Speed", subject);

//        double pid_setpoint = 10;
//        ((WsDriveBase) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_DRIVE_BASE)).enableDistancePidControl();
//        ((WsDriveBase) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_DRIVE_BASE)).setDriveDistancePidSetpoint(pid_setpoint);
        
        int right_encoder = 0;
        int left_encoder = 0;
        double left_drive_speed = 0.0;
        double right_drive_speed = 0.0;
        periodTimer.startTimingSection();
        
        if(autonomousRun)
        {
            WsAutonomousManager.getInstance().setProgram(1);
            WsAutonomousManager.getInstance().startCurrentProgram();
        }
        
        while (true) {
            periodTimer.endTimingSection();
            periodTimer.startTimingSection();
            durationTimer.startTimingSection();
            ((Encoder) ((WsDriveBase) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_DRIVE_BASE)).getLeftEncoder()).set(left_encoder);
            ((Encoder) ((WsDriveBase) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_DRIVE_BASE)).getRightEncoder()).set(right_encoder);
            if (left_drive_speed > 0) {
                left_encoder++;
            } else {
                left_encoder--;
            }
            if (right_drive_speed > 0) {
                right_encoder++;
            } else {
                right_encoder--;
            }

            //Update the Victor graphs
            leftDriveSpeed.update();
            rightDriveSpeed.update();
            accumulatorSpeed.update(); 
            funnelatorSpeed.update(); 

            WsInputFacade.getInstance().updateSensorData();
            if(autonomousRun)
            {
                WsInputFacade.getInstance().updateOiDataAutonomous();
                WsAutonomousManager.getInstance().update();
            }
            else
            {
                WsInputFacade.getInstance().updateOiData();
            }
            WsSubsystemContainer.getInstance().update();
            WsOutputFacade.getInstance().update();
            WsSolenoidContainer.getInstance().update();

            left_drive_speed = ((Double) WsOutputFacade.getInstance().getOutput(WsOutputFacade.LEFT_DRIVE_SPEED).get((IOutputEnum) null));
            right_drive_speed = ((Double) WsOutputFacade.getInstance().getOutput(WsOutputFacade.RIGHT_DRIVE_SPEED).get((IOutputEnum) null));
            
            
            durationTimer.endTimingSection();
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
            }

        }

    }
}
