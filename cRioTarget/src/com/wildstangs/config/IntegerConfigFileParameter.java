package com.wildstangs.config;

import com.wildstangs.configfacade.WsConfigFacade;
import com.wildstangs.configfacade.WsConfigFacade;

/**
 *
 * @author Nathan
 */
public class IntegerConfigFileParameter extends ConfigFileParameter {
    int defaultValue;

    /**
     * Creates an integer config file parameter.
     * 
     * @param cName The name of the class
     * @param pName The name of the parameter
     * @param defValue a default value to use.
     */
    public IntegerConfigFileParameter(String cName, String pName,
            int defValue) {
        super(cName, pName);
        defaultValue = defValue;
    }
    
 /**
  * Retrieve the config file parameter.
  * 
  * @return the config file parameter value. 
  */
    public int getValue() {
        String fullName = getFullParamName();
        try {
            return Integer.parseInt(WsConfigFacade.getInstance().getConfigParamByName(fullName));
        } catch (Throwable e) {
            return defaultValue;
        }
    }
}