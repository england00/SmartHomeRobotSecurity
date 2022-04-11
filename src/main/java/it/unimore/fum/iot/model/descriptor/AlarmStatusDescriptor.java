package it.unimore.fum.iot.model.descriptor;

import java.util.Arrays;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 11/04/2022 - 09:18
 */
public class AlarmStatusDescriptor {

    // object's parameters
    private int activeAlarm; // 0 - OFF, 1 - ON
    private int soundAlarm; // 0 - OFF, 1 - ON

    public AlarmStatusDescriptor(int activeAlarm, int soundAlarm) {
        this.activeAlarm = activeAlarm;
        this.soundAlarm = soundAlarm;
    }

    public int getActiveAlarm() {
        return activeAlarm;
    }

    public void setActiveAlarm(int activeAlarm) {
        this.activeAlarm = activeAlarm;
    }

    public int getSoundAlarm() {
        return soundAlarm;
    }

    public void setSoundAlarm(int soundAlarm) {
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
