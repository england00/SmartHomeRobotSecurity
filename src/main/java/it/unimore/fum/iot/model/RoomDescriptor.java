package it.unimore.fum.iot.model;

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

    public RoomDescriptor(String room, double[] dimensions) {
        this.room = room;
        this.dimensions = dimensions;
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

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("RoomDescriptor{");
        sb.append(", room='").append(room).append('\'');
        sb.append(", position=").append(Arrays.toString(dimensions));
        sb.append('}');
        return sb.toString();
    }
}
