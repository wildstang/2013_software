/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.pid.outputs;

import com.wildstangs.outputfacade.base.IOutputEnum;
import com.wildstangs.outputfacade.base.WsOutputFacade;
import com.wildstangs.pid.outputs.base.IPidOutput;
import com.wildstangs.subsystems.WsDriveBase;
import com.wildstangs.subsystems.base.WsSubsystemContainer;

/**
 *
 * @author Nathan
 */
public class WsDriveBaseHeadingPidOutput implements IPidOutput {

    public WsDriveBaseHeadingPidOutput() {
        //Nothing to do here
    }

    public void pidWrite(double output) {
        //Write to the Ouput Facade via the Drive Base Subsystem
    }
}
