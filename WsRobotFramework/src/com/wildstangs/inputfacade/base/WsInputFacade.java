package com.wildstangs.inputfacade.base;

import com.wildstangs.inputfacade.inputs.joystick.driver.WsDriverJoystick;
import com.wildstangs.inputfacade.inputs.joystick.manipulator.WsManipulatorJoystick;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

/**
 *
 * @author Nathan
 */
public class WsInputFacade {

    private static WsInputFacade instance = null;
    private static Hashtable oiInputs = new Hashtable();
    private static Hashtable sensorInputs = new Hashtable();

    /**
     * Method to get the instance of this singleton object.
     *
     * @return The instance of WsInputFacade
     */
    public static WsInputFacade getInstance() {
        if (instance == null) {
            instance = new WsInputFacade();
        }
        return instance;
    }

    /**
     * Method to trigger updates of all the sensor data input containers
     */
    public void updateSensorData() {
        Collection sensorc = sensorInputs.values();
        Iterator sensorIter = sensorc.iterator();
        IInput sIn;
        while (sensorIter.hasNext()) {
            sIn = (IInput) sensorIter.next();
            sIn.pullData();
            sIn.update();
        }
    }

    /**
     * Method to trigger updates of all the oi data input containers.
     */
    public void updateOiData() {
        Collection oic = oiInputs.values();
        Iterator oiIter = oic.iterator();
        IInput oiIn;
        while (oiIter.hasNext()) {
            oiIn = (IInput) oiIter.next();
            oiIn.pullData();
            oiIn.update();
        }
    }

    /**
     * Method to notify all input containers that a config update occurred.
     *
     * Used by the ConfigFacade when the config is re-read.
     */
    public void notifyConfigChange() {
        Collection oic = oiInputs.values();
        Collection sc = sensorInputs.values();
        Iterator oiIter = oic.iterator();
        Iterator sIter = sc.iterator();
        IInput oiIn;
        while (oiIter.hasNext()) {
            oiIn = (IInput) oiIter.next();
            oiIn.notifyConfigChange();
        }
        while (sIter.hasNext()) {
            oiIn = (IInput) sIter.next();
            oiIn.notifyConfigChange();
        }
    }

    /**
     * Gets an OI container, based on a key.
     *
     * @param key The key that represents the OI input container
     * @return A WsInputInterface.
     */
    public IInput getOiInput(String key) {
        return (IInput) oiInputs.get(key);
    }

    /**
     * Gets a sensor container, based on a key.
     *
     * @param key The key that represents the sensor input container
     * @return A WsInputInterface.
     */
    public IInput getSensorInput(String key) {
        return (IInput) sensorInputs.get(key);
    }

    /**
     * Keys to represent Inputs
     */
    public static final String DRIVER_JOYSTICK = "DriverJoystick";
    public static final String MANIPULATOR_JOYSTICK = "ManipulatorJoystick";
    
    /**
     * Constructor for the WsInputFacade.
     *
     * Each new data element to be added to the facade must be added here and
     * have keys added above.
     */
    protected WsInputFacade() {
        //Add the facade data elements
        oiInputs.put(DRIVER_JOYSTICK, new WsDriverJoystick());
        oiInputs.put(MANIPULATOR_JOYSTICK, new WsManipulatorJoystick());
    }
}
