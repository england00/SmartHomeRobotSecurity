package it.unimore.fum.iot.server;

import it.unimore.fum.iot.exception.*;
import it.unimore.fum.iot.model.descriptor.RoomDescriptor;
import it.unimore.fum.iot.model.descriptor.RobotDescriptor;
import it.unimore.fum.iot.model.raw.*;
import it.unimore.fum.iot.persistence.IRoomsManager;
import it.unimore.fum.iot.persistence.RoomsManager;
import it.unimore.fum.iot.resource.robot.*;
import org.eclipse.californium.core.CoapServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.UUID;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 29/03/2022 - 01:51
 */
public class RobotCoapProcess extends CoapServer {

    private final static Logger logger = LoggerFactory.getLogger(RobotCoapProcess.class);

    public RobotCoapProcess() throws RoomsManagerException, RoomsManagerConflict {

        super();
        String robotId = String.format("robot-%s", UUID.randomUUID().toString());

        logger.info(String.valueOf(logger.isDebugEnabled()));

        // room
        RoomDescriptor roomDescriptor = Room();

        // object
        RobotDescriptor robotDescriptor = new RobotDescriptor(robotId, roomDescriptor.getRoom(), 5.0, "Phillips");
        logger.info(String.valueOf(robotDescriptor));

        // resources creation
        ResourcesCreation(roomDescriptor, robotDescriptor);
    }

    private RoomDescriptor Room() throws RoomsManagerException, RoomsManagerConflict {

        // take existing room from file
        IRoomsManager roomsManager = new RoomsManager();
        RoomDescriptor roomDescriptor = roomsManager.getRoom("home"); // take data from file

        // create a new room
        //roomDescriptor = new RoomDescriptor("home", new double[] {15.0, 20.0}, new double[] {0.0, 0.0});
        //roomsManager.createNewRoom(roomDescriptor);

        // update an existing room
        // RoomDescriptor roomDescriptor = new RoomDescriptor("home", new double[] {3.0, 4.0}, new double[] {1.5, 2.0});
        // roomsManager.updateRoom(roomDescriptor);

        // delete an existing room
        // roomsManager.deleteRoom("home")

        roomsManager.hashMapToTextFile();
        return roomDescriptor;
    }

    private void ResourcesCreation(RoomDescriptor roomDescriptor, RobotDescriptor robotDescriptor) {

        // init emulated physical sensors and actuators
        BatteryLevelRawSensor robotBatteryLevelSensor = new BatteryLevelRawSensor(robotDescriptor.getRobotId(), 0.1, false);
        IndoorPositionRawSensor robotIndoorPositionSensor = new IndoorPositionRawSensor(robotDescriptor.getRobotId(), 0.1, roomDescriptor.getDimensions(), roomDescriptor.getOrigin());
        PresenceRawSensor robotPresenceInCameraStreamSensor = new PresenceRawSensor(robotDescriptor.getRobotId(), 0.1, true);
        SwitchRawActuator robotCameraSwitchActuator = new SwitchRawActuator(robotDescriptor.getRobotId(), 0.1);
        ModeRawActuator robotModeActuator = new ModeRawActuator(robotDescriptor.getRobotId(), 0.1);
        ReturnHomeRawActuator robotReturnHomeActuator = new ReturnHomeRawActuator(robotDescriptor.getRobotId(), 0.1);

        // descriptor
        this.add(new RobotResource("descriptor", robotDescriptor));

        // sensors
        this.add(new BatteryLevelSensorResource("battery", robotBatteryLevelSensor));
        this.add(new IndoorPositionSensorResource("position", robotIndoorPositionSensor));
        this.add(new PresenceInCameraStreamSensorResource("presence", robotPresenceInCameraStreamSensor));

        // actuators
        this.add(new CameraSwitchActuatorResource("camera", robotCameraSwitchActuator));
        this.add(new ModeActuatorResource("mode", robotModeActuator, robotIndoorPositionSensor, robotBatteryLevelSensor));
        this.add(new ReturnHomeActuatorResource("home", robotReturnHomeActuator, robotIndoorPositionSensor));
    }

    public static void main(String[] args) throws RoomsManagerException, RoomsManagerConflict {

        RobotCoapProcess robotCoapProcess = new RobotCoapProcess();
        robotCoapProcess.start();

        logger.info("Robot Server Started! Available resources: ");

        robotCoapProcess.getRoot().getChildren().forEach(resource -> {
            logger.info("Resource {} -> URI: {} (Observable: {})", resource.getName(), resource.getURI(), resource.isObservable());
            if(!resource.getURI().equals("/.well-known")){
                resource.getChildren().forEach(childResource -> {
                    logger.info("\t Resource {} -> URI: {} (Observable: {})", childResource.getName(), childResource.getURI(), childResource.isObservable());
                });
            }
        });
    }
}
