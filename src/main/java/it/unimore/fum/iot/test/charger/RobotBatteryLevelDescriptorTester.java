package it.unimore.fum.iot.test.charger;

import it.unimore.fum.iot.model.charger.IRobotBatteryLevelSensorDescriptor;
import it.unimore.fum.iot.model.charger.raw.RobotBatteryLevelSensorDescriptor;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 23/03/2022 - 03:07
 */
public class RobotBatteryLevelDescriptorTester {

    public static void main(String[] args) {

        System.out.println("Testing Battery Level Sensor ...");

        // requesting battery level
        IRobotBatteryLevelSensorDescriptor robotBatteryLevelDescriptor = new RobotBatteryLevelSensorDescriptor();

        int control;
        for (control = 0; control < 1000; control++) {

            robotBatteryLevelDescriptor.checkRechargingBatteryLevel();
            System.out.println(robotBatteryLevelDescriptor);

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
