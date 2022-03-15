package it.unimore.fum.iot.model.robot;

import java.util.Random;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 15/03/2022 - 01:44
 */
public class PresenceInCameraStreamSensorDescriptor {

    // sensor's parameters
    private long timestamp;
    private boolean value;

    // utility variables
    private final transient Random random; // this variable mustn't be serialized

    public PresenceInCameraStreamSensorDescriptor() {
        this.random = new Random();
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

    public void checkPresenceInCameraStream(){
        // managing value
        int num = this.random.nextInt(10);
        this.value = num == 9;

        // managing timestamp
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("PresenceInCameraStreamSensorDescriptor{");
        sb.append("timestamp=").append(timestamp);
        sb.append(", value=").append(value);
        sb.append('}');
        return sb.toString();
    }
}
