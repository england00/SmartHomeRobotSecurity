package it.unimore.fum.iot.model.robot;

import java.util.Random;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 12/03/2022 - 16:39
 */
public class IndoorPositionSensorDescriptor {

    // sensor's parameters
    private long timestamp;
    private double x = 0;
    private double y = 0;

    // utility variables
    private final transient Random random; // this variable mustn't be serialized
    private final double xRoomDimension;
    private final double yRoomDimension;
    private int direction = 1;
    private long timer = System.currentTimeMillis();

    public IndoorPositionSensorDescriptor(double xRoomDimension, double yRoomDimension) {
        this.random = new Random();
        this.xRoomDimension = xRoomDimension;
        this.yRoomDimension = yRoomDimension;
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

    public Random getRandom() {
        return random;
    }

    public void updateIndoorPosition(){

        /*
         * 'direction' values:
         * 1 => up-right
         * 2 => down-right
         * 3 => down-left
         * 4 => up-left
         */

        // distance traveled during time
        double distance = (0.0002 * (System.currentTimeMillis() - this.timer));

        // simulating robot's moves inside the dimensions of the room
        if (direction == 1) {
            // from up-right
            if (((this.x + distance) < this.xRoomDimension) && ((this.y + distance) < this.yRoomDimension)) {
                this.x += distance;
                this.y += distance; // continue
            } else if (((this.x + distance) < this.xRoomDimension) && ((this.y + distance) > this.yRoomDimension)) {
                this.x += distance;
                this.y -= distance;
                direction = 2; // to down-right
            } else if (((this.x + distance) > this.xRoomDimension) && ((this.y + distance) < this.yRoomDimension)) {
                this.x -= distance;
                this.y += distance;
                direction = 4; // to up-left
            } else {
                this.x -= distance;
                this.y -= distance;
                direction = 3; // to down-left
            }

        } else if (direction == 2) {
            // from down-right
            if (((this.x + distance) < this.xRoomDimension) && ((this.y - distance) > 0)) {
                this.x += distance;
                this.y -= distance; // continue
            } else if (((this.x + distance) < this.xRoomDimension) && ((this.y - distance) < 0)) {
                this.x += distance;
                this.y += distance;
                direction = 1; // to up-right
            } else if (((this.x + distance) > this.xRoomDimension) && ((this.y - distance) > 0)) {
                this.x -= distance;
                this.y -= distance;
                direction = 3; // to down-left
            } else {
                this.x -= distance;
                this.y += distance;
                direction = 4; // to up-left
            }

        } else if (direction == 3) {
            // from down-left
            if (((this.x - distance) > 0) && ((this.y - distance) > 0)) {
                this.x -= distance;
                this.y -= distance; // continue
            } else if (((this.x - distance) > 0) && ((this.y - distance) < 0)) {
                this.x -= distance;
                this.y += distance;
                direction = 4; // to up-left
            } else if (((this.x - distance) < 0) && ((this.y - distance) > 0)) {
                this.x += distance;
                this.y -= distance;
                direction = 2; // to down-right
            } else {
                this.x += distance;
                this.y += distance;
                direction = 1; // to up-right
            }

        } else {
            // from up-left
            if (((this.x - distance) > 0) && ((this.y + distance) < this.yRoomDimension)) {
                this.x -= distance;
                this.y += distance; // continue
            } else if (((this.x - distance) > 0) && ((this.y + distance) > this.yRoomDimension)) {
                this.x -= distance;
                this.y -= distance;
                direction = 3; // to up-left
            } else if (((this.x - 3) < 0) && ((this.y + 3) < this.yRoomDimension)) {
                this.x += distance;
                this.y += distance;
                direction = 1; // to up-right
            } else {
                this.x += distance;
                this.y -= distance;
                direction = 2; // to down-right
            }

        }

        // updating time
        this.timer = System.currentTimeMillis();

        // managing timestamp
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("IndoorPositionSensorDescriptor{");
        sb.append("timestamp=").append(timestamp);
        sb.append(", x=").append(x);
        sb.append(", y='").append(y).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
