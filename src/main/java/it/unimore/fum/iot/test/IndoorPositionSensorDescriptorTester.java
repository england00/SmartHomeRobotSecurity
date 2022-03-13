package it.unimore.fum.iot.test;

import it.unimore.fum.iot.model.sensors.IndoorPositionSensorDescriptor;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 13/03/2022 - 21:29
 */
public class IndoorPositionSensorDescriptorTester {

    public static void main(String[] args) {

        System.out.println("Testing Indoor Position Sensor ...");

        // requesting position
        IndoorPositionSensorDescriptor indoorPositionSensorDescriptor = new IndoorPositionSensorDescriptor(3, 4);

        int control;
        for (control = 0; control < 1000; control++) {

            indoorPositionSensorDescriptor.updateIndoorPosition();
            System.out.println(indoorPositionSensorDescriptor.toString());

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
