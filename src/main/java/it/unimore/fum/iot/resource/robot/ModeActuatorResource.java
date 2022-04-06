package it.unimore.fum.iot.resource.robot;

import com.google.gson.Gson;
import it.unimore.fum.iot.model.robot.GeneralDataListener;
import it.unimore.fum.iot.model.robot.GeneralDescriptor;
import it.unimore.fum.iot.model.robot.raw.ModeRawActuator;
import it.unimore.fum.iot.request.MakeModeRequest;
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
 * @created 28/03/2022 - 17:16
 */
public class ModeActuatorResource extends CoapResource {

    private final static Logger logger = LoggerFactory.getLogger(ModeActuatorResource.class);
    private static final String OBJECT_TITLE = "ModeActuator";
    private Gson gson;
    private final ModeRawActuator modeRawActuator;

    public ModeActuatorResource (String name, ModeRawActuator modeRawActuator) {
        super(name);
        this.modeRawActuator = modeRawActuator;

        if (modeRawActuator != null && modeRawActuator.getUuid() != null) {
            init();
        } else {
            logger.error("Error -> NULL Raw Reference !");
        }
    }

    public void init() {
        this.gson = new Gson();

        getAttributes().setTitle(OBJECT_TITLE);
        getAttributes().addAttribute("rt", "it.unimore.robot.actuator.mode");
        getAttributes().addAttribute("if", CoreInterfaces.CORE_A.getValue());
        getAttributes().addAttribute("ct", Integer.toString(MediaTypeRegistry.APPLICATION_SENML_JSON));
        getAttributes().addAttribute("ct", Integer.toString(MediaTypeRegistry.TEXT_PLAIN));

        modeRawActuator.addDataListener(new GeneralDataListener<String>() {
            @Override
            public void onDataChanged(GeneralDescriptor<String> resource, String updatedValue) {
                logger.info("Raw Resource Notification Callback ! New Value: {}", updatedValue);
                changed();
            }
        });
    }

    private Optional<String> getJsonSenmlResponse(){

        try {

            SenMLPack senMLPack = new SenMLPack();

            SenMLRecord senMLRecord = new SenMLRecord();
            senMLRecord.setBn(this.modeRawActuator.getUuid());
            senMLRecord.setN("mode");
            senMLRecord.setT(this.modeRawActuator.getTimestamp());
            senMLRecord.setBver(this.modeRawActuator.getVersion());
            senMLRecord.setVs(this.modeRawActuator.getValue());

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
                exchange.respond(CoAP.ResponseCode.CONTENT, String.valueOf(this.modeRawActuator.toString()), MediaTypeRegistry.TEXT_PLAIN);

        } catch (Exception e){
            exchange.respond(CoAP.ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    // behaviour to POST function
    @Override
    public void handlePOST(CoapExchange exchange) {

        try {
            if (this.modeRawActuator.getValue().equals("START")) {
                this.modeRawActuator.modePause();
                logger.info("Resource Status Updated: {}", this.modeRawActuator.getValue());
                exchange.respond(CoAP.ResponseCode.CHANGED);
                changed();
            } else if (this.modeRawActuator.getValue().equals("PAUSE")) {
                this.modeRawActuator.modeStart();
                logger.info("Resource Status Updated: {}", this.modeRawActuator.getValue());
                exchange.respond(CoAP.ResponseCode.CHANGED);
                changed();
            } else {
                this.modeRawActuator.modeStop();
                logger.info("Resource Status Updated: {}", this.modeRawActuator.getValue());
                exchange.respond(CoAP.ResponseCode.CHANGED);
                changed();
            }

        } catch (Exception e){
            logger.error("Error Handling POST -> {}", e.getLocalizedMessage());
            exchange.respond(CoAP.ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    // behaviour to PUT function
    @Override
    public void handlePUT(CoapExchange exchange) {

        try {
            String receivedPayload = new String(exchange.getRequestPayload());
            MakeModeRequest makeModeRequest = this.gson.fromJson(receivedPayload, MakeModeRequest.class);

            if (makeModeRequest != null && makeModeRequest.getType() != null && makeModeRequest.getType().length() > 0) {
                switch (makeModeRequest.getType()) {
                    case MakeModeRequest.MODE_START -> {
                        this.modeRawActuator.modeStart();
                        logger.info("Resource Status Updated: {}", this.modeRawActuator.getValue());
                        exchange.respond(CoAP.ResponseCode.CHANGED);
                        changed();
                    }
                    case MakeModeRequest.MODE_PAUSE -> {
                        this.modeRawActuator.modePause();
                        logger.info("Resource Status Updated: {}", this.modeRawActuator.getValue());
                        exchange.respond(CoAP.ResponseCode.CHANGED);
                        changed();
                    }
                    case MakeModeRequest.MODE_STOP -> {
                        this.modeRawActuator.modeStop();
                        logger.info("Resource Status Updated: {}", this.modeRawActuator.getValue());
                        exchange.respond(CoAP.ResponseCode.CHANGED);
                        changed();
                    }
                    default -> exchange.respond(CoAP.ResponseCode.BAD_REQUEST);
                }
            }
            else
                exchange.respond(CoAP.ResponseCode.BAD_REQUEST);

        } catch (Exception e){
            logger.error("Error Handling PUT -> {}", e.getLocalizedMessage());
            exchange.respond(CoAP.ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }
}
