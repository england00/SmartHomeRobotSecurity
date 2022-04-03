package it.unimore.fum.iot.server;

import it.unimore.fum.iot.exception.RoomsManagerConflict;
import it.unimore.fum.iot.exception.RoomsManagerException;
import it.unimore.fum.iot.model.charger.ChargingStationDescriptor;
import it.unimore.fum.iot.model.charger.IEnergyConsumptionSensorDescriptor;
import it.unimore.fum.iot.model.charger.IRobotBatteryLevelSensorDescriptor;
import it.unimore.fum.iot.model.charger.IRobotPresenceSensorDescriptor;
import it.unimore.fum.iot.model.charger.raw.EnergyConsumptionSensorDescriptor;
import it.unimore.fum.iot.model.charger.raw.RobotBatteryLevelSensorDescriptor;
import it.unimore.fum.iot.model.charger.raw.RobotPresenceSensorDescriptor;
import it.unimore.fum.iot.model.home.RoomDescriptor;
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

    public ChargingStationCoapProcess(int port) throws RoomsManagerException, RoomsManagerConflict {

        super(port);
        String chargerId = String.format("charger-%s", UUID.randomUUID().toString());

        // room
        RoomDescriptor roomDescriptor = Room();

        // object
        ChargingStationDescriptor chargingStationDescriptor = new ChargingStationDescriptor(chargerId, roomDescriptor.getRoom(), 5.0, "Phillips", new double[] {3.0, 4.0});

        // resources creation
        ResourcesCreation(roomDescriptor, chargingStationDescriptor);
    }

    private RoomDescriptor Room() throws RoomsManagerException {

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
        IEnergyConsumptionSensorDescriptor energyConsumptionSensorDescriptor = new EnergyConsumptionSensorDescriptor(chargingStationDescriptor.getChargerId(), 0.1);
        IRobotBatteryLevelSensorDescriptor robotBatteryLevelSensorDescriptor = new RobotBatteryLevelSensorDescriptor(chargingStationDescriptor.getChargerId(), 0.1);
        IRobotPresenceSensorDescriptor robotPresenceSensorDescriptor = new RobotPresenceSensorDescriptor(chargingStationDescriptor.getChargerId(), 0.1);

        // descriptor
        this.add(new ChargingStationResource("descriptor", chargingStationDescriptor));

        // sensor
        this.add(new EnergyConsumptionSensorResource("sensor/energy_consumption", energyConsumptionSensorDescriptor));
        this.add(new RobotBatteryLevelSensorResource("sensor/recharging_battery", robotBatteryLevelSensorDescriptor));
        this.add(new RobotPresenceSensorResource("sensor/robot_presence", robotPresenceSensorDescriptor));
    }

    public static void main(String[] args) throws RoomsManagerException, RoomsManagerConflict {

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
