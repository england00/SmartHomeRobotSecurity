package it.unimore.fum.iot.server;

import it.unimore.fum.iot.model.RoomDescriptor;
import it.unimore.fum.iot.model.robot.RobotDescriptor;
import it.unimore.fum.iot.resource.robot.*;
import org.eclipse.californium.core.CoapServer;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 29/03/2022 - 01:51
 */
public class RobotCoapProcess extends CoapServer {

    public RobotCoapProcess() {

        super();

        // room
        RoomDescriptor roomDescriptor = new RoomDescriptor("home", new double[]{15.0, 20.0});

        // descriptor
        RobotDescriptor robotDescriptor = new RobotDescriptor("robot-0001", roomDescriptor.getRoom(), 5.0, "Phillips");
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

        robotCoapProcess.getRoot().getChildren().forEach(resource -> {
            System.out.println(String.format("Resource %s -> URI: %s (Observable: %b)", resource.getName(), resource.getURI(), resource.isObservable()));
        });
    }
}
