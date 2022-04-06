package it.unimore.fum.iot.test.model;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 03/04/2022 - 17:35
 */
public interface IPresenceInCameraStreamSensorDescriptor {

    // Presence In Camera Stream Sensor Interface

    public String getRobotId();

    public long getTimestamp();

    public Number getVersion();

    public boolean isValue();

    public void checkPresenceInCameraStream();

    @Override
    public String toString();
}
