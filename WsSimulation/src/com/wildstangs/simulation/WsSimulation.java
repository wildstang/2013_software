/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.simulation;

import com.wildstangs.autonomous.WsAutonomousManager;
import com.wildstangs.configfacade.WsConfigFacade;
import com.wildstangs.configfacade.WsConfigFacadeException;
import com.wildstangs.inputfacade.base.WsInputFacade;
import com.wildstangs.inputfacade.inputs.joystick.driver.WsDriverJoystickEnum;
import com.wildstangs.logger.*;
import com.wildstangs.logviewer.LogViewer;
import com.wildstangs.outputfacade.base.WsOutputFacade;
import com.wildstangs.outputfacade.outputs.WsDriveSpeed;
import com.wildstangs.outputfacade.outputs.WsVictor;
import com.wildstangs.simulation.encoders.DriveBaseEncoders;
import com.wildstangs.simulation.encoders.FlywheelEncoders;
import com.wildstangs.simulation.funnelator.FunnelatorLimitSwitch;
import com.wildstangs.simulation.gyro.GyroSimulation;
import com.wildstangs.simulation.hopper.HopperLimitSwitches;
import com.wildstangs.subjects.base.Subject;
import com.wildstangs.subsystems.base.WsSubsystemContainer;
import edu.wpi.first.wpilibj.*;

/**
 *
 * @author ChadS
 */
public class WsSimulation {

    static String c = "WsSimulation";
    
    static boolean autonomousRun = true;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

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
//        WsConfigFacade.getInstance().dumpConfigData();

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

        
        subject = ((WsInputFacade.getInstance().getOiInput(WsInputFacade.DRIVER_JOYSTICK)).getSubject(WsDriverJoystickEnum.THROTTLE));
        DoubleSubjectGraph driverThrottle = new DoubleSubjectGraph("Driver Throttle", subject);


        subject = ((WsVictor) WsOutputFacade.getInstance().getOutput(WsOutputFacade.ACCUMULATOR_VICTOR)).getSubject(null);
        DoubleSubjectGraph accumulatorSpeed = new DoubleSubjectGraph("Accumulator Speed", subject);
        
        subject = ((WsVictor) WsOutputFacade.getInstance().getOutput(WsOutputFacade.FUNNELATOR_ROLLER)).getSubject(null);
        DoubleSubjectGraph funnelatorSpeed = new DoubleSubjectGraph("Funnelator Speed", subject);
        
        subject = ((WsVictor) WsOutputFacade.getInstance().getOutput(WsOutputFacade.SHOOTER_VICTOR_ENTER)).getSubject(null);
        DoubleSubjectGraph enterSpeed = new DoubleSubjectGraph("Enter Speed", subject);
        
        subject = ((WsVictor) WsOutputFacade.getInstance().getOutput(WsOutputFacade.SHOOTER_VICTOR_EXIT)).getSubject(null);
        DoubleSubjectGraph exitSpeed = new DoubleSubjectGraph("Exit Speed", subject);

        subject = ((WsDriveSpeed) WsOutputFacade.getInstance().getOutput(WsOutputFacade.RIGHT_DRIVE_SPEED)).getSubject(null);
        DoubleSubjectGraph rightDriveSpeed = new DoubleSubjectGraph("Right Drive Speed", subject);

//        double pid_setpoint = 10;
//        ((WsDriveBase) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_DRIVE_BASE)).enableDistancePidControl();
//        ((WsDriveBase) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_DRIVE_BASE)).setDriveDistancePidSetpoint(pid_setpoint);
        
        DriveBaseEncoders dbEncoders = new DriveBaseEncoders(); 
        FlywheelEncoders flywheelEncoders = new FlywheelEncoders(); 
        HopperLimitSwitches limitSwitches = new HopperLimitSwitches(); 
        AccumulatorLimitSwitch aclimitSwitches = new AccumulatorLimitSwitch(); 
        FunnelatorLimitSwitch funnellimitSwitches = new FunnelatorLimitSwitch();
        GyroSimulation gyro = new GyroSimulation();
//        periodTimer.startTimingSection();
        
//        ContinuousAccelFilter accelFilter = new ContinuousAccelFilter(0, 0, 0);
//        double distance_to_go = 60.5;
//        double currentProfileX =0.0; 
//        double currentProfileV =0.0; 
//        double currentProfileA =0.0; 
//        for (int i = 0; i < 60; i++) {
//            //Update measured values 
//            
//            //Update PID using profile velocity as setpoint and measured velocity as PID input 
//            
//            //Update system to get feed forward terms
//            double distance_left = distance_to_go - currentProfileX;
//            logger.debug(c, "AccelFilter", "distance_left: " + distance_left + " p: " + accelFilter.getCurrPos()+ " v: " + accelFilter.getCurrVel() + " a: " + accelFilter.getCurrAcc() );
//            accelFilter.calculateSystem(distance_left , currentProfileV, 0, 600, 102, 0.020);
//            currentProfileX = accelFilter.getCurrPos();
//            currentProfileV = accelFilter.getCurrVel();
//            currentProfileA = accelFilter.getCurrAcc();
//            
//            //Update motor output with PID output and feed forward velocity and acceleration 
//            
//        }
        
        
        logger.always(c, "sim_startup", "Simulation init done.");
        if(autonomousRun)
        {
            WsAutonomousManager.getInstance().setPosition(6);
            WsAutonomousManager.getInstance().setProgram(4);
            WsAutonomousManager.getInstance().startCurrentProgram();
        }
        
        while (true) {
//            periodTimer.endTimingSection();
//            periodTimer.startTimingSection();
//            durationTimer.startTimingSection();
            if (false == autonomousRun || (false == WsAutonomousManager.getInstance().getRunningProgramName().equalsIgnoreCase("Sleeper"))){
                
                //Update the Victor graphs
                leftDriveSpeed.update();
                rightDriveSpeed.update();
                accumulatorSpeed.update(); 
                funnelatorSpeed.update(); 
                driverThrottle.update(); 
                enterSpeed.update();
                exitSpeed.update();
                gyro.update();

                //Update the encoders
                dbEncoders.update();
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

                flywheelEncoders.update(); 
                limitSwitches.update();
                aclimitSwitches.update();
                funnellimitSwitches.update();
            }

//            durationTimer.endTimingSection();
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
            }

        }

    }
}
