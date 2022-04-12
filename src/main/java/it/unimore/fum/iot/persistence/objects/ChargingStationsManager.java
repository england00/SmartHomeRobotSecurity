package it.unimore.fum.iot.persistence.objects;

import it.unimore.fum.iot.exception.ManagerConflict;
import it.unimore.fum.iot.exception.ManagerException;
import it.unimore.fum.iot.persistence.IManager;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 12/04/2022 - 13:03
 */
public class ChargingStationsManager implements IManager {

    // storing all the data in a file through maps and lists
    private final static String filePath = "./src/main/java/it/unimore/fum/iot/persistence/charging_stations.txt";
    private HashMap<String, String> chargingStationsIpHashMap;

    public ChargingStationsManager() {
        // obtaining the map from file
        this.chargingStationsIpHashMap = new HashMap<>();
        this.chargingStationsIpHashMap = hashMapFromTextFile(); // comment this when a new server is created
    }

    public HashMap<String, String> getChargingStationsIpHashMap() {
        return chargingStationsIpHashMap;
    }

    public void setChargingStationsIpHashMap(HashMap<String, String> chargingStationsIpHashMap) {
        this.chargingStationsIpHashMap = chargingStationsIpHashMap;
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
                String chargingStationsId = identifier[0].trim();
                String chargingStationsIp = identifier[1].trim();
                String chargingStationsPort = identifier[2].trim();

                // put identificator and object in HashMap if they are not empty
                if (!chargingStationsId.equals("") && !chargingStationsIp.equals("") && !chargingStationsPort.equals(""))
                    map.put(chargingStationsId, chargingStationsIp + ":" + chargingStationsPort);
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
            for (HashMap.Entry<String, String> entry : this.chargingStationsIpHashMap.entrySet()) {

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
    // READ THE LIST of all the charging stations
    public List<String> getObjectsList() throws ManagerException {

        return new ArrayList<>(this.chargingStationsIpHashMap.values());
    }

    @Override
    // READ a single charging station
    public String getObject(String id) throws ManagerException {

        return this.chargingStationsIpHashMap.get(id);
    }

    @Override
    // CREATE a new charging station
    public String createNewObject(String id, String ip, String port) throws ManagerException, ManagerConflict {

        if(id != null && this.getObject(id) != null) {

            throw new ManagerConflict("PRESENCE MONITORING OBJECT already available!");

        } else if (ip == null || port == null) {

            throw new ManagerConflict("Null IP ADDRESS or PORT value!");

        } else {

            this.chargingStationsIpHashMap.put(id, ip + ":" + port);
            return id;
        }
    }

    @Override
    // UPDATE a single charging station
    public void updateObject(String id, String ip, String port) throws ManagerException, ManagerConflict {

        if (ip == null || port == null) {

            throw new ManagerConflict("Null IP ADDRESS or PORT value!");

        } else {

            this.chargingStationsIpHashMap.put(id, ip + ":" + port);
        }
    }

    @Override
    // DELETE a single charging station
    public String deleteObject(String id) throws ManagerException {

        return this.chargingStationsIpHashMap.remove(id);
    }
}
