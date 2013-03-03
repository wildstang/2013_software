/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.subsystems;

import com.wildstangs.inputfacade.base.WsInputFacade;
import com.wildstangs.inputfacade.inputs.joystick.driver.WsDriverJoystickButtonEnum;
import com.wildstangs.logger.Logger;
import com.wildstangs.subjects.base.BooleanSubject;
import com.wildstangs.subjects.base.IObserver;
import com.wildstangs.subjects.base.Subject;
import com.wildstangs.subsystems.base.WsSubsystem;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.networktables2.type.NumberArray;


/**
 *
 * @author Rick
 */
public class WsVision extends WsSubsystem implements IObserver
{
    private Point rectPoints[] = new Point[4];
    private NetworkTable server;
    
    public WsVision (String name) 
    {
         super(name);

         Subject subject = WsInputFacade.getInstance().getOiInput(WsInputFacade.DRIVER_JOYSTICK).getSubject(WsDriverJoystickButtonEnum.BUTTON4);
         subject.attach(this);
         
         server = NetworkTable.getTable("SmartDashboard");
         init();
    }

    public void init()
    {
    }
    
    public void update() 
    {
        
        NumberArray numbers = new NumberArray();
        server.retrieveValue("BFR_COORDINATES", numbers);
        
        for(int c = 0; c < numbers.size(); c++)
        {
            Logger.getLogger().debug(this.getName(), "BFR " + c, Double.toString(numbers.get(c)));
        }
    }

    public void notifyConfigChange() {
    }

    public void acceptNotification(Subject subjectThatCaused) 
    {
        BooleanSubject button = (BooleanSubject)subjectThatCaused;
    }
    
    private class Point
    {
        public double x, y;
    }
}
