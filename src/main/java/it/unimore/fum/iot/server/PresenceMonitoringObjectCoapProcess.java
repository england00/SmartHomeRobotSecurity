package it.unimore.fum.iot.server;

import it.unimore.fum.iot.exception.RoomsManagerConflict;
import it.unimore.fum.iot.exception.RoomsManagerException;
import it.unimore.fum.iot.model.home.RoomDescriptor;
import it.unimore.fum.iot.model.presence.IPassiveInfraRedSensorDescriptor;
import it.unimore.fum.iot.model.presence.PresenceMonitoringObjectDescriptor;
import it.unimore.fum.iot.model.presence.raw.PassiveInfraRedSensorDescriptor;
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

    public PresenceMonitoringObjectCoapProcess(int port) throws RoomsManagerException, RoomsManagerConflict {

        super(port);
        String presenceId = String.format("presence-%s", UUID.randomUUID().toString());

        // room
        RoomDescriptor roomDescriptor = Room();

        // object
        PresenceMonitoringObjectDescriptor presenceMonitoringObjectDescriptor = new PresenceMonitoringObjectDescriptor(presenceId, roomDescriptor.getRoom(), 5.0, "Phillips");

        // resources creation
        ResourcesCreation(roomDescriptor, presenceMonitoringObjectDescriptor);
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

    private void ResourcesCreation(RoomDescriptor roomDescriptor, PresenceMonitoringObjectDescriptor presenceMonitoringObjectDescriptor) {

        // init emulated physical sensor
        IPassiveInfraRedSensorDescriptor passiveInfraRedSensorDescriptor = new PassiveInfraRedSensorDescriptor(presenceMonitoringObjectDescriptor.getPresenceId(), 0.1);

        // descriptor
        this.add(new PresenceMonitoringObjectResource("descriptor", presenceMonitoringObjectDescriptor));

        // sensor
        this.add(new PassiveInfraRedSensorResource("sensor/pir", passiveInfraRedSensorDescriptor));
    }

    public static void main(String[] args) throws RoomsManagerException, RoomsManagerConflict {

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
