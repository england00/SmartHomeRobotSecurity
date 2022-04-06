package it.unimore.fum.iot.resource.presence;

import com.google.gson.Gson;
import it.unimore.fum.iot.model.general.GeneralDataListener;
import it.unimore.fum.iot.model.general.GeneralDescriptor;
import it.unimore.fum.iot.model.raw.BatteryLevelRawSensor;
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
 * @created 30/03/2022 - 17:06
 */
public class PassiveInfraRedSensorResource extends CoapResource {

    private final static Logger logger = LoggerFactory.getLogger(PassiveInfraRedSensorResource.class);
    private static final String OBJECT_TITLE = "PassiveInfraRedSensor";
    private Gson gson;
    private final PresenceRawSensor passiveInfraRedSensorDescriptor;

    public PassiveInfraRedSensorResource(String name, PresenceRawSensor passiveInfraRedSensorDescriptor) {
        super(name);
        this.passiveInfraRedSensorDescriptor = passiveInfraRedSensorDescriptor;

        if (passiveInfraRedSensorDescriptor != null && passiveInfraRedSensorDescriptor.getUuid() != null) {
            init();
        } else {
            logger.error("Error -> NULL Raw Reference !");
        }

        assert this.passiveInfraRedSensorDescriptor != null;
        this.passiveInfraRedSensorDescriptor.addDataListener(new GeneralDataListener<Boolean>() {
            @Override
            public void onDataChanged(GeneralDescriptor<Boolean> resource, Boolean updatedValue) {

                // produce the data only if detect a presence
                if (updatedValue)
                    changed();
            }
        });
    }

    private void init() {
        this.gson = new Gson();

        // enable observing and configure notification type
        setObservable(true);
        setObserveType(CoAP.Type.CON);

        getAttributes().setTitle(OBJECT_TITLE);
        getAttributes().setObservable();
        getAttributes().addAttribute("rt", "it.unimore.presence_monitor.sensor.pir");
        getAttributes().addAttribute("if", CoreInterfaces.CORE_S.getValue());
        getAttributes().addAttribute("ct", Integer.toString(MediaTypeRegistry.APPLICATION_SENML_JSON));
        getAttributes().addAttribute("ct", Integer.toString(MediaTypeRegistry.TEXT_PLAIN));
    }

    private Optional<String> getJsonSenmlResponse(){

        try {

            SenMLPack senMLPack = new SenMLPack();

            SenMLRecord senMLRecord = new SenMLRecord();
            senMLRecord.setBn(this.passiveInfraRedSensorDescriptor.getUuid());
            senMLRecord.setN("pir");
            senMLRecord.setT(this.passiveInfraRedSensorDescriptor.getTimestamp());
            senMLRecord.setBver(this.passiveInfraRedSensorDescriptor.getVersion());
            senMLRecord.setVb(this.passiveInfraRedSensorDescriptor.getValue());

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
                    exchange.respond(CoAP.ResponseCode.CONTENT, senmlPayload.get(), exchange.getRequestOptions().getAccept());
                else
                    exchange.respond(CoAP.ResponseCode.INTERNAL_SERVER_ERROR);
            } else
                exchange.respond(CoAP.ResponseCode.CONTENT, String.valueOf(this.passiveInfraRedSensorDescriptor.toString()), MediaTypeRegistry.TEXT_PLAIN);

        }  catch (Exception e){
            logger.error("Error Handling GET -> {}", e.getLocalizedMessage());
            exchange.respond(CoAP.ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }
}
