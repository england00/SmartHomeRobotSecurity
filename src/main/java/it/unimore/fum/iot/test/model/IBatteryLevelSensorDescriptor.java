package it.unimore.fum.iot.test.model;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 03/04/2022 - 17:21
 */
public interface IBatteryLevelSensorDescriptor {

    // Battery Level Sensor Interface

    public String getRobotId();

    public long getTimestamp();

    public Number getVersion();

    public double getBatteryLevel();

    // reset on 100.0 by the CHARGING STATION
    public void setBatteryLevel(double batteryLevel);

    public String getUnit();

    public void checkBatteryLevel();

    @Override
    public String toString();
}
