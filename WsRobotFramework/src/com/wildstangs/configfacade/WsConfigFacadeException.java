package com.wildstangs.configfacade;

/**
 *
 * @author Nathan
 */
public class WsConfigFacadeException extends Exception {
        public WsConfigFacadeException(String message) {
        super(message);
        System.out.println(message);
    }
}