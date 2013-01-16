/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008-2012. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JLabel;

     /**
 * Solenoid class for running high voltage Digital Output (9472 module).
 *
 * The Solenoid class is typically used for pneumatics solenoids, but could be used
 * for any device within the current spec of the 9472 module.
 */
public class Solenoid {

    private int mChannel;
    private boolean outputState;
    
    private JFrame frame;
    private JLabel outputLabel;
/**
     * Common function to implement constructor behavior.
     */
    private void initSolenoid() {
        outputState = false;
        frame = new JFrame("Solenoid: " + mChannel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(300, 100));
	frame.setLayout(new BorderLayout());
		
	outputLabel = new JLabel("Solenoid State: Off");
	frame.add(outputLabel, BorderLayout.NORTH);
		
	frame.pack();
	frame.setVisible(true);
    }

    /**
     * Constructor.
     *
     * @param moduleNumber The module number of the solenoid module to use.
     * @param channel The channel on the module to control.
     */
    public Solenoid(final int moduleNumber, final int channel) {
	mChannel = channel;
        initSolenoid();
    }

    /**
     * Constructor.
     *
     * @param channel The channel on the module to control.
     */
    public Solenoid(final int channel) {
	mChannel = channel;
        initSolenoid();
    }

    /**
     * Destructor.
     */
    public void free() {}

    /**
     * Set the value of a solenoid.
     *
     * @param on Turn the solenoid output off or on.
     */
    public void set(boolean value) {
        outputState = value;
	if ( outputState == true ) {
		outputLabel.setText("Solenoid State: on");
	} else {
		outputLabel.setText("Solenoid State: off");
	}
    }

    /**
     * Read the current value of the solenoid.
     *
     * @return The current value of the solenoid.
     */
    public boolean get() {
        return outputState;
    }

    }