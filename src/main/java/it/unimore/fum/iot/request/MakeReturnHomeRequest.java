package it.unimore.fum.iot.request;

import java.util.Arrays;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 28/03/2022 - 21:10
 */
public class MakeReturnHomeRequest {

    public static final String SWITCH_ON_RETURN_HOME = "return_on";
    public static final String SWITCH_OFF_RETURN_HOME = "return_off";
    private String type;
    private double[] position = new double[2];

    public MakeReturnHomeRequest() {}

    public MakeReturnHomeRequest(String type, double[] position) {
        this.type = type;
        this.position = position;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double[] getPosition() {
        return position;
    }

    public void setPosition(double[] position) {
        this.position = position;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("MakeReturnHomeRequest{");
        sb.append("type='").append(type).append('\'');
        sb.append(", position=").append(Arrays.toString(position));
        sb.append('}');
        return sb.toString();
    }
}
