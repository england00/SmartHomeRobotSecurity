package it.unimore.fum.iot.test.client;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.Utils;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.OptionSet;
import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.elements.exception.ConnectorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 06/04/2022 - 00:42
 */
public class DataManagerGetClientProcess {

    // client's parameters
    private final static Logger logger = LoggerFactory.getLogger(DataManagerGetClientProcess.class);

    private static final String ROBOT_DESCRIPTOR = "coap://127.0.0.1:5683/descriptor";
    private static final String ROBOT_MODE_ACTUATOR = "coap://127.0.0.1:5683/mode";
    private static final String ROBOT_CAMERA_SWITCH_ACTUATOR = "coap://127.0.0.1:5683/camera";
    private static final String ROBOT_RETURN_HOME_ACTUATOR = "coap://127.0.0.1:5683/home";

    private static final String PRESENCE_MONITORING_DESCRIPTOR = "coap://127.0.0.1:5684/descriptor";

    private static final String CHARGING_STATION_DESCRIPTOR = "coap://127.0.0.1:5685/descriptor";
    private static final String CHARGING_ROBOT_PRESENCE_SENSOR = "coap://127.0.0.1:5685/robot_presence";

    private static final String ENDPOINT = CHARGING_ROBOT_PRESENCE_SENSOR;

    public static void main(String[] args) {

        // initialize coapClient
        CoapClient coapClient = new CoapClient(ENDPOINT);

        // request class is a generic CoAP message: in this case we want a GET.
        // "Message ID", "Token" and other header's fields can be set
        Request request = new Request(CoAP.Code.GET);

        //request.setOptions(new OptionSet().setAccept(MediaTypeRegistry.APPLICATION_SENML_JSON));
        //request.setOptions(new OptionSet().setAccept(MediaTypeRegistry.APPLICATION_JSON));
        request.setOptions(new OptionSet().setAccept(MediaTypeRegistry.TEXT_PLAIN));

        request.setURI(ENDPOINT);
        request.setConfirmable(true);

        logger.info("Request Pretty Print: \n{}", Utils.prettyPrint(request));

        // synchronously send the GET message (blocking call)
        CoapResponse coapResp = null;

        try {

            coapResp = coapClient.advanced(request);

            // pretty print for the received response
            logger.info("Response Pretty Print: \n{}", Utils.prettyPrint(coapResp));
            String content = coapResp.getResponseText(); // "content" has the payload.

            /*
            logger.info("Payload: {}", content);
            logger.info("Message ID: " + coapResp.advanced().getMID());
            logger.info("Token: " + coapResp.advanced().getTokenString());
             */

        } catch (ConnectorException | IOException e) {
            e.printStackTrace();
        }
    }
}