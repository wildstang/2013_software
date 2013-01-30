/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.pid.outputs;

import com.wildstangs.outputfacade.base.IOutputEnum;
import com.wildstangs.outputfacade.base.WsOutputFacade;
import com.wildstangs.pid.outputs.base.IPidOutput;

/**
 *
 * @author Nathan
 */
public class WsDriveBasePidRightOutput implements IPidOutput {

    public WsDriveBasePidRightOutput() {
        //Nothing to do here
    }

    public void pidWrite(double output) {
        WsOutputFacade.getInstance().getOutput(WsOutputFacade.RIGHT_DRIVE_SPEED).set((IOutputEnum)null, new Double(output));
    }
}
