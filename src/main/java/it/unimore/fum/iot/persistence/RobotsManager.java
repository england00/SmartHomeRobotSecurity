/*
package it.unimore.fum.iot.persistence;

import com.google.gson.Gson;
import it.unimore.fum.iot.exception.ManagerConflict;
import it.unimore.fum.iot.exception.ManagerException;
import it.unimore.fum.iot.model.descriptor.RobotDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.util.*;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 02/04/2022 - 20:08

public class RobotsManager{

    // storing all the data in a file through maps and lists
    private final static String filePath = "./src/main/java/it/unimore/fum/iot/persistence/robots.txt";
    private HashMap<String, RobotDescriptor> robotHashMap;
    private final static Logger logger = LoggerFactory.getLogger(RoomsManager.class);
    private final Gson gson;

    public RobotsManager() {
        // obtaining the map from file
        this.gson = new Gson();
        this.robotHashMap = hashMapFromTextFile();
    }

    public HashMap<String, RobotDescriptor> getRobotHashMap() {
        return robotHashMap;
    }

    public void setRobotHashMap(HashMap<String, RobotDescriptor> robotHashMap) {
        this.robotHashMap = robotHashMap;
    }

    @Override
    public HashMap<String, RobotDescriptor> hashMapFromTextFile() {
        HashMap<String, RobotDescriptor> map = new HashMap<>();
        BufferedReader br = null;

        try {
            // create file object
            File file = new File(filePath);

            // create BufferedReader object from the File
            br = new BufferedReader(new FileReader(file));

            String line = null;

            // read file line by line
            while ((line = br.readLine()) != null) {

                // split the line by ':'
                String[] identifier = line.split(":");
                String robot = identifier[0].trim();
                RobotDescriptor robotDescriptor = this.gson.fromJson(identifier[1].replace("RobotDescriptor", "").trim(), RobotDescriptor.class);

                // put identificator and object in HashMap if they are not empty
                if (!robot.equals("") && !robotDescriptor.getRoom().equals("") &&
                        roomDescriptor.getDimensions() != null &&
                        roomDescriptor.getOrigin() != null)
                    map.put(room, roomDescriptor);
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            // closing the BufferedReader
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {}
            }
        }

        return map;
    }

    @Override
    public void hashMapToTextFile() {
        // new file object
        File file = new File(filePath);
        BufferedWriter bf = null;

        try {
            // create new BufferedWriter for the output file
            bf = new BufferedWriter(new FileWriter(file));

            // iterate map entries
            for (HashMap.Entry<String, RoomDescriptor> entry : this.roomHashMap.entrySet()) {

                // put key and value separated by a colon
                bf.write(entry.getKey() + ":" + entry.getValue());

                // new line
                bf.newLine();
            }

            bf.flush();

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {
                // always close the writer
                bf.close();

            } catch (Exception e) {}
        }
    }

    // READ THE LIST of all the robots
    @Override
    public List<RoomDescriptor> getRoomsList() throws ManagerException {

        return new ArrayList<>(this.roomHashMap.values());
    }

    // READ a single room
    @Override
    public RoomDescriptor getRoom(String room) throws ManagerException {

        return this.roomHashMap.get(room);
    }

    // CREATE a new robot
    @Override
    public RoomDescriptor createNewRoom(RoomDescriptor roomDescriptor) throws ManagerException, ManagerConflict {

        if(roomDescriptor.getRoom() != null &&
                this.getRoom(roomDescriptor.getRoom()) != null) {

            throw new ManagerConflict("ROOM already available!");

        } else if (roomDescriptor.getRoom() == null) {

            throw new ManagerConflict("Null ROOM name value!");

        } else {

            this.roomHashMap.put(roomDescriptor.getRoom(), roomDescriptor);
            return roomDescriptor;
        }
    }

    // UPDATE a single robot
    @Override
    public void updateRoom(RoomDescriptor roomDescriptor) throws ManagerException, ManagerConflict {

        if (roomDescriptor.getRoom() == null) {

            throw new ManagerConflict("Null ROOM name value!");

        } else {

            this.roomHashMap.put(roomDescriptor.getRoom(), roomDescriptor);
        }
    }

    // DELETE a single robot
    @Override
    public RobotDescriptor deleteRobot(String robot) throws ManagerException {

        return this.robotHashMap.remove(robot);
    }
}
*/
