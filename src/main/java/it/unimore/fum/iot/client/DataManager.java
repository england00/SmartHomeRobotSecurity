package it.unimore.fum.iot.client;

import com.google.gson.Gson;
import it.unimore.fum.iot.model.descriptor.*;
import it.unimore.fum.iot.model.raw.BatteryLevelRawSensor;
import it.unimore.fum.iot.model.raw.IndoorPositionRawSensor;
import it.unimore.fum.iot.model.raw.PresenceRawSensor;
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

    // endpoints
    // robot
    private static final String ROBOT_DESCRIPTOR = "coap://127.0.0.1:5683/descriptor";
    private static final String ROBOT_BATTERY_LEVEL_SENSOR = "coap://127.0.0.1:5683/battery";
    private static final String ROBOT_INDOOR_POSITION_SENSOR = "coap://127.0.0.1:5683/position";
    private static final String ROBOT_PRESENCE_SENSOR = "coap://127.0.0.1:5683/presence";
    private static final String ROBOT_CAMERA_SWITCH_ACTUATOR = "coap://127.0.0.1:5683/camera";
    private static final String ROBOT_MODE_ACTUATOR = "coap://127.0.0.1:5683/mode";
    private static final String ROBOT_RETURN_HOME_ACTUATOR = "coap://127.0.0.1:5683/home";

    // presence
    private static final String PRESENCE_MONITORING_DESCRIPTOR = "coap://127.0.0.1:5684/descriptor";
    private static final String PRESENCE_MONITORING_PIR_SENSOR = "coap://127.0.0.1:5684/pir";

    // charger
    private static final String CHARGING_STATION_DESCRIPTOR = "coap://127.0.0.1:5685/descriptor";
    private static final String CHARGING_ROBOT_PRESENCE_SENSOR = "coap://127.0.0.1:5685/robot_presence";
    private static final String CHARGING_ROBOT_BATTERY_LEVEL_SENSOR = "coap://127.0.0.1:5685/recharging_battery";
    private static final String CHARGING_ENERGY_CONSUMPTION_SENSOR = "coap://127.0.0.1:5685/energy_consumption";

    // functional variables
    private static AlarmStatusDescriptor alarmStatusDescriptor;

    public static void main(String[] args) {

        // initializing and saving alarm status
        alarmStatusDescriptor = new AlarmStatusDescriptor(1, 1);
        writeAlarmStateToFile();

        if (alarmStatusDescriptor.getActiveAlarm() == 1)
            AlarmOn();


    }


    private static void AlarmOn() {

        AlarmOnDescriptor alarmOnDescriptor = new AlarmOnDescriptor();

        // map which contains the active observing resources
        Map<String, CoapObserveRelation> observingRelationMap;

        // initialize coapClient
        CoapClient coapClient = new CoapClient();

        Gson gson = new Gson();

        do {
            observingRelationMap = new HashMap<>();

            // FIRST STAGE
            // presence
            GetClientProcess(coapClient, PRESENCE_MONITORING_DESCRIPTOR, true, alarmOnDescriptor);
            // charger
            GetClientProcess(coapClient, CHARGING_STATION_DESCRIPTOR, true, alarmOnDescriptor);
            // robot
            GetClientProcess(coapClient, ROBOT_DESCRIPTOR, true, alarmOnDescriptor);

            if (!alarmOnDescriptor.getRobotRoom().equals(alarmOnDescriptor.getChargerRoom()) || !alarmOnDescriptor.getRobotRoom().equals(alarmOnDescriptor.getPresenceRoom())) {
                logger.error("ERROR. PRESENCE MONITORING OBJECT or CHARGING STATION in different room then ROBOT");
                break;
            }

            PutClientProcess(coapClient, ROBOT_MODE_ACTUATOR, gson.toJson(new MakeModeRequest(MakeModeRequest.MODE_START)));
            PutClientProcess(coapClient, ROBOT_CAMERA_SWITCH_ACTUATOR, gson.toJson(new MakeCameraSwitchRequest(MakeCameraSwitchRequest.SWITCH_ON_CAMERA)));
            ObservingClientProcess(coapClient, ROBOT_INDOOR_POSITION_SENSOR, true,  observingRelationMap, alarmOnDescriptor);
            ObservingClientProcess(coapClient, ROBOT_BATTERY_LEVEL_SENSOR, true, observingRelationMap, alarmOnDescriptor);
            ObservingClientProcess(coapClient, ROBOT_PRESENCE_SENSOR, true, observingRelationMap, alarmOnDescriptor);

            boolean control = true;
            // cycle with sleep and then cancel registrations
            do {
                // activating alarm
                if (alarmOnDescriptor.getPresence().equals(true)) {
                    logger.warn("ALARM");
                }

                readAlarmStateFromFile();
                if (alarmStatusDescriptor.getSoundAlarm() == 0) {
                    alarmOnDescriptor.setPresence(false);
                }

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // SECOND STAGE
                if (alarmOnDescriptor.getBattery().doubleValue() < 10 && control) {
                    PutClientProcess(coapClient, ROBOT_RETURN_HOME_ACTUATOR, gson.toJson(new MakeReturnHomeRequest(MakeReturnHomeRequest.SWITCH_ON_RETURN_HOME, alarmOnDescriptor.getChargerPosition())));
                    control = false;
                }

            } while (!Arrays.equals(alarmOnDescriptor.getCurrentRobotPosition(), alarmOnDescriptor.getChargerPosition()));

            observingRelationMap.forEach((key, value) -> {
                logger.info("Canceling Observation for target Url: {}", key);
                value.proactiveCancel();
            });

            observingRelationMap = new HashMap<>();

            GetClientProcess(coapClient, CHARGING_ROBOT_PRESENCE_SENSOR, true, alarmOnDescriptor);
            if (alarmOnDescriptor.getChargerRobotPresence()) {
                PutClientProcess(coapClient, ROBOT_MODE_ACTUATOR, gson.toJson(new MakeModeRequest(MakeModeRequest.MODE_PAUSE)));
                PutClientProcess(coapClient, ROBOT_CAMERA_SWITCH_ACTUATOR, gson.toJson(new MakeCameraSwitchRequest(MakeCameraSwitchRequest.SWITCH_OFF_CAMERA)));
                PutClientProcess(coapClient, ROBOT_RETURN_HOME_ACTUATOR, gson.toJson(new MakeReturnHomeRequest(MakeReturnHomeRequest.SWITCH_OFF_RETURN_HOME, null)));
            }

            // THIRD STAGE
            ObservingClientProcess(coapClient, PRESENCE_MONITORING_PIR_SENSOR, true, observingRelationMap, alarmOnDescriptor);
            ObservingClientProcess(coapClient, CHARGING_ROBOT_BATTERY_LEVEL_SENSOR, true, observingRelationMap, alarmOnDescriptor);
            ObservingClientProcess(coapClient, CHARGING_ENERGY_CONSUMPTION_SENSOR, true, observingRelationMap, alarmOnDescriptor);

            // cycle with sleep and then cancel registrations
            do {
                // activating alarm
                if (alarmOnDescriptor.getPresence().equals(true)) {
                    logger.warn("ALARM");
                }

                readAlarmStateFromFile();
                if (alarmStatusDescriptor.getSoundAlarm() == 0) {
                    alarmOnDescriptor.setPresence(false);
                }

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            } while (alarmOnDescriptor.getBattery().doubleValue() < 100);

            observingRelationMap.forEach((key, value) -> {
                logger.info("Canceling Observation for target Url: {}", key);
                value.proactiveCancel();
            });

            break;

        } while (true);
    }


    private static void ObservingClientProcess(CoapClient coapClient, String targetUrl, boolean useSenml, Map<String, CoapObserveRelation> observingRelationMap, AlarmOnDescriptor alarmOnDescriptor) {

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
                        alarmOnDescriptor.setBattery(senMLRecord.getV());
                    } else {
                        BatteryLevelRawSensor batteryLevelRawSensor = gson.fromJson(content.replace("BatteryLevelRawSensor", "").trim(), BatteryLevelRawSensor.class);
                        alarmOnDescriptor.setBattery(batteryLevelRawSensor.getBatteryLevel());
                    }
                    if ((alarmOnDescriptor.getBattery().doubleValue() > 10) &&
                            Objects.equals(targetUrl, "coap://127.0.0.1:5683/battery")) {
                        //logger.info("Notification Response Pretty Print: \n{}", Utils.prettyPrint(response));
                        logger.info("NOTIFICATION Body: " + content);
                    } else if ((alarmOnDescriptor.getBattery().doubleValue() < 100) &&
                            Objects.equals(targetUrl, "coap://127.0.0.1:5685/recharging_battery")) {
                        //logger.info("Notification Response Pretty Print: \n{}", Utils.prettyPrint(response));
                        logger.info("NOTIFICATION Body: " + content);
                    }
                }

                // presence
                else if (Objects.equals(targetUrl, "coap://127.0.0.1:5683/presence") || Objects.equals(targetUrl, "coap://127.0.0.1:5684/pir")) {
                    if (useSenml && alarmOnDescriptor.getPresence().equals(false)) {
                        SenMLRecord senMLRecord = gson.fromJson(content.replace("]", "").replace("[", "").trim(), SenMLRecord.class);
                        alarmOnDescriptor.setPresence(senMLRecord.getVb());
                        if(alarmOnDescriptor.getPresence().equals(true))
                            logger.warn("PRESENCE DETECTED!");
                    } else if (alarmOnDescriptor.getPresence().equals(false)){
                        PresenceRawSensor presenceRawSensor = gson.fromJson(content.replace("PresenceRawSensor", "").trim(), PresenceRawSensor.class);
                        alarmOnDescriptor.setPresence(presenceRawSensor.getValue());
                        if(alarmOnDescriptor.getPresence().equals(true))
                            logger.warn("PRESENCE DETECTED!");
                    }
                    if ((!Arrays.equals(alarmOnDescriptor.getCurrentRobotPosition(), alarmOnDescriptor.getChargerPosition())) &&
                            Objects.equals(targetUrl, "coap://127.0.0.1:5683/presence")){
                        //logger.info("Notification Response Pretty Print: \n{}", Utils.prettyPrint(response));
                        logger.info("NOTIFICATION Body: " + content);
                    } else if ((alarmOnDescriptor.getBattery().doubleValue() < 100) &&
                            Objects.equals(targetUrl, "coap://127.0.0.1:5684/pir")){
                        //logger.info("Notification Response Pretty Print: \n{}", Utils.prettyPrint(response));
                        logger.info("NOTIFICATION Body: " + content);
                    }
                }

                // position
                else if (Objects.equals(targetUrl, "coap://127.0.0.1:5683/position")) {
                    if (useSenml) {
                        SenMLPack senMLPack = gson.fromJson(content.trim(), SenMLPack.class);
                        alarmOnDescriptor.setCurrentRobotPosition(new double[]{senMLPack.get(1).getV().doubleValue(), senMLPack.get(2).getV().doubleValue()});
                    } else {
                        IndoorPositionRawSensor indoorPositionRawSensor = gson.fromJson(content.replace("IndoorPositionRawSensor", "").trim(), IndoorPositionRawSensor.class);
                        alarmOnDescriptor.setCurrentRobotPosition((new double[]{indoorPositionRawSensor.getPosition()[0], indoorPositionRawSensor.getPosition()[1]}));
                    }
                    if (!Arrays.equals(alarmOnDescriptor.getCurrentRobotPosition(), alarmOnDescriptor.getChargerPosition())) {
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


    private static void GetClientProcess(CoapClient coapClient, String targetUrl, boolean useSenml, AlarmOnDescriptor alarmOnDescriptor) {

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
                    alarmOnDescriptor.setRobotRoom(senMLPack.get(1).getVs());
                } else {
                    RobotDescriptor robotDescriptor = gson.fromJson(content.replace("RobotDescriptor", "").trim(), RobotDescriptor.class);
                    alarmOnDescriptor.setRobotRoom(robotDescriptor.getRoom());
                }
            }

            // presence descriptor
            else if (Objects.equals(targetUrl, "coap://127.0.0.1:5684/descriptor")) {
                if (useSenml) {
                    SenMLPack senMLPack = gson.fromJson(content.trim(), SenMLPack.class);
                    alarmOnDescriptor.setPresenceRoom(senMLPack.get(1).getVs());
                } else {
                    PresenceMonitoringObjectDescriptor presenceMonitoringObjectDescriptor = gson.fromJson(content.replace("PresenceMonitoringObjectDescriptor", "").trim(), PresenceMonitoringObjectDescriptor.class);
                    alarmOnDescriptor.setPresenceRoom(presenceMonitoringObjectDescriptor.getRoom());
                }
            }

            // charger descriptor
            else if (Objects.equals(targetUrl, "coap://127.0.0.1:5685/descriptor")) {
                if (useSenml) {
                    SenMLPack senMLPack = gson.fromJson(content.trim(), SenMLPack.class);
                    alarmOnDescriptor.setChargerRoom(senMLPack.get(1).getVs());
                    alarmOnDescriptor.setChargerPosition(new double[]{senMLPack.get(4).getV().doubleValue(), senMLPack.get(5).getV().doubleValue()});
                } else {
                    ChargingStationDescriptor chargingStationDescriptor = gson.fromJson(content.replace("ChargingStationDescriptor", "").trim(), ChargingStationDescriptor.class);
                    alarmOnDescriptor.setChargerRoom(chargingStationDescriptor.getRoom());
                    alarmOnDescriptor.setChargerPosition(chargingStationDescriptor.getPosition());
                }
            }

            // charger robot presence sensor
            else if (Objects.equals(targetUrl, "coap://127.0.0.1:5685/robot_presence")) {
                if (useSenml) {
                    SenMLRecord senMLRecord = gson.fromJson(content.replace("]", "").replace("[", "").trim(), SenMLRecord.class);
                    alarmOnDescriptor.setChargerRobotPresence(senMLRecord.getVb());
                } else {
                    PresenceRawSensor presenceRawSensor = gson.fromJson(content.replace("PresenceRawSensor", "").trim(), PresenceRawSensor.class);
                    alarmOnDescriptor.setChargerRobotPresence(presenceRawSensor.getValue());
                }
            }

            // pretty print for the received response
            //logger.info("Response Pretty Print: \n{}", Utils.prettyPrint(coapResp));
            logger.info("NOTIFICATION Body: " + content);

        } catch (ConnectorException | IOException e) {
            e.printStackTrace();
        }
    }


    private static void PutClientProcess(CoapClient coapClient, String targetUrl, String requestPayload) {

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
