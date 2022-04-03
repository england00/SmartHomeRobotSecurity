package it.unimore.fum.iot.persistence;

import it.unimore.fum.iot.exception.RoomsManagerConflict;
import it.unimore.fum.iot.exception.RoomsManagerException;
import it.unimore.fum.iot.model.home.RoomDescriptor;
import java.io.*;
import java.util.*;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 02/04/2022 - 20:08
 */
public class RoomsManager implements IRoomsManager{

    // storing all the data in a file through maps and lists
    private RoomDescriptor roomDescriptor = new RoomDescriptor(null, null, null);
    private final static String filePath = "./src/main/java/it/unimore/fum/iot/persistence/rooms.txt";
    private HashMap<String, RoomDescriptor> roomHashMap;

    public RoomsManager() {
        // obtaining the map from file
        this.roomHashMap = hashMapFromTextFile();
    }

    public RoomDescriptor getRoomDescriptor() {
        return roomDescriptor;
    }

    public void setRoomDescriptor(RoomDescriptor roomDescriptor) {
        this.roomDescriptor = roomDescriptor;
    }

    public HashMap<String, RoomDescriptor> getRoomHashMap() {
        return roomHashMap;
    }

    public void setRoomHashMap(HashMap<String, RoomDescriptor> roomHashMap) {
        this.roomHashMap = roomHashMap;
    }

    public HashMap<String, RoomDescriptor> hashMapFromTextFile() {
        HashMap<String, RoomDescriptor> map = new HashMap<>();
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
                String room = identifier[0].trim();

                // split the line by '='
                String[] descriptor = line.split("=");
                this.roomDescriptor.setRoom(descriptor[1].split(",")[0].replace("'","").trim());
                String dim1 = descriptor[2].split(",")[0].replace("[","").trim();
                String dim2 = descriptor[2].split(",")[1].split("]")[0].replace(" ","").trim();
                this.roomDescriptor.setDimensions(new double[] {Double.parseDouble(dim1), Double.parseDouble(dim2)});
                dim1 = descriptor[3].split(",")[0].replace("[","").trim();
                dim2 = descriptor[3].split(",")[1].split("]")[0].replace(" ","").trim();
                this.roomDescriptor.setOrigin(new double[] {Double.parseDouble(dim1), Double.parseDouble(dim2)});

                // put identificator and object in HashMap if they are not empty
                if (!room.equals("") && !this.roomDescriptor.getRoom().equals("") &&
                        this.roomDescriptor.getDimensions() != null &&
                        this.roomDescriptor.getOrigin() != null)
                    map.put(room, this.roomDescriptor);
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

    // READ THE LIST of all the rooms
    public List<RoomDescriptor> getRoomsList() throws RoomsManagerException {

        return new ArrayList<>(this.roomHashMap.values());
    }

    // READ a single room
    public RoomDescriptor getRoom(String room) throws RoomsManagerException {

        return this.roomHashMap.get(room);
    }

    // CREATE a new room
    public RoomDescriptor createNewRoom(RoomDescriptor roomDescriptor) throws RoomsManagerException, RoomsManagerConflict {

        if(roomDescriptor.getRoom() != null &&
                this.getRoom(roomDescriptor.getRoom()) != null) {

            throw new RoomsManagerConflict("ROOM already available!");

        } else if (roomDescriptor.getRoom() == null) {

            throw new RoomsManagerConflict("Null ROOM name value!");

        } else {

            this.roomHashMap.put(roomDescriptor.getRoom(), roomDescriptor);
            return roomDescriptor;
        }
    }

    // UPDATE a single room
    public void updateRoom(RoomDescriptor roomDescriptor) throws RoomsManagerException, RoomsManagerConflict {

        if (roomDescriptor.getRoom() == null) {

            throw new RoomsManagerConflict("Null ROOM name value!");

        } else {

            this.roomHashMap.put(roomDescriptor.getRoom(), roomDescriptor);
        }
    }

    // DELETE a single room
    public RoomDescriptor deleteRoom(String room) throws RoomsManagerException {

        return this.roomHashMap.remove(room);
    }
}