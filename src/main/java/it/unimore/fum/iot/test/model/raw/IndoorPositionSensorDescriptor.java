package it.unimore.fum.iot.test.model.raw;

import it.unimore.fum.iot.test.model.IIndoorPositionSensorDescriptor;

import java.util.Arrays;
import java.util.Random;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 12/03/2022 - 16:39
 */
public class IndoorPositionSensorDescriptor implements IIndoorPositionSensorDescriptor {

    // sensor's parameters
    private String robotId;
    private long timestamp = System.currentTimeMillis();
    private Number version;
    private double[] position = new double[2]; // 2 values array
    private String unit = "meter";

    // utility variables
    private final transient Random random; // this variable mustn't be serialized
    private final double[] roomDimensions; // 2 values array
    private final double[] origin; // offset from the robot begin
    private static final double SPEED = 0.0005; // 50 cm/s
    private double[] chargerPosition;
    private boolean returnFlag = false;

    public IndoorPositionSensorDescriptor(String robotId, Number version, double[] roomDimensions, double[] origin) {
        this.robotId = robotId;
        this.version = version;
        this.random = new Random();
        this.roomDimensions = roomDimensions;
        this.origin = origin;
    }

    @Override
    public String getRobotId() {
        return robotId;
    }

    public void setRobotId(String robotId) {
        this.robotId = robotId;
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
    public double[] getPosition() {
        return position;
    }

    public void setPosition(double[] position) {
        this.position = position;
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
    public double[] getRoomDimensions() {
        return roomDimensions;
    }

    @Override
    public double[] getOrigin() {
        return origin;
    }

    public double[] getChargerPosition() {
        return chargerPosition;
    }

    @Override
    // set by the RETURN HOME ACTUATOR
    public void setChargerPosition(double[] chargerPosition) {
        this.chargerPosition = chargerPosition;
    }

    public boolean isReturnFlag() {
        return returnFlag;
    }

    @Override
    // set by the RETURN HOME ACTUATOR
    public void setReturnFlag(boolean returnFlag) {
        this.returnFlag = returnFlag;
    }

    @Override
    public void updateIndoorPosition() {

        if (!this.returnFlag) {

            // max distance traveled during time
            double distance = (SPEED * (System.currentTimeMillis() - this.timestamp));
            if (distance == 0.0) {
                distance = 1.0;
            }

            // simulating normal robot's moves inside the dimensions of the room
            boolean sign;
            boolean control = false;

            // obtaining x
            while (!control) {
                sign = this.random.nextBoolean();
                if (!sign) {
                    this.position[0] -= this.random.nextDouble(distance);
                } else {
                    this.position[0] += this.random.nextDouble(distance);
                }
                if (this.position[0] < (this.roomDimensions[0] + this.origin[0]) && this.position[0] > this.origin[0]) {
                    control = true;
                }
            }

            // obtaining y
            while (control) {
                sign = this.random.nextBoolean();
                if (!sign) {
                    this.position[1] -= this.random.nextDouble(distance);
                } else {
                    this.position[1] += this.random.nextDouble(distance);
                }
                if (this.position[1] < (this.roomDimensions[1] + this.origin[1]) && this.position[1] > this.origin[1]) {
                    control = false;
                }
            }

        } else {

            // simulating robot's returning home moves inside the dimensions of the room
            // angular coefficient and q
            double m = (this.position[1] - chargerPosition[1]) / (this.position[0] - chargerPosition[0]);
            double q = (this.chargerPosition[1] - m * this.chargerPosition[0]);

            // effective distance from destination
            double dDestination = Math.pow((Math.pow((this.position[1] - chargerPosition[1]), 2) +
                    Math.pow((this.position[0] - chargerPosition[0]), 2)), 0.5);

            if (dDestination > 0.001) {
                if (this.position[0] > this.chargerPosition[0]) {
                    this.position[0] -= this.random.nextDouble(this.position[0] - this.chargerPosition[0]);
                } else {
                    this.position[0] += this.random.nextDouble(this.chargerPosition[0] - this.position[0]);
                }
                this.position[1] = this.position[0] * m + q;
            } else {
                this.position[0] = this.chargerPosition[0];
                this.position[1] = this.chargerPosition[1];
            }
        }

        // managing timestamp
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("IndoorPositionSensorDescriptor{");
        sb.append("robotId='").append(robotId).append('\'');
        sb.append(", timestamp=").append(timestamp);
        sb.append(", version=").append(version);
        sb.append(", position=").append(Arrays.toString(position));
        sb.append(", unit='").append(unit).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
