package it.unimore.fum.iot.model.robot;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 24/03/2022 - 14:41
 */
public interface IIndoorPositionSensorDescriptor {

    // Indoor Position Sensor Interface
    public void updateIndoorPosition();

    @Override
    public String toString();
}
