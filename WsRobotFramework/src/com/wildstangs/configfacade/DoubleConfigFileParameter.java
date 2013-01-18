package com.wildstangs.configfacade;

/**
 *
 * @author Nathan
 */
public class DoubleConfigFileParameter extends ConfigFileParameter {
        double defaultValue;

    /**
     * Creates a double config file parameter.
     * 
     * @param cName The name of the class
     * @param pName The name of the parameter
     * @param defValue a default value to use.
     */
    public DoubleConfigFileParameter(String cName, String pName,
            double defValue) {
        super(cName, pName);
        defaultValue = defValue;
    }
    
 /**
  * Retrieve the config file parameter.
  * 
  * @return the config file parameter value. 
  */
    public double getValue() {
        String fullName = getFullParamName();
        try {
            Double d = Double.parseDouble(WsConfigFacade.getInstance().getConfigParamByName(fullName));
            return d.doubleValue();
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
