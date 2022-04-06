package it.unimore.fum.iot.client;

import com.google.gson.Gson;
import it.unimore.fum.iot.request.MakeCameraSwitchRequest;
import it.unimore.fum.iot.request.MakeModeRequest;
import it.unimore.fum.iot.request.MakeReturnHomeRequest;
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
 * @created 06/04/2022 - 04:01
 */
public class DataManagerPutClientProcess {

    // client's parameters
    private final static Logger logger = LoggerFactory.getLogger(DataManagerPostClientProcess.class);
    private Gson gson;
    private static final String ROBOT_MODE_ACTUATOR = "coap://127.0.0.1:5683/mode";
    private static final String ROBOT_CAMERA_SWITCH_ACTUATOR = "coap://127.0.0.1:5683/camera";
    private static final String ROBOT_RETURN_HOME_ACTUATOR = "coap://127.0.0.1:5683/home";
    private static final String ENDPOINT = ROBOT_RETURN_HOME_ACTUATOR;

    public static void main(String[] args) {

        // initialize coapClient
        CoapClient coapClient = new CoapClient(ENDPOINT);

        // request class is a generic CoAP message: in this case we want a PUT.
        // "Message ID", "Token" and other header's fields can be set
        Request request = new Request(CoAP.Code.PUT);

        Gson gson = new Gson();

        //Set PUT request's payload
        if (ENDPOINT == ROBOT_MODE_ACTUATOR) {
            String requestPayload = gson.toJson(new MakeModeRequest(MakeModeRequest.MODE_START));
            //String requestPayload = gson.toJson(new MakeModeRequest(MakeModeRequest.MODE_PAUSE));
            //String requestPayload = gson.toJson(new MakeModeRequest(MakeModeRequest.MODE_STOP));
            request.setPayload(requestPayload);
        } else if (ENDPOINT == ROBOT_CAMERA_SWITCH_ACTUATOR) {
            String requestPayload = gson.toJson(new MakeCameraSwitchRequest(MakeCameraSwitchRequest.SWITCH_ON_CAMERA));
            //String requestPayload = gson.toJson(new MakeCameraSwitchRequest(MakeCameraSwitchRequest.SWITCH_OFF_CAMERA));
            request.setPayload(requestPayload);
        } else if (ENDPOINT == ROBOT_RETURN_HOME_ACTUATOR) {
            String requestPayload = gson.toJson(new MakeReturnHomeRequest(MakeReturnHomeRequest.SWITCH_ON_RETURN_HOME, new double[] {2.6, 6.9}));
            //String requestPayload = gson.toJson(new MakeReturnHomeRequest(MakeReturnHomeRequest.SWITCH_OFF_RETURN_HOME, null));
            request.setPayload(requestPayload);
        }

        request.setURI(ENDPOINT);
        request.setConfirmable(true);

        logger.info("Request Pretty Print: \n{}", Utils.prettyPrint(request));

        // synchronously send the PUT message (blocking call)
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
