package it.unimore.fum.iot.test.robot;

import it.unimore.fum.iot.model.robot.IPresenceInCameraStreamSensorDescriptor;
import it.unimore.fum.iot.model.robot.raw.PresenceInCameraStreamSensorDescriptor;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 15/03/2022 - 01:53
 */
public class PresenceInCameraStreamSensorDescriptorTester {

    public static void main(String[] args) {

        System.out.println("Testing Presence In Camera Stream Sensor ...");

        // requesting
        IPresenceInCameraStreamSensorDescriptor presenceInCameraStreamSensorDescriptor = new PresenceInCameraStreamSensorDescriptor("robot-0001", 0.1);

        int control;
        for (control = 0; control < 1000; control++) {

            presenceInCameraStreamSensorDescriptor.checkPresenceInCameraStream();
            System.out.println(presenceInCameraStreamSensorDescriptor);

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
