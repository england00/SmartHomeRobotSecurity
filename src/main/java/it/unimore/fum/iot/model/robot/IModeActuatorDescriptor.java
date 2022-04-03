package it.unimore.fum.iot.model.robot;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 03/04/2022 - 17:40
 */
public interface IModeActuatorDescriptor {

    // Mode Actuator Interface

    public String getRobotId();

    public long getTimestamp();

    public Number getVersion();

    public String getValue();

    public void modeStart();

    public void modePause();

    public void modeStop();

    @Override
    public String toString();
}
