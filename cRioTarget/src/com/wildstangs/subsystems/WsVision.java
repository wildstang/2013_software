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
 * @author User
 */
public class WsVision extends WsSubsystem implements IObserver
{
   public WsVision (String name) {
        super(name);
   }

    public void init()
    {
    }
    
    public void update() {
    }

    public void notifyConfigChange() {
    }

    public void acceptNotification(Subject subjectThatCaused) {
    }
}
