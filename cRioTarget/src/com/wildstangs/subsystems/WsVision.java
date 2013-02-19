/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.subsystems;

import com.wildstangs.inputfacade.base.WsInputFacade;
import com.wildstangs.inputfacade.inputs.joystick.driver.WsDriverJoystickButtonEnum;
import com.wildstangs.subjects.base.BooleanSubject;
import com.wildstangs.subjects.base.IObserver;
import com.wildstangs.subjects.base.Subject;
import com.wildstangs.subsystems.base.WsSubsystem;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.networktables2.type.NumberArray;


/**
 *
 * @author User
 */
public class WsVision extends WsSubsystem implements IObserver
{
    private NetworkTable cameraTable;
    private Point rectPoints[] = new Point[4];
    private boolean turning;
    
    public WsVision (String name) 
    {
         super(name);

         Subject subject = WsInputFacade.getInstance().getOiInput(WsInputFacade.DRIVER_JOYSTICK).getSubject(WsDriverJoystickButtonEnum.BUTTON4);
         subject.attach(this);
         
         NetworkTable.setIPAddress("10.1.11.2");
         cameraTable = NetworkTable.getTable("camera");
         init();
    }

    public void init()
    {
        turning = false;
    }
    
    public void update() 
    {
        if(turning == false)
            return;
        
        NumberArray numbers = new NumberArray();
        cameraTable.retrieveValue("BFR_COORDINATES", numbers);
        if(numbers.size() > 0)
        {
            for(int count = 0; count < 4; count++)
            {
                rectPoints[count].x = numbers.get(count * 2);
                rectPoints[count].y = numbers.get((count * 2) + 1);
            }
        }
    }

    public void notifyConfigChange() {
    }

    public void acceptNotification(Subject subjectThatCaused) 
    {
        BooleanSubject button = (BooleanSubject)subjectThatCaused;
        turning = button.getValue();
    }
    
    private class Point
    {
        public double x, y;
    }
}
