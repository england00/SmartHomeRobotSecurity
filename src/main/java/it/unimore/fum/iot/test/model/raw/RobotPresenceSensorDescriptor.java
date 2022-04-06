package it.unimore.fum.iot.test.model.raw;

import it.unimore.fum.iot.test.model.IRobotPresenceSensorDescriptor;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 22/03/2022 - 15:18
 */
public class RobotPresenceSensorDescriptor implements IRobotPresenceSensorDescriptor {

    // sensor's parameters
    private String chargerId;
    private long timestamp;
    private Number version;
    private boolean value = false;

    public RobotPresenceSensorDescriptor() {}

    public RobotPresenceSensorDescriptor(String chargerId, Number version) {
        this.chargerId = chargerId;
        this.version = version;
    }

    @Override
    public String getChargerId() {
        return chargerId;
    }

    public void setChargerId(String chargerId) {
        this.chargerId = chargerId;
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

    @Override
    public void setValue(boolean value) {
        this.value = value;
    }

    @Override
    public void checkRobotPresence() {
        // managing timestamp
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("RobotPresenceSensorDescriptor{");
        sb.append("chargerId='").append(chargerId).append('\'');
        sb.append("timestamp=").append(timestamp);
        sb.append(", version=").append(version);
        sb.append(", value=").append(value);
        sb.append('}');
        return sb.toString();
    }
}
