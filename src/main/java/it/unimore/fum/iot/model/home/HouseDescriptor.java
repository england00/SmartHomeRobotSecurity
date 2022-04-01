package it.unimore.fum.iot.model.home;

import java.util.Arrays;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 31/03/2022 - 12:30
 */
public class HouseDescriptor {

    // object's parameters;
    private String name;
    private double[] dimensions = new double[2]; // 2 values array

    public HouseDescriptor(String name, double[] dimensions) {
        this.name = name;
        this.dimensions = dimensions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        sb.append(", name='").append(name).append('\'');
        sb.append(", dimensions=").append(Arrays.toString(dimensions));
        sb.append('}');
        return sb.toString();
    }
}
