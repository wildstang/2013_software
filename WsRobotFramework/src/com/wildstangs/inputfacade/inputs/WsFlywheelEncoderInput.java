package com.wildstangs.inputfacade.inputs;

import com.wildstangs.configfacade.DoubleConfigFileParameter;
import com.wildstangs.inputfacade.base.IInputEnum;
import com.wildstangs.subjects.base.DoubleSubject;
import com.wildstangs.subjects.base.ISubjectEnum;
import com.wildstangs.subjects.base.Subject;
import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Utility;
import java.util.LinkedList;

/**
 *
 * @author Nathan
 */
public class WsFlywheelEncoderInput {

    public class WsFlywheelSnapshot {

        public int tickCount;
        public double FPGATime;
    }
    DoubleSubject flywheelSpeed;
    Encoder flywheelSpeedEncoder;
    LinkedList flywheelSnapshotList;
    DoubleConfigFileParameter startState = new DoubleConfigFileParameter(
            this.getClass().getName(), "startState", 0.0);
    private static final int WS_FLYWHEEL_TICK_LIST_DEPTH = 4;

    public WsFlywheelEncoderInput() {
        this.flywheelSpeed = new DoubleSubject("FlywheelSpeedEncoder");
        this.flywheelSpeedEncoder = new Encoder(9, 10, true, CounterBase.EncodingType.k1X);
        flywheelSpeedEncoder.reset();
        flywheelSpeedEncoder.start();
        flywheelSnapshotList = new LinkedList();

        flywheelSpeed.setValue(startState.getValue());
    }

    public void set(IInputEnum key, Object value) {
        flywheelSpeed.setValue(((Double) value).doubleValue());

    }

    public Subject getSubject(ISubjectEnum subjectEnum) {
        return flywheelSpeed;
    }

    public Object get(IInputEnum key) {
        return flywheelSpeed.getValueAsObject();
    }

    public double getCurrentSpeed() {
        return flywheelSpeed.getValue();
    }

    public void update() {
        flywheelSpeed.updateValue();
    }

    public void pullData() {
        WsFlywheelSnapshot snapshot = new WsFlywheelSnapshot();
        snapshot.FPGATime = Utility.getFPGATime();
        snapshot.tickCount = flywheelSpeedEncoder.get();

        // Add the current snapshot to the list
        flywheelSnapshotList.addFirst(snapshot);

        // Make sure the list is of expected size
        // @TODO:  Make this size at least a #define
        while (flywheelSnapshotList.size() > WS_FLYWHEEL_TICK_LIST_DEPTH) {
            flywheelSnapshotList.removeLast();
        }

        // Grab the first and last ticks and times values, and calculate the average speed
        double flywheel_speed = 0.0f;
        if (flywheelSnapshotList.size() > 1) {
            int newest_tick_count = ((WsFlywheelSnapshot) (flywheelSnapshotList.getFirst())).tickCount;
            int oldest_tick_count = ((WsFlywheelSnapshot) (flywheelSnapshotList.getLast())).tickCount;
            double newest_timestamp = ((WsFlywheelSnapshot) (flywheelSnapshotList.getFirst())).FPGATime;
            double oldest_timestamp = ((WsFlywheelSnapshot) (flywheelSnapshotList.getLast())).FPGATime;
            double numerator = Math.abs(newest_tick_count - oldest_tick_count);
            double denominator = Math.abs(newest_timestamp - oldest_timestamp);

            // Don't divide by zero (floating point gets close)
            if (denominator > 0.0001) {
                // Average the speeds & apply our conversion factor (currently,
                double ticks_per_second = numerator / denominator;
                double rpm_intermediate_axle = ticks_per_second * (60.0f / 128.0f);
                double rpm_flywheel_axle = rpm_intermediate_axle * (18.0f / 35.0f);
                flywheel_speed = rpm_flywheel_axle;
            }
        }
        flywheelSpeed.setValue(flywheel_speed);
    }

    public void notifyConfigChange() {
        //Nothing to update here, since the config value only affect the
        //start state.
    }
}
