package it.unimore.fum.iot.test.model;

import java.util.Random;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 03/04/2022 - 20:14
 */
public interface IEnergyConsumptionSensorDescriptor {

    // Energy Consumption Sensor Interface

    public String getChargerId();

    public long getTimestamp();

    public Number getVersion();

    public double getValue();

    public String getUnit();

    public void checkEnergyConsumption();

    @Override
    public String toString();
}
