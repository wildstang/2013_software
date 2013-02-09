package com.wildstangs.autonomous.programs;

import com.wildstangs.autonomous.WsAutonomousProgram;
import com.wildstangs.autonomous.steps.control.WsAutonomousStepDelay;
import com.wildstangs.autonomous.steps.floorpickup.WsAutonomousStepLowerAccumulatorState;
import com.wildstangs.autonomous.steps.floorpickup.WsAutonomousStepMotorBackward;
import com.wildstangs.autonomous.steps.floorpickup.WsAutonomousStepMotorForward;
import com.wildstangs.autonomous.steps.floorpickup.WsAutonomousStepMotorStop;
import com.wildstangs.autonomous.steps.floorpickup.WsAutonomousStepRaiseAccumulatorState;
import com.wildstangs.autonomous.steps.hopper.WsAutonomousStepLowerHopper;

/**
 *
 * @author Liam Fruzyna
 */
public class WsAutonomousProgramFloorPickup extends WsAutonomousProgram
{
    public WsAutonomousProgramFloorPickup()
    {
        super(10);
    }
    protected void defineSteps() 
    {
        programSteps[0] = new WsAutonomousStepLowerAccumulatorState();
        programSteps[1] = new WsAutonomousStepLowerHopper();
        programSteps[2] = new WsAutonomousStepMotorForward();
        programSteps[3] = new WsAutonomousStepDelay(2000);
        programSteps[4] = new WsAutonomousStepMotorBackward();
        programSteps[5] = new WsAutonomousStepMotorStop();
        programSteps[6] = new WsAutonomousStepDelay(2000);
        programSteps[7] = new WsAutonomousStepRaiseAccumulatorState();
        programSteps[8] = new WsAutonomousStepDelay(2000);
        programSteps[9] = new WsAutonomousStepLowerAccumulatorState();
    }

    public String toString() 
    {
        return "Raise Accumulator, Lower Accumulator & Hopper, Move motor forward then backward, Raise Accumulator, and lower it";
    }
    
}