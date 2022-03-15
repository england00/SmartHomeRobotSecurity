package it.unimore.fum.iot.model.robot;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 15/03/2022 - 02:45
 */
public class ModeActuatorDescriptor {

    // actuator's parameters
    private long timestamp;
    private String mode;

    public ModeActuatorDescriptor() {}

    public ModeActuatorDescriptor(String mode) {
        this.mode = mode;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void modeStart(){
        // managing mode
        this.mode = "START";

        // managing timestamp
        this.timestamp = System.currentTimeMillis();
    }

    public void modePause(){
        // managing mode
        this.mode = "PAUSE";

        // managing timestamp
        this.timestamp = System.currentTimeMillis();
    }

    public void modeStop(){
        // managing mode
        this.mode = "STOP";

        // managing timestamp
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ModeActuatorDescriptor{");
        sb.append("timestamp=").append(timestamp);
        sb.append(", mode").append(mode);
        sb.append('}');
        return sb.toString();
    }
}
