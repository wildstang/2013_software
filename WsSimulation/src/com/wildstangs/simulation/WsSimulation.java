/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.simulation;

import com.wildstangs.configfacade.WsConfigFacade;
import com.wildstangs.configfacade.WsConfigFacadeException;
import com.wildstangs.inputfacade.base.WsInputFacade;
import com.wildstangs.outputfacade.base.WsOutputFacade;
import com.wildstangs.subsystems.base.WsSubsystemContainer;
import com.wildstangs.logviewer.LogViewer;
import com.wildstangs.logger.*;

/**
 *
 * @author ChadS
 */
public class WsSimulation {
    
    static String c = "WsSimulation";

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
        }
        catch (WsConfigFacadeException wscfe) {
            System.out.println(wscfe.toString());
        }
        
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
        
        
        
        while (true) {
            WsInputFacade.getInstance().updateOiData();
            WsInputFacade.getInstance().updateSensorData();
            WsSubsystemContainer.getInstance().update();
            WsOutputFacade.getInstance().update();
            WsSolenoidContainer.getInstance().update();
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                
            }
            
        }
        
    }
}

