package it.unimore.fum.iot.resource.presence;

import com.google.gson.Gson;
import it.unimore.fum.iot.model.descriptor.PresenceMonitoringObjectDescriptor;
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
 * @created 30/03/2022 - 12:29
 */
public class PresenceMonitoringObjectResource extends CoapResource {

    private final static Logger logger = LoggerFactory.getLogger(PresenceMonitoringObjectResource.class);
    private static final String OBJECT_TITLE = "PresenceMonitoringObject";
    private Gson gson;
    private final PresenceMonitoringObjectDescriptor presenceMonitoringObjectDescriptor;

    public PresenceMonitoringObjectResource(String name, PresenceMonitoringObjectDescriptor presenceMonitoringObjectDescriptor) {
        super(name);
        this.presenceMonitoringObjectDescriptor = presenceMonitoringObjectDescriptor;
        init();
    }

    private void init() {
        this.gson = new Gson();

        getAttributes().setTitle(OBJECT_TITLE);
        getAttributes().addAttribute("rt", "it.unimore.presence_monitor.descriptor");
        getAttributes().addAttribute("if", CoreInterfaces.CORE_RP.getValue());
        getAttributes().addAttribute("ct", Integer.toString(MediaTypeRegistry.APPLICATION_SENML_JSON));
        getAttributes().addAttribute("ct", Integer.toString(MediaTypeRegistry.TEXT_PLAIN));
    }

    private Optional<String> getJsonSenmlResponse(){

        try {

            SenMLPack senMLPack = new SenMLPack();

            SenMLRecord senMLRecord1 = new SenMLRecord();
            senMLRecord1.setBn("descriptor");
            senMLRecord1.setN("presenceId");
            senMLRecord1.setVs(this.presenceMonitoringObjectDescriptor.getPresenceId());

            SenMLRecord senMLRecord2 = new SenMLRecord();
            senMLRecord2.setN("room");
            senMLRecord2.setVs(this.presenceMonitoringObjectDescriptor.getRoom());

            SenMLRecord senMLRecord3 = new SenMLRecord();
            senMLRecord3.setN("softwareVersion");
            senMLRecord3.setV(this.presenceMonitoringObjectDescriptor.getSoftwareVersion());

            SenMLRecord senMLRecord4 = new SenMLRecord();
            senMLRecord4.setN("manufacturer");
            senMLRecord4.setVs(this.presenceMonitoringObjectDescriptor.getManufacturer());

            senMLPack.add(senMLRecord1);
            senMLPack.add(senMLRecord2);
            senMLPack.add(senMLRecord3);
            senMLPack.add(senMLRecord4);

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
                exchange.respond(CoAP.ResponseCode.CONTENT, String.valueOf(this.presenceMonitoringObjectDescriptor.toString()), MediaTypeRegistry.TEXT_PLAIN);

        }  catch (Exception e){
            logger.error("Error Handling GET -> {}", e.getLocalizedMessage());
            exchange.respond(CoAP.ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }
}
