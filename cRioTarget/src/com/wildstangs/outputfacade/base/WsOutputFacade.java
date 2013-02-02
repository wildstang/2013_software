package com.wildstangs.outputfacade.base;

import com.wildstangs.outputfacade.outputs.WsDriveSpeed;
import com.wildstangs.outputfacade.outputs.WsSolenoid;
import com.wildstangs.outputfacade.outputs.WsDoubleSolenoid;
import com.wildstangs.outputfacade.outputs.WsVictor;
import edu.wpi.first.wpilibj.networktables2.util.List;
import com.wildstangs.types.DataElement;

/**
 *
 * @author Nathan
 */
public class WsOutputFacade {

    private static WsOutputFacade instance = null;
    private static List outputs = new List();

    /**
     * Method to obtain the instance of the WsOutputFacade singleton.
     *
     * @return the instance of the WsOutputFacade.
     */
    public static WsOutputFacade getInstance() {
        if (WsOutputFacade.instance == null) {
            WsOutputFacade.instance = new WsOutputFacade();
        }
        return WsOutputFacade.instance;
    }

    /**
     * Method to cause all output elements to update.
     */
    
    public void init()
    {
    }
    
    public void update() {
        for (int i = 0; i < outputs.size(); i++) {
            ((IOutput)(((DataElement) outputs.get(i)).getValue())).update();
        }
    }

    /**
     * Method to notify all output elements that a config change has occurred
     * and config values need to be re-read.
     */
    public void notifyConfigChange() {
        for (int i = 0; i < outputs.size(); i++) {
            ((IOutput)(((DataElement) outputs.get(i)).getValue())).notifyConfigChange();
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
        for (int i = 0; i < outputs.size(); i++) {
            if ((((DataElement) outputs.get(i)).getKey()).equals(key)) {
                return (IOutput)(((DataElement) outputs.get(i)).getValue());
            }
        }
        return (IOutput)null;
    }
    //Key Values - Need to update for each new output element.
    public static final String RIGHT_DRIVE_SPEED = "RightDriveSpeed";
    public static final String LEFT_DRIVE_SPEED = "LeftDriveSpeed";
    public static final String SHIFTER = "shifter";
    public static final String LIFT = "lift";
    public static final String KICKER = "kicker";
    public static final String SHOOTER_VICTOR_ENTER = "ShooterVictorEnter";
    public static final String SHOOTER_VICTOR_EXIT = "ShooterVictorExit";
    public static final String SHOOTER_ANGLE = "ShooterAngle";
    public static final String FRISBIE_CONTROL = "FrisbieControl";
    public static final String ACCUMULATOR_SOLENOID_LEFT = "AccumulatorSolenoidLeft";
    public static final String ACCUMULATOR_SOLENOID_RIGHT = "AccumulatorSolenoidRight";
    
    /**
     * Constructor for WsOutputFacade.
     *
     * All new output elements need to be added in the constructor as well as
     * having a key value added above.
     */
    protected WsOutputFacade() {
        //Add the facade data elements
        outputs.add(new DataElement(RIGHT_DRIVE_SPEED, new WsDriveSpeed(RIGHT_DRIVE_SPEED, 1, 2)));
        outputs.add(new DataElement(LEFT_DRIVE_SPEED, new WsDriveSpeed(LEFT_DRIVE_SPEED, 3, 4)));
        outputs.add(new DataElement(SHIFTER, new WsSolenoid(SHIFTER, 1, 4)));
        outputs.add(new DataElement(LIFT, new WsDoubleSolenoid(LIFT, 1, 1)));
        outputs.add(new DataElement(KICKER, new WsSolenoid(KICKER, 1, 2)));
        outputs.add(new DataElement(SHOOTER_VICTOR_ENTER, new WsVictor(SHOOTER_VICTOR_ENTER, 5)));
        outputs.add(new DataElement(SHOOTER_VICTOR_EXIT, new WsVictor(SHOOTER_VICTOR_EXIT, 6)));
        outputs.add(new DataElement(SHOOTER_ANGLE, new WsSolenoid(SHOOTER_ANGLE, 1, 5))); //probably needs different value
        outputs.add(new DataElement(FRISBIE_CONTROL, new WsSolenoid(FRISBIE_CONTROL, 1, 3)));
        outputs.add(new DataElement(ACCUMULATOR_SOLENOID_LEFT, new WsSolenoid(ACCUMULATOR_SOLENOID_LEFT, 1, 6)));
        outputs.add(new DataElement(ACCUMULATOR_SOLENOID_RIGHT, new WsSolenoid(ACCUMULATOR_SOLENOID_RIGHT, 1, 7)));

    }
}
