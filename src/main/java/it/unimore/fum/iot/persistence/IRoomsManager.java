package it.unimore.fum.iot.persistence;

import it.unimore.fum.iot.exception.ManagerConflict;
import it.unimore.fum.iot.exception.ManagerException;
import it.unimore.fum.iot.model.descriptor.RoomDescriptor;
import java.util.HashMap;
import java.util.List;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 03/04/2022 - 16:52
 */
public interface IRoomsManager {

    // Rooms Manager Interface

    public HashMap<String, RoomDescriptor> hashMapFromTextFile();

    // Save the HashMap into a text file
    public void hashMapToTextFile();

    // READ THE LIST of all the rooms
    public List<RoomDescriptor> getRoomsList() throws ManagerException;

    // READ a single room
    public RoomDescriptor getRoom(String room) throws ManagerException;

    // CREATE a new room
    public RoomDescriptor createNewRoom(RoomDescriptor roomDescriptor) throws ManagerException, ManagerConflict;

    // UPDATE a single room
    public void updateRoom(RoomDescriptor roomDescriptor) throws ManagerException, ManagerConflict;

    // DELETE a single room
    public RoomDescriptor deleteRoom(String room) throws ManagerException;
}
