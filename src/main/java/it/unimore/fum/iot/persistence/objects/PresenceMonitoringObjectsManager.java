package it.unimore.fum.iot.persistence.objects;

import it.unimore.fum.iot.exception.ManagerConflict;
import it.unimore.fum.iot.exception.ManagerException;
import it.unimore.fum.iot.persistence.IManager;

import java.io.*;
import java.util.*;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 12/04/2022 - 11:54
 */
public class PresenceMonitoringObjectsManager implements IManager {

    // storing all the data in a file through maps and lists
    private final static String filePath = "./src/main/java/it/unimore/fum/iot/persistence/presence_monitoring_objects.txt";
    private HashMap<String, String> presenceMonitoringObjectsIpHashMap;

    public PresenceMonitoringObjectsManager() {
        // obtaining the map from file
        this.presenceMonitoringObjectsIpHashMap = new HashMap<>();
        this.presenceMonitoringObjectsIpHashMap = hashMapFromTextFile(); // comment this when a new server is created
    }

    public HashMap<String, String> getPresenceMonitoringObjectsIpHashMap() {
        return presenceMonitoringObjectsIpHashMap;
    }

    public void setPresenceMonitoringObjectsIpHashMap(HashMap<String, String> presenceMonitoringObjectsIpHashMap) {
        this.presenceMonitoringObjectsIpHashMap = presenceMonitoringObjectsIpHashMap;
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
                String presenceMonitoringObjectsId = identifier[0].trim();
                String presenceMonitoringObjectsIp = identifier[1].trim();
                String presenceMonitoringObjectsPort = identifier[2].trim();

                // put identificator and object in HashMap if they are not empty
                if (!presenceMonitoringObjectsId.equals("") && !presenceMonitoringObjectsIp.equals("") && !presenceMonitoringObjectsPort.equals(""))
                    map.put(presenceMonitoringObjectsId, presenceMonitoringObjectsIp + ":" + presenceMonitoringObjectsPort);
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
            for (HashMap.Entry<String, String> entry : this.presenceMonitoringObjectsIpHashMap.entrySet()) {

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
    // READ THE LIST of all the presence monitoring objects
    public List<String> getObjectsList() throws ManagerException {

        return new ArrayList<>(this.presenceMonitoringObjectsIpHashMap.values());
    }

    @Override
    // READ a single presence monitoring object
    public String getObject(String id) throws ManagerException {

        return this.presenceMonitoringObjectsIpHashMap.get(id);
    }

    @Override
    // CREATE a new presence monitoring object
    public String createNewObject(String id, String ip, String port) throws ManagerException, ManagerConflict {

        if(id != null && this.getObject(id) != null) {

            throw new ManagerConflict("PRESENCE MONITORING OBJECT already available!");

        } else if (ip == null || port == null) {

            throw new ManagerConflict("Null IP ADDRESS or PORT value!");

        } else {

            this.presenceMonitoringObjectsIpHashMap.put(id, ip + ":" + port);
            return id;
        }
    }

    @Override
    // UPDATE a single presence monitoring object
    public void updateObject(String id, String ip, String port) throws ManagerException, ManagerConflict {

        if (ip == null || port == null) {

            throw new ManagerConflict("Null IP ADDRESS or PORT value!");

        } else {

            this.presenceMonitoringObjectsIpHashMap.put(id, ip + ":" + port);
        }
    }

    @Override
    // DELETE a single presence monitoring object
    public String deleteObject(String id) throws ManagerException {

        return this.presenceMonitoringObjectsIpHashMap.remove(id);
    }
}
