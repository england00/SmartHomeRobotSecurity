package it.unimore.fum.iot.test.persistence;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import it.unimore.fum.iot.exception.RoomsManagerConflict;
import it.unimore.fum.iot.exception.RoomsManagerException;
import it.unimore.fum.iot.model.robot.RobotDescriptor;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 01/04/2022 - 02:04
 */
public class RobotManager {

    // storing all the data in a file through maps and lists
    private RobotDescriptor robotDescriptor = new RobotDescriptor();
    private final static String filePath = "./src/main/java/it/unimore/fum/iot/persistence/robotId.txt";
    private HashMap<String, RobotDescriptor> robotMap;

    public RobotManager() {
        // obtaining the map from file
        this.robotMap = hashMapFromTextFile();
    }

    public RobotDescriptor getRobotDescriptor() {
        return robotDescriptor;
    }

    public void setRobotDescriptor(RobotDescriptor robotDescriptor) {
        this.robotDescriptor = robotDescriptor;
    }

    public HashMap<String, RobotDescriptor> getRobotMap() {
        return robotMap;
    }

    public void setRobotMap(HashMap<String, RobotDescriptor> robotMap) {
        this.robotMap = robotMap;
    }

    public HashMap<String, RobotDescriptor> hashMapFromTextFile() {
        var map = new HashMap<String, RobotDescriptor>();
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
                String robotId = identifier[0].trim();


                System.out.println(line);
                // split the line by '='
                String[] descriptor = line.split("=");
                this.robotDescriptor.setRobotId(descriptor[1].split(",")[0].replace("'","").trim());
                this.robotDescriptor.setRoom(descriptor[2].split(",")[0].replace("'","").trim());
                this.robotDescriptor.setSoftwareVersion(Double.parseDouble(descriptor[3].split(",")[0].trim()));
                this.robotDescriptor.setManufacturer(descriptor[4].split("}")[0].replace("'","").trim());
                System.out.println(robotDescriptor);

                // put identificator and object in HashMap if they are not empty
                if (!robotId.equals("") && !this.robotDescriptor.getRobotId().equals("") &&
                        !this.robotDescriptor.getRoom().equals("") &&
                        !this.robotDescriptor.getSoftwareVersion().equals("") &&
                        !this.robotDescriptor.getManufacturer().equals(""))
                    map.put(robotId, robotDescriptor);
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

    public void hashMapToTextFile() {
        // new file object
        File file = new File(filePath);
        BufferedWriter bf = null;

        try {
            // create new BufferedWriter for the output file
            bf = new BufferedWriter(new FileWriter(file));

            // iterate map entries
            for (HashMap.Entry<String, RobotDescriptor> entry : this.robotMap.entrySet()) {

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

    // Robot

    // READ THE LIST of all the robots
    public List<RobotDescriptor> getRobotsList() throws RoomsManagerException {

        return new ArrayList<>(this.robotMap.values());
    }

    // READ THE LIST of all the robots by room
    public List<RobotDescriptor> getRobotsListByRoom(String room) throws RoomsManagerException {

        return this.robotMap.values().stream()
                .filter(robotDescriptor -> robotDescriptor != null && robotDescriptor.getRoom().equals(room))
                .collect(Collectors.toList());
    }

    // READ a single robot by ID
    public Optional<RobotDescriptor> getRobot(String robotId) throws RoomsManagerException {

        return this.robotMap.values().stream()
                .filter(robotDescriptor -> robotDescriptor.getRobotId().equals(robotId)).findAny();
    }

    // READ a single robot by ROOM
    public Optional<RobotDescriptor> getRobotByRoom(String room) throws RoomsManagerException {

        return this.robotMap.values().stream()
                .filter(robotDescriptor -> robotDescriptor.getRoom().equals(room)).findAny();
    }

    // CREATE a new robot
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
    public RobotDescriptor deleteRobot(String robotId) throws RoomsManagerException {

        return this.robotMap.remove(robotId);
    }
}
