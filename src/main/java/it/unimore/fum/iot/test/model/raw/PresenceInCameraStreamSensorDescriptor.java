package it.unimore.fum.iot.test.model.raw;

import it.unimore.fum.iot.test.model.IPresenceInCameraStreamSensorDescriptor;

import java.util.Random;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 15/03/2022 - 01:44
 */
public class PresenceInCameraStreamSensorDescriptor implements IPresenceInCameraStreamSensorDescriptor {

    // sensor's parameters
    private String robotId;
    private long timestamp;
    private Number version;
    private boolean value;

    // utility variables
    private final transient Random random; // this variable mustn't be serialized

    public PresenceInCameraStreamSensorDescriptor(String robotId, Number version) {
        this.robotId = robotId;
        this.version = version;
        this.random = new Random();
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
    public void checkPresenceInCameraStream(){
        // managing value
        int num = this.random.nextInt(10);
        this.value = num == 9; // if num == 9, return true, otherwise is always false

        // managing timestamp
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("PresenceInCameraStreamSensorDescriptor{");
        sb.append("robotId='").append(robotId).append('\'');
        sb.append(", timestamp=").append(timestamp);
        sb.append(", version=").append(version);
        sb.append(", value=").append(value);
        sb.append('}');
        return sb.toString();
    }
}
