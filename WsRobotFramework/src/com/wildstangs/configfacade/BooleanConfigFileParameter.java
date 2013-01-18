/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.configfacade;

/**
 *
 * @author Nathan
 */
public class BooleanConfigFileParameter extends ConfigFileParameter {
    boolean defaultValue;

    /**
     * Creates a boolean config file parameter.
     * 
     * @param cName The name of the class
     * @param pName The name of the parameter
     * @param defValue a default value to use.
     */
    public BooleanConfigFileParameter(String cName, String pName,
            boolean defValue) {
        super(cName, pName);
        defaultValue = defValue;
    }
    
 /**
  * Retrieve the config file parameter.
  * 
  * @return the config file parameter value. 
  */
    public boolean getValue() {
        String fullName = getFullParamName();
        try {
            Boolean b = Boolean.parseBoolean(WsConfigFacade.getInstance().getConfigParamByName(fullName));
            return b.booleanValue();
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
