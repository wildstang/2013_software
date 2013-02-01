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
        try {
            WsConfigFacade.getInstance().setFileName("/ws_config.txt");
            WsConfigFacade.getInstance().readConfig();
            WsConfigFacade.getInstance().dumpConfigData();
        } catch (WsConfigFacadeException wscfe) {
            System.out.println(wscfe.toString());
        }
            WsSubsystemContainer.getInstance().init();
        
        Logger.getLogger().always(this.getClass().getName(), "robotInit", "Startup");
        WsInputFacade.getInstance();
        WsOutputFacade.getInstance();
        WsSubsystemContainer.getInstance();
        WsAutonomousManager.getInstance();
    }

    public void disabledInit()
    {
        WsAutonomousManager.getInstance().clear();
    }

    public void disabledPeriodic()
    {
        WsInputFacade.getInstance().updateOiData();
    }
    

    public void autonomousInit()
    {
        WsAutonomousManager.getInstance().startCurrentProgram();
    }
    
    public void teleopInit()
    {
        WsSubsystemContainer.getInstance().init();
    }
    
    public void autonomousInit()
    {
        WsSubsystemContainer.getInstance().init();
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
    public void teleopPeriodic() {
        WsInputFacade.getInstance().updateOiData();
        WsInputFacade.getInstance().updateSensorData();
        WsSubsystemContainer.getInstance().update();
        WsOutputFacade.getInstance().update();
        Watchdog.getInstance().feed();
    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        Watchdog.getInstance().feed();
    }
    
    public void disabledInit()
    {
        WsSubsystemContainer.getInstance().init();
        try
        {
            WsConfigFacade.getInstance().readConfig();
        }
        catch(Throwable e)
        {
            System.out.println(e.getMessage());
        }
        WsConfigFacade.getInstance().dumpConfigData();
    }
}
