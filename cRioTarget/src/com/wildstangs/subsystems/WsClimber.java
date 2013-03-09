package com.wildstangs.subsystems;

import com.wildstangs.inputfacade.base.WsInputFacade;
import com.wildstangs.inputfacade.inputs.joystick.driver.WsDriverJoystickButtonEnum;
import com.wildstangs.outputfacade.base.IOutputEnum;
import com.wildstangs.outputfacade.base.WsOutputFacade;
import com.wildstangs.subjects.base.IObserver;
import com.wildstangs.subjects.base.Subject;
import com.wildstangs.subsystems.base.WsSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author Joshua Gustafson
 */
public class WsClimber extends WsSubsystem implements IObserver {

    private static final boolean CLIMB_DEFAULT_VALUE = true;
    private boolean climbState;

    public WsClimber(String name) {
        super(name);

        Subject subject = WsInputFacade.getInstance().getOiInput(WsInputFacade.DRIVER_JOYSTICK).getSubject(WsDriverJoystickButtonEnum.BUTTON2);
        subject.attach(this);
    }

    public void init() {
        climbState = CLIMB_DEFAULT_VALUE;
    }

    public void update() {
        WsOutputFacade.getInstance().getOutput(WsOutputFacade.CLIMBER).set((IOutputEnum) null, new Boolean(climbState));

        SmartDashboard.putBoolean("Climb State", climbState);
    }

    public void notifyConfigChange() {
    }

    public void acceptNotification(Subject subjectThatCaused) {
        if (subjectThatCaused.getType() == WsDriverJoystickButtonEnum.BUTTON2) {
            climbState = !climbState;
        }
    }

    public boolean getClimbState() {
        return climbState;
    }
}