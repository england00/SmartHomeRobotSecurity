package it.unimore.fum.iot.test.persistence;

import it.unimore.fum.iot.exception.ManagerConflict;
import it.unimore.fum.iot.exception.ManagerException;
import it.unimore.fum.iot.model.descriptor.ChargingStationDescriptor;
import it.unimore.fum.iot.model.descriptor.PresenceMonitoringObjectDescriptor;
import it.unimore.fum.iot.model.descriptor.RobotDescriptor;
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
    List<ChargingStationDescriptor> getChargingStationsList() throws ManagerException;

    // READ THE LIST of all the charging stations by room
    List<ChargingStationDescriptor> getChargingStationsListByRoom(String room) throws ManagerException;

    // READ a single charging station by ID
    Optional<ChargingStationDescriptor> getChargingStation(String chargerId) throws ManagerException;

    // READ a single charging station by ROOM
    Optional<ChargingStationDescriptor> getChargingStationByRoom(String room) throws ManagerException;

    // CREATE a new charging station
    ChargingStationDescriptor createNewChargingStation(ChargingStationDescriptor chargingStationDescriptor) throws ManagerException, ManagerConflict;

    // UPDATE a single charging station
    ChargingStationDescriptor updateChargingStation(ChargingStationDescriptor chargingStationDescriptor) throws ManagerException, ManagerConflict;

    // DELETE a single charging station
    ChargingStationDescriptor deleteChargingStation(String chargerId) throws ManagerException;

    // Presence Monitoring management

    // READ THE LIST of all the presence monitoring objects
    List<PresenceMonitoringObjectDescriptor> getPresenceMonitoringObjectsList() throws ManagerException;

    // READ THE LIST of all the presence monitoring objects by room
    List<PresenceMonitoringObjectDescriptor> getPresenceMonitoringObjectsListByRoom(String room) throws ManagerException;

    // READ a single presence monitoring object by ID
    public Optional<PresenceMonitoringObjectDescriptor> getPresenceMonitoringObject(String presenceId) throws ManagerException;

    // READ a single presence monitoring object by ROOM
    public Optional<PresenceMonitoringObjectDescriptor> getPresenceMonitoringObjectByRoom(String room) throws ManagerException;

    // CREATE a new presence monitoring object
    public PresenceMonitoringObjectDescriptor createNewPresenceMonitoringObject(PresenceMonitoringObjectDescriptor presenceMonitoringObjectDescriptor) throws ManagerException, ManagerConflict;

    // UPDATE a single presence monitoring object
    public PresenceMonitoringObjectDescriptor updateChargingStation(PresenceMonitoringObjectDescriptor presenceMonitoringObjectDescriptor) throws ManagerException, ManagerConflict;

    // DELETE a single presence monitoring object
    public PresenceMonitoringObjectDescriptor deletePresenceMonitoringObject(String presenceId) throws ManagerException;

    // Robot

    // READ THE LIST of all the robots
    public List<RobotDescriptor> getRobotsList() throws ManagerException;

    // READ THE LIST of all the robots by room
    public List<RobotDescriptor> getRobotsListByRoom(String room) throws ManagerException;

    // READ a single robot by ID
    public Optional<RobotDescriptor> getRobot(String robotId) throws ManagerException;

    // READ a single robot by ROOM
    public Optional<RobotDescriptor> getRobotByRoom(String room) throws ManagerException;

    // CREATE a new presence monitoring object
    public RobotDescriptor createNewRobot(RobotDescriptor robotDescriptor) throws ManagerException, ManagerConflict;

    // UPDATE a single presence monitoring object
    public RobotDescriptor updateRobot(RobotDescriptor robotDescriptor) throws ManagerException, ManagerConflict;

    // DELETE a single presence monitoring object
    public RobotDescriptor deleteRobot(String robotId) throws ManagerException;

}
