package it.unimore.fum.iot.server;

import it.unimore.fum.iot.exception.ManagerConflict;
import it.unimore.fum.iot.exception.ManagerException;
import it.unimore.fum.iot.model.descriptor.RoomDescriptor;
import it.unimore.fum.iot.model.descriptor.PresenceMonitoringObjectDescriptor;
import it.unimore.fum.iot.model.raw.PresenceRawSensor;
import it.unimore.fum.iot.persistence.IRoomsManager;
import it.unimore.fum.iot.persistence.RoomsManager;
import it.unimore.fum.iot.resource.presence.PassiveInfraRedSensorResource;
import it.unimore.fum.iot.resource.presence.PresenceMonitoringObjectResource;
import org.eclipse.californium.core.CoapServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.UUID;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 03/04/2022 - 18:49
 */
public class PresenceMonitoringObjectCoapProcess extends CoapServer {

    private final static Logger logger = LoggerFactory.getLogger(RobotCoapProcess.class);

    public PresenceMonitoringObjectCoapProcess(int port) throws ManagerException, ManagerConflict {

        super(port);
        String presenceId = String.format("presence-%s", UUID.randomUUID().toString());

        // room
        RoomDescriptor roomDescriptor = Room();

        // object
        PresenceMonitoringObjectDescriptor presenceMonitoringObjectDescriptor = new PresenceMonitoringObjectDescriptor(presenceId, roomDescriptor.getRoom(), 5.0, "Phillips");

        // resources creation
        ResourcesCreation(roomDescriptor, presenceMonitoringObjectDescriptor);
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

    private void ResourcesCreation(RoomDescriptor roomDescriptor, PresenceMonitoringObjectDescriptor presenceMonitoringObjectDescriptor) {

        // init emulated physical sensor
        PresenceRawSensor presencePIRSensorDescriptor = new PresenceRawSensor(presenceMonitoringObjectDescriptor.getPresenceId(), 0.1, true);

        // descriptor
        this.add(new PresenceMonitoringObjectResource("descriptor", presenceMonitoringObjectDescriptor));

        // sensor
        this.add(new PassiveInfraRedSensorResource("pir", presencePIRSensorDescriptor));
    }

    public static void main(String[] args) throws ManagerException, ManagerConflict {

        // creating the server adding a new port because all the servers run on the same ip
        PresenceMonitoringObjectCoapProcess presenceMonitoringObjectCoapProcess = new PresenceMonitoringObjectCoapProcess(5684);
        presenceMonitoringObjectCoapProcess.start();

        logger.info("Presence Monitoring Object Server Started! Available resources: ");

        presenceMonitoringObjectCoapProcess.getRoot().getChildren().forEach(resource -> {
            logger.info("Resource {} -> URI: {} (Observable: {})", resource.getName(), resource.getURI(), resource.isObservable());
            if(!resource.getURI().equals("/.well-known")){
                resource.getChildren().forEach(childResource -> {
                    logger.info("\t Resource {} -> URI: {} (Observable: {})", childResource.getName(), childResource.getURI(), childResource.isObservable());
                });
            }
        });
    }
}
