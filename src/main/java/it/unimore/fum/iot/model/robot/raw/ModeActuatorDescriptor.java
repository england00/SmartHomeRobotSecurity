package it.unimore.fum.iot.model.robot.raw;

import it.unimore.fum.iot.model.robot.IModeActuatorDescriptor;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 15/03/2022 - 02:45
 */
public class ModeActuatorDescriptor implements IModeActuatorDescriptor {

    // actuator's parameters
    private String robotId;
    private long timestamp;
    private Number version;
    private String value;

    public ModeActuatorDescriptor() {}

    public ModeActuatorDescriptor(String robotId, Number version) {
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
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public void modeStart(){
        // managing mode
        this.value = "START";

        // managing timestamp
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public void modePause(){
        // managing mode
        this.value = "PAUSE";

        // managing timestamp
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public void modeStop(){
        // managing mode
        this.value = "STOP";

        // managing timestamp
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ModeActuatorDescriptor{");
        sb.append("robotId='").append(robotId).append('\'');
        sb.append(", timestamp=").append(timestamp);
        sb.append(", version=").append(version);
        sb.append(", value='").append(value).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
