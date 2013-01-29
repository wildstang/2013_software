/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.configfacade;

import com.wildstangs.configfacade.impl.WsConfigFacadeImpl;
import com.wildstangs.configfacade.impl.WsConfigFacadeImplException;
import com.wildstangs.inputfacade.base.WsInputFacade;
import com.wildstangs.logger.Logger;
import com.wildstangs.outputfacade.base.WsOutputFacade;
import com.wildstangs.subsystems.base.WsSubsystemContainer;
import com.wildstangs.types.DataElement;
import edu.wpi.first.wpilibj.networktables2.util.List;

/**
 *
 * @author Nathan
 */
public class WsConfigFacade {
    static String myClassName = "WsConfigFacade";

    private static WsConfigFacade instance = null;
    private static String configFileName = "/ws_config.txt";
    private static List config = new List();

    /**
     * Gets the instance of the WsConfigFacade Singleton.
     *
     * @return The instance of the WsConfigFacade.
     */
    public static WsConfigFacade getInstance() {
        if (instance == null) {
            instance = new WsConfigFacade();
        }
        return instance;
    }

    protected WsConfigFacade() {
    }

    /**
     * Sets the filename to parse. Overrides the default /ws_config.txt. The
     * filename will only be set if it exists and is readable.
     *
     * @param filename The new filename to use for reading.
     * @throws WsConfigFacadeException
     */
    public void setFileName(String filename) throws WsConfigFacadeException {
        if (WsConfigFacadeImpl.checkCreateFile(filename)) {
            configFileName = filename;
        } else {
            throw new WsConfigFacadeException("Problem setting config file name");
        }

    }

    /**
     * Reads the config file /ws_config.txt unless setFileName has been used to
     * change the filename. Supports comment lines using // delineator. Comments
     * can be by themselves on a line or at the end of the line.
     *
     * The config file should be on the format key=value Example:
     * com.wildstangs.WsInputFacade.WsDriverJoystick.trim=0.1
     *
     * @throws WsConfigFacadeException
     */
    public void readConfig() throws WsConfigFacadeException {
        try {
            config = (List) WsConfigFacadeImpl.readConfig(configFileName);
        } catch (WsConfigFacadeImplException e) {
            throw new WsConfigFacadeException(e.toString());
        }
        Logger.getLogger().always(this.getClass().getName(), "readConfig", "Read config File: " + configFileName);
        //Update all the facades
        WsInputFacade.getInstance().notifyConfigChange();
        WsOutputFacade.getInstance().notifyConfigChange();
        WsSubsystemContainer.getInstance().notifyConfigChange();

    }

    /**
     * Return a Config value the matches the key
     *
     * @param name The key value to search for.
     * @return An Object that contains the value.
     * @throws WsConfigFacadeException if the key cannot be found.
     */
    public String getConfigParamByName(String name) throws WsConfigFacadeException {
        for (int i = 0; i < config.size(); i++) {
            if ((((String) ((DataElement) config.get(i)).getKey())).equals(name)) {
                return (String) ((DataElement) config.get(i)).getValue();
            }
        }
        throw new WsConfigFacadeException("Config Param " + name + " not found");

    }

    public String dumpConfigData() {
        for (int i = 0; i < config.size(); i++) {
            String name = ((String) ((DataElement) config.get(i)).getKey());
            String value = ((String) ((DataElement) config.get(i)).getValue().toString());
            System.out.println(myClassName + "dumpConfigData" + name + "=" + value);
        }
        return null;
    }

    /**
     * Config Item name parser
     *
     * Example: com.wildstangs.WsInputFacade.WsDriverJoystick.trim will return
     * trim.
     *
     * @returns The config Item name or null if the string is unparsable
     * @param configItem A String representing the config item to parse
     */
    public String getConfigItemName(String configItem) {
        return WsConfigFacadeImpl.getConfigItemName(configItem);
    }
}
