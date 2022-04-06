package it.unimore.fum.iot.resource.charger;

import com.google.gson.Gson;
import it.unimore.fum.iot.model.general.GeneralDataListener;
import it.unimore.fum.iot.model.general.GeneralDescriptor;
import it.unimore.fum.iot.model.raw.PresenceRawSensor;
import it.unimore.fum.iot.utils.CoreInterfaces;
import it.unimore.fum.iot.utils.SenMLPack;
import it.unimore.fum.iot.utils.SenMLRecord;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Optional;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 03/04/2022 - 21:28
 */
public class RobotPresenceSensorResource extends CoapResource {

    private final static Logger logger = LoggerFactory.getLogger(RobotPresenceSensorResource.class);
    private static final String OBJECT_TITLE = "RobotPresenceSensor";
    private Gson gson;
    private final PresenceRawSensor robotPresenceSensor;

    public RobotPresenceSensorResource(String name, PresenceRawSensor robotPresenceSensor) {
        super(name);
        this.robotPresenceSensor = robotPresenceSensor;

        if (robotPresenceSensor != null && robotPresenceSensor.getUuid() != null) {
            init();
        } else {
            logger.error("Error -> NULL Raw Reference !");
        }

        assert this.robotPresenceSensor != null;
        this.robotPresenceSensor.addDataListener(new GeneralDataListener<Boolean>() {
            @Override
            public void onDataChanged(GeneralDescriptor<Boolean> resource, Boolean updatedValue) {
                changed();
            }
        });
    }

    private void init(){
        this.gson = new Gson();
        this.robotPresenceSensor.setActivate(false);

        getAttributes().setTitle(OBJECT_TITLE);
        getAttributes().addAttribute("rt", "it.unimore.charger.sensor.robot_presence");
        getAttributes().addAttribute("if", CoreInterfaces.CORE_S.getValue());
        getAttributes().addAttribute("ct", Integer.toString(MediaTypeRegistry.APPLICATION_SENML_JSON));
        getAttributes().addAttribute("ct", Integer.toString(MediaTypeRegistry.TEXT_PLAIN));
    }

    private Optional<String> getJsonSenmlResponse(){

        try {

            SenMLPack senMLPack = new SenMLPack();

            SenMLRecord senMLRecord = new SenMLRecord();
            senMLRecord.setBn(this.robotPresenceSensor.getUuid());
            senMLRecord.setN("robot presence");
            senMLRecord.setT(this.robotPresenceSensor.getTimestamp());
            senMLRecord.setBver(this.robotPresenceSensor.getVersion());
            senMLRecord.setVb(this.robotPresenceSensor.getValue());

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
            // if the request specify the MediaType as JSON or JSON+SenML
            if (exchange.getRequestOptions().getAccept() == MediaTypeRegistry.APPLICATION_SENML_JSON ||
                    exchange.getRequestOptions().getAccept() == MediaTypeRegistry.APPLICATION_JSON){

                Optional<String> senmlPayload = getJsonSenmlResponse();

                if (senmlPayload.isPresent())
                    exchange.respond(CoAP.ResponseCode.CONTENT, senmlPayload.get(), exchange.getRequestOptions().getAccept());
                else
                    exchange.respond(CoAP.ResponseCode.INTERNAL_SERVER_ERROR);
            } else
                exchange.respond(CoAP.ResponseCode.CONTENT, String.valueOf(this.robotPresenceSensor.toString()), MediaTypeRegistry.TEXT_PLAIN);

        }  catch (Exception e){
            exchange.respond(CoAP.ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }
}
