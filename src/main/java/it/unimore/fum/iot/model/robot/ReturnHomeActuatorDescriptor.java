package it.unimore.fum.iot.model.robot;

import java.util.Arrays;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 15/03/2022 - 02:59
 */
public class ReturnHomeActuatorDescriptor {

    // actuator's parameters
    private long timestamp;
    private boolean value;
    private double[] chargerPosition = new double[2];

    public ReturnHomeActuatorDescriptor() {
    }

    public ReturnHomeActuatorDescriptor(double[] chargerPosition) {
        this.chargerPosition = chargerPosition;
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

    public double[] getChargerPosition() {
        return chargerPosition;
    }

    public void setChargerPosition(double[] chargerPosition) {
        this.chargerPosition = chargerPosition;
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
        sb.append("timestamp=").append(timestamp);
        sb.append(" value=").append(value);
        sb.append(", chargerPosition=").append(Arrays.toString(chargerPosition));
        sb.append('}');
        return sb.toString();
    }
}
