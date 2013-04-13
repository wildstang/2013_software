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

/**
 *
 * @author John
 */
public class WsLED extends WsSubsystem implements IObserver {

    private I2C i2c;
    boolean kickerButtonPressed = false;
    MessageHandler messageSender;
    boolean enabledSendData = true;
    boolean autoSendData = true;
    boolean disableSendData = true;
    boolean kickSent = false;

    public WsLED(String name) {
        super(name);
        i2c = new I2C(DigitalModule.getInstance(1), 0x52 << 1);
        Thread t = new Thread(messageSender = new MessageHandler(i2c));
        t.start();
        Subject subject = WsInputFacade.getInstance().getOiInput(WsInputFacade.MANIPULATOR_JOYSTICK).getSubject(WsManipulatorJoystickButtonEnum.BUTTON6);
        subject.attach(this);
    }

    public void init() {
        enabledSendData = true;
        autoSendData = true;
        disableSendData = true;
        kickSent = false;
    }

    public void update() {
        byte[] dataBytes = new byte[5];
        byte commandByte = dataBytes[0];
        byte payloadByteOne = dataBytes[1];
        byte payloadByteTwo = dataBytes[2];

        // Get Data Handles
        //DriverStation* p_ds = DriverStation::GetInstance();
        //WsOutputFacade* p_of = WsOutputFacade::instance();

        // Get all inputs relevant to the LEDs
        boolean isRobotEnabled = DriverStation.getInstance().isEnabled();
        boolean isRobotTeleop = DriverStation.getInstance().isOperatorControl();
        boolean isRobotAuton = DriverStation.getInstance().isAutonomous();
        DriverStation.Alliance alliance = DriverStation.getInstance().getAlliance();
        int station_location = DriverStation.getInstance().getLocation();



        if (DriverStation.getInstance().isEnabled() == true) {
            if (DriverStation.getInstance().isOperatorControl() == true) {

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

                    dataBytes[0] = commandByte;
                    dataBytes[1] = payloadByteOne;
                    dataBytes[2] = payloadByteTwo;
                    dataBytes[3] = 0;
                    dataBytes[4] = 0;
                    if (!kickSent) {
                        messageSender.setSendData(dataBytes, dataBytes.length);
                        messageSender.setSendData(dataBytes, dataBytes.length);
                        synchronized (messageSender) {
                            messageSender.notify();
                        }
                        enabledSendData = true;
                        kickSent = true;
                    }
                    //messageSender.send();
                } else if (enabledSendData) {
                    kickSent = false;
                    commandByte = 0x01;
                    payloadByteOne = 0x6C;
                    payloadByteTwo = 0x0A;

                    dataBytes[0] = commandByte;
                    dataBytes[1] = payloadByteOne;
                    dataBytes[2] = payloadByteTwo;
                    dataBytes[3] = 0;
                    dataBytes[4] = 0;


                    messageSender.setSendData(dataBytes, dataBytes.length);
                    messageSender.setSendData(dataBytes, dataBytes.length);
                    synchronized (messageSender) {
                        messageSender.notify();
                    }
                    enabledSendData = false;
                }

            } else {
                commandByte = 0x01;
                payloadByteOne = 0x1C;
                payloadByteTwo = 0x1D;
                
                if (true == autoSendData) {
                    dataBytes[0] = commandByte;
                    dataBytes[1] = payloadByteOne;
                    dataBytes[2] = payloadByteTwo;
                    dataBytes[3] = 0;
                    dataBytes[4] = 0;
                    messageSender.setSendData(dataBytes, dataBytes.length);
                    synchronized (messageSender) {
                        messageSender.notify();
                    }
                    autoSendData = false;
                }
            }
        } else {
            //Update is not called during disabled.  Code for reference only.
            //---------------------------------------------------------------------
            // Handle Disabled signalling here
            //---------------------------------------------------------------------
            switch (alliance.value) {
                    case DriverStation.Alliance.kRed_val: {
                        commandByte = 0x04;
                        payloadByteOne = 0x52;
                        payloadByteTwo = ((byte) station_location);
                    }
                    break;

                    case DriverStation.Alliance.kBlue_val: {
                        commandByte = 0x04;
                        payloadByteOne = 0x47;
                        payloadByteTwo = ((byte) station_location);
                    }
                    break;

                    default: {
                        disableSendData = false;
                    }
                    break;
                }

                if ((station_location < 1)
                        || (station_location > 3)) {
                    disableSendData = false;
                }

                if (true == disableSendData) {
                    dataBytes[0] = commandByte;
                    dataBytes[1] = payloadByteOne;
                    dataBytes[2] = payloadByteTwo;
                    dataBytes[3] = 0;
                    dataBytes[4] = 0;
                    messageSender.setSendData(dataBytes, dataBytes.length);
                    synchronized (messageSender) {
                        messageSender.notify();
                    }
                    disableSendData = false;
                }
        }
    }

    public void acceptNotification(Subject subjectThatCaused) {
        BooleanSubject button = (BooleanSubject) subjectThatCaused;

        if (subjectThatCaused.getType() == WsManipulatorJoystickButtonEnum.BUTTON6) {
            kickerButtonPressed = button.getValue();
        }
    }

    private static class MessageHandler implements Runnable {

        static byte[] rcvBytes = new byte[5];
        boolean update;
        byte[] sendData;
        int sendSize = 0;
        boolean dataToSend = false;
        static I2C i2c;
        boolean running = true;

        public MessageHandler(I2C i2cOutput) {
            i2c = i2cOutput;
        }

        public void run() {
            while (running) {
                synchronized (this) {
                    try {
                        this.wait();
                        sendPayloadData(sendData, sendSize);
                        update = false;
                        dataToSend = false;
                        sendSize = 0;
                    } catch (InterruptedException e) {
                    }
                }
            }
        }

        public void setSendData(byte[] data, int size) {
            sendData = data;
            sendSize = size;
            dataToSend = true;
        }

        public void send() {
            update = true;
        }

        public void stop() {
            running = false;
        }

        private static void sendPayloadData(byte[] data, int size) {
            if (5 != size && data != null) {
                return;
            }

            // Extremely fast and cheap data confirmation algorithm
            data[3] = (byte) (~data[1]);
            data[4] = (byte) (~data[2]);

            System.out.println("Input2 w: Data size: " + data.length + " byte[0]: " + Integer.toHexString(data[0]) + " byte[1]: " + Integer.toHexString(data[1]) + " byte[2]: " + Integer.toHexString(data[2]) + " byte[3]: " + Integer.toHexString(data[3]) + " byte[4]: " + Integer.toHexString(data[4]));            // Send the bytes - this is a blocking call
            //byte[] dataToSend, int sendSize, byte[] dataReceived, int receiveSize
            i2c.transaction(data, size, rcvBytes, rcvBytes.length);
        }
    }
}
