package it.unimore.fum.iot.test.persistence;

import it.unimore.fum.iot.exception.RoomsManagerConflict;
import it.unimore.fum.iot.exception.RoomsManagerException;
import it.unimore.fum.iot.model.charger.ChargingStationDescriptor;
import it.unimore.fum.iot.model.presence.PresenceMonitoringObjectDescriptor;
import it.unimore.fum.iot.model.robot.RobotDescriptor;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 23/03/2022 - 17:58
 */
public class DefaultSmartObjectsInventoryManager implements ISmartObjectsInventoryManager {

    // Storing all the data in a local cache implemented through maps and lists
    private HashMap<String, ChargingStationDescriptor> chargersMap;
    private HashMap<String, PresenceMonitoringObjectDescriptor> pirMap;
    private HashMap<String, RobotDescriptor> robotMap;

    public DefaultSmartObjectsInventoryManager() {
        this.chargersMap = new HashMap<>();
        this.pirMap = new HashMap<>();
        this.robotMap = new HashMap<>();
    }



    // Charging Station management

    // READ THE LIST of all the charging stations
    @Override
    public List<ChargingStationDescriptor> getChargingStationsList() throws RoomsManagerException {

        return new ArrayList<>(this.chargersMap.values());
    }

    // READ THE LIST of all the charging stations by room
    @Override
    public List<ChargingStationDescriptor> getChargingStationsListByRoom(String room) throws RoomsManagerException {

        return this.chargersMap.values().stream()
                .filter(chargingStationDescriptor -> chargingStationDescriptor != null && chargingStationDescriptor.getRoom().equals(room))
                .collect(Collectors.toList());
    }

    // READ a single charging station by ID
    @Override
    public Optional<ChargingStationDescriptor> getChargingStation(String chargerId) throws RoomsManagerException {

        return this.chargersMap.values().stream()
                .filter(chargingStationDescriptor -> chargingStationDescriptor.getChargerId().equals(chargerId)).findAny();
    }

    // READ a single charging station by ROOM
    @Override
    public Optional<ChargingStationDescriptor> getChargingStationByRoom(String room) throws RoomsManagerException {

        return this.chargersMap.values().stream()
                .filter(chargingStationDescriptor -> chargingStationDescriptor.getRoom().equals(room)).findAny();
    }

    // CREATE a new charging station
    @Override
    public ChargingStationDescriptor createNewChargingStation(ChargingStationDescriptor chargingStationDescriptor) throws RoomsManagerException, RoomsManagerConflict {

        if(chargingStationDescriptor.getChargerId() != null &&
                chargingStationDescriptor.getRoom() != null &&
                this.getChargingStation(chargingStationDescriptor.getChargerId()).isPresent()) {

            throw new RoomsManagerConflict("Charging Station with the same CHARGERID already available!");

        } else if(chargingStationDescriptor.getChargerId() != null &&
                chargingStationDescriptor.getRoom() != null &&
                this.getChargingStationByRoom(chargingStationDescriptor.getRoom()).isPresent()) {

            throw new RoomsManagerConflict("Charging Station in the same ROOM already available!");

        } else if (chargingStationDescriptor.getChargerId() == null ||
                chargingStationDescriptor.getRoom() == null) {

            throw new RoomsManagerConflict("Null CHARGERID or ROOM values!");

        } else {

            this.chargersMap.put(chargingStationDescriptor.getChargerId(), chargingStationDescriptor);
            return chargingStationDescriptor;
        }
    }

    // UPDATE a single charging station
    @Override
    public ChargingStationDescriptor updateChargingStation(ChargingStationDescriptor chargingStationDescriptor) throws RoomsManagerException, RoomsManagerConflict {

        if(chargingStationDescriptor.getChargerId() != null &&
                chargingStationDescriptor.getRoom() != null &&
                this.getChargingStation(chargingStationDescriptor.getChargerId()).isPresent() &&
                this.getChargingStationByRoom(chargingStationDescriptor.getRoom()).isEmpty()) {

            throw new RoomsManagerConflict("Charging Station with the same CHARGERID but in different ROOM already available!");

        } else if(chargingStationDescriptor.getChargerId() != null &&
                chargingStationDescriptor.getRoom() != null &&
                this.getChargingStation(chargingStationDescriptor.getChargerId()).isEmpty() &&
                this.getChargingStationByRoom(chargingStationDescriptor.getRoom()).isPresent()){

            throw new RoomsManagerConflict("Charging Station in the same ROOM but with different CHARGERID already available!");

        } else if (chargingStationDescriptor.getChargerId() == null ||
                chargingStationDescriptor.getRoom() == null) {

            throw new RoomsManagerConflict("Null CHARGERID or ROOM values!");

        } else {

            this.chargersMap.put(chargingStationDescriptor.getChargerId(), chargingStationDescriptor);
            return chargingStationDescriptor;
        }
    }

    // DELETE a single presence monitoring object
    @Override
    public ChargingStationDescriptor deleteChargingStation(String chargerId) throws RoomsManagerException {

        return this.chargersMap.remove(chargerId);
    }

    // Presence Monitoring management

    // READ THE LIST of all the presence monitoring objects
    @Override
    public List<PresenceMonitoringObjectDescriptor> getPresenceMonitoringObjectsList() throws RoomsManagerException {

        return new ArrayList<>(this.pirMap.values());
    }

    // READ THE LIST of all the presence monitoring objects by room
    @Override
    public List<PresenceMonitoringObjectDescriptor> getPresenceMonitoringObjectsListByRoom(String room) throws RoomsManagerException {

        return this.pirMap.values().stream()
                .filter(presenceMonitoringObjectDescriptor -> presenceMonitoringObjectDescriptor != null && presenceMonitoringObjectDescriptor.getRoom().equals(room))
                .collect(Collectors.toList());
    }

    // READ a single presence monitoring object by ID
    @Override
    public Optional<PresenceMonitoringObjectDescriptor> getPresenceMonitoringObject(String presenceId) throws RoomsManagerException {

        return this.pirMap.values().stream()
                .filter(presenceMonitoringObjectDescriptor -> presenceMonitoringObjectDescriptor.getPresenceId().equals(presenceId)).findAny();
    }

    // READ a single presence monitoring object by ROOM
    @Override
    public Optional<PresenceMonitoringObjectDescriptor> getPresenceMonitoringObjectByRoom(String room) throws RoomsManagerException {

        return this.pirMap.values().stream()
                .filter(presenceMonitoringObjectDescriptor -> presenceMonitoringObjectDescriptor.getRoom().equals(room)).findAny();
    }

    // CREATE a new presence monitoring object
    @Override
    public PresenceMonitoringObjectDescriptor createNewPresenceMonitoringObject(PresenceMonitoringObjectDescriptor presenceMonitoringObjectDescriptor) throws RoomsManagerException, RoomsManagerConflict {

        if(presenceMonitoringObjectDescriptor.getPresenceId() != null &&
                presenceMonitoringObjectDescriptor.getRoom() != null &&
                this.getPresenceMonitoringObject(presenceMonitoringObjectDescriptor.getPresenceId()).isPresent()) {

            throw new RoomsManagerConflict("Presence Monitoring Object with the same PRESENCEID already available!");

        } else if(presenceMonitoringObjectDescriptor.getPresenceId() != null &&
                presenceMonitoringObjectDescriptor.getRoom() != null &&
                this.getPresenceMonitoringObjectByRoom(presenceMonitoringObjectDescriptor.getRoom()).isPresent()) {

            throw new RoomsManagerConflict("Presence Monitoring Object in the same ROOM already available!");

        } else if (presenceMonitoringObjectDescriptor.getPresenceId() == null ||
                presenceMonitoringObjectDescriptor.getRoom() == null) {

            throw new RoomsManagerConflict("Null PRESENCEID or ROOM values!");

        } else {

            this.pirMap.put(presenceMonitoringObjectDescriptor.getPresenceId(), presenceMonitoringObjectDescriptor);
            return presenceMonitoringObjectDescriptor;
        }
    }

    // UPDATE a single presence monitoring object
    @Override
    public PresenceMonitoringObjectDescriptor updateChargingStation(PresenceMonitoringObjectDescriptor presenceMonitoringObjectDescriptor) throws RoomsManagerException, RoomsManagerConflict {

        if(presenceMonitoringObjectDescriptor.getPresenceId() != null &&
                presenceMonitoringObjectDescriptor.getRoom() != null &&
                this.getPresenceMonitoringObject(presenceMonitoringObjectDescriptor.getPresenceId()).isPresent() &&
                this.getPresenceMonitoringObjectByRoom(presenceMonitoringObjectDescriptor.getRoom()).isEmpty()) {

            throw new RoomsManagerConflict("Presence Monitoring Object with the same PRESENCEID but in different ROOM already available!");

        } else if(presenceMonitoringObjectDescriptor.getPresenceId() != null &&
                presenceMonitoringObjectDescriptor.getRoom() != null &&
                this.getPresenceMonitoringObject(presenceMonitoringObjectDescriptor.getPresenceId()).isEmpty() &&
                this.getPresenceMonitoringObjectByRoom(presenceMonitoringObjectDescriptor.getRoom()).isPresent()){

            throw new RoomsManagerConflict("Presence Monitoring Object in the same ROOM but with different PRESENCEID already available!");

        } else if (presenceMonitoringObjectDescriptor.getPresenceId() == null ||
                presenceMonitoringObjectDescriptor.getRoom() == null) {

            throw new RoomsManagerConflict("Null CHARGERID or ROOM values!");

        } else {

            this.pirMap.put(presenceMonitoringObjectDescriptor.getPresenceId(), presenceMonitoringObjectDescriptor);
            return presenceMonitoringObjectDescriptor;
        }
    }

    // DELETE a single presence monitoring object
    @Override
    public PresenceMonitoringObjectDescriptor deletePresenceMonitoringObject(String presenceId) throws RoomsManagerException {

        return this.pirMap.remove(presenceId);
    }

    // Robot

    // READ THE LIST of all the robots
    @Override
    public List<RobotDescriptor> getRobotsList() throws RoomsManagerException {

        return new ArrayList<>(this.robotMap.values());
    }

    // READ THE LIST of all the robots by room
    @Override
    public List<RobotDescriptor> getRobotsListByRoom(String room) throws RoomsManagerException {

        return this.robotMap.values().stream()
                .filter(robotDescriptor -> robotDescriptor != null && robotDescriptor.getRoom().equals(room))
                .collect(Collectors.toList());
    }

    // READ a single robot by ID
    @Override
    public Optional<RobotDescriptor> getRobot(String robotId) throws RoomsManagerException {

        return this.robotMap.values().stream()
                .filter(robotDescriptor -> robotDescriptor.getRobotId().equals(robotId)).findAny();
    }

    // READ a single robot by ROOM
    @Override
    public Optional<RobotDescriptor> getRobotByRoom(String room) throws RoomsManagerException {

        return this.robotMap.values().stream()
                .filter(robotDescriptor -> robotDescriptor.getRoom().equals(room)).findAny();
    }

    // CREATE a new robot
    @Override
    public RobotDescriptor createNewRobot(RobotDescriptor robotDescriptor) throws RoomsManagerException, RoomsManagerConflict {

        if(robotDescriptor.getRobotId() != null &&
                robotDescriptor.getRoom() != null &&
                this.getRobot(robotDescriptor.getRobotId()).isPresent()) {

            throw new RoomsManagerConflict("Robot with the same ROBOTID already available!");

        } else if(robotDescriptor.getRobotId() != null &&
                robotDescriptor.getRoom() != null &&
                this.getRobotByRoom(robotDescriptor.getRoom()).isPresent()) {

            throw new RoomsManagerConflict("Robot in the same ROOM already available!");

        } else if (robotDescriptor.getRobotId() == null ||
                robotDescriptor.getRoom() == null) {

            throw new RoomsManagerConflict("Null ROBOTID or ROOM values!");

        } else {

            this.robotMap.put(robotDescriptor.getRobotId(), robotDescriptor);
            return robotDescriptor;
        }
    }

    // UPDATE a single robot
    @Override
    public RobotDescriptor updateRobot(RobotDescriptor robotDescriptor) throws RoomsManagerException, RoomsManagerConflict {

        if(robotDescriptor.getRobotId() != null &&
                robotDescriptor.getRoom() != null &&
                this.getRobot(robotDescriptor.getRobotId()).isPresent() &&
                this.getRobotByRoom(robotDescriptor.getRoom()).isEmpty()) {

            throw new RoomsManagerConflict("Presence Monitoring Object with the same PRESENCEID but in different ROOM already available!");

        } else if(robotDescriptor.getRobotId() != null &&
                robotDescriptor.getRoom() != null &&
                this.getRobot(robotDescriptor.getRobotId()).isEmpty() &&
                this.getRobotByRoom(robotDescriptor.getRoom()).isPresent()){

            throw new RoomsManagerConflict("Presence Monitoring Object in the same ROOM but with different PRESENCEID already available!");

        } else if (robotDescriptor.getRobotId() == null ||
                robotDescriptor.getRoom() == null) {

            throw new RoomsManagerConflict("Null CHARGERID or ROOM values!");

        } else {

            this.robotMap.put(robotDescriptor.getRobotId(), robotDescriptor);
            return robotDescriptor;
        }
    }

    // DELETE a single robot
    @Override
    public RobotDescriptor deleteRobot(String robotId) throws RoomsManagerException {

        return this.robotMap.remove(robotId);
    }
}
