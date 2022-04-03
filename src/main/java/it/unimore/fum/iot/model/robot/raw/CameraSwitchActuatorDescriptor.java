package it.unimore.fum.iot.model.robot.raw;

import it.unimore.fum.iot.model.robot.ICameraSwitchActuatorDescriptor;

import java.util.Random;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 15/03/2022 - 02:14
 */
public class CameraSwitchActuatorDescriptor implements ICameraSwitchActuatorDescriptor {

    // actuator's parameters
    private String robotId;
    private long timestamp;
    private Number version;
    private boolean value = false;

    public CameraSwitchActuatorDescriptor() {}

    public CameraSwitchActuatorDescriptor(String robotId, Number version) {
        this.robotId = robotId;
        this.version = version;
    }

    @Override
    public String getRobotId() {
        return robotId;
    }

    public void setRobotId(String robotId) {
        this.robotId = robotId;
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public Number getVersion() {
        return version;
    }

    public void setVersion(Number version) {
        this.version = version;
    }

    @Override
    public boolean isValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    @Override
    public void switchStatusOn(){
        // managing status
        this.value = true;

        // managing timestamp
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public void switchStatusOff(){
        // managing status
        this.value = false;

        // managing timestamp
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("CameraSwitchActuatorDescriptor{");
        sb.append("robotId='").append(robotId).append('\'');
        sb.append(", timestamp=").append(timestamp);
        sb.append(", version=").append(version);
        sb.append(", value=").append(value);
        sb.append('}');
        return sb.toString();
    }
}
