package it.unimore.fum.iot.model.sensors;

import java.util.Random;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 13/03/2022 - 18:19
 */
public class BatteryLevelSensorDescriptor {

    // sensor's parameters
    private long timestamp;
    private double batteryLevel = 100.0;

    // utility variables
    private final transient Random random; // this variable mustn't be serialized

    public BatteryLevelSensorDescriptor() {
        this.random = new Random();
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(double batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public void checkBatteryLevel(){
        // managing battery values
        double controlValue = this.batteryLevel - (this.random.nextDouble() * 5.0);
        if (controlValue > 0) {
            this.batteryLevel = controlValue;
        } else
            this.batteryLevel = 0.0;

        // managing timestamp
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("BatteryLevelSensorDescriptor{");
        sb.append("timestamp=").append(timestamp);
        sb.append(", batteryLevel=").append(batteryLevel);
        sb.append('}');
        return sb.toString();
    }
}
