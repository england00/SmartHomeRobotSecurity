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
    private boolean status;

    public CameraSwitchActuatorDescriptor() {}

    public CameraSwitchActuatorDescriptor(boolean status) {
        this.status = status;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void changeSwitchStatusOn(){
        // managing status
        this.status = true;

        // managing timestamp
        this.timestamp = System.currentTimeMillis();
    }

    public void changeSwitchStatusOff(){
        // managing status
        this.status = false;

        // managing timestamp
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("CameraSwitchActuatorDescriptor{");
        sb.append("timestamp=").append(timestamp);
        sb.append(", status=").append(status);
        sb.append('}');
        return sb.toString();
    }
}
