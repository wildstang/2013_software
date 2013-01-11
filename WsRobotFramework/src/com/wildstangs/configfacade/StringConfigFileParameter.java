/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.configfacade;

/**
 *
 * @author Nathan
 */
public class StringConfigFileParameter extends ConfigFileParameter {
    String defaultValue;
    
 /**
  * Create a String config file parameter.
  * 
  * @param cName The class name to get
  * @param pName The parameter name get
  * @param defValue A default value for the parameter.
  */   
 public StringConfigFileParameter(String cName, String pName, 
                String defValue) {
     super(cName, pName);
     defaultValue = defValue;
 }
 
 /**
  * Retrieve the config file parameter.
  * 
  * @return the config file parameter value. 
  */
 public String getValue() {
     String fullName = getFullParamName();
     try {
         return WsConfigFacade.getInstance().getConfigParamByName(fullName);
     }
     catch (WsConfigFacadeException e) {
         return defaultValue;
     }
 }
}
