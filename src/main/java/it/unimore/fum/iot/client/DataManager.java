package it.unimore.fum.iot.client;

import com.google.gson.Gson;
import it.unimore.fum.iot.exception.ManagerException;
import it.unimore.fum.iot.model.descriptor.*;
import it.unimore.fum.iot.model.raw.BatteryLevelRawSensor;
import it.unimore.fum.iot.model.raw.IndoorPositionRawSensor;
import it.unimore.fum.iot.model.raw.PresenceRawSensor;
import it.unimore.fum.iot.persistence.IManager;
import it.unimore.fum.iot.persistence.objects.ChargingStationsManager;
import it.unimore.fum.iot.persistence.objects.PresenceMonitoringObjectsManager;
import it.unimore.fum.iot.persistence.objects.RobotsManager;
import it.unimore.fum.iot.request.MakeCameraSwitchRequest;
import it.unimore.fum.iot.request.MakeModeRequest;
import it.unimore.fum.iot.request.MakeReturnHomeRequest;
import it.unimore.fum.iot.utils.SenMLPack;
import it.unimore.fum.iot.utils.SenMLRecord;
import org.eclipse.californium.core.*;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.OptionSet;
import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.elements.exception.ConnectorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.util.*;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 09/04/2022 - 01:04
 */
public class DataManager {

    // client's utilities
    private final static Logger logger = LoggerFactory.getLogger(DataManager.class);

    // functional variables
    private static AlarmStatusDescriptor alarmStatusDescriptor;

    public static void main(String[] args) throws InterruptedException, ManagerException {

        // initializing and saving alarm status
        alarmStatusDescriptor = new AlarmStatusDescriptor(1, 1);
        writeAlarmStateToFile();

        IManager robotsManager = new RobotsManager();
        int nRobots = robotsManager.getObjectsList().size();
        final List<String> robotAddress = robotsManager.getObjectsList();

        IManager presenceMonitoringObjectsManager = new PresenceMonitoringObjectsManager();
        int nPresenceMonitoringObjects = presenceMonitoringObjectsManager.getObjectsList().size();
        final List<String> presenceMonitoringObjectAddress = presenceMonitoringObjectsManager.getObjectsList();

        IManager chargingStationsManager = new ChargingStationsManager();
        int nChargingStations = chargingStationsManager.getObjectsList().size();
        final List<String> chargingStationsAddress = chargingStationsManager.getObjectsList();

        if (nRobots != nPresenceMonitoringObjects || nRobots != nChargingStations) {
            logger.warn("ERROR. The number of saved ROBOT IPs doesn't match the number of saved PRESENCE MONITORING OBJECT IPs or CHARGING STATION IPs.");
        }

        ArrayList<Thread> threads = new ArrayList();

        do {
            if (alarmStatusDescriptor.getActiveAlarm() == 1) {
                for (int i = 0; i < nRobots; i++) { // un'iterazione per ogni robot che hai

                    int finalI = i;
                    Thread t = new Thread(() -> alarmOn(robotAddress.get(finalI), presenceMonitoringObjectAddress.get(finalI), chargingStationsAddress.get(finalI)));// ho passato al costruttore di thread una lambda function
                    threads.add(t);
                    t.start();
                }

                for (Thread t : threads) {
                    t.join();
                }
            } else if (alarmStatusDescriptor.getActiveAlarm() == 0) {
                for (int i = 0; i < nRobots; i++) { // un'iterazione per ogni robot che hai

                    int finalI = i;
                    Thread t = new Thread(() -> alarmOff(robotAddress.get(finalI), presenceMonitoringObjectAddress.get(finalI), chargingStationsAddress.get(finalI)));// ho passato al costruttore di thread una lambda function
                    threads.add(t);
                    t.start();
                }

                for (Thread t : threads) {
                    t.join();
                }
            }

            else
                System.exit(0);
        } while (true);
    }

    private static void alarmOn(String robotIp, String presenceIp, String chargerIp) {

        AlarmDataDescriptor alarmDataDescriptor = new AlarmDataDescriptor();
        AddressDescriptor addressDescriptor = new AddressDescriptor(robotIp, presenceIp, chargerIp);
        Gson gson = new Gson();
        boolean exit = false;

        // map which contains the active observing resources
        Map<String, CoapObserveRelation> observingRelationMap;

        // initialize coapClient
        CoapClient coapClient = new CoapClient();

        logger.info("ACTIVATE ALARM");

        do {
            observingRelationMap = new HashMap<>();

            // FIRST STAGE
            // presence
            getClientProcess(coapClient, addressDescriptor.getPresence_monitoring_descriptor(), true, alarmDataDescriptor);
            // charger
            getClientProcess(coapClient, addressDescriptor.getCharging_station_descriptor(), true, alarmDataDescriptor);
            // robot
            getClientProcess(coapClient, addressDescriptor.getRobot_descriptor(), true, alarmDataDescriptor);

            if (!alarmDataDescriptor.getRobotRoom().equals(alarmDataDescriptor.getChargerRoom()) || !alarmDataDescriptor.getRobotRoom().equals(alarmDataDescriptor.getPresenceRoom())) {
                logger.error("ERROR. PRESENCE MONITORING OBJECT or CHARGING STATION in different room then ROBOT");
                System.exit(0);
            }

            putClientProcess(coapClient, addressDescriptor.getRobot_mode_actuator(), gson.toJson(new MakeModeRequest(MakeModeRequest.MODE_START)));
            putClientProcess(coapClient, addressDescriptor.getRobot_camera_switch_actuator(), gson.toJson(new MakeCameraSwitchRequest(MakeCameraSwitchRequest.SWITCH_ON_CAMERA)));
            observingClientProcess(coapClient, addressDescriptor.getRobot_indoor_position_sensor(), true,  observingRelationMap, alarmDataDescriptor);
            observingClientProcess(coapClient, addressDescriptor.getRobot_battery_level_sensor(), true, observingRelationMap, alarmDataDescriptor);
            observingClientProcess(coapClient, addressDescriptor.getRobot_presence_sensor(), true, observingRelationMap, alarmDataDescriptor);

            boolean control = true;
            // cycle with sleep and then cancel registrations
            do {
                // activating alarm
                if (alarmDataDescriptor.getPresence().equals(true)) {
                    logger.warn("ALARM");
                }

                readAlarmStateFromFile();
                if (alarmStatusDescriptor.getSoundAlarm() == 0) {
                    alarmDataDescriptor.setPresence(false);
                }

                // checking the alarm state
                if (alarmStatusDescriptor.getActiveAlarm() != 1) {
                    exit = true;
                    break;
                }

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // SECOND STAGE
                if (alarmDataDescriptor.getBattery().doubleValue() < 10 && control) {
                    putClientProcess(coapClient, addressDescriptor.getRobot_return_home_actuator(), gson.toJson(new MakeReturnHomeRequest(MakeReturnHomeRequest.SWITCH_ON_RETURN_HOME, alarmDataDescriptor.getChargerPosition())));
                    control = false;
                }

            } while (!Arrays.equals(alarmDataDescriptor.getCurrentRobotPosition(), alarmDataDescriptor.getChargerPosition()));

            observingRelationMap.forEach((key, value) -> {
                logger.info("Canceling Observation for target Url: {}", key);
                value.proactiveCancel();
            });

            observingRelationMap = new HashMap<>();

            getClientProcess(coapClient, addressDescriptor.getCharging_robot_presence_sensor(), true, alarmDataDescriptor);
            if (alarmDataDescriptor.getChargerRobotPresence()) {
                putClientProcess(coapClient, addressDescriptor.getRobot_mode_actuator(), gson.toJson(new MakeModeRequest(MakeModeRequest.MODE_PAUSE)));
                putClientProcess(coapClient, addressDescriptor.getRobot_camera_switch_actuator(), gson.toJson(new MakeCameraSwitchRequest(MakeCameraSwitchRequest.SWITCH_OFF_CAMERA)));
                putClientProcess(coapClient, addressDescriptor.getRobot_return_home_actuator(), gson.toJson(new MakeReturnHomeRequest(MakeReturnHomeRequest.SWITCH_OFF_RETURN_HOME, null)));
            }

            // THIRD STAGE
            observingClientProcess(coapClient, addressDescriptor.getPresence_monitoring_pir_sensor(), true, observingRelationMap, alarmDataDescriptor);
            observingClientProcess(coapClient, addressDescriptor.getCharging_robot_battery_level_sensor(), true, observingRelationMap, alarmDataDescriptor);
            observingClientProcess(coapClient, addressDescriptor.getCharging_energy_consumption_sensor(), true, observingRelationMap, alarmDataDescriptor);

            // cycle with sleep and then cancel registrations
            do {
                // activating alarm
                if (alarmDataDescriptor.getPresence().equals(true)) {
                    logger.warn("ALARM");
                }

                readAlarmStateFromFile();
                if (alarmStatusDescriptor.getSoundAlarm() == 0) {
                    alarmDataDescriptor.setPresence(false);
                }

                // checking the alarm state
                if (alarmStatusDescriptor.getActiveAlarm() != 1 || exit) {
                    exit = true;
                    break;
                }

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            } while (alarmDataDescriptor.getBattery().doubleValue() < 100);

            observingRelationMap.forEach((key, value) -> {
                logger.info("Canceling Observation for target Url: {}", key);
                value.proactiveCancel();
            });

        } while (!exit);
    }

    private static void alarmOff(String robotIp, String presenceIp, String chargerIp) {

        AlarmDataDescriptor alarmDataDescriptor = new AlarmDataDescriptor();
        AddressDescriptor addressDescriptor = new AddressDescriptor(robotIp, presenceIp, chargerIp);
        Gson gson = new Gson();
        boolean exit = false;

        // map which contains the active observing resources
        Map<String, CoapObserveRelation> observingRelationMap;

        // initialize coapClient
        CoapClient coapClient = new CoapClient();

        logger.info("DEACTIVATE ALARM");

        observingRelationMap = new HashMap<>();

        getClientProcess(coapClient, addressDescriptor.getCharging_station_descriptor(), true, alarmDataDescriptor);

        // presence
        getClientProcess(coapClient, addressDescriptor.getPresence_monitoring_descriptor(), true, alarmDataDescriptor);
        // charger
        getClientProcess(coapClient, addressDescriptor.getCharging_station_descriptor(), true, alarmDataDescriptor);
        // robot
        getClientProcess(coapClient, addressDescriptor.getRobot_descriptor(), true, alarmDataDescriptor);

        if (!alarmDataDescriptor.getRobotRoom().equals(alarmDataDescriptor.getChargerRoom()) || !alarmDataDescriptor.getRobotRoom().equals(alarmDataDescriptor.getPresenceRoom())) {
            logger.error("ERROR. PRESENCE MONITORING OBJECT or CHARGING STATION in different room then ROBOT");
            System.exit(0);
        }

        putClientProcess(coapClient, addressDescriptor.getRobot_camera_switch_actuator(), gson.toJson(new MakeCameraSwitchRequest(MakeCameraSwitchRequest.SWITCH_OFF_CAMERA)));
        putClientProcess(coapClient, addressDescriptor.getRobot_return_home_actuator(), gson.toJson(new MakeReturnHomeRequest(MakeReturnHomeRequest.SWITCH_ON_RETURN_HOME, alarmDataDescriptor.getChargerPosition())));
        observingClientProcess(coapClient, addressDescriptor.getRobot_indoor_position_sensor(), true,  observingRelationMap, alarmDataDescriptor);

        // cycle with sleep and then cancel registrations
        do {
            // checking the alarm state
            readAlarmStateFromFile();
            if (alarmStatusDescriptor.getActiveAlarm() != 0) {
                exit = true;
                break;
            }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } while (!Arrays.equals(alarmDataDescriptor.getCurrentRobotPosition(), alarmDataDescriptor.getChargerPosition()));

        observingRelationMap.forEach((key, value) -> {
            logger.info("Canceling Observation for target Url: {}", key);
            value.proactiveCancel();
        });

        putClientProcess(coapClient, addressDescriptor.getRobot_mode_actuator(), gson.toJson(new MakeModeRequest(MakeModeRequest.MODE_STOP)));
        putClientProcess(coapClient, addressDescriptor.getRobot_return_home_actuator(), gson.toJson(new MakeReturnHomeRequest(MakeReturnHomeRequest.SWITCH_OFF_RETURN_HOME, alarmDataDescriptor.getChargerPosition())));

        do {
            readAlarmStateFromFile();
            if (alarmStatusDescriptor.getActiveAlarm() != 0 || exit) {
                exit = true;
                break;
            }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (!exit);
    }

    private static void observingClientProcess(CoapClient coapClient, String targetUrl, boolean useSenml, Map<String, CoapObserveRelation> observingRelationMap, AlarmDataDescriptor alarmDataDescriptor) {

        Gson gson = new Gson();
        logger.info("OBSERVING ... {}", targetUrl);
        Request request = Request.newGet();

        // checking the availability of SenML
        if (useSenml) {
            request.setOptions(new OptionSet().setAccept(MediaTypeRegistry.APPLICATION_SENML_JSON));
        } else {
            request.setOptions(new OptionSet().setAccept(MediaTypeRegistry.TEXT_PLAIN));
        }

        // set observe, URI and confirmable request
        request.setObserve();
        request.setURI(targetUrl);
        request.setConfirmable(true);

        CoapObserveRelation relation = coapClient.observe(request, new CoapHandler() {

            @Override
            public void onLoad(CoapResponse response) {
                String content = response.getResponseText();

                // battery
                if (Objects.equals(targetUrl, "coap://127.0.0.1:5683/battery") || Objects.equals(targetUrl, "coap://127.0.0.1:5685/recharging_battery")) {
                    if (useSenml) {
                        SenMLRecord senMLRecord = gson.fromJson(content.replace("]", "").replace("[", "").trim(), SenMLRecord.class);
                        alarmDataDescriptor.setBattery(senMLRecord.getV());
                    } else {
                        BatteryLevelRawSensor batteryLevelRawSensor = gson.fromJson(content.replace("BatteryLevelRawSensor", "").trim(), BatteryLevelRawSensor.class);
                        alarmDataDescriptor.setBattery(batteryLevelRawSensor.getBatteryLevel());
                    }
                    if ((alarmDataDescriptor.getBattery().doubleValue() > 10) &&
                            Objects.equals(targetUrl, "coap://127.0.0.1:5683/battery")) {
                        //logger.info("Notification Response Pretty Print: \n{}", Utils.prettyPrint(response));
                        logger.info("NOTIFICATION Body: " + content);
                    } else if ((alarmDataDescriptor.getBattery().doubleValue() < 100) &&
                            Objects.equals(targetUrl, "coap://127.0.0.1:5685/recharging_battery")) {
                        //logger.info("Notification Response Pretty Print: \n{}", Utils.prettyPrint(response));
                        logger.info("NOTIFICATION Body: " + content);
                    }
                }

                // presence
                else if (Objects.equals(targetUrl, "coap://127.0.0.1:5683/presence") || Objects.equals(targetUrl, "coap://127.0.0.1:5684/pir")) {
                    if (useSenml && alarmDataDescriptor.getPresence().equals(false)) {
                        SenMLRecord senMLRecord = gson.fromJson(content.replace("]", "").replace("[", "").trim(), SenMLRecord.class);
                        alarmDataDescriptor.setPresence(senMLRecord.getVb());
                        if(alarmDataDescriptor.getPresence().equals(true))
                            logger.warn("PRESENCE DETECTED!");
                    } else if (alarmDataDescriptor.getPresence().equals(false)){
                        PresenceRawSensor presenceRawSensor = gson.fromJson(content.replace("PresenceRawSensor", "").trim(), PresenceRawSensor.class);
                        alarmDataDescriptor.setPresence(presenceRawSensor.getValue());
                        if(alarmDataDescriptor.getPresence().equals(true))
                            logger.warn("PRESENCE DETECTED!");
                    }
                    if ((!Arrays.equals(alarmDataDescriptor.getCurrentRobotPosition(), alarmDataDescriptor.getChargerPosition())) &&
                            Objects.equals(targetUrl, "coap://127.0.0.1:5683/presence")){
                        //logger.info("Notification Response Pretty Print: \n{}", Utils.prettyPrint(response));
                        logger.info("NOTIFICATION Body: " + content);
                    } else if ((alarmDataDescriptor.getBattery().doubleValue() < 100) &&
                            Objects.equals(targetUrl, "coap://127.0.0.1:5684/pir")){
                        //logger.info("Notification Response Pretty Print: \n{}", Utils.prettyPrint(response));
                        logger.info("NOTIFICATION Body: " + content);
                    }
                }

                // position
                else if (Objects.equals(targetUrl, "coap://127.0.0.1:5683/position")) {
                    if (useSenml) {
                        SenMLPack senMLPack = gson.fromJson(content.trim(), SenMLPack.class);
                        alarmDataDescriptor.setCurrentRobotPosition(new double[]{senMLPack.get(1).getV().doubleValue(), senMLPack.get(2).getV().doubleValue()});
                    } else {
                        IndoorPositionRawSensor indoorPositionRawSensor = gson.fromJson(content.replace("IndoorPositionRawSensor", "").trim(), IndoorPositionRawSensor.class);
                        alarmDataDescriptor.setCurrentRobotPosition((new double[]{indoorPositionRawSensor.getPosition()[0], indoorPositionRawSensor.getPosition()[1]}));
                    }
                    if (!Arrays.equals(alarmDataDescriptor.getCurrentRobotPosition(), alarmDataDescriptor.getChargerPosition())) {
                        //logger.info("Notification Response Pretty Print: \n{}", Utils.prettyPrint(response));
                        logger.info("NOTIFICATION Body: " + content);
                    }
                }

                else {
                    //logger.info("Notification Response Pretty Print: \n{}", Utils.prettyPrint(response));
                    logger.info("NOTIFICATION Body: " + content);
                }
            }

            @Override
            public void onError() {
                logger.error("OBSERVING {} FAILED", targetUrl);
            }
        });

        observingRelationMap.put(targetUrl, relation);
    }

    private static void getClientProcess(CoapClient coapClient, String targetUrl, boolean useSenml, AlarmDataDescriptor alarmDataDescriptor) {

        Gson gson = new Gson();

        // request class is a generic CoAP message: in this case we want a GET.
        // "Message ID", "Token" and other header's fields can be set
        Request request = new Request(CoAP.Code.GET);

        // checking the availability of SenML
        if (useSenml) {
            request.setOptions(new OptionSet().setAccept(MediaTypeRegistry.APPLICATION_SENML_JSON));
        } else {
            request.setOptions(new OptionSet().setAccept(MediaTypeRegistry.TEXT_PLAIN));
        }

        // set URI and confirmable request
        request.setURI(targetUrl);
        request.setConfirmable(true);

        logger.info("Request Pretty Print: \n{}", Utils.prettyPrint(request));

        // synchronously send the GET message (blocking call)
        CoapResponse coapResp = null;

        try {

            coapResp = coapClient.advanced(request);
            String content = coapResp.getResponseText();

            // robot descriptor
            if (Objects.equals(targetUrl, "coap://127.0.0.1:5683/descriptor")) {
                if (useSenml) {
                    SenMLPack senMLPack = gson.fromJson(content.trim(), SenMLPack.class);
                    alarmDataDescriptor.setRobotRoom(senMLPack.get(1).getVs());
                } else {
                    RobotDescriptor robotDescriptor = gson.fromJson(content.replace("RobotDescriptor", "").trim(), RobotDescriptor.class);
                    alarmDataDescriptor.setRobotRoom(robotDescriptor.getRoom());
                }
            }

            // presence descriptor
            else if (Objects.equals(targetUrl, "coap://127.0.0.1:5684/descriptor")) {
                if (useSenml) {
                    SenMLPack senMLPack = gson.fromJson(content.trim(), SenMLPack.class);
                    alarmDataDescriptor.setPresenceRoom(senMLPack.get(1).getVs());
                } else {
                    PresenceMonitoringObjectDescriptor presenceMonitoringObjectDescriptor = gson.fromJson(content.replace("PresenceMonitoringObjectDescriptor", "").trim(), PresenceMonitoringObjectDescriptor.class);
                    alarmDataDescriptor.setPresenceRoom(presenceMonitoringObjectDescriptor.getRoom());
                }
            }

            // charger descriptor
            else if (Objects.equals(targetUrl, "coap://127.0.0.1:5685/descriptor")) {
                if (useSenml) {
                    SenMLPack senMLPack = gson.fromJson(content.trim(), SenMLPack.class);
                    alarmDataDescriptor.setChargerRoom(senMLPack.get(1).getVs());
                    alarmDataDescriptor.setChargerPosition(new double[]{senMLPack.get(4).getV().doubleValue(), senMLPack.get(5).getV().doubleValue()});
                } else {
                    ChargingStationDescriptor chargingStationDescriptor = gson.fromJson(content.replace("ChargingStationDescriptor", "").trim(), ChargingStationDescriptor.class);
                    alarmDataDescriptor.setChargerRoom(chargingStationDescriptor.getRoom());
                    alarmDataDescriptor.setChargerPosition(chargingStationDescriptor.getPosition());
                }
            }

            // charger robot presence sensor
            else if (Objects.equals(targetUrl, "coap://127.0.0.1:5685/robot_presence")) {
                if (useSenml) {
                    SenMLRecord senMLRecord = gson.fromJson(content.replace("]", "").replace("[", "").trim(), SenMLRecord.class);
                    alarmDataDescriptor.setChargerRobotPresence(senMLRecord.getVb());
                } else {
                    PresenceRawSensor presenceRawSensor = gson.fromJson(content.replace("PresenceRawSensor", "").trim(), PresenceRawSensor.class);
                    alarmDataDescriptor.setChargerRobotPresence(presenceRawSensor.getValue());
                }
            }

            // pretty print for the received response
            //logger.info("Response Pretty Print: \n{}", Utils.prettyPrint(coapResp));
            logger.info("NOTIFICATION Body: " + content);

        } catch (ConnectorException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void putClientProcess(CoapClient coapClient, String targetUrl, String requestPayload) {

        // request class is a generic CoAP message: in this case we want a PUT.
        // "Message ID", "Token" and other header's fields can be set
        Request request = new Request(CoAP.Code.PUT);

        // set PUT request's payload
        request.setPayload(requestPayload);

        // set URI and confirmable request
        request.setURI(targetUrl);
        request.setConfirmable(true);

        logger.info("Request Pretty Print: \n{}", Utils.prettyPrint(request));

        // synchronously send the PUT message (blocking call)
        CoapResponse coapResp = null;

        try {

            coapResp = coapClient.advanced(request);

            // pretty print for the received response
            logger.info("Response Pretty Print: \n{}", Utils.prettyPrint(coapResp));

        } catch (ConnectorException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeAlarmStateToFile() {
        // new file object
        File file = new File("./src/main/java/it/unimore/fum/iot/client/alarm.txt");
        BufferedWriter bw = null;

        try {
            // create new BufferedWriter for the output file
            bw = new BufferedWriter(new FileWriter(file));
            bw.write(String.valueOf(alarmStatusDescriptor));
            bw.newLine();
            bw.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void readAlarmStateFromFile() {
        Gson gson = new Gson();

        // new file object
        File file = new File("./src/main/java/it/unimore/fum/iot/client/alarm.txt");
        BufferedReader br = null;

        try {
            // create BufferedReader object from the File
            br = new BufferedReader(new FileReader(file));
            String line = br.readLine();
            alarmStatusDescriptor = gson.fromJson(line.replace("AlarmDescriptor", "").trim(), AlarmStatusDescriptor.class);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
