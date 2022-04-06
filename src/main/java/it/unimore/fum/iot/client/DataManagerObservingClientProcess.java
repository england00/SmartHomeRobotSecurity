package it.unimore.fum.iot.client;

import org.eclipse.californium.core.*;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.OptionSet;
import org.eclipse.californium.core.coap.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 05/04/2022 - 15:22
 */
public class DataManagerObservingClientProcess {

    // client's parameters
    private final static Logger logger = LoggerFactory.getLogger(DataManagerObservingClientProcess.class);
    private static final String ROBOT_BATTERY_LEVEL_SENSOR = "coap://127.0.0.1:5683/battery";
    private static final String ROBOT_INDOOR_POSITION_SENSOR = "coap://127.0.0.1:5683/position";
    private static final String ROBOT_PRESENCE_SENSOR = "coap://127.0.0.1:5683/presence";

    private static final String PRESENCE_MONITORING_PIR_SENSOR = "coap://127.0.0.1:5684/pir";

    private static final String CHARGING_ROBOT_BATTERY_LEVEL_SENSOR = "coap://127.0.0.1:5685/recharging_battery";
    private static final String CHARGING_ENERGY_CONSUMPTION_SENSOR = "coap://127.0.0.1:5685/energy_consumption";

    private static final String ENDPOINT = CHARGING_ENERGY_CONSUMPTION_SENSOR;

    public static void main(String[] args) {

        // initialize coapClient
        CoapClient coapClient = new CoapClient(ENDPOINT);

        logger.info("OBSERVING ... {}", ENDPOINT);

        // request class is a generic CoAP message: in this case we want a GET.
        // "Message ID", "Token" and other header's fields can be set
        Request request = new Request(CoAP.Code.GET);

        request.setOptions(new OptionSet().setAccept(MediaTypeRegistry.APPLICATION_SENML_JSON));
        //request.setOptions(new OptionSet().setAccept(MediaTypeRegistry.APPLICATION_JSON));
        //request.setOptions(new OptionSet().setAccept(MediaTypeRegistry.TEXT_PLAIN));

        request.setURI(ENDPOINT);
        request.setObserve();
        request.setConfirmable(true);

        // NOTE:
        // The client.observe(Request, CoapHandler) method visibility has been changed from "private"
        // to "public" in order to get the ability to change the parameter of the observable GET
        //(e.g., to change token and MID).
        CoapObserveRelation relation = coapClient.observe(request, new CoapHandler() {

            public void onLoad(CoapResponse response) {
                String content = response.getResponseText();
                logger.info("Notification Response Pretty Print: \n{}", Utils.prettyPrint(response));
                //logger.info("NOTIFICATION Body: " + content);
            }

            public void onError() {
                logger.error("OBSERVING FAILED");
            }
        });


        // Observes the coap resource for 30 seconds then the observing relation is deleted
        try {
            Thread.sleep(60*10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        logger.info("CANCELLATION.....");
        relation.proactiveCancel();
    }
}
