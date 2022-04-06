package it.unimore.fum.iot.test.model;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 03/04/2022 - 19:54
 */
public interface IRobotBatteryLevelSensorDescriptor {

    // Robot Battery Level Sensor Interface

    public String getChargerId();

    public long getTimestamp();

    public Number getVersion();

    public double getBatteryLevel();

    public void checkRechargingBatteryLevel();

    @Override
    public String toString();
}
