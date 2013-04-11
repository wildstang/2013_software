/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.subsystems;

import com.wildstangs.inputfacade.base.WsInputFacade;
import com.wildstangs.inputfacade.inputs.joystick.manipulator.WsManipulatorJoystickButtonEnum;
import com.wildstangs.subjects.base.BooleanSubject;
import com.wildstangs.subjects.base.IObserver;
import com.wildstangs.subjects.base.Subject;
import com.wildstangs.subsystems.base.WsSubsystem;
import edu.wpi.first.wpilibj.DigitalModule;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;


/**
 *
 * @author John
 */
public class WsLED extends WsSubsystem implements IObserver 
{
    private I2C i2c;
    private int waitCounter = 0; //Waits for 5 update calls before sending the payload
    boolean kickerButtonPressed = false;
    public WsLED (String name)
    {
        super(name);
        i2c = new I2C(DigitalModule.getInstance(1), 0x52 << 1);
        Subject subject = WsInputFacade.getInstance().getOiInput(WsInputFacade.MANIPULATOR_JOYSTICK).getSubject(WsManipulatorJoystickButtonEnum.BUTTON6);
        subject.attach(this);
    }
    
    public void init()
    {
        waitCounter = 0;
    }
     
    public void update()
    {
        waitCounter++;
        byte[] dataBytes = new byte[3];
        byte commandByte    = dataBytes[0];
        byte payloadByteOne = dataBytes[1];
        byte payloadByteTwo = dataBytes[2];
        
        // If we end up in a weird state, don't send anything
        boolean shouldSendData = true;
    
        // Get Data Handles
        //DriverStation* p_ds = DriverStation::GetInstance();
        //WsOutputFacade* p_of = WsOutputFacade::instance();
    
        // Get all inputs relevant to the LEDs
        boolean isRobotEnabled              = DriverStation.getInstance().isEnabled();
        boolean isRobotTeleop               = DriverStation.getInstance().isOperatorControl();
        boolean isRobotAuton                = DriverStation.getInstance().isAutonomous();
        DriverStation.Alliance alliance     = DriverStation.getInstance().getAlliance();
        int station_location                = DriverStation.getInstance().getLocation();
        boolean robotIntakeState            = false;
        boolean robotFloorPickupState       = false;
        boolean robotHopperState            = false;
        boolean robotTurboState             = false;
        
            
        
        if(DriverStation.getInstance().isEnabled() == true)
        {
            if(DriverStation.getInstance().isOperatorControl() == true)
            {
                
                //-----------------------------------------------------------------
                // Handle TeleOp signalling here
                //-----------------------------------------------------------------
                
                // Display Robot Grabber
                // 0x01 0x6C 0x0A  RED
                // 0x01 0x6C 0x0B  GREEN
                if (kickerButtonPressed) {
                    commandByte = 0x01;
                    payloadByteOne = 0x6C;
                    payloadByteTwo = 0x0B;
                }
                else {
                    commandByte = 0x01;
                    payloadByteOne = 0x6C;
                    payloadByteTwo = 0x0A;
                }
                    
                dataBytes[0] = commandByte;
                dataBytes[1] = payloadByteOne;
                dataBytes[2] = payloadByteTwo;
                this.sendPayloadData(dataBytes, 3);
                //Timer.delay(0.02);  todo: Figure out how to do this
                
            }
            else
            {
                // Display Tie-Dye Mode
                // 0x01 0x1C 0x1D
                commandByte = 0x01;
                payloadByteOne = 0x1C;
                payloadByteTwo = 0x1D;
            }
        }
         else
        {
            //---------------------------------------------------------------------
            // Handle Disabled signalling here
            //---------------------------------------------------------------------
        
            // Display alliance
            // Command:  0x04
            // Byte 1:   0x52 - Red Alliance, 0x47 - Blue Alliance
            // Byte 2:   0x01, 0x02, 0x03 - Station Number
            switch (alliance.value)
            {
                case DriverStation.Alliance.kRed_val:
                {
                    commandByte = 0x04;
                    payloadByteOne = 0x52;
                    payloadByteTwo = ((byte) station_location);
                } break;
            
                case DriverStation.Alliance.kBlue_val:
                {
                    commandByte = 0x04;
                    payloadByteOne = 0x47;
                    payloadByteTwo = ((byte) station_location);
                } break;
                
                default:
                {
                    shouldSendData = false;
                } break;
            }
        
            if ((station_location < 1) ||
                (station_location > 3))
            {
                shouldSendData = false;
            }
        }
    
        if (true == shouldSendData && waitCounter >= 5)
        {
            waitCounter = 0;
            dataBytes[0] = commandByte;
            dataBytes[1] = payloadByteOne;
            dataBytes[2] = payloadByteTwo;
            this.sendPayloadData(dataBytes, dataBytes.length);
        }
    }
     
    public void acceptNotification(Subject subjectThatCaused) 
    {
        BooleanSubject button = (BooleanSubject) subjectThatCaused;
        
        if (subjectThatCaused.getType() == WsManipulatorJoystickButtonEnum.BUTTON6) {
            kickerButtonPressed = button.getValue();  
        }
    }
    
    public void sendPayloadData(byte[] data, int size)
    {
        /*if (5 != size)
        { return; }
    
        // Extremely fast and cheap data confirmation algorithm
        data[3] = data[1];  //Not sure about this
        data[4] = data[2];*/

        // Send the bytes - this is a blocking call
        //byte[] dataToSend, int sendSize, byte[] dataReceived, int receiveSize
        i2c.transaction(data, size, data, 0); //This isn't right but I'm not sure what is
    }
}
