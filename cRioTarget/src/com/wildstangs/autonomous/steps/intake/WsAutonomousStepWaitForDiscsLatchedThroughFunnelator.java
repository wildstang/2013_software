package com.wildstangs.autonomous.steps.intake;

import com.wildstangs.autonomous.WsAutonomousStep;
import com.wildstangs.inputfacade.base.WsInputFacade;
import com.wildstangs.subjects.base.BooleanSubject;
import com.wildstangs.subjects.base.IObserver;
import com.wildstangs.subjects.base.ISubjectEnum;
import com.wildstangs.subjects.base.Subject;
import com.wildstangs.subsystems.WsIntake;
import com.wildstangs.subsystems.base.WsSubsystemContainer;

/**
 *
 * @author Nathan
 */
public class WsAutonomousStepWaitForDiscsLatchedThroughFunnelator extends WsAutonomousStep implements IObserver {
    
    int numLatchedDiscs = 0;
    int numDiscsCollected = 0;

    public WsAutonomousStepWaitForDiscsLatchedThroughFunnelator() {
        
    }

    public void initialize() {
        Subject subject = WsInputFacade.getInstance().getSensorInput(WsInputFacade.FUNNELATOR_LIMIT_SWITCH).getSubject((ISubjectEnum) null);
        subject.attach(this);
        numLatchedDiscs = ((WsIntake) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_INTAKE)).getNumLatchedDiscs();
        if (numLatchedDiscs == 0) {
            //No discs were collected, no need to intake
//            finished = true;
            numLatchedDiscs = 1 ; 
        }
    }

    public void update() {
        if(numDiscsCollected >= numLatchedDiscs) {
            //We have collected all the discs in the accumulator, we're done here
            finished = true;
        }
    }

    public String toString() {
        return "Waits for the number of discs latched in the accumulator to intake";
    }

    public void acceptNotification(Subject subjectThatCaused) {
        if (((BooleanSubject) subjectThatCaused).getValue() == false && ((BooleanSubject) subjectThatCaused).getPreviousValue() == true) {
            numDiscsCollected++;
        }
    }
}
