package it.unimore.fum.iot.test.model.raw;

import it.unimore.fum.iot.test.model.IEnergyConsumptionSensorDescriptor;

import java.util.Random;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 22/03/2022 - 16:22
 */
public class EnergyConsumptionSensorDescriptor implements IEnergyConsumptionSensorDescriptor {

    // sensor's parameters
    private String chargerId;
    private long timestamp = System.currentTimeMillis();
    private Number version;
    private double value;
    private String unit = "kWh";

    // utility variables
    private final transient Random random; // this variable mustn't be serialized
    private static final double CAPACITY = 0.7;
    private static final double RECHARGING_SPEED = 0.003;

    public EnergyConsumptionSensorDescriptor(String chargerId, Number version) {
        this.chargerId = chargerId;
        this.version = version;
        this.random = new Random();
    }

    @Override
    public String getChargerId() {
        return chargerId;
    }

    public void setChargerId(String chargerId) {
        this.chargerId = chargerId;
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public Number getVersion() {
        return version;
    }

    public void setVersion(Number version) {
        this.version = version;
    }

    @Override
    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Random getRandom() {
        return random;
    }

    @Override
    public void checkEnergyConsumption(){
        // managing energy consumption values
        this.value += ((RECHARGING_SPEED * (System.currentTimeMillis() - this.timestamp)) / 100) * CAPACITY;
        if (this.value >= CAPACITY) {
            this.value = CAPACITY;
        }

        // managing timestamp
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("EnergyConsumptionSensorDescriptor{");
        sb.append("chargerId='").append(chargerId).append('\'');
        sb.append("timestamp=").append(timestamp);
        sb.append(", version=").append(version);
        sb.append(", value=").append(value);
        sb.append(", unit='").append(unit).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
