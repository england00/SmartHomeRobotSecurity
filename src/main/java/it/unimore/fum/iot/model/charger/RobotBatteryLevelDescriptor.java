package it.unimore.fum.iot.model.charger;

import java.awt.font.TextHitInfo;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 22/03/2022 - 15:59
 */
public class RobotBatteryLevelDescriptor {

    // sensor's parameters
    private long timestamp = System.currentTimeMillis();
    private double batteryLevel = 0;

    // utility variables
    private static final double RECHARGING_SPEED = 0.003;

    public RobotBatteryLevelDescriptor() {}

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

    public void checkRechargingBatteryLevel(){
        // managing battery values
        this.batteryLevel += (RECHARGING_SPEED * (System.currentTimeMillis() - this.timestamp));
        if (this.batteryLevel >= 100) {
            this.batteryLevel = 100.0;
        }

        // managing timestamp
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("RobotBatteryLevelDescriptor{");
        sb.append("timestamp=").append(timestamp);
        sb.append(", batteryLevel=").append(batteryLevel);
        sb.append('}');
        return sb.toString();
    }
}
