package it.unimore.fum.iot.model.charger;

import java.util.Arrays;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 15/03/2022 - 13:05
 */
public class ChargingStationDescriptor {

    // object's parameters
    private String chargerId;
    private String room;
    private Number softwareVersion;
    private String manufacturer;
    private double[] position = new double[2];
    private String unit = "meter";

    public ChargingStationDescriptor() {}

    public ChargingStationDescriptor(String chargerId, String room, Number softwareVersion, String manufacturer, double[] position) {
        this.chargerId = chargerId;
        this.room = room;
        this.softwareVersion = softwareVersion;
        this.manufacturer = manufacturer;
        this.position = position;
    }

    public String getChargerId() {
        return chargerId;
    }

    public void setChargerId(String chargerId) {
        this.chargerId = chargerId;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public Number getSoftwareVersion() {
        return softwareVersion;
    }

    public void setSoftwareVersion(Number softwareVersion) {
        this.softwareVersion = softwareVersion;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public double[] getPosition() {
        return position;
    }

    public void setPosition(double[] position) {
        this.position = position;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ChargingStationDescriptor{");
        sb.append("chargerId='").append(chargerId).append('\'');
        sb.append(", room='").append(room).append('\'');
        sb.append(", softwareVersion=").append(softwareVersion);
        sb.append(", manufacturer='").append(manufacturer).append('\'');
        sb.append(", position=").append(Arrays.toString(position));
        sb.append(", unit='").append(unit).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
