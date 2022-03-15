package it.unimore.fum.iot.model.robot;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 15/03/2022 - 02:59
 */
public class ReturnHomeActuatorDescriptor {

    // actuator's parameters
    private long timestamp;
    private double[] ChargerPosition = new double[2];

    public ReturnHomeActuatorDescriptor() {
    }

    public ReturnHomeActuatorDescriptor(double[] ChargerPosition) {
        this.ChargerPosition = ChargerPosition;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double[] getChargerPosition() {
        return ChargerPosition;
    }

    public void setChargerPosition(double[] ChargerPosition) {
        this.ChargerPosition = ChargerPosition;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ReturnHomeActuatorDescriptor{");
        sb.append("timestamp=").append(timestamp);
        sb.append(", xChargerPosition=").append(ChargerPosition[0]);
        sb.append(", yChargerPosition='").append(ChargerPosition[1]).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
