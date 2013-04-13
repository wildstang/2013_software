/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package com.wildstangs.crio;

import com.wildstangs.autonomous.WsAutonomousManager;
import com.wildstangs.configfacade.WsConfigFacade;
import com.wildstangs.configfacade.WsConfigFacadeException;
import com.wildstangs.inputfacade.base.WsInputFacade;
import com.wildstangs.logger.Logger;
import com.wildstangs.outputfacade.base.WsOutputFacade;
import com.wildstangs.profiling.WsProfilingTimer;
import com.wildstangs.subsystems.base.WsSubsystemContainer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Watchdog;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotTemplate extends IterativeRobot {

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        startupTimer.startTimingSection();
        System.out.println("RobotInit Start");
        try {
            WsConfigFacade.getInstance().setFileName("/ws_config.txt");
            WsConfigFacade.getInstance().readConfig();
            //WsConfigFacade.getInstance().dumpConfigData();
        } catch (WsConfigFacadeException wscfe) {
            System.out.println(wscfe.toString());
        }

        WsInputFacade.getInstance();
        WsOutputFacade.getInstance();
//        Logger.getLogger().always(this.getClass().getName(), "robotInit", "Facades Completed");
        WsSubsystemContainer.getInstance().init();
//        Logger.getLogger().always(this.getClass().getName(), "robotInit", "Subsystem Completed");
        Logger.getLogger().readConfig();
//        Logger.getLogger().always(this.getClass().getName(), "robotInit", "Logger Read Config Completed");
        WsAutonomousManager.getInstance();
        Logger.getLogger().always(this.getClass().getName(), "robotInit", "Startup Completed");
        startupTimer.endTimingSection();

    }
    WsProfilingTimer durationTimer = new WsProfilingTimer("Periodic method duration", 50);
    WsProfilingTimer periodTimer = new WsProfilingTimer("Periodic method period", 50);
    WsProfilingTimer startupTimer = new WsProfilingTimer("Startup duration", 1);
    WsProfilingTimer initTimer = new WsProfilingTimer("Init duration", 1);

    public void disabledInit() {
        initTimer.startTimingSection();
        WsAutonomousManager.getInstance().clear();
        try {
            WsConfigFacade.getInstance().readConfig();
        } catch (Throwable e) {
            System.out.println(e.getMessage());
        }
//        Logger.getLogger().always(this.getClass().getName(), "disbledInit", "Config Completed");
        WsSubsystemContainer.getInstance().init();
        Logger.getLogger().readConfig();
        //WsConfigFacade.getInstance().dumpConfigData();
        initTimer.endTimingSection();
        Logger.getLogger().always(this.getClass().getName(), "disabledInit", "Disabled Init Complete");
        
    }

    public void disabledPeriodic() {
        WsInputFacade.getInstance().updateOiData();
        //Make LED stuff go in disabled.
        ((WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_LED))).update();
    }

    public void autonomousInit() {
        WsSubsystemContainer.getInstance().init();
        Logger.getLogger().readConfig();
        WsAutonomousManager.getInstance().startCurrentProgram();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        WsInputFacade.getInstance().updateOiDataAutonomous();
        WsInputFacade.getInstance().updateSensorData();
        WsAutonomousManager.getInstance().update();
        WsSubsystemContainer.getInstance().update();
        WsOutputFacade.getInstance().update();
        Watchdog.getInstance().feed();
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopInit() {
        WsSubsystemContainer.getInstance().init();
        Logger.getLogger().readConfig();
        periodTimer.startTimingSection();
    }

    public void teleopPeriodic() {
//        periodTimer.endTimingSection();
//        periodTimer.startTimingSection();
//        durationTimer.startTimingSection();
        WsInputFacade.getInstance().updateOiData();
        WsInputFacade.getInstance().updateSensorData();
        WsSubsystemContainer.getInstance().update();
        WsOutputFacade.getInstance().update();
        Watchdog.getInstance().feed();
//        durationTimer.endTimingSection();
    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        Watchdog.getInstance().feed();
    }
}
