/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.subsystems;

import com.wildstangs.subjects.base.IObserver;
import com.wildstangs.subjects.base.Subject;
import com.wildstangs.subsystems.base.WsSubsystem;

/**
 *
 * @author Nathan
 */
public class WsDriveBase extends WsSubsystem implements IObserver {

    public WsDriveBase(String name) {
        super(name);
    }

    public void acceptNotification(Subject subjectThatCaused) {
    }

    public void update() {
    }
}
