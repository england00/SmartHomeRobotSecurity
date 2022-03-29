package it.unimore.fum.iot.resource.robot;

import com.google.gson.Gson;
import it.unimore.fum.iot.model.robot.ReturnHomeActuatorDescriptor;
import it.unimore.fum.iot.request.MakeReturnHomeRequest;
import it.unimore.fum.iot.utils.CoreInterfaces;
import it.unimore.fum.iot.utils.SenMLPack;
import it.unimore.fum.iot.utils.SenMLRecord;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.server.resources.CoapExchange;
import java.util.Optional;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 28/03/2022 - 21:09
 */
public class ReturnHomeActuatorResource extends CoapResource {

    private static final String OBJECT_TITLE = "ReturnHomeActuator";
    private Gson gson;
    private ReturnHomeActuatorDescriptor returnHomeActuatorDescriptor;
    private String robotId = null;
    private static final Number ACTUATOR_VERSION = 0.1;
    private static final String UNIT = "m";

    public ReturnHomeActuatorResource(String name, String robotId) {
        super(name);
        this.robotId = robotId;
        init();
    }

    private void init(){
        this.gson = new Gson();
        this.returnHomeActuatorDescriptor = new ReturnHomeActuatorDescriptor(this.robotId, ACTUATOR_VERSION);

        getAttributes().setTitle(OBJECT_TITLE);
        getAttributes().addAttribute("rt", "it.unimore.robot.actuator.home");
        getAttributes().addAttribute("if", CoreInterfaces.CORE_A.getValue());
        getAttributes().addAttribute("ct", Integer.toString(MediaTypeRegistry.APPLICATION_SENML_JSON));
        getAttributes().addAttribute("ct", Integer.toString(MediaTypeRegistry.TEXT_PLAIN));
    }

    private Optional<String> getJsonSenmlResponse(){

        try {

            SenMLPack senMLPack = new SenMLPack();

            SenMLRecord senMLRecord = new SenMLRecord();
            senMLRecord.setBn(this.robotId);
            senMLRecord.setN("home");
            senMLRecord.setT(this.returnHomeActuatorDescriptor.getTimestamp());
            senMLRecord.setBver(ACTUATOR_VERSION);
            senMLRecord.setVb(this.returnHomeActuatorDescriptor.isValue());

            SenMLRecord measureRecordX = new SenMLRecord();
            senMLRecord.setU("X");
            senMLRecord.setV(this.returnHomeActuatorDescriptor.getChargerPosition()[0]);
            senMLRecord.setU(UNIT);

            SenMLRecord measureRecordY = new SenMLRecord();
            senMLRecord.setU("Y");
            senMLRecord.setV(this.returnHomeActuatorDescriptor.getChargerPosition()[1]);
            senMLRecord.setU(UNIT);

            senMLPack.add(senMLRecord);
            senMLPack.add(measureRecordX);
            senMLPack.add(measureRecordY);

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
                exchange.respond(ResponseCode.CONTENT, String.valueOf(this.returnHomeActuatorDescriptor.toString()), MediaTypeRegistry.TEXT_PLAIN);

        } catch (Exception e){
            exchange.respond(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    // behaviour to POST function
    @Override
    public void handlePOST(CoapExchange exchange) {
        try {
            this.returnHomeActuatorDescriptor.switchReturnOff();
            this.returnHomeActuatorDescriptor.setChargerPosition(null);
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
            MakeReturnHomeRequest makeReturnHomeRequest = this.gson.fromJson(receivedPayload, MakeReturnHomeRequest.class);

            if (makeReturnHomeRequest != null && makeReturnHomeRequest.getType() != null
                    && makeReturnHomeRequest.getPosition() != null && makeReturnHomeRequest.getType().length() > 0) {
                if (makeReturnHomeRequest.getType().equals(MakeReturnHomeRequest.SWITCH_ON_RETURN_HOME)) {
                    this.returnHomeActuatorDescriptor.switchReturnOn();
                    this.returnHomeActuatorDescriptor.setChargerPosition(makeReturnHomeRequest.getPosition());
                    exchange.respond(ResponseCode.CHANGED);
                    changed();
                } else if (makeReturnHomeRequest.getType().equals(MakeReturnHomeRequest.SWITCH_OFF_RETURN_HOME)) {
                    this.returnHomeActuatorDescriptor.switchReturnOff();
                    this.returnHomeActuatorDescriptor.setChargerPosition(null);
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
