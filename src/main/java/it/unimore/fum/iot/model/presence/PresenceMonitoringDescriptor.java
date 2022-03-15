package it.unimore.fum.iot.model.presence;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 15/03/2022 - 12:46
 */
public class PresenceMonitoringDescriptor {

    // device's parameters
    private String presenceId;
    private String room;
    private String softwareVersion;
    private String manufacturer;

    public PresenceMonitoringDescriptor() {}

    public PresenceMonitoringDescriptor(String presenceId, String room, String softwareVersion, String manufacturer) {
        this.presenceId = presenceId;
        this.room = room;
        this.softwareVersion = softwareVersion;
        this.manufacturer = manufacturer;
    }

    public String getPresenceId() {
        return presenceId;
    }

    public void setPresenceId(String presenceId) {
        this.presenceId = presenceId;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
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
        final StringBuffer sb = new StringBuffer("PresenceMonitoringDescriptor{");
        sb.append("presenceId='").append(presenceId).append('\'');
        sb.append(", room='").append(room).append('\'');
        sb.append(", manufacturer='").append(manufacturer).append('\'');
        sb.append(", softwareVersion='").append(softwareVersion).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
