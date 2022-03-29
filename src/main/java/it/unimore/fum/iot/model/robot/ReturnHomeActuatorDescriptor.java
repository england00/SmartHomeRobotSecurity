package it.unimore.fum.iot.model.robot;

import java.util.Arrays;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 15/03/2022 - 02:59
 */
public class ReturnHomeActuatorDescriptor {

    // actuator's parameters
    private String robotId;
    private long timestamp;
    private Number version;
    private boolean value;
    private double[] chargerPosition = null;
    private String unit = "meter";

    public ReturnHomeActuatorDescriptor() {
    }

    public ReturnHomeActuatorDescriptor(String robotId, Number version) {
        this.robotId = robotId;
        this.version = version;
    }

    public String getRobotId() {
        return robotId;
    }

    public void setRobotId(String robotId) {
        this.robotId = robotId;
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

    public double[] getChargerPosition() {
        return chargerPosition;
    }

    public void setChargerPosition(double[] chargerPosition) {
        this.chargerPosition = chargerPosition;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void switchReturnOn(){
        // managing status
        this.value = true;

        // managing timestamp
        this.timestamp = System.currentTimeMillis();
    }

    public void switchReturnOff(){
        // managing status
        this.value = false;

        // managing timestamp
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ReturnHomeActuatorDescriptor{");
        sb.append("robotId='").append(robotId).append('\'');
        sb.append(", timestamp=").append(timestamp);
        sb.append(", version=").append(version);
        sb.append(", value=").append(value);
        sb.append(", chargerPosition=").append(Arrays.toString(chargerPosition));
        sb.append(", unit='").append(unit).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
