package it.unimore.fum.iot.resource.robot;

import com.google.gson.Gson;
import it.unimore.fum.iot.model.robot.ICameraSwitchActuatorDescriptor;
import it.unimore.fum.iot.request.MakeCameraSwitchRequest;
import it.unimore.fum.iot.utils.CoreInterfaces;
import it.unimore.fum.iot.utils.SenMLPack;
import it.unimore.fum.iot.utils.SenMLRecord;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.server.resources.CoapExchange;
import java.util.Optional;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 27/03/2022 - 12:20
 */
public class CameraSwitchActuatorResource extends CoapResource {

    private static final String OBJECT_TITLE = "CameraSwitchActuator";
    private Gson gson;
    private final ICameraSwitchActuatorDescriptor cameraSwitchActuatorDescriptor;

    public CameraSwitchActuatorResource (String name, ICameraSwitchActuatorDescriptor cameraSwitchActuatorDescriptor) {
        super(name);
        this.cameraSwitchActuatorDescriptor = cameraSwitchActuatorDescriptor;
        init();
    }

    public void init() {
        this.gson = new Gson();

        getAttributes().setTitle(OBJECT_TITLE);
        getAttributes().addAttribute("rt", "it.unimore.robot.actuator.camera");
        getAttributes().addAttribute("if", CoreInterfaces.CORE_A.getValue());
        getAttributes().addAttribute("ct", Integer.toString(MediaTypeRegistry.APPLICATION_SENML_JSON));
        getAttributes().addAttribute("ct", Integer.toString(MediaTypeRegistry.TEXT_PLAIN));
    }

    private Optional<String> getJsonSenmlResponse(){

        try {

            SenMLPack senMLPack = new SenMLPack();

            SenMLRecord senMLRecord = new SenMLRecord();
            senMLRecord.setBn(this.cameraSwitchActuatorDescriptor.getRobotId());
            senMLRecord.setN("camera");
            senMLRecord.setT(this.cameraSwitchActuatorDescriptor.getTimestamp());
            senMLRecord.setBver(this.cameraSwitchActuatorDescriptor.getVersion());
            senMLRecord.setVb(this.cameraSwitchActuatorDescriptor.isValue());

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
                exchange.respond(ResponseCode.CONTENT, String.valueOf(this.cameraSwitchActuatorDescriptor.toString()), MediaTypeRegistry.TEXT_PLAIN);

        } catch (Exception e){
            exchange.respond(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    // behaviour to POST function
    @Override
    public void handlePOST(CoapExchange exchange) {

        try {
            if (this.cameraSwitchActuatorDescriptor.isValue()) {
                this.cameraSwitchActuatorDescriptor.switchStatusOff();
            } else {
                this.cameraSwitchActuatorDescriptor.switchStatusOn();
            }

            exchange.respond(ResponseCode.CHANGED);
            changed();

        } catch (Exception e){
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
                    this.cameraSwitchActuatorDescriptor.switchStatusOn();
                    exchange.respond(ResponseCode.CHANGED);
                    changed();
                } else if(makeCameraSwitchRequest.getType().equals(MakeCameraSwitchRequest.SWITCH_OFF_CAMERA)){
                    this.cameraSwitchActuatorDescriptor.switchStatusOff();
                    exchange.respond(ResponseCode.CHANGED);
                    changed();
                } else
                    exchange.respond(ResponseCode.BAD_REQUEST);
            }
            else
                exchange.respond(ResponseCode.BAD_REQUEST);

        } catch (Exception e){
            exchange.respond(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }
}
