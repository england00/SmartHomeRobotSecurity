package it.unimore.fum.iot.test.robot;

import it.unimore.fum.iot.model.robot.CameraSwitchActuatorDescriptor;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 15/03/2022 - 02:25
 */
public class CameraSwitchActuatorDescriptorTester {

    public static void main(String[] args) {

        System.out.println("Testing Camera Switch Actuator ...");

        // requesting battery level
        CameraSwitchActuatorDescriptor cameraSwitchActuatorDescriptor = new CameraSwitchActuatorDescriptor();

        int control;
        for (control = 0; control < 1000; control++) {

            if (cameraSwitchActuatorDescriptor.isStatus()) {
                cameraSwitchActuatorDescriptor.changeSwitchStatusOff();
            } else
                cameraSwitchActuatorDescriptor.changeSwitchStatusOn();
            System.out.println(cameraSwitchActuatorDescriptor);

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

