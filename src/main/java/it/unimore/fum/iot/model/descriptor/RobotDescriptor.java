package it.unimore.fum.iot.model.descriptor;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 15/03/2022 - 12:14
 */
public class RobotDescriptor {

    // object's parameters
    private String robotId;
    private String room;
    private Number softwareVersion;
    private String manufacturer;

    public RobotDescriptor() {}

    public RobotDescriptor(String robotId, String room, Number softwareVersion, String manufacturer) {
        this.robotId = robotId;
        this.room = room;
        this.softwareVersion = softwareVersion;
        this.manufacturer = manufacturer;
    }

    public String getRobotId() {
        return robotId;
    }

    public void setRobotId(String robotId) {
        this.robotId = robotId;
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

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("RobotDescriptor{");
        sb.append("robotId='").append(robotId).append('\'');
        sb.append(", room='").append(room).append('\'');
        sb.append(", softwareVersion=").append(softwareVersion);
        sb.append(", manufacturer='").append(manufacturer).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
