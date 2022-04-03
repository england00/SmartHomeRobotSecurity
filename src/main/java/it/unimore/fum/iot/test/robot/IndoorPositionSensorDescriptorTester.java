package it.unimore.fum.iot.test.robot;

import it.unimore.fum.iot.model.home.RoomDescriptor;
import it.unimore.fum.iot.model.robot.IIndoorPositionSensorDescriptor;
import it.unimore.fum.iot.model.robot.raw.IndoorPositionSensorDescriptor;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 13/03/2022 - 21:29
 */
public class IndoorPositionSensorDescriptorTester {

    public static void main(String[] args) {

        System.out.println("Testing Indoor Position Sensor ...");

        // defining room's dimensions
        RoomDescriptor roomDescriptor = new RoomDescriptor("Living Room", new double[]{3.0, 4.0}, new double[]{1.0, 0.0});

        // requesting position
        // IIndoorPositionSensorDescriptor iIndoorPositionSensorDescriptor = new IndoorPositionSensorDescriptor(new double[]{3.0, 4.0});
        IIndoorPositionSensorDescriptor indoorPositionSensorDescriptor = new IndoorPositionSensorDescriptor("robot-0001", 0.1, roomDescriptor.getDimensions(), roomDescriptor.getOrigin());

        int control;
        for (control = 0; control < 1000; control++) {

            if (control < 20 || control >= 40) {
                indoorPositionSensorDescriptor.setReturnFlag(false);
                indoorPositionSensorDescriptor.updateIndoorPosition();
            } else {
                indoorPositionSensorDescriptor.setChargerPosition(new double[]{1.5, 2.7});
                indoorPositionSensorDescriptor.setReturnFlag(true);
                indoorPositionSensorDescriptor.updateIndoorPosition();
            }
            System.out.println(indoorPositionSensorDescriptor);

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
