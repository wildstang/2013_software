package com.wildstangs.subsystems;

import com.wildstangs.inputmanager.base.WsInputManager;
import com.wildstangs.inputmanager.inputs.joystick.driver.WsDriverJoystickButtonEnum;
import com.wildstangs.inputmanager.inputs.joystick.manipulator.WsManipulatorJoystickButtonEnum;
import com.wildstangs.outputmanager.base.IOutputEnum;
import com.wildstangs.outputmanager.base.WsOutputManager;
import com.wildstangs.subjects.base.BooleanSubject;
import com.wildstangs.subjects.base.IObserver;
import com.wildstangs.subjects.base.Subject;
import com.wildstangs.subsystems.base.WsSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author Joshua
 */
public class WsTurret extends WsSubsystem implements IObserver {

    private boolean leftButton = false;
    private boolean rightButton = false;
    
    public WsTurret(String name) {
        super(name);
        WsInputManager.getInstance().attachJoystickButton(WsManipulatorJoystickButtonEnum.BUTTON11 , this); 
        WsInputManager.getInstance().attachJoystickButton(WsManipulatorJoystickButtonEnum.BUTTON12 , this); 
    }

    public void init() {
        leftButton = false; 
        rightButton = false; 
    }

    public void update() {
        double motorOutput = 0.0; 
        if ( leftButton){
            motorOutput = 0.5; 
        } else if (rightButton){
            motorOutput = -0.5; 
            
        }
        WsOutputManager.getInstance().getOutput(WsOutputManager.TURRET).set((IOutputEnum) null, new Double(motorOutput));

        SmartDashboard.putBoolean("Left Turret Button", leftButton);
        SmartDashboard.putBoolean("Right Turret Button", rightButton);
        SmartDashboard.putNumber("Turret output", motorOutput);
    }

    public void notifyConfigChange() {
    }

    public void acceptNotification(Subject subjectThatCaused) {
        if (subjectThatCaused.getType() == WsManipulatorJoystickButtonEnum.BUTTON12) {
            BooleanSubject button = (BooleanSubject) subjectThatCaused;
            rightButton = button.getValue(); 
        } else if (subjectThatCaused.getType() == WsManipulatorJoystickButtonEnum.BUTTON11) {
            BooleanSubject button = (BooleanSubject) subjectThatCaused;
            leftButton = button.getValue();
        }
    }

}