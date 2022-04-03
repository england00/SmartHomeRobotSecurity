package it.unimore.fum.iot.model.robot.raw;

import it.unimore.fum.iot.model.robot.IBatteryLevelSensorDescriptor;

import java.util.Random;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 13/03/2022 - 18:19
 */
public class BatteryLevelSensorDescriptor implements IBatteryLevelSensorDescriptor {

    // sensor's parameters
    private String robotId;
    private long timestamp;
    private Number version;
    private double batteryLevel = 100.0;
    private String unit = "%";

    // utility variables
    private final transient Random random; // this variable mustn't be serialized

    public BatteryLevelSensorDescriptor(String robotId, Number version) {
        this.robotId = robotId;
        this.version = version;
        this.random = new Random();
    }

    @Override
    public String getRobotId() {
        return robotId;
    }

    public void setRobotId(String robotId) {
        this.robotId = robotId;
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

    @Override
    // reset on 100.0 by the CHARGING STATION
    public void setBatteryLevel(double batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    @Override
    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public void checkBatteryLevel(){
        // managing battery values
        this.batteryLevel = this.batteryLevel - (this.random.nextDouble() * 5.0);
        if (this.batteryLevel <= 0) {
            this.batteryLevel = 0.0;
        }

        // managing timestamp
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("BatteryLevelSensorDescriptor{");
        sb.append("robotId='").append(robotId).append('\'');
        sb.append(", timestamp=").append(timestamp);
        sb.append(", version=").append(version);
        sb.append(", batteryLevel=").append(batteryLevel);
        sb.append(", unit='").append(unit).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
