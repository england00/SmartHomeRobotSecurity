package it.unimore.fum.iot.test.robot;

import it.unimore.fum.iot.test.model.ICameraSwitchActuatorDescriptor;
import it.unimore.fum.iot.test.model.raw.CameraSwitchActuatorDescriptor;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 15/03/2022 - 02:25
 */
public class CameraSwitchActuatorDescriptorTester {

    public static void main(String[] args) {

        System.out.println("Testing Camera Switch Actuator ...");

        // requesting
        ICameraSwitchActuatorDescriptor cameraSwitchActuatorDescriptor = new CameraSwitchActuatorDescriptor();

        int control;
        for (control = 0; control < 1000; control++) {

            if (cameraSwitchActuatorDescriptor.isValue()) {
                cameraSwitchActuatorDescriptor.switchStatusOff();
            } else
                cameraSwitchActuatorDescriptor.switchStatusOn();
            System.out.println(cameraSwitchActuatorDescriptor);

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
