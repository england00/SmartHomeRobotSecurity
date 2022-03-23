package it.unimore.fum.iot.test.charger;

import it.unimore.fum.iot.model.charger.EnergyConsumptionSensorDescriptor;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 23/03/2022 - 03:16
 */
public class EnergyConsumptionSensorDescriptorTester {

    public static void main(String[] args) {

        System.out.println("Testing Battery Level Sensor ...");

        // requesting battery level
        EnergyConsumptionSensorDescriptor energyConsumptionSensorDescriptor = new EnergyConsumptionSensorDescriptor();

        int control;
        for (control = 0; control < 1000; control++) {

            energyConsumptionSensorDescriptor.checkEnergyConsumption();
            System.out.println(energyConsumptionSensorDescriptor);

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

