package it.unimore.fum.iot.resource.robot;

import com.google.gson.Gson;
import it.unimore.fum.iot.model.robot.PresenceInCameraStreamSensorDescriptor;
import it.unimore.fum.iot.utils.CoreInterfaces;
import it.unimore.fum.iot.utils.SenMLPack;
import it.unimore.fum.iot.utils.SenMLRecord;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP.Type;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.server.resources.CoapExchange;

import java.util.Optional;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 27/03/2022 - 12:12
 */
public class PresenceInCameraStreamSensorResource extends CoapResource {

    private static final String OBJECT_TITLE = "PresenceInCameraStreamSensor";
    private Gson gson;
    private PresenceInCameraStreamSensorDescriptor presenceInCameraStreamSensorDescriptor;
    private String robotId = null;
    private static final Number SENSOR_VERSION = 0.1;

    public PresenceInCameraStreamSensorResource(String name, String robotId) {
        super(name);
        this.robotId = robotId;
        init();
    }

    private void init(){
        this.gson = new Gson();
        this.presenceInCameraStreamSensorDescriptor = new PresenceInCameraStreamSensorDescriptor(this.robotId, SENSOR_VERSION);

        // enable observing and configure notification type
        setObservable(true);
        setObserveType(Type.CON);

        getAttributes().setTitle(OBJECT_TITLE);
        getAttributes().addAttribute("rt", "it.unimore.robot.sensor.presence");
        getAttributes().addAttribute("if", CoreInterfaces.CORE_S.getValue());
        getAttributes().addAttribute("ct", Integer.toString(MediaTypeRegistry.APPLICATION_SENML_JSON));
        getAttributes().addAttribute("ct", Integer.toString(MediaTypeRegistry.TEXT_PLAIN));
    }

    private Optional<String> getJsonSenmlResponse(){

        try {

            SenMLPack senMLPack = new SenMLPack();

            SenMLRecord senMLRecord = new SenMLRecord();
            senMLRecord.setBn(this.robotId);
            senMLRecord.setN("presence");
            senMLRecord.setT(this.presenceInCameraStreamSensorDescriptor.getTimestamp());
            senMLRecord.setBver(SENSOR_VERSION);
            senMLRecord.setVb(this.presenceInCameraStreamSensorDescriptor.isValue());

            senMLPack.add(senMLRecord);

            return Optional.of(this.gson.toJson(senMLPack));

        } catch (Exception e){
            return Optional.empty();
        }
    }

    // response to GET function
    @Override
    public void handleGET(CoapExchange exchange) {
        try {
            this.presenceInCameraStreamSensorDescriptor.checkPresenceInCameraStream();

            // if the request specify the MediaType as JSON or JSON+SenML
            if (exchange.getRequestOptions().getAccept() == MediaTypeRegistry.APPLICATION_SENML_JSON ||
                    exchange.getRequestOptions().getAccept() == MediaTypeRegistry.APPLICATION_JSON){

                Optional<String> senmlPayload = getJsonSenmlResponse();

                if (senmlPayload.isPresent())
                    exchange.respond(ResponseCode.CONTENT, senmlPayload.get(), exchange.getRequestOptions().getAccept());
                else
                    exchange.respond(ResponseCode.INTERNAL_SERVER_ERROR);
            } else
                exchange.respond(ResponseCode.CONTENT, String.valueOf(this.presenceInCameraStreamSensorDescriptor.toString()), MediaTypeRegistry.TEXT_PLAIN);

        }  catch (Exception e){
            exchange.respond(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }
}
