package it.unimore.fum.iot.client;

import org.eclipse.californium.core.CoapClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 09/04/2022 - 01:04
 */
public class DataManager {

    // client's parameters
    private final static Logger logger = LoggerFactory.getLogger(DataManager.class);
    private static final String ROBOT_BATTERY_LEVEL_SENSOR = "coap://127.0.0.1:5683/battery";
    private static final String ROBOT_INDOOR_POSITION_SENSOR = "coap://127.0.0.1:5683/position";


    public static void main(String[] args) {

        //Initialize coapClient
        CoapClient coapClient = new CoapClient();




    }

}
