package com.wildstangs.configfacade;

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
            Integer i = Integer.parseInt(WsConfigFacade.getInstance().getConfigParamByName(fullName));
            return i.intValue();
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
