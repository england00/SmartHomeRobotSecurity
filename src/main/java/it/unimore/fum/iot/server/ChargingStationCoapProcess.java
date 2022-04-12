package it.unimore.fum.iot.server;

import it.unimore.fum.iot.exception.ManagerConflict;
import it.unimore.fum.iot.exception.ManagerException;
import it.unimore.fum.iot.model.descriptor.ChargingStationDescriptor;
import it.unimore.fum.iot.model.descriptor.RoomDescriptor;
import it.unimore.fum.iot.model.raw.BatteryLevelRawSensor;
import it.unimore.fum.iot.model.raw.EnergyConsumptionRawSensor;
import it.unimore.fum.iot.model.raw.PresenceRawSensor;
import it.unimore.fum.iot.persistence.IRoomsManager;
import it.unimore.fum.iot.persistence.RoomsManager;
import it.unimore.fum.iot.resource.charger.ChargingStationResource;
import it.unimore.fum.iot.resource.charger.EnergyConsumptionSensorResource;
import it.unimore.fum.iot.resource.charger.RobotBatteryLevelSensorResource;
import it.unimore.fum.iot.resource.charger.RobotPresenceSensorResource;
import org.eclipse.californium.core.CoapServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.UUID;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 03/04/2022 - 21:34
 */
public class ChargingStationCoapProcess extends CoapServer {

    private final static Logger logger = LoggerFactory.getLogger(RobotCoapProcess.class);

    public ChargingStationCoapProcess(int port) throws ManagerException, ManagerConflict {

        super(port);
        String chargerId = String.format("charger-%s", UUID.randomUUID().toString());

        // room
        RoomDescriptor roomDescriptor = Room();

        // object
        ChargingStationDescriptor chargingStationDescriptor = new ChargingStationDescriptor(chargerId, roomDescriptor.getRoom(), 5.0, "Phillips", new double[] {3.0, 4.0});

        // resources creation
        ResourcesCreation(roomDescriptor, chargingStationDescriptor);
    }

    private RoomDescriptor Room() throws ManagerException {

        // take existing room from file
        IRoomsManager roomsManager = new RoomsManager();
        RoomDescriptor roomDescriptor = roomsManager.getRoom("home"); // take data from file

        // create a new room
        // RoomDescriptor roomDescriptor = new RoomDescriptor("living-room", new double[] {3.0, 4.0}, new double[] {1.5, 2.0});
        // roomsManager.createNewRoom(roomDescriptor);

        // update an existing room
        // RoomDescriptor roomDescriptor = new RoomDescriptor("home", new double[] {3.0, 4.0}, new double[] {1.5, 2.0});
        // roomsManager.updateRoom(roomDescriptor);

        // delete an existing room
        // roomsManager.deleteRoom("home")

        roomsManager.hashMapToTextFile();
        return roomDescriptor;
    }

    private void ResourcesCreation(RoomDescriptor roomDescriptor, ChargingStationDescriptor chargingStationDescriptor) {

        // init emulated physical sensor
        BatteryLevelRawSensor chargerRobotBatteryLevelSensor = new BatteryLevelRawSensor(chargingStationDescriptor.getChargerId(), 0.1, true);
        EnergyConsumptionRawSensor chargerEnergyConsumptionSensor = new EnergyConsumptionRawSensor(chargingStationDescriptor.getChargerId(), 0.1);
        PresenceRawSensor chargerRobotPresenceSensor = new PresenceRawSensor(chargingStationDescriptor.getChargerId(), 0.1, false);

        // descriptor
        this.add(new ChargingStationResource("descriptor", chargingStationDescriptor));

        // sensor
        this.add(new RobotBatteryLevelSensorResource("recharging_battery", chargerRobotBatteryLevelSensor));
        this.add(new EnergyConsumptionSensorResource("energy_consumption", chargerEnergyConsumptionSensor));
        chargerRobotPresenceSensor.setValue(true);
        this.add(new RobotPresenceSensorResource("robot_presence", chargerRobotPresenceSensor));
    }

    public static void main(String[] args) throws ManagerException, ManagerConflict {

        // creating the server adding a new port because all the servers run on the same ip
        ChargingStationCoapProcess chargingStationCoapProcess = new ChargingStationCoapProcess(5685);
        chargingStationCoapProcess.start();

        logger.info("Charging Station Server Started! Available resources: ");

        chargingStationCoapProcess.getRoot().getChildren().forEach(resource -> {
            logger.info("Resource {} -> URI: {} (Observable: {})", resource.getName(), resource.getURI(), resource.isObservable());
            if(!resource.getURI().equals("/.well-known")){
                resource.getChildren().forEach(childResource -> {
                    logger.info("\t Resource {} -> URI: {} (Observable: {})", childResource.getName(), childResource.getURI(), childResource.isObservable());
                });
            }
        });
    }
}
