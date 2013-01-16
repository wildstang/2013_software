package com.wildstangs.outputfacade.base;

import com.wildstangs.outputfacade.outputs.WsDriveSpeed;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

/**
 *
 * @author Nathan
 */
public class WsOutputFacade {

    private static WsOutputFacade instance = null;
    private static Hashtable outputs = new Hashtable();

    /**
     * Method to obtain the instance of the WsOutputFacade singleton.
     *
     * @return the instance of the WsOutputFacade.
     */
    public static WsOutputFacade getInstance() {
        if (instance == null) {
            instance = new WsOutputFacade();
        }
        return instance;
    }

    /**
     * Method to cause all output elements to update.
     */
    public void update() {
        Collection c = outputs.values();
        Iterator i = c.iterator();
        while (i.hasNext()) {
            ((IOutput) i.next()).update();
        }
    }

    /**
     * Method to notify all output elements that a config change has occurred
     * and config values need to be re-read.
     */
    public void notifyConfigChange() {
        Collection c = outputs.values();
        Iterator i = c.iterator();
        while (i.hasNext()) {
            ((IOutput) i.next()).notifyConfigChange();
        }
    }

    /**
     * Gets an output element based on a key.
     *
     * @param key A string representation of the output element.
     *
     * @return The output element.
     */
    public IOutput getOutput(String key) {
        return (IOutput) outputs.get(key);
    }
    //Key Values - Need to update for each new output element.
    public static final String RIGHT_DRIVE_SPEED = "RightDriveSpeed";
    public static final String LEFT_DRIVE_SPEED = "LeftDriveSpeed";

    /**
     * Constructor for WsOutputFacade.
     *
     * All new output elements need to be added in the constructor as well as
     * having a key value added above.
     */
    protected WsOutputFacade() {
        //Add the facade data elements
        outputs.put(RIGHT_DRIVE_SPEED, new WsDriveSpeed(RIGHT_DRIVE_SPEED, 1, 2));
        outputs.put(LEFT_DRIVE_SPEED, new WsDriveSpeed(LEFT_DRIVE_SPEED, 3, 4));
    }
}
