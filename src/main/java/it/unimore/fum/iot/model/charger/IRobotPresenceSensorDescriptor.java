package it.unimore.fum.iot.model.charger;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 03/04/2022 - 19:31
 */
public interface IRobotPresenceSensorDescriptor {

    // Robot Presence Sensor Interface

    public String getChargerId();

    public long getTimestamp();

    public Number getVersion();

    public boolean isValue();

    // set when the robot position equals the charger position or when the battery charger value reaches 100
    public void setValue(boolean value);

    public void checkRobotPresence();

    @Override
    public String toString();
}
