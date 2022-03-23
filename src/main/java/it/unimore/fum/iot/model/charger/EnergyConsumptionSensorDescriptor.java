package it.unimore.fum.iot.model.charger;

import java.util.Random;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 22/03/2022 - 16:22
 */
public class EnergyConsumptionSensorDescriptor {

    // sensor's parameters
    private long timestamp = System.currentTimeMillis();
    private double value;
    private String unit = "Kw/h";

    // utility variables
    private final transient Random random; // this variable mustn't be serialized
    private static final double CAPACITY = 0.7;
    private static final double RECHARGING_SPEED = 0.003;

    public EnergyConsumptionSensorDescriptor() {
        this.random = new Random();
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Random getRandom() {
        return random;
    }

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
        sb.append("timestamp=").append(timestamp);
        sb.append(", value=").append(value);
        sb.append(", unit='").append(unit).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
