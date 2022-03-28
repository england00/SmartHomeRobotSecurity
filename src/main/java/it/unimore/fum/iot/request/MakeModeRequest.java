package it.unimore.fum.iot.request;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 28/03/2022 - 20:27
 */
public class MakeModeRequest {

    public static final String MODE_START = "start";
    public static final String MODE_PAUSE = "pause";
    public static final String MODE_STOP = "stop";
    private String type;

    public MakeModeRequest() {}

    public MakeModeRequest(String type) {
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
        final StringBuffer sb = new StringBuffer("MakeModeRequest{");
        sb.append("type='").append(type).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
