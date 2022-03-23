package it.unimore.fum.iot.model.charger;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 22/03/2022 - 15:18
 */
public class RobotPresenceSensorDescriptor {

    // sensor's parameters
    private long timestamp;
    private boolean value = false;

    public RobotPresenceSensorDescriptor() {}

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    public void checkRobotPresence() {
        // managing timestamp
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("RobotPresenceSensorDescriptor{");
        sb.append("timestamp=").append(timestamp);
        sb.append(", value=").append(value);
        sb.append('}');
        return sb.toString();
    }
}
