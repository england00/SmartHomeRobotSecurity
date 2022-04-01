package it.unimore.fum.iot.persistence;

import java.io.*;
import java.util.HashMap;

import it.unimore.fum.iot.model.robot.RobotDescriptor;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 01/04/2022 - 02:04
 */
public class ObjectManager {

    // storing all the data in a file through maps and lists
    private RobotDescriptor robotDescriptor;
    private final static String filePath = "./src/main/java/it/unimore/fum/iot/persistence/write.txt";
    private HashMap<String, RobotDescriptor> robotMap;

    public ObjectManager() {
        this.robotMap = HashMapFromTextFile();
    }

    public HashMap<String, RobotDescriptor> HashMapFromTextFile() {

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

                // split the line by '='
                String[] descriptor = line.split("=");
                this.robotDescriptor.setRobotId(descriptor[1].split(",")[0].replace("'","").trim());
                this.robotDescriptor.setRoom(descriptor[2].split(",")[0].replace("'","").trim());
                this.robotDescriptor.setSoftwareVersion(Double.parseDouble(descriptor[3].split(",")[0].trim()));
                this.robotDescriptor.setManufacturer(descriptor[4].split("}")[0].replace("'","").trim());

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

    public void TextFileToHashMap() {
        // new file object
        File file = new File(filePath);
        BufferedWriter bf = null;

        try {

            // create new BufferedWriter for the output file
            bf = new BufferedWriter(new FileWriter(file));

            // iterate map entries
            for (HashMap.Entry<String, RobotDescriptor> entry : robotMap.entrySet()) {

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

}
