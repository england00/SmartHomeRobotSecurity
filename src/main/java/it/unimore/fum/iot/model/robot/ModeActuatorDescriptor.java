package it.unimore.fum.iot.model.robot;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 15/03/2022 - 02:45
 */
public class ModeActuatorDescriptor {

    // actuator's parameters
    private long timestamp;
    private String value;

    public ModeActuatorDescriptor() {}

    public ModeActuatorDescriptor(String value) {
        this.value = value;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void modeStart(){
        // managing mode
        this.value = "START";

        // managing timestamp
        this.timestamp = System.currentTimeMillis();
    }

    public void modePause(){
        // managing mode
        this.value = "PAUSE";

        // managing timestamp
        this.timestamp = System.currentTimeMillis();
    }

    public void modeStop(){
        // managing mode
        this.value = "STOP";

        // managing timestamp
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ModeActuatorDescriptor{");
        sb.append("timestamp=").append(timestamp);
        sb.append(", value='").append(value).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
