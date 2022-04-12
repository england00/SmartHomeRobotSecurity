package it.unimore.fum.iot.test.client;

import com.google.gson.Gson;
import it.unimore.fum.iot.utils.SenMLRecord;
import org.eclipse.californium.core.*;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.OptionSet;
import org.eclipse.californium.core.coap.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

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

    private static final String ENDPOINT = ROBOT_PRESENCE_SENSOR;

    private static Number battery = 100;
    private static Boolean presence = false;

    public static void main(String[] args) {

        Gson gson = new Gson();

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

            @Override
            public void onLoad(CoapResponse response) {
                String content = response.getResponseText();
                if (Objects.equals(ENDPOINT, "coap://127.0.0.1:5683/battery")) {
                    SenMLRecord senMLRecord = gson.fromJson(content.replace("]", "").replace("[", "").trim(), SenMLRecord.class);
                    battery = senMLRecord.getV();
                    if (battery.doubleValue() > 10) {
                        logger.info("Notification Response Pretty Print: \n{}", Utils.prettyPrint(response));
                        //logger.info("NOTIFICATION Body: " + content);
                    }
                }
                if (Objects.equals(ENDPOINT, "coap://127.0.0.1:5683/presence")) {
                    SenMLRecord senMLRecord = gson.fromJson(content.replace("]", "").replace("[", "").trim(), SenMLRecord.class);
                    presence = senMLRecord.getVb();
                }

                logger.info("Notification Response Pretty Print: \n{}", Utils.prettyPrint(response));
                if (presence.equals(true)) {
                    logger.warn("ALARM");
                    //logger.info("NOTIFICATION Body: " + content);
                }

            }

            @Override
            public void onError() {
                logger.error("OBSERVING FAILED");
            }
        });



        // Observes the coap resource for 30 seconds then the observing relation is deleted
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /*
        do {
            logger.info(String.valueOf(battery.doubleValue()));
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (!(battery.doubleValue() < 10));


         */

        logger.info("CANCELLATION.....");
        relation.proactiveCancel();
    }
}
