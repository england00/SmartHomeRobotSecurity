package it.unimore.fum.iot.resource.charger;

import com.google.gson.Gson;
import it.unimore.fum.iot.model.descriptor.ChargingStationDescriptor;
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
 * @created 03/04/2022 - 20:26
 */
public class ChargingStationResource extends CoapResource {

    private final static Logger logger = LoggerFactory.getLogger(ChargingStationResource.class);
    private static final String OBJECT_TITLE = "ChargingStation";
    private Gson gson;
    private final ChargingStationDescriptor chargingStationDescriptor;
    private static final String UNIT = "m";

    public ChargingStationResource(String name, ChargingStationDescriptor chargingStationDescriptor) {
        super(name);
        this.chargingStationDescriptor = chargingStationDescriptor;
        init();
    }

    private void init() {
        this.gson = new Gson();

        getAttributes().setTitle(OBJECT_TITLE);
        getAttributes().addAttribute("rt", "it.unimore.charger.descriptor");
        getAttributes().addAttribute("if", CoreInterfaces.CORE_RP.getValue());
        getAttributes().addAttribute("ct", Integer.toString(MediaTypeRegistry.APPLICATION_SENML_JSON));
        getAttributes().addAttribute("ct", Integer.toString(MediaTypeRegistry.TEXT_PLAIN));
    }

    private Optional<String> getJsonSenmlResponse(){

        try {

            SenMLPack senMLPack = new SenMLPack();

            SenMLRecord senMLRecord1 = new SenMLRecord();
            senMLRecord1.setBn("descriptor");
            senMLRecord1.setN("chargerId");
            senMLRecord1.setVs(this.chargingStationDescriptor.getChargerId());

            SenMLRecord senMLRecord2 = new SenMLRecord();
            senMLRecord2.setN("room");
            senMLRecord2.setVs(this.chargingStationDescriptor.getRoom());

            SenMLRecord senMLRecord3 = new SenMLRecord();
            senMLRecord3.setN("softwareVersion");
            senMLRecord3.setV(this.chargingStationDescriptor.getSoftwareVersion());

            SenMLRecord senMLRecord4 = new SenMLRecord();
            senMLRecord4.setN("manufacturer");
            senMLRecord4.setVs(this.chargingStationDescriptor.getManufacturer());

            SenMLRecord senMLRecord5 = new SenMLRecord();
            senMLRecord5.setN("X");
            senMLRecord5.setV(this.chargingStationDescriptor.getPosition()[0]);
            senMLRecord5.setU(UNIT);

            SenMLRecord senMLRecord6 = new SenMLRecord();
            senMLRecord6.setN("Y");
            senMLRecord6.setV(this.chargingStationDescriptor.getPosition()[1]);
            senMLRecord6.setU(UNIT);

            senMLPack.add(senMLRecord1);
            senMLPack.add(senMLRecord2);
            senMLPack.add(senMLRecord3);
            senMLPack.add(senMLRecord4);
            senMLPack.add(senMLRecord5);
            senMLPack.add(senMLRecord6);

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
                exchange.respond(CoAP.ResponseCode.CONTENT, String.valueOf(this.chargingStationDescriptor.toString()), MediaTypeRegistry.TEXT_PLAIN);

        }  catch (Exception e){
            logger.error("Error Handling GET -> {}", e.getLocalizedMessage());
            exchange.respond(CoAP.ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }
}
