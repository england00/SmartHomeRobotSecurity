package it.unimore.fum.iot.model.presence;

import java.util.Random;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 15/03/2022 - 12:51
 */
public class PassiveInfraRedSensorDescriptor {

    // sensor's parameters
    private long timestamp;
    private boolean value;

    // utility variables
    private final transient Random random; // this variable mustn't be serialized

    public PassiveInfraRedSensorDescriptor() {
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

    public void checkPassiveInfraRedSensorDescriptor(){
        // managing value
        int num = this.random.nextInt(10);
        this.value = num == 9; // if num == 9, return true, else is always false

        // managing timestamp
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("PresenceInCameraStreamSensorDescriptor{");
        sb.append("timestamp=").append(timestamp);
        if (this.value) {
            sb.append(", value=").append(value);
        }
        sb.append('}');
        return sb.toString();
    }
}
