package it.unimore.fum.iot.persistence;

import it.unimore.fum.iot.exception.SmartObjectsInventoryManagerConflict;
import it.unimore.fum.iot.exception.SmartObjectsInventoryManagerException;
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
    List<ChargingStationDescriptor> getChargingStationsList() throws SmartObjectsInventoryManagerException;

    // READ THE LIST of all the charging stations by room
    List<ChargingStationDescriptor> getChargingStationsListByRoom(String room) throws SmartObjectsInventoryManagerException;

    // READ a single charging station by ID
    Optional<ChargingStationDescriptor> getChargingStation(String chargerId) throws SmartObjectsInventoryManagerException;

    // READ a single charging station by ROOM
    Optional<ChargingStationDescriptor> getChargingStationByRoom(String room) throws SmartObjectsInventoryManagerException;

    // CREATE a new charging station
    ChargingStationDescriptor createNewChargingStation(ChargingStationDescriptor chargingStationDescriptor) throws SmartObjectsInventoryManagerException, SmartObjectsInventoryManagerConflict;

    // UPDATE a single charging station
    ChargingStationDescriptor updateChargingStation(ChargingStationDescriptor chargingStationDescriptor) throws SmartObjectsInventoryManagerException, SmartObjectsInventoryManagerConflict;

    // DELETE a single charging station
    ChargingStationDescriptor deleteChargingStation(String chargerId) throws SmartObjectsInventoryManagerException;

    // Presence Monitoring management

    // READ THE LIST of all the presence monitoring objects
    List<PresenceMonitoringObjectDescriptor> getPresenceMonitoringObjectsList() throws SmartObjectsInventoryManagerException;

    // READ THE LIST of all the presence monitoring objects by room
    List<PresenceMonitoringObjectDescriptor> getPresenceMonitoringObjectsListByRoom(String room) throws SmartObjectsInventoryManagerException;

    // READ a single presence monitoring object by ID
    public Optional<PresenceMonitoringObjectDescriptor> getPresenceMonitoringObject(String presenceId) throws SmartObjectsInventoryManagerException;

    // READ a single presence monitoring object by ROOM
    public Optional<PresenceMonitoringObjectDescriptor> getPresenceMonitoringObjectByRoom(String room) throws SmartObjectsInventoryManagerException;

    // CREATE a new presence monitoring object
    public PresenceMonitoringObjectDescriptor createNewPresenceMonitoringObject(PresenceMonitoringObjectDescriptor presenceMonitoringObjectDescriptor) throws SmartObjectsInventoryManagerException, SmartObjectsInventoryManagerConflict;

    // UPDATE a single presence monitoring object
    public PresenceMonitoringObjectDescriptor updateChargingStation(PresenceMonitoringObjectDescriptor presenceMonitoringObjectDescriptor) throws SmartObjectsInventoryManagerException, SmartObjectsInventoryManagerConflict;

    // DELETE a single presence monitoring object
    public PresenceMonitoringObjectDescriptor deletePresenceMonitoringObject(String presenceId) throws SmartObjectsInventoryManagerException;

    // Robot

    // READ THE LIST of all the robots
    public List<RobotDescriptor> getRobotsList() throws SmartObjectsInventoryManagerException;

    // READ THE LIST of all the robots by room
    public List<RobotDescriptor> getRobotsListByRoom(String room) throws SmartObjectsInventoryManagerException;

    // READ a single robot by ID
    public Optional<RobotDescriptor> getRobot(String robotId) throws SmartObjectsInventoryManagerException;

    // READ a single robot by ROOM
    public Optional<RobotDescriptor> getRobotByRoom(String room) throws SmartObjectsInventoryManagerException;

    // CREATE a new presence monitoring object
    public RobotDescriptor createNewRobot(RobotDescriptor robotDescriptor) throws SmartObjectsInventoryManagerException, SmartObjectsInventoryManagerConflict;

    // UPDATE a single presence monitoring object
    public RobotDescriptor updateRobot(RobotDescriptor robotDescriptor) throws SmartObjectsInventoryManagerException, SmartObjectsInventoryManagerConflict;

    // DELETE a single presence monitoring object
    public RobotDescriptor deleteRobot(String robotId) throws SmartObjectsInventoryManagerException;

}
