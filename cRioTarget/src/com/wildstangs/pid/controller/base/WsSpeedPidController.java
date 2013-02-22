/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.pid.controller.base;

import com.wildstangs.pid.inputs.base.IPidInput;
import com.wildstangs.pid.outputs.base.IPidOutput;

/**
 *
 * @author chadschmidt
 */
public class WsSpeedPidController extends WsPidController{

    public WsSpeedPidController(IPidInput source, IPidOutput output, String pidControllerName) {
        super(source, output, pidControllerName);
    }

    protected double calcDerivativeTerm() {
        //Change this
        return super.calcDerivativeTerm();
    }
    
    
}
