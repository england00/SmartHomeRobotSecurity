package it.unimore.fum.iot.model.descriptor;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 11/04/2022 - 10:20
 */
public class AlarmOnDescriptor {

    // object's parameters
    private Number battery = 100.0;
    private Boolean presence = false;
    private String robotRoom;
    private String presenceRoom;
    private String chargerRoom;
    private double[] chargerPosition = new double[2];
    private double[] currentRobotPosition = new double[2];
    private Boolean chargerRobotPresence = false;

    public AlarmOnDescriptor() {}

    public Number getBattery() {
        return battery;
    }

    public void setBattery(Number battery) {
        this.battery = battery;
    }

    public Boolean getPresence() {
        return presence;
    }

    public void setPresence(Boolean presence) {
        this.presence = presence;
    }

    public String getRobotRoom() {
        return robotRoom;
    }

    public void setRobotRoom(String robotRoom) {
        this.robotRoom = robotRoom;
    }

    public String getChargerRoom() {
        return chargerRoom;
    }

    public void setChargerRoom(String chargerRoom) {
        this.chargerRoom = chargerRoom;
    }

    public String getPresenceRoom() {
        return presenceRoom;
    }

    public void setPresenceRoom(String presenceRoom) {
        this.presenceRoom = presenceRoom;
    }

    public double[] getChargerPosition() {
        return chargerPosition;
    }

    public void setChargerPosition(double[] chargerPosition) {
        this.chargerPosition = chargerPosition;
    }

    public double[] getCurrentRobotPosition() {
        return currentRobotPosition;
    }

    public void setCurrentRobotPosition(double[] currentRobotPosition) {
        this.currentRobotPosition = currentRobotPosition;
    }

    public Boolean getChargerRobotPresence() {
        return chargerRobotPresence;
    }

    public void setChargerRobotPresence(Boolean chargerRobotPresence) {
        this.chargerRobotPresence = chargerRobotPresence;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("AlarmDescriptor{");
        sb.append("battery=").append(battery);
        sb.append(", presence=").append(presence);
        sb.append('}');
        return sb.toString();
    }
}
