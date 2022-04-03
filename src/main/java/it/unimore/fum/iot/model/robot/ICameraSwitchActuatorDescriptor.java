package it.unimore.fum.iot.model.robot;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 03/04/2022 - 17:30
 */
public interface ICameraSwitchActuatorDescriptor {

    // Camera Switch Actuator Interface

    public String getRobotId();

    public long getTimestamp();

    public Number getVersion();

    public boolean isValue();

    public void switchStatusOn();

    public void switchStatusOff();

    @Override
    public String toString();
}
