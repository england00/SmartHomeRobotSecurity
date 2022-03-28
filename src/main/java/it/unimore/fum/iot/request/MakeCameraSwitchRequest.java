package it.unimore.fum.iot.request;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 28/03/2022 - 16:27
 */
public class MakeCameraSwitchRequest {

    public static final String SWITCH_ON_CAMERA = "switch_on";
    public static final String SWITCH_OFF_CAMERA = "switch_off";
    private String type;

    public MakeCameraSwitchRequest() {}

    public MakeCameraSwitchRequest(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("MakeCameraSwitchRequest{");
        sb.append("type='").append(type).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
