package it.unimore.fum.iot.test.persistence;

import it.unimore.fum.iot.exception.RoomsManagerConflict;
import it.unimore.fum.iot.exception.RoomsManagerException;
import it.unimore.fum.iot.model.charger.ChargingStationDescriptor;
import it.unimore.fum.iot.model.presence.PresenceMonitoringObjectDescriptor;
import it.unimore.fum.iot.model.robot.RobotDescriptor;
import java.util.List;
import java.util.Optional;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 15/03/2022 - 12:34
 */
public interface ISmartObjectsInventoryManager {

    // Charging Station management

    // READ THE LIST of all the charging stations
    List<ChargingStationDescriptor> getChargingStationsList() throws RoomsManagerException;

    // READ THE LIST of all the charging stations by room
    List<ChargingStationDescriptor> getChargingStationsListByRoom(String room) throws RoomsManagerException;

    // READ a single charging station by ID
    Optional<ChargingStationDescriptor> getChargingStation(String chargerId) throws RoomsManagerException;

    // READ a single charging station by ROOM
    Optional<ChargingStationDescriptor> getChargingStationByRoom(String room) throws RoomsManagerException;

    // CREATE a new charging station
    ChargingStationDescriptor createNewChargingStation(ChargingStationDescriptor chargingStationDescriptor) throws RoomsManagerException, RoomsManagerConflict;

    // UPDATE a single charging station
    ChargingStationDescriptor updateChargingStation(ChargingStationDescriptor chargingStationDescriptor) throws RoomsManagerException, RoomsManagerConflict;

    // DELETE a single charging station
    ChargingStationDescriptor deleteChargingStation(String chargerId) throws RoomsManagerException;

    // Presence Monitoring management

    // READ THE LIST of all the presence monitoring objects
    List<PresenceMonitoringObjectDescriptor> getPresenceMonitoringObjectsList() throws RoomsManagerException;

    // READ THE LIST of all the presence monitoring objects by room
    List<PresenceMonitoringObjectDescriptor> getPresenceMonitoringObjectsListByRoom(String room) throws RoomsManagerException;

    // READ a single presence monitoring object by ID
    public Optional<PresenceMonitoringObjectDescriptor> getPresenceMonitoringObject(String presenceId) throws RoomsManagerException;

    // READ a single presence monitoring object by ROOM
    public Optional<PresenceMonitoringObjectDescriptor> getPresenceMonitoringObjectByRoom(String room) throws RoomsManagerException;

    // CREATE a new presence monitoring object
    public PresenceMonitoringObjectDescriptor createNewPresenceMonitoringObject(PresenceMonitoringObjectDescriptor presenceMonitoringObjectDescriptor) throws RoomsManagerException, RoomsManagerConflict;

    // UPDATE a single presence monitoring object
    public PresenceMonitoringObjectDescriptor updateChargingStation(PresenceMonitoringObjectDescriptor presenceMonitoringObjectDescriptor) throws RoomsManagerException, RoomsManagerConflict;

    // DELETE a single presence monitoring object
    public PresenceMonitoringObjectDescriptor deletePresenceMonitoringObject(String presenceId) throws RoomsManagerException;

    // Robot

    // READ THE LIST of all the robots
    public List<RobotDescriptor> getRobotsList() throws RoomsManagerException;

    // READ THE LIST of all the robots by room
    public List<RobotDescriptor> getRobotsListByRoom(String room) throws RoomsManagerException;

    // READ a single robot by ID
    public Optional<RobotDescriptor> getRobot(String robotId) throws RoomsManagerException;

    // READ a single robot by ROOM
    public Optional<RobotDescriptor> getRobotByRoom(String room) throws RoomsManagerException;

    // CREATE a new presence monitoring object
    public RobotDescriptor createNewRobot(RobotDescriptor robotDescriptor) throws RoomsManagerException, RoomsManagerConflict;

    // UPDATE a single presence monitoring object
    public RobotDescriptor updateRobot(RobotDescriptor robotDescriptor) throws RoomsManagerException, RoomsManagerConflict;

    // DELETE a single presence monitoring object
    public RobotDescriptor deleteRobot(String robotId) throws RoomsManagerException;

}
