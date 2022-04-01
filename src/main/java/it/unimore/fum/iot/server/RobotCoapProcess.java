package it.unimore.fum.iot.server;

import it.unimore.fum.iot.model.home.RoomDescriptor;
import it.unimore.fum.iot.model.robot.RobotDescriptor;
import it.unimore.fum.iot.resource.robot.*;
import org.eclipse.californium.core.CoapServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 29/03/2022 - 01:51
 */
public class RobotCoapProcess extends CoapServer {

    private final static Logger logger = LoggerFactory.getLogger(RobotCoapProcess.class);

    public RobotCoapProcess() {

        super();
        String robotId = String.format("robot-%s", UUID.randomUUID().toString());

        // room
        RoomDescriptor roomDescriptor = new RoomDescriptor("home", new double[]{15.0, 20.0});

        // descriptor
        RobotDescriptor robotDescriptor = new RobotDescriptor(robotId, roomDescriptor.getRoom(), 5.0, "Phillips");
        this.add(new RobotResource("descriptor", robotDescriptor.getRobotId(), robotDescriptor.getRoom(), robotDescriptor.getSoftwareVersion(), robotDescriptor.getManufacturer()));

        // sensors
        this.add(new BatteryLevelSensorResource("battery", robotDescriptor.getRobotId()));
        this.add(new IndoorPositionSensorResource("position", robotDescriptor.getRobotId(), roomDescriptor.getDimensions()));
        this.add(new PresenceInCameraStreamSensorResource("presence", robotDescriptor.getRobotId()));

        // actuators
        this.add(new CameraSwitchActuatorResource("camera", robotDescriptor.getRobotId()));
        this.add(new ModeActuatorResource("mode", robotDescriptor.getRobotId()));
        this.add(new ReturnHomeActuatorResource("home", robotDescriptor.getRobotId()));
    }

    public static void main(String[] args) {

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
