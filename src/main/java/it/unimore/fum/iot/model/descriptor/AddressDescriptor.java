package it.unimore.fum.iot.model.descriptor;

import java.util.Arrays;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 12/04/2022 - 01:43
 */
public class AddressDescriptor {

    // robot
    private String robodIp;
    private String robot_descriptor;
    private String robot_battery_level_sensor;
    private String robot_indoor_position_sensor;
    private String robot_presence_sensor;
    private String robot_camera_switch_actuator;
    private String robot_mode_actuator;
    private String robot_return_home_actuator;

    // presence
    private String presenceIp;
    private String presence_monitoring_descriptor;
    private String presence_monitoring_pir_sensor;

    // charger
    private String chargerIp;
    private String charging_station_descriptor;
    private String charging_robot_presence_sensor;
    private String charging_robot_battery_level_sensor;
    private String charging_energy_consumption_sensor;

    public AddressDescriptor(String robodIp, String presenceIp, String chargerIp) {
        this.robodIp = robodIp;
        this.presenceIp = presenceIp;
        this.chargerIp = chargerIp;

        setRobot_descriptor(robodIp);
        setRobot_battery_level_sensor(robodIp);
        setRobot_indoor_position_sensor(robodIp);
        setRobot_presence_sensor(robodIp);
        setRobot_camera_switch_actuator(robodIp);
        setRobot_mode_actuator(robodIp);
        setRobot_return_home_actuator(robodIp);

        setPresence_monitoring_descriptor(presenceIp);
        setPresence_monitoring_pir_sensor(presenceIp);

        setCharging_station_descriptor(chargerIp);
        setCharging_robot_presence_sensor(chargerIp);
        setCharging_robot_battery_level_sensor(chargerIp);
        setCharging_energy_consumption_sensor(chargerIp);
    }

    public String getRobot_descriptor() {
        return robot_descriptor;
    }

    private void setRobot_descriptor(String robodIp) {
        this.robot_descriptor = "coap://" + robodIp + "/descriptor";
    }

    public String getRobot_battery_level_sensor() {
        return robot_battery_level_sensor;
    }

    private void setRobot_battery_level_sensor(String robodIp) {
        this.robot_battery_level_sensor = "coap://" + robodIp + "/battery";
    }

    public String getRobot_indoor_position_sensor() {
        return robot_indoor_position_sensor;
    }

    private void setRobot_indoor_position_sensor(String robodIp) {
        this.robot_indoor_position_sensor = "coap://" + robodIp + "/position";
    }

    public String getRobot_presence_sensor() {
        return robot_presence_sensor;
    }

    private void setRobot_presence_sensor(String robodIp) {
        this.robot_presence_sensor = "coap://" + robodIp + "/presence";
    }

    public String getRobot_camera_switch_actuator() {
        return robot_camera_switch_actuator;
    }

    private void setRobot_camera_switch_actuator(String robodIp) {
        this.robot_camera_switch_actuator = "coap://" + robodIp + "/camera";
    }

    public String getRobot_mode_actuator() {
        return robot_mode_actuator;
    }

    private void setRobot_mode_actuator(String robodIp) {
        this.robot_mode_actuator = "coap://" + robodIp + "/mode";
    }

    public String getRobot_return_home_actuator() {
        return robot_return_home_actuator;
    }

    private void setRobot_return_home_actuator(String robodIp) {
        this.robot_return_home_actuator = "coap://" + robodIp + "/home";
    }

    public String getPresence_monitoring_descriptor() {
        return presence_monitoring_descriptor;
    }

    private void setPresence_monitoring_descriptor(String presenceIp) {
        this.presence_monitoring_descriptor = "coap://" + presenceIp + "/descriptor";
    }

    public String getPresence_monitoring_pir_sensor() {
        return presence_monitoring_pir_sensor;
    }

    private void setPresence_monitoring_pir_sensor(String presenceIp) {
        this.presence_monitoring_pir_sensor = "coap://" + presenceIp + "/pir";
    }

    public String getCharging_station_descriptor() {
        return charging_station_descriptor;
    }

    private void setCharging_station_descriptor(String chargerIp) {
        this.charging_station_descriptor = "coap://" + chargerIp + "/descriptor";
    }

    public String getCharging_robot_presence_sensor() {
        return charging_robot_presence_sensor;
    }

    private void setCharging_robot_presence_sensor(String chargerIp) {
        this.charging_robot_presence_sensor = "coap://" + chargerIp + "/robot_presence";
    }

    public String getCharging_robot_battery_level_sensor() {
        return charging_robot_battery_level_sensor;
    }

    private void setCharging_robot_battery_level_sensor(String chargerIp) {
        this.charging_robot_battery_level_sensor = "coap://" + chargerIp + "/recharging_battery";
    }

    public String getCharging_energy_consumption_sensor() {
        return charging_energy_consumption_sensor;
    }

    private void setCharging_energy_consumption_sensor(String chargerIp) {
        this.charging_energy_consumption_sensor = "coap://" + chargerIp + "/energy_consumption";
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("AddressDescriptor{");
        sb.append("robot_descriptor'=").append(robot_descriptor).append('\'');
        sb.append(", robot_battery_level_sensor'=").append(robot_battery_level_sensor).append('\'');
        sb.append(", robot_indoor_position_sensor'=").append(robot_indoor_position_sensor).append('\'');
        sb.append(", robot_presence_sensor'=").append(robot_presence_sensor).append('\'');
        sb.append(", robot_camera_switch_actuator'=").append(robot_camera_switch_actuator).append('\'');
        sb.append(", robot_mode_actuator'=").append(robot_mode_actuator).append('\'');
        sb.append(", robot_return_home_actuator'=").append(robot_return_home_actuator).append('\'');
        sb.append(", presence_monitoring_descriptor'=").append(presence_monitoring_descriptor).append('\'');
        sb.append(", presence_monitoring_pir_sensor'=").append(presence_monitoring_pir_sensor).append('\'');
        sb.append(", charging_station_descriptor'=").append(charging_station_descriptor).append('\'');
        sb.append(", charging_robot_presence_sensor'=").append(charging_robot_presence_sensor).append('\'');
        sb.append(", charging_robot_battery_level_sensor'=").append(charging_robot_battery_level_sensor).append('\'');
        sb.append(", charging_energy_consumption_sensor'=").append(charging_energy_consumption_sensor).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
