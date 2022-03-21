package it.unimore.fum.iot.model.robot;

import java.util.Arrays;
import java.util.Random;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 12/03/2022 - 16:39
 */
public class IndoorPositionSensorDescriptor {

    // sensor's parameters
    private long timestamp;
    private double[] position = new double[2]; // 2 values array

    // utility variables
    private final transient Random random; // this variable mustn't be serialized
    private final double[] roomDimensions; // 2 values array
    private double[] chargerPosition = new double[3]; // 2 values array
    private int direction = 1;
    private static final double SPEED = 0.0002;
    private long timer = System.currentTimeMillis();

    public IndoorPositionSensorDescriptor(double[] roomDimensions) {
        this.random = new Random();
        this.roomDimensions = roomDimensions;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double[] getPosition() {
        return position;
    }

    public void setPosition(double[] position) {
        this.position = position;
    }

    public Random getRandom() {
        return random;
    }

    public void updateIndoorPosition(){


        /*
         * 'direction' values:
         * 0 => up
         * 1 => up-right
         * 2 => right
         * 3 => down-right
         * 4 => down
         * 5 => down-left
         * 6 => left
         * 7 => up-left
         */
        //this.direction = this.random.nextInt(8);

        // max distance traveled during time
        double distance = (SPEED * (System.currentTimeMillis() - this.timer));
        if (distance == 0.0) {
            distance = 1.0;
        }

        // simulating robot's moves inside the dimensions of the room

        // obtaining x
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
            if (this.position[0] < this.roomDimensions[0] && this.position[0] > 0)
                control = true;
        }

        // obtaining y
        while (control) {
            sign = this.random.nextBoolean();
            if (!sign) {
                this.position[1] -= this.random.nextDouble(distance);
            } else {
                this.position[1] += this.random.nextDouble(distance);
            }
            if (this.position[1] < this.roomDimensions[1] && this.position[1] > 0)
                control = false;
        }

        /*

        if (direction == 1) {
            // from up-right
            if (((this.position[0] + distance) < this.roomDimensions[0]) && ((this.position[1] + distance) < this.roomDimensions[1])) {
                this.position[0] += distance;
                this.position[1] += distance; // continue
            } else if (((this.position[0] + distance) < this.roomDimensions[0]) && ((this.position[1] + distance) > this.roomDimensions[1])) {
                this.position[0] += distance;
                this.position[1] -= distance;
                direction = 2; // to down-right
            } else if (((this.position[0] + distance) > this.roomDimensions[0]) && ((this.position[1] + distance) < this.roomDimensions[1])) {
                this.position[0] -= distance;
                this.position[1] += distance;
                direction = 4; // to up-left
            } else {
                this.position[0] -= distance;
                this.position[1] -= distance;
                direction = 3; // to down-left
            }

        } else if (direction == 2) {
            // from down-right
            if (((this.position[0] + distance) < this.roomDimensions[0]) && ((this.position[1] - distance) > 0)) {
                this.position[0] += distance;
                this.position[1] -= distance; // continue
            } else if (((this.position[0] + distance) < this.roomDimensions[0]) && ((this.position[1] - distance) < 0)) {
                this.position[0] += distance;
                this.position[1] += distance;
                direction = 1; // to up-right
            } else if (((this.position[0] + distance) > this.roomDimensions[0]) && ((this.position[1] - distance) > 0)) {
                this.position[0] -= distance;
                this.position[1] -= distance;
                direction = 3; // to down-left
            } else {
                this.position[0] -= distance;
                this.position[1] += distance;
                direction = 4; // to up-left
            }

        } else if (direction == 3) {
            // from down-left
            if (((this.position[0] - distance) > 0) && ((this.position[1] - distance) > 0)) {
                this.position[0] -= distance;
                this.position[1] -= distance; // continue
            } else if (((this.position[0] - distance) > 0) && ((this.position[1] - distance) < 0)) {
                this.position[0] -= distance;
                this.position[1] += distance;
                direction = 4; // to up-left
            } else if (((this.position[0] - distance) < 0) && ((this.position[1] - distance) > 0)) {
                this.position[0] += distance;
                this.position[1] -= distance;
                direction = 2; // to down-right
            } else {
                this.position[0] += distance;
                this.position[1] += distance;
                direction = 1; // to up-right
            }

        } else {
            // from up-left
            if (((this.position[0] - distance) > 0) && ((this.position[1] + distance) < this.roomDimensions[1])) {
                this.position[0] -= distance;
                this.position[1] += distance; // continue
            } else if (((this.position[0] - distance) > 0) && ((this.position[1] + distance) > this.roomDimensions[1])) {
                this.position[0] -= distance;
                this.position[1] -= distance;
                direction = 3; // to up-left
            } else if (((this.position[0] - 3) < 0) && ((this.position[1] + 3) < this.roomDimensions[1])) {
                this.position[0] += distance;
                this.position[1] += distance;
                direction = 1; // to up-right
            } else {
                this.position[0] += distance;
                this.position[1] -= distance;
                direction = 2; // to down-right
            }

        }

        this.position[2] = this.roomDimensions[2];


         */
        // updating time
        this.timer = System.currentTimeMillis();

        // managing timestamp
        this.timestamp = System.currentTimeMillis();
    }

    public void updateReturningHomePosition(double[] chargerPosition) {
        this.chargerPosition = chargerPosition;

        // angular coefficient
        double m = (this.position[1] - this.chargerPosition[1]) / (this.position[0] - this.chargerPosition[0]);

        // distance traveled during time
        double distance = (0.0002 * (System.currentTimeMillis() - this.timer));



    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("IndoorPositionSensorDescriptor{");
        sb.append("timestamp=").append(timestamp);
        sb.append(", position=").append(Arrays.toString(position));
        sb.append('}');
        return sb.toString();
    }
}
