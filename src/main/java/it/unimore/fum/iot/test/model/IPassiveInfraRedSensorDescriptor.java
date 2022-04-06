package it.unimore.fum.iot.test.model;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 03/04/2022 - 18:50
 */
public interface IPassiveInfraRedSensorDescriptor {

    // Passive InfraRed Sensor Interface

    public String getPresenceId();

    public long getTimestamp();

    public Number getVersion();

    public boolean isValue();

    public void checkPassiveInfraRedSensorDescriptor();

    @Override
    public String toString();
}
