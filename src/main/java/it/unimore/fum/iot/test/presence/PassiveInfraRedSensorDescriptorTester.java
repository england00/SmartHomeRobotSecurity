package it.unimore.fum.iot.test.presence;

import it.unimore.fum.iot.model.presence.PassiveInfraRedSensorDescriptor;
import it.unimore.fum.iot.model.robot.PresenceInCameraStreamSensorDescriptor;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 17/03/2022 - 01:43
 */
public class PassiveInfraRedSensorDescriptorTester {

    public static void main(String[] args) {

        System.out.println("Testing PIR Sensor ...");

        // requesting PIR signal
        PassiveInfraRedSensorDescriptor passiveInfraRedSensorDescriptor = new PassiveInfraRedSensorDescriptor("presence-monitoring-0001", 0.1);

        int control;
        for (control = 0; control < 1000; control++) {

            passiveInfraRedSensorDescriptor.checkPassiveInfraRedSensorDescriptor();
            System.out.println(passiveInfraRedSensorDescriptor);

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
