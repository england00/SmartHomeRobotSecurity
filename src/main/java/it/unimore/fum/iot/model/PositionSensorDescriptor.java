package it.unimore.fum.iot.model;

import java.util.Random;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 12/03/2022 - 16:39
 */
public class PositionSensorDescriptor {

    // sensor's parameters
    private long timestamp;

    private double x;

    private double y;

    private transient Random random;

    public PositionSensorDescriptor(Random random) {
        this.random = random;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
