package it.unimore.fum.iot.resource.robot;

import com.google.gson.Gson;
import it.unimore.fum.iot.model.robot.GeneralDataListener;
import it.unimore.fum.iot.model.robot.GeneralDescriptor;
import it.unimore.fum.iot.model.robot.raw.BatteryLevelRawSensor;
import it.unimore.fum.iot.model.robot.raw.PresenceRawSensor;
import it.unimore.fum.iot.utils.CoreInterfaces;
import it.unimore.fum.iot.utils.SenMLPack;
import it.unimore.fum.iot.utils.SenMLRecord;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP.Type;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 27/03/2022 - 12:12
 */
public class PresenceInCameraStreamSensorResource extends CoapResource {

    private final static Logger logger = LoggerFactory.getLogger(PresenceInCameraStreamSensorResource.class);
    private static final String OBJECT_TITLE = "PresenceInCameraStreamSensor";
    private Gson gson;
    private final PresenceRawSensor presenceRawSensor;

    public PresenceInCameraStreamSensorResource(String name, PresenceRawSensor presenceRawSensor) {
        super(name);
        this.presenceRawSensor = presenceRawSensor;

        if (presenceRawSensor != null && presenceRawSensor.getUuid() != null) {
            init();
        } else {
            logger.error("Error -> NULL Raw Reference !");
        }

        assert this.presenceRawSensor != null;
        this.presenceRawSensor.addDataListener(new GeneralDataListener<Boolean>() {
            @Override
            public void onDataChanged(GeneralDescriptor<Boolean> resource, Boolean updatedValue) {
                changed();
            }
        });
    }

    private void init(){
        this.gson = new Gson();

        // enable observing and configure notification type
        setObservable(true);
        setObserveType(Type.CON);

        getAttributes().setTitle(OBJECT_TITLE);
        getAttributes().setObservable();
        getAttributes().addAttribute("rt", "it.unimore.robot.sensor.presence");
        getAttributes().addAttribute("if", CoreInterfaces.CORE_S.getValue());
        getAttributes().addAttribute("ct", Integer.toString(MediaTypeRegistry.APPLICATION_SENML_JSON));
        getAttributes().addAttribute("ct", Integer.toString(MediaTypeRegistry.TEXT_PLAIN));
    }

    private Optional<String> getJsonSenmlResponse(){

        try {

            SenMLPack senMLPack = new SenMLPack();

            SenMLRecord senMLRecord = new SenMLRecord();
            senMLRecord.setBn(this.presenceRawSensor.getUuid());
            senMLRecord.setN("presence");
            senMLRecord.setT(this.presenceRawSensor.getTimestamp());
            senMLRecord.setBver(this.presenceRawSensor.getVersion());
            senMLRecord.setVb(this.presenceRawSensor.getValue());

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
            // the Max-Age value should match the update interval
            exchange.setMaxAge(BatteryLevelRawSensor.UPDATE_PERIOD);

            // if the request specify the MediaType as JSON or JSON+SenML
            if (exchange.getRequestOptions().getAccept() == MediaTypeRegistry.APPLICATION_SENML_JSON ||
                    exchange.getRequestOptions().getAccept() == MediaTypeRegistry.APPLICATION_JSON){

                Optional<String> senmlPayload = getJsonSenmlResponse();

                if (senmlPayload.isPresent())
                    exchange.respond(ResponseCode.CONTENT, senmlPayload.get(), exchange.getRequestOptions().getAccept());
                else
                    exchange.respond(ResponseCode.INTERNAL_SERVER_ERROR);
            } else
                exchange.respond(ResponseCode.CONTENT, String.valueOf(this.presenceRawSensor.toString()), MediaTypeRegistry.TEXT_PLAIN);

        } catch (Exception e){
            logger.error("Error Handling GET -> {}", e.getLocalizedMessage());
            exchange.respond(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }
}
