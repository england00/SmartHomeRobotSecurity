package it.unimore.fum.iot.model.charger;

import java.util.Arrays;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 15/03/2022 - 13:05
 */
public class ChargingStationDescriptor {

    // device's parameters
    private String chargerId;
    private String room;
    private double[] position = new double[2];
    private String softwareVersion;
    private String manufacturer;

    public ChargingStationDescriptor() {}

    public ChargingStationDescriptor(String robotId, String room, double[] position, String softwareVersion, String manufacturer) {
        this.chargerId = robotId;
        this.room = room;
        this.position = position;
        this.softwareVersion = softwareVersion;
        this.manufacturer = manufacturer;
    }

    public String getRobotId() {
        return chargerId;
    }

    public void setRobotId(String robotId) {
        this.chargerId = robotId;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public double[] getPosition() {
        return position;
    }

    public void setPosition(double[] position) {
        this.position = position;
    }

    public String getSoftwareVersion() {
        return softwareVersion;
    }

    public void setSoftwareVersion(String softwareVersion) {
        this.softwareVersion = softwareVersion;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("RobotDescriptor{");
        sb.append("chargerId='").append(chargerId).append('\'');
        sb.append(", room='").append(room).append('\'');
        sb.append(", position='").append(Arrays.toString(position)).append('\'');
        sb.append(", manufacturer='").append(manufacturer).append('\'');
        sb.append(", softwareVersion='").append(softwareVersion).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
