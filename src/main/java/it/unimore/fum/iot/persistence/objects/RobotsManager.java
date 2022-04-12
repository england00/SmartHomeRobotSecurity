package it.unimore.fum.iot.persistence.objects;

import it.unimore.fum.iot.exception.ManagerConflict;
import it.unimore.fum.iot.exception.ManagerException;
import it.unimore.fum.iot.persistence.IManager;

import java.io.*;
import java.util.*;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 02/04/2022 - 20:08
*/
public class RobotsManager implements IManager {

    // storing all the data in a file through maps and lists
    private final static String filePath = "./src/main/java/it/unimore/fum/iot/persistence/robots.txt";
    private HashMap<String, String> robotIpHashMap;

    public RobotsManager() {
        // obtaining the map from file
        this.robotIpHashMap = new HashMap<>();
        this.robotIpHashMap = hashMapFromTextFile(); // comment this when a new server is created
    }

    public HashMap<String, String> getRobotIpHashMap() {
        return robotIpHashMap;
    }

    public void setRobotIpHashMap(HashMap<String, String> robotIpHashMap) {
        this.robotIpHashMap = robotIpHashMap;
    }

    @Override
    public HashMap<String, String> hashMapFromTextFile() {
        HashMap<String, String> map = new HashMap<>();
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
                String robotIp = identifier[1].trim();
                String robotPort = identifier[2].trim();

                // put identificator and object in HashMap if they are not empty
                if (!robotId.equals("") && !robotIp.equals("") && !robotPort.equals(""))
                    map.put(robotId, robotIp + ":" + robotPort);
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
            for (HashMap.Entry<String, String> entry : this.robotIpHashMap.entrySet()) {

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

    @Override
    // READ THE LIST of all the robots
    public List<String> getObjectsList() throws ManagerException {

        return new ArrayList<>(this.robotIpHashMap.values());
    }

    @Override
    // READ a single robot
    public String getObject(String id) throws ManagerException {

        return this.robotIpHashMap.get(id);
    }

    @Override
    // CREATE a new robot
    public String createNewObject(String id, String ip, String port) throws ManagerException, ManagerConflict {

        if(id != null && this.getObject(id) != null) {

            throw new ManagerConflict("ROBOT already available!");

        } else if (ip == null || port == null) {

            throw new ManagerConflict("Null IP ADDRESS or PORT value!");

        } else {

            this.robotIpHashMap.put(id, ip + ":" + port);
            return id;
        }
    }

    @Override
    // UPDATE a single robot
    public void updateObject(String id, String ip, String port) throws ManagerException, ManagerConflict {

        if (ip == null || port == null) {

            throw new ManagerConflict("Null IP ADDRESS or PORT value!");

        } else {

            this.robotIpHashMap.put(id, ip + ":" + port);
        }
    }

    @Override
    // DELETE a single robot
    public String deleteObject(String id) throws ManagerException {

        return this.robotIpHashMap.remove(id);
    }
}
