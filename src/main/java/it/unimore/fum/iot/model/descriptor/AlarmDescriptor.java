package it.unimore.fum.iot.model.descriptor;

import java.util.Arrays;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 11/04/2022 - 09:18
 */
public class AlarmDescriptor {

    // object's parameters
    private boolean activeAlarm;
    private boolean soundAlarm;

    public AlarmDescriptor(boolean activeAlarm, boolean soundAlarm) {
        this.activeAlarm = activeAlarm;
        this.soundAlarm = soundAlarm;
    }

    public boolean isActiveAlarm() {
        return activeAlarm;
    }

    public void setActiveAlarm(boolean activeAlarm) {
        this.activeAlarm = activeAlarm;
    }

    public boolean isSoundAlarm() {
        return soundAlarm;
    }

    public void setSoundAlarm(boolean soundAlarm) {
        this.soundAlarm = soundAlarm;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("AlarmDescriptor{");
        sb.append("activeAlarm='").append(activeAlarm).append('\'');
        sb.append(", soundAlarm='").append(soundAlarm).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
