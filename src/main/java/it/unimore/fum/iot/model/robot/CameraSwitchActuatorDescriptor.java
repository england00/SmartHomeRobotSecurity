package it.unimore.fum.iot.model.robot;

import java.util.Random;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 15/03/2022 - 02:14
 */
public class CameraSwitchActuatorDescriptor {

    // actuator's parameters
    private long timestamp;
    private boolean value;

    public CameraSwitchActuatorDescriptor() {}

    public CameraSwitchActuatorDescriptor(boolean value) {
        this.value = value;
    }

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

    public void switchStatusOn(){
        // managing status
        this.value = true;

        // managing timestamp
        this.timestamp = System.currentTimeMillis();
    }

    public void switchStatusOff(){
        // managing status
        this.value = false;

        // managing timestamp
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("CameraSwitchActuatorDescriptor{");
        sb.append("timestamp=").append(timestamp);
        sb.append(", value=").append(value);
        sb.append('}');
        return sb.toString();
    }
}
