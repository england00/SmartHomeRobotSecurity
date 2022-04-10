package it.unimore.fum.iot.client;

import com.google.gson.Gson;
import it.unimore.fum.iot.request.MakeCameraSwitchRequest;
import it.unimore.fum.iot.request.MakeModeRequest;
import it.unimore.fum.iot.utils.SenMLRecord;
import org.eclipse.californium.core.*;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.OptionSet;
import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.elements.exception.ConnectorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.plaf.TreeUI;
import java.io.IOException;
import java.util.*;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 09/04/2022 - 01:04
 */
public class DataManager {

    // client's utilities
    private final static Logger logger = LoggerFactory.getLogger(DataManager.class);
    private final boolean alarmState = false;

    // endpoints
    private static final String ROBOT_DESCRIPTOR = "coap://127.0.0.1:5683/descriptor";
    private static final String ROBOT_BATTERY_LEVEL_SENSOR = "coap://127.0.0.1:5683/battery";
    private static final String ROBOT_INDOOR_POSITION_SENSOR = "coap://127.0.0.1:5683/position";
    private static final String ROBOT_PRESENCE_SENSOR = "coap://127.0.0.1:5683/presence";
    private static final String ROBOT_CAMERA_SWITCH_ACTUATOR = "coap://127.0.0.1:5683/camera";
    private static final String ROBOT_MODE_ACTUATOR = "coap://127.0.0.1:5683/mode";

    private static Map<String, CoapObserveRelation> observingRelationMap = null;
    private static Number battery = 100;

    public static void main(String[] args) {

        // init
        logger.info("ACTIVATING DATA MANAGER");
        int select = 0;
        Scanner reader = new Scanner(System.in);

        // main cycle
        do {
            logger.info("Insert the character corresponding to the chosen option:");
            logger.info("0 - activate ALARM;");
            logger.info("1 - deactivate ALARM;");
            logger.info("other - shutdown DATA MANAGER;");

            // reading the chosen value from keyboard
            select = reader.nextInt();

            if (select == 0) {
                logger.info("ACTIVATING ALARM");
                AlarmOn();

                /*
                ArrayList<Thread> threads = new ArrayList<>();
                for(int i=0; i<3; i++) {
                    Thread t = new Thread();
                    t.run(AlarmOn);
                }

                 */

            } else if (select == 1) {
                logger.info("DEACTIVATING ALARM");
            } else {
                logger.info("SHUTTING DOWN DATA MANAGER");
            }

        } while (select == 0 || select == 1);

        // close the reading stream from stdin
        reader.close();



        /*
        // map which contains the active observing resources
        observingRelationMap = new HashMap<>();

        //Initialize coapClient
        CoapClient coapClient = new CoapClient();

        do {
            startObservingTargetResource(coapClient, ROBOT_INDOOR_POSITION_SENSOR, false);
            startObservingTargetResource(coapClient, ROBOT_BATTERY_LEVEL_SENSOR, false);


            //Sleep and then cancel registrations
            try {
                Thread.sleep(60 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            observingRelationMap.forEach((key, value) -> {
                logger.info("Canceling Observation for target Url: {}", key);
                value.proactiveCancel();

            });

            break;
        } while (true);

        */



    }

    private static void AlarmOn() {

        // map which contains the active observing resources
        observingRelationMap = new HashMap<>();

        //Initialize coapClient
        CoapClient coapClient = new CoapClient();

        Gson gson = new Gson();

        do {

            // first stage
            GetClientProcess(coapClient, ROBOT_DESCRIPTOR, true);
            PutClientProcess(coapClient, ROBOT_MODE_ACTUATOR, gson.toJson(new MakeModeRequest(MakeModeRequest.MODE_START)));
            PutClientProcess(coapClient, ROBOT_CAMERA_SWITCH_ACTUATOR, gson.toJson(new MakeCameraSwitchRequest(MakeCameraSwitchRequest.SWITCH_ON_CAMERA)));
            ObservingClientProcess(coapClient, ROBOT_INDOOR_POSITION_SENSOR, true);
            ObservingClientProcess(coapClient, ROBOT_BATTERY_LEVEL_SENSOR, true);
            ObservingClientProcess(coapClient, ROBOT_PRESENCE_SENSOR, true);

            //Sleep and then cancel registrations
            do {
                logger.info(String.valueOf(battery.doubleValue()));
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (!(battery.doubleValue() < 10));

            observingRelationMap.forEach((key, value) -> {
                logger.info("Canceling Observation for target Url: {}", key);
                value.proactiveCancel();


            });



            break;




        } while (true);
    }

    private static void ObservingClientProcess(CoapClient coapClient, String targetUrl, boolean useSenml) {

        Gson gson = new Gson();
        logger.info("OBSERVING ... {}", targetUrl);
        Request request = Request.newGet();

        if(useSenml)
            request.setOptions(new OptionSet().setAccept(MediaTypeRegistry.APPLICATION_SENML_JSON));

        request.setObserve();
        request.setConfirmable(true);
        request.setURI(targetUrl);

        CoapObserveRelation relation = coapClient.observe(request, new CoapHandler() {

            @Override
            public void onLoad(CoapResponse response) {
                String content = response.getResponseText();
                if (Objects.equals(targetUrl, "coap://127.0.0.1:5683/battery")) {
                    SenMLRecord senMLRecord1 = gson.fromJson(content.replace("]", "").replace("[", "").trim(), SenMLRecord.class);
                    battery = senMLRecord1.getV();
                }
                if (battery.doubleValue() > 10) {
                    logger.info("Notification Response Pretty Print: \n{}", Utils.prettyPrint(response));
                    //logger.info("NOTIFICATION Body: " + content);
                }
            }

            @Override
            public void onError() {
                logger.error("OBSERVING {} FAILED", targetUrl);
            }
        });

        observingRelationMap.put(targetUrl, relation);
    }

    private static void GetClientProcess(CoapClient coapClient, String targetUrl, boolean useSenml) {

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

            // pretty print for the received response
            logger.info("Response Pretty Print: \n{}", Utils.prettyPrint(coapResp));
            String content = coapResp.getResponseText();

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
}
