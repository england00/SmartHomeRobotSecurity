package it.unimore.fum.iot.persistence;

import it.unimore.fum.iot.exception.ManagerConflict;
import it.unimore.fum.iot.exception.ManagerException;
import java.util.HashMap;
import java.util.List;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 12/04/2022 - 10:46
 */
public interface IManager {

    // Manager Interface

    public HashMap<String, String> hashMapFromTextFile();

    // Save the HashMap into a text file
    public void hashMapToTextFile();

    // READ THE LIST of all the objects
    public List<String> getObjectsList() throws ManagerException;

    // READ a single object
    public String getObject(String id) throws ManagerException;

    // CREATE a new object
    public String createNewObject(String id, String ip, String port) throws ManagerException, ManagerConflict;

    // UPDATE a single object
    public void updateObject(String id, String ip, String port) throws ManagerException, ManagerConflict;

    // DELETE a single object
    public String deleteObject(String id) throws ManagerException;
}
