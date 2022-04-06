package it.unimore.fum.iot.test.model;

import java.util.Arrays;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 03/04/2022 - 17:44
 */
public interface IReturnHomeActuatorDescriptor {

    // Return Home Actuator Interface

    public String getRobotId();

    public long getTimestamp();

    public Number getVersion();

    public boolean isValue();

    public double[] getChargerPosition();

    public void setChargerPosition(double[] chargerPosition);

    public String getUnit();

    public void switchReturnOn();

    public void switchReturnOff();

    @Override
    public String toString();
}
