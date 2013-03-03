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
    private Point rectPoints[];
    private NetworkTable server;
    
    private static final double TARGET_HEIGHT = 12;
    private static final double TARGET_WIDTH = 54;
    private static final double FOV = 47.5;
    
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
        rectPoints = new Point[4];
        for(int c = 0; c < rectPoints.length; c++)
            rectPoints[c] = new Point();
    }
    
    public void update() 
    {
        double imageHPX = server.getNumber("IMAGE_HEIGHT");
        double imageWPX = server.getNumber("IMAGE_WIDTH");
        
        NumberArray numbers = new NumberArray();
        server.retrieveValue("BFR_COORDINATES", numbers);

        for(int c = 0; c < (numbers.size() / 2); c++)
        {
            rectPoints[c].x = numbers.get(c * 2);
            rectPoints[c].y = numbers.get((c * 2) + 1);
        }
        
        double sliceHeight = ((rectPoints[0].y + rectPoints[1].y) / 2) - 
                             ((rectPoints[2].y + rectPoints[3].y) / 2);
        double adjustedTargetHeight = TARGET_HEIGHT * (imageHPX / sliceHeight);
        double sliceX = (rectPoints[0].x + rectPoints[1].x) / 2;
        double center = (imageWPX / 2);
        double pixelOffset = sliceX - center;
        
        
        double distance = (adjustedTargetHeight / 2) / Math.tan(Math.toRadians(FOV / 2));
        double theta = (pixelOffset * FOV) / imageWPX;
        Logger.getLogger().debug(this.getName(), "Target Distance", Double.toString(distance) + " in");
        Logger.getLogger().debug(this.getName(), "Theta", Double.toString(theta) + "degrees");
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
