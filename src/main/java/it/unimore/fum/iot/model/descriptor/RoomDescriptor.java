package it.unimore.fum.iot.model.descriptor;

import java.util.Arrays;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 27/03/2022 - 18:37
 */
public class RoomDescriptor {

    // object's parameters;
    private String room;
    private double[] dimensions = new double[2]; // 2 values array
    private double[] origin = new double[2]; // coordinates of the down-left corner of the room

    public RoomDescriptor(String room, double[] dimensions, double[] origin) {
        this.room = room;
        this.dimensions = dimensions;
        this.origin = origin;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public double[] getDimensions() {
        return dimensions;
    }

    public void setDimensions(double[] dimensions) {
        this.dimensions = dimensions;
    }

    public double[] getOrigin() {
        return origin;
    }

    public void setOrigin(double[] origin) {
        this.origin = origin;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("RoomDescriptor{");
        sb.append("room='").append(room).append('\'');
        sb.append(", dimensions=").append(Arrays.toString(dimensions));
        sb.append(", origin=").append(Arrays.toString(origin));
        sb.append('}');
        return sb.toString();
    }
}
