package it.unimore.fum.iot.resource.robot;

import com.google.gson.Gson;
import it.unimore.fum.iot.model.general.GeneralDataListener;
import it.unimore.fum.iot.model.general.GeneralDescriptor;
import it.unimore.fum.iot.model.raw.SwitchRawActuator;
import it.unimore.fum.iot.request.MakeCameraSwitchRequest;
import it.unimore.fum.iot.utils.CoreInterfaces;
import it.unimore.fum.iot.utils.SenMLPack;
import it.unimore.fum.iot.utils.SenMLRecord;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Optional;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 27/03/2022 - 12:20
 */
public class CameraSwitchActuatorResource extends CoapResource {

    private final static Logger logger = LoggerFactory.getLogger(CameraSwitchActuatorResource.class);
    private static final String OBJECT_TITLE = "CameraSwitchActuator";
    private Gson gson;
    private final SwitchRawActuator switchRawActuator;

    public CameraSwitchActuatorResource (String name, SwitchRawActuator switchRawActuator) {
        super(name);
        this.switchRawActuator = switchRawActuator;

        if (switchRawActuator != null && switchRawActuator.getUuid() != null) {
            init();
        } else {
            logger.error("Error -> NULL Raw Reference !");
        }
    }

    public void init() {
        this.gson = new Gson();

        getAttributes().setTitle(OBJECT_TITLE);
        getAttributes().addAttribute("rt", "it.unimore.robot.actuator.camera");
        getAttributes().addAttribute("if", CoreInterfaces.CORE_A.getValue());
        getAttributes().addAttribute("ct", Integer.toString(MediaTypeRegistry.APPLICATION_SENML_JSON));
        getAttributes().addAttribute("ct", Integer.toString(MediaTypeRegistry.TEXT_PLAIN));

        switchRawActuator.addDataListener(new GeneralDataListener<Boolean>() {
            @Override
            public void onDataChanged(GeneralDescriptor<Boolean> resource, Boolean updatedValue) {
                logger.info("Raw Resource Notification Callback ! New Value: {}", updatedValue);
                changed();
            }
        });
    }

    private Optional<String> getJsonSenmlResponse(){

        try {

            SenMLPack senMLPack = new SenMLPack();

            SenMLRecord senMLRecord = new SenMLRecord();
            senMLRecord.setBn(this.switchRawActuator.getUuid());
            senMLRecord.setN("camera");
            senMLRecord.setT(this.switchRawActuator.getTimestamp());
            senMLRecord.setBver(this.switchRawActuator.getVersion());
            senMLRecord.setVb(this.switchRawActuator.isValue());

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
                    exchange.respond(ResponseCode.CONTENT, senmlPayload.get(), exchange.getRequestOptions().getAccept());
                else
                    exchange.respond(ResponseCode.INTERNAL_SERVER_ERROR);
            } else
                exchange.respond(ResponseCode.CONTENT, String.valueOf(this.switchRawActuator.toString()), MediaTypeRegistry.TEXT_PLAIN);

        } catch (Exception e){
            logger.error("Error Handling GET -> {}", e.getLocalizedMessage());
            exchange.respond(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    // behaviour to POST function
    @Override
    public void handlePOST(CoapExchange exchange) {

        try {
            if (this.switchRawActuator.isValue()) {
                this.switchRawActuator.switchStatusOff();
            } else {
                this.switchRawActuator.switchStatusOn();
            }
            logger.info("Resource Status Updated: {}", this.switchRawActuator.isValue());
            exchange.respond(ResponseCode.CHANGED);
            changed();

        } catch (Exception e){
            logger.error("Error Handling POST -> {}", e.getLocalizedMessage());
            exchange.respond(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    // behaviour to PUT function
    @Override
    public void handlePUT(CoapExchange exchange) {

        try {
            String receivedPayload = new String(exchange.getRequestPayload());
            MakeCameraSwitchRequest makeCameraSwitchRequest = this.gson.fromJson(receivedPayload, MakeCameraSwitchRequest.class);

            if (makeCameraSwitchRequest != null && makeCameraSwitchRequest.getType() != null && makeCameraSwitchRequest.getType().length() > 0) {
                if (makeCameraSwitchRequest.getType().equals(MakeCameraSwitchRequest.SWITCH_ON_CAMERA)) {
                    this.switchRawActuator.switchStatusOn();
                    logger.info("Resource Status Updated: {}", this.switchRawActuator.isValue());
                    exchange.respond(ResponseCode.CHANGED);
                    changed();
                } else if(makeCameraSwitchRequest.getType().equals(MakeCameraSwitchRequest.SWITCH_OFF_CAMERA)){
                    this.switchRawActuator.switchStatusOff();
                    logger.info("Resource Status Updated: {}", this.switchRawActuator.isValue());
                    exchange.respond(ResponseCode.CHANGED);
                    changed();
                } else
                    exchange.respond(ResponseCode.BAD_REQUEST);
            }
            else
                exchange.respond(ResponseCode.BAD_REQUEST);

        } catch (Exception e){
            logger.error("Error Handling PUT -> {}", e.getLocalizedMessage());
            exchange.respond(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }
}
