package it.unimore.fum.iot.resource.presence;

import com.google.gson.Gson;
import it.unimore.fum.iot.model.presence.PassiveInfraRedSensorDescriptor;
import it.unimore.fum.iot.utils.CoreInterfaces;
import it.unimore.fum.iot.utils.SenMLPack;
import it.unimore.fum.iot.utils.SenMLRecord;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.server.resources.CoapExchange;

import java.util.Optional;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 30/03/2022 - 17:06
 */
public class PassiveInfraRedSensorResource extends CoapResource {

    private static final String OBJECT_TITLE = "PresenceInCameraStreamSensor";
    private Gson gson;
    private PassiveInfraRedSensorDescriptor passiveInfraRedSensorDescriptor;
    private String presenceId = null;
    private static final Number SENSOR_VERSION = 0.1;

    public PassiveInfraRedSensorResource(String name, String presenceId) {
        super(name);
        this.presenceId = presenceId;
        init();
    }

    private void init() {
        this.gson = new Gson();
        this.passiveInfraRedSensorDescriptor = new PassiveInfraRedSensorDescriptor(this.presenceId, SENSOR_VERSION);

        // enable observing and configure notification type
        setObservable(true);
        setObserveType(CoAP.Type.CON);

        getAttributes().setTitle(OBJECT_TITLE);
        getAttributes().addAttribute("rt", "it.unimore.presence_monitor.sensor.pir");
        getAttributes().addAttribute("if", CoreInterfaces.CORE_S.getValue());
        getAttributes().addAttribute("ct", Integer.toString(MediaTypeRegistry.APPLICATION_SENML_JSON));
        getAttributes().addAttribute("ct", Integer.toString(MediaTypeRegistry.TEXT_PLAIN));
    }

    private Optional<String> getJsonSenmlResponse(){

        try {

            SenMLPack senMLPack = new SenMLPack();

            SenMLRecord senMLRecord = new SenMLRecord();
            senMLRecord.setBn(this.presenceId);
            senMLRecord.setN("pir");
            senMLRecord.setT(this.passiveInfraRedSensorDescriptor.getTimestamp());
            senMLRecord.setBver(SENSOR_VERSION);
            senMLRecord.setVb(this.passiveInfraRedSensorDescriptor.isValue());

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
            this.passiveInfraRedSensorDescriptor.checkPassiveInfraRedSensorDescriptor();

            // if the request specify the MediaType as JSON or JSON+SenML
            if (exchange.getRequestOptions().getAccept() == MediaTypeRegistry.APPLICATION_SENML_JSON ||
                    exchange.getRequestOptions().getAccept() == MediaTypeRegistry.APPLICATION_JSON){

                Optional<String> senmlPayload = getJsonSenmlResponse();

                if (senmlPayload.isPresent())
                    exchange.respond(CoAP.ResponseCode.CONTENT, senmlPayload.get(), exchange.getRequestOptions().getAccept());
                else
                    exchange.respond(CoAP.ResponseCode.INTERNAL_SERVER_ERROR);
            } else
                exchange.respond(CoAP.ResponseCode.CONTENT, String.valueOf(this.passiveInfraRedSensorDescriptor.toString()), MediaTypeRegistry.TEXT_PLAIN);

        }  catch (Exception e){
            exchange.respond(CoAP.ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }
}
