package it.unimore.fum.iot.test.robot;

import it.unimore.fum.iot.model.robot.BatteryLevelSensorDescriptor;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 13/03/2022 - 22:53
 */
public class BatteryLevelSensorDescriptorTester {

    public static void main(String[] args) {

        System.out.println("Testing Battery Level Sensor ...");

        // requesting battery level
        BatteryLevelSensorDescriptor batteryLevelSensorDescriptor = new BatteryLevelSensorDescriptor();

        int control;
        for (control = 0; control < 1000; control++) {

            batteryLevelSensorDescriptor.checkBatteryLevel();
            System.out.println(batteryLevelSensorDescriptor);

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
