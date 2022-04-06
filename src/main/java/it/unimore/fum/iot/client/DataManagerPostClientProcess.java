package it.unimore.fum.iot.client;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.Utils;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.elements.exception.ConnectorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 06/04/2022 - 03:56
 */
public class DataManagerPostClientProcess {

    // client's parameters
    private final static Logger logger = LoggerFactory.getLogger(DataManagerPostClientProcess.class);
    private static final String ROBOT_MODE_ACTUATOR = "coap://127.0.0.1:5683/mode";
    private static final String ROBOT_CAMERA_SWITCH_ACTUATOR = "coap://127.0.0.1:5683/camera";
    private static final String ROBOT_RETURN_HOME_ACTUATOR = "coap://127.0.0.1:5683/home";
    private static final String ENDPOINT = ROBOT_RETURN_HOME_ACTUATOR;

    public static void main(String[] args) {

        // initialize coapClient
        CoapClient coapClient = new CoapClient(ENDPOINT);

        // request class is a generic CoAP message: in this case we want a POST.
        // "Message ID", "Token" and other header's fields can be set
        Request request = new Request(CoAP.Code.POST);

        request.setURI(ENDPOINT);
        request.setConfirmable(true);

        logger.info("Request Pretty Print: \n{}", Utils.prettyPrint(request));

        // synchronously send the POST message (blocking call)
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
