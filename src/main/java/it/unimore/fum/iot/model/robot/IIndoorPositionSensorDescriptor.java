package it.unimore.fum.iot.model.robot;

import java.util.Arrays;
import java.util.Random;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 24/03/2022 - 14:41
 */
public interface IIndoorPositionSensorDescriptor {

    // Indoor Position Sensor Interface

    public String getRobotId();

    public long getTimestamp();

    public Number getVersion();

    public double[] getPosition();

    public String getUnit();

    public double[] getRoomDimensions();

    public double[] getOrigin();

    // set by the RETURN HOME ACTUATOR
    public void setChargerPosition(double[] chargerPosition);

    // set by the RETURN HOME ACTUATOR
    public void setReturnFlag(boolean returnFlag);

    public void updateIndoorPosition();

    @Override
    public String toString();
}
