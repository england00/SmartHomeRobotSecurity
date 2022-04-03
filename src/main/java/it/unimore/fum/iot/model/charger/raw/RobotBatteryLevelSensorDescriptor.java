package it.unimore.fum.iot.model.charger.raw;

import it.unimore.fum.iot.model.charger.IRobotBatteryLevelSensorDescriptor;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 22/03/2022 - 15:59
 */
public class RobotBatteryLevelSensorDescriptor implements IRobotBatteryLevelSensorDescriptor {

    // sensor's parameters
    private String chargerId;
    private long timestamp = System.currentTimeMillis();
    private Number version;
    private double batteryLevel = 0;
    private String unit = "%";

    // utility variables
    private static final double RECHARGING_SPEED = 0.003;

    public RobotBatteryLevelSensorDescriptor() {}

    public RobotBatteryLevelSensorDescriptor(String chargerId, Number version) {
        this.chargerId = chargerId;
        this.version = version;
    }

    @Override
    public String getChargerId() {
        return chargerId;
    }

    public void setChargerId(String chargerId) {
        this.chargerId = chargerId;
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public Number getVersion() {
        return version;
    }

    public void setVersion(Number version) {
        this.version = version;
    }

    @Override
    public double getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(double batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
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
        sb.append("chargerId='").append(chargerId).append('\'');
        sb.append("timestamp=").append(timestamp);
        sb.append(", version=").append(version);
        sb.append(", batteryLevel=").append(batteryLevel);
        sb.append(", unit='").append(unit).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
