package it.unimore.fum.iot.resource.robot;

import com.google.gson.Gson;
import it.unimore.fum.iot.model.robot.ModeActuatorDescriptor;
import it.unimore.fum.iot.request.MakeModeRequest;
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
 * @created 28/03/2022 - 17:16
 */
public class ModeActuatorResource extends CoapResource {

    private static final String OBJECT_TITLE = "ModeActuator";
    private Gson gson;
    private ModeActuatorDescriptor modeActuatorDescriptor;
    private String robotId = null;
    private static final Number ACTUATOR_VERSION = 0.1;

    public ModeActuatorResource (String name, String robotId) {
        super(name);
        this.robotId = robotId;
        init();
    }

    public void init() {
        this.gson = new Gson();
        this.modeActuatorDescriptor = new ModeActuatorDescriptor(this.robotId, ACTUATOR_VERSION);

        getAttributes().setTitle(OBJECT_TITLE);
        getAttributes().addAttribute("rt", "it.unimore.robot.actuator.mode");
        getAttributes().addAttribute("if", CoreInterfaces.CORE_A.getValue());
        getAttributes().addAttribute("ct", Integer.toString(MediaTypeRegistry.APPLICATION_SENML_JSON));
        getAttributes().addAttribute("ct", Integer.toString(MediaTypeRegistry.TEXT_PLAIN));
    }

    private Optional<String> getJsonSenmlResponse(){

        try {

            SenMLPack senMLPack = new SenMLPack();

            SenMLRecord senMLRecord = new SenMLRecord();
            senMLRecord.setBn(this.robotId);
            senMLRecord.setN("mode");
            senMLRecord.setT(this.modeActuatorDescriptor.getTimestamp());
            senMLRecord.setBver(ACTUATOR_VERSION);
            senMLRecord.setVs(this.modeActuatorDescriptor.getValue());

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
                exchange.respond(CoAP.ResponseCode.CONTENT, String.valueOf(this.modeActuatorDescriptor.toString()), MediaTypeRegistry.TEXT_PLAIN);

        } catch (Exception e){
            exchange.respond(CoAP.ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    // behaviour to POST function
    @Override
    public void handlePOST(CoapExchange exchange) {
        try {
            if (this.modeActuatorDescriptor.getValue().equals("START")) {
                this.modeActuatorDescriptor.modePause();
                exchange.respond(CoAP.ResponseCode.CHANGED);
                changed();
            } else if (this.modeActuatorDescriptor.getValue().equals("PAUSE")) {
                this.modeActuatorDescriptor.modeStart();
                exchange.respond(CoAP.ResponseCode.CHANGED);
                changed();
            } else {
                this.modeActuatorDescriptor.modeStop();
                exchange.respond(CoAP.ResponseCode.CHANGED);
                changed();
            }

        } catch (Exception e){
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
                        this.modeActuatorDescriptor.modeStart();
                        exchange.respond(CoAP.ResponseCode.CHANGED);
                        changed();
                    }
                    case MakeModeRequest.MODE_PAUSE -> {
                        this.modeActuatorDescriptor.modePause();
                        exchange.respond(CoAP.ResponseCode.CHANGED);
                        changed();
                    }
                    case MakeModeRequest.MODE_STOP -> {
                        this.modeActuatorDescriptor.modeStop();
                        exchange.respond(CoAP.ResponseCode.CHANGED);
                        changed();
                    }
                    default -> exchange.respond(CoAP.ResponseCode.BAD_REQUEST);
                }
            }
            else
                exchange.respond(CoAP.ResponseCode.BAD_REQUEST);

        } catch (Exception e){
            exchange.respond(CoAP.ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }
}
