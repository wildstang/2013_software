package com.wildstangs.inputfacade.base;

import com.wildstangs.inputfacade.inputs.driverstation.WsDSAnalogInput;
import com.wildstangs.inputfacade.inputs.driverstation.WsDSDigitalInput;
import com.wildstangs.inputfacade.inputs.joystick.driver.WsDriverJoystick;
import com.wildstangs.inputfacade.inputs.joystick.manipulator.WsManipulatorJoystick;
import edu.wpi.first.wpilibj.networktables2.util.List;
import com.wildstangs.types.DataElement;

/**
 *
 * @author Nathan
 */
public class WsInputFacade {

    private static WsInputFacade instance = null;
    private static List oiInputs = new List();
    private static List sensorInputs = new List();

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
        IInput sIn;
        for (int i = 0; i < sensorInputs.size(); i++) {
            sIn = (IInput)(((DataElement) sensorInputs.get(i)).getValue());
            sIn.pullData();
            sIn.update();
        }
    }

    /**
     * Method to trigger updates of all the oi data input containers.
     */
    public void updateOiData() {
        IInput oiIn;
        for (int i = 0; i < oiInputs.size(); i++) {
            oiIn = (IInput)(((DataElement) oiInputs.get(i)).getValue());
            oiIn.pullData();
            oiIn.update();
        }
    }
    
    public void updateOiDataAutonomous()
    {
        IInput oiIn;
        for (int i = 0; i < oiInputs.size(); i++) {
            oiIn = (IInput)(((DataElement) oiInputs.get(i)).getValue());
            if (!(oiIn instanceof WsDriverJoystick || oiIn instanceof WsManipulatorJoystick))
            {
                oiIn.pullData();
            }
            oiIn.update();
        }
    }

    /**
     * Method to notify all input containers that a config update occurred.
     *
     * Used by the ConfigFacade when the config is re-read.
     */
    public void notifyConfigChange() {
        for (int i = 0; i < sensorInputs.size(); i++) {
            ((IInput)(((DataElement) sensorInputs.get(i)).getValue())).notifyConfigChange();
        }
        for (int i = 0; i < oiInputs.size(); i++) {
            ((IInput)(((DataElement) oiInputs.get(i)).getValue())).notifyConfigChange();
        }
    }

    /**
     * Gets an OI container, based on a key.
     *
     * @param key The key that represents the OI input container
     * @return A WsInputInterface.
     */
    public IInput getOiInput(String key) {
        for (int i = 0; i < oiInputs.size(); i++) {
            if ((((DataElement) oiInputs.get(i)).getKey()).equals(key)) {
                return (IInput)(((DataElement) oiInputs.get(i)).getValue());
            }
        }
        return (IInput)null;
    }

    /**
     * Gets a sensor container, based on a key.
     *
     * @param key The key that represents the sensor input container
     * @return A WsInputInterface.
     */
    public IInput getSensorInput(String key) {
        for (int i = 0; i < sensorInputs.size(); i++) {
            if ((((DataElement) sensorInputs.get(i)).getKey()).equals(key)) {
                return (IInput)(((DataElement) sensorInputs.get(i)).getValue());
            }
        }
        return (IInput)null;
    }

    /**
     * Keys to represent Inputs
     */
    public static final String DRIVER_JOYSTICK = "DriverJoystick";
    public static final String MANIPULATOR_JOYSTICK = "ManipulatorJoystick";
    public static final String AUTO_PROGRAM_SELECTOR = "AutoProgramSelector";
    public static final String LOCK_IN_SWITCH = "LockInSwitch";

    /**
     * Constructor for the WsInputFacade.
     *
     * Each new data element to be added to the facade must be added here and
     * have keys added above.
     */
    protected WsInputFacade() {
        //Add the facade data elements
        oiInputs.add(new DataElement(DRIVER_JOYSTICK, new WsDriverJoystick()));
        oiInputs.add(new DataElement(MANIPULATOR_JOYSTICK, new WsManipulatorJoystick()));
        oiInputs.add(new DataElement(AUTO_PROGRAM_SELECTOR, new WsDSAnalogInput(2)));
        oiInputs.add(new DataElement(LOCK_IN_SWITCH, new WsDSDigitalInput(1)));
    }
}
