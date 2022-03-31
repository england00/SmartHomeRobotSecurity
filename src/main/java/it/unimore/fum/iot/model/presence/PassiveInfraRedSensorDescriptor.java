package it.unimore.fum.iot.model.presence;

import java.util.Random;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 15/03/2022 - 12:51
 */
public class PassiveInfraRedSensorDescriptor {

    // sensor's parameters
    private String presenceId;
    private long timestamp;
    private Number version;
    private boolean value;

    // utility variables
    private final transient Random random; // this variable mustn't be serialized

    public PassiveInfraRedSensorDescriptor(String presenceId, Number version) {
        this.presenceId = presenceId;
        this.version = version;
        this.random = new Random();
    }

    public String getPresenceId() {
        return presenceId;
    }

    public void setPresenceId(String presenceId) {
        this.presenceId = presenceId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Number getVersion() {
        return version;
    }

    public void setVersion(Number version) {
        this.version = version;
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
        final StringBuffer sb = new StringBuffer("PassiveInfraRedSensorDescriptor{");
        sb.append("presenceId='").append(presenceId).append('\'');
        sb.append(", timestamp=").append(timestamp);
        sb.append(", version=").append(version);
        sb.append(", value=").append(value);
        sb.append('}');
        return sb.toString();
    }
}
