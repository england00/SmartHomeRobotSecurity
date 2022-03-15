package it.unimore.fum.iot.model.robot;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 15/03/2022 - 02:59
 */
public class ReturnHomeActuatorDescriptor {

    // actuator's parameters
    private long timestamp;
    private double xChargerPosition;
    private double yChargerPosition;

    //external values


    public ReturnHomeActuatorDescriptor() {
    }

    public ReturnHomeActuatorDescriptor(double xChargerPosition, double yChargerPosition) {
        this.xChargerPosition = xChargerPosition;
        this.yChargerPosition = yChargerPosition;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double getxChargerPosition() {
        return xChargerPosition;
    }

    public void setxChargerPosition(double xChargerPosition) {
        this.xChargerPosition = xChargerPosition;
    }

    public double getyChargerPosition() {
        return yChargerPosition;
    }

    public void setyChargerPosition(double yChargerPosition) {
        this.yChargerPosition = yChargerPosition;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("IndoorPositionSensorDescriptor{");
        sb.append("timestamp=").append(timestamp);
        sb.append(", xChargerPosition=").append(xChargerPosition);
        sb.append(", yChargerPosition='").append(yChargerPosition).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
