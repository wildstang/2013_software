/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.subsystems.base;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

/**
 *
 * @author Nathan
 */
public class WsSubsystemContainer {

    private static WsSubsystemContainer instance = null;
    private static Hashtable subsystem = new Hashtable();

    public static WsSubsystemContainer getInstance() {
        if (instance == null) {
            instance = new WsSubsystemContainer();
        }
        return instance;
    }

    /**
     * Retrieves a subsystem based on a key value.
     *
     * @param key The key representing the subsystem.
     * @return A subsystem.
     */
    public WsSubsystem getSubsystem(String key) {
        return (WsSubsystem) subsystem.get(key);
    }

    /**
     * Triggers all subsystems to be updated.
     */
    public void update() {
        Collection c = subsystem.values();
        Iterator i = c.iterator();
        while (i.hasNext()) {
            ((WsSubsystem) i.next()).update();
        }
    }

    /**
     * Notifies all subsystems a config change has occurred and config params
     * should be re-read.
     */
    public void notifyConfigChange() {
        Collection c = subsystem.values();
        Iterator i = c.iterator();
        while (i.hasNext()) {
            ((WsSubsystem) i.next()).notifyConfigChange();
        }
    }
    //Subsystem keys - must add a new key for each subsystem.

    /**
     * Constructor for the subsystem container.
     *
     * Each new subsystem must be added here. This is where they are
     * instantiated as well as placed in the subsystem container.
     */
    protected WsSubsystemContainer() {
    }
}
