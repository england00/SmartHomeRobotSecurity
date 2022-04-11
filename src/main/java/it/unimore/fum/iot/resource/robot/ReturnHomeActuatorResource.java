package it.unimore.fum.iot.resource.robot;

import com.google.gson.Gson;
import it.unimore.fum.iot.model.general.GeneralDataListener;
import it.unimore.fum.iot.model.general.GeneralDescriptor;
import it.unimore.fum.iot.model.raw.IndoorPositionRawSensor;
import it.unimore.fum.iot.model.raw.ReturnHomeRawActuator;
import it.unimore.fum.iot.request.MakeReturnHomeRequest;
import it.unimore.fum.iot.utils.CoreInterfaces;
import it.unimore.fum.iot.utils.SenMLPack;
import it.unimore.fum.iot.utils.SenMLRecord;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Optional;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 28/03/2022 - 21:09
 */
public class ReturnHomeActuatorResource extends CoapResource {

    private final static Logger logger = LoggerFactory.getLogger(ReturnHomeActuatorResource.class);
    private static final String OBJECT_TITLE = "ReturnHomeActuator";
    private Gson gson;
    private final ReturnHomeRawActuator returnHomeRawActuator;
    private final IndoorPositionRawSensor indoorPositionRawSensor;
    private static final String UNIT = "m";

    public ReturnHomeActuatorResource(String name, ReturnHomeRawActuator returnHomeRawActuator, IndoorPositionRawSensor indoorPositionRawSensor) {
        super(name);
        this.returnHomeRawActuator = returnHomeRawActuator;
        this.indoorPositionRawSensor = indoorPositionRawSensor;

        if (returnHomeRawActuator != null && returnHomeRawActuator.getUuid() != null) {
            init();
        } else {
            logger.error("Error -> NULL Raw Reference !");
        }
    }

    private void init(){
        this.gson = new Gson();

        getAttributes().setTitle(OBJECT_TITLE);
        getAttributes().addAttribute("rt", "it.unimore.robot.actuator.home");
        getAttributes().addAttribute("if", CoreInterfaces.CORE_A.getValue());
        getAttributes().addAttribute("ct", Integer.toString(MediaTypeRegistry.APPLICATION_SENML_JSON));
        getAttributes().addAttribute("ct", Integer.toString(MediaTypeRegistry.TEXT_PLAIN));

        returnHomeRawActuator.addDataListener(new GeneralDataListener<Boolean>() {
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

            SenMLRecord baseRecord = new SenMLRecord();
            baseRecord.setBn(this.returnHomeRawActuator.getUuid());
            baseRecord.setN("home");
            baseRecord.setT(this.returnHomeRawActuator.getTimestamp());
            baseRecord.setBver(this.returnHomeRawActuator.getVersion());
            baseRecord.setVb(this.returnHomeRawActuator.isValue());

            //if the charger position is null, the senML payload is not sent

            SenMLRecord measureRecordX = new SenMLRecord();
            measureRecordX.setN("X");
            measureRecordX.setV(this.returnHomeRawActuator.getChargerPosition()[0]);
            measureRecordX.setU(UNIT);

            SenMLRecord measureRecordY = new SenMLRecord();
            measureRecordY.setN("Y");
            measureRecordY.setV(this.returnHomeRawActuator.getChargerPosition()[1]);
            measureRecordY.setU(UNIT);

            senMLPack.add(baseRecord);
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
                    //if the charger position is null, the senML payload is not sent
                    exchange.respond(ResponseCode.INTERNAL_SERVER_ERROR);
            } else
                exchange.respond(ResponseCode.CONTENT, String.valueOf(this.returnHomeRawActuator.toString()), MediaTypeRegistry.TEXT_PLAIN);

        } catch (Exception e){
            logger.error("Error Handling GET -> {}", e.getLocalizedMessage());
            exchange.respond(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    // behaviour to POST function
    @Override
    public void handlePOST(CoapExchange exchange) {

        try {
            this.returnHomeRawActuator.switchReturnOff();
            this.returnHomeRawActuator.setChargerPosition(null);
            logger.info("Resource Status Updated: {}", this.returnHomeRawActuator.isValue());
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
            MakeReturnHomeRequest makeReturnHomeRequest = this.gson.fromJson(receivedPayload, MakeReturnHomeRequest.class);

            if (makeReturnHomeRequest != null && makeReturnHomeRequest.getType() != null
                    && makeReturnHomeRequest.getPosition() != null && makeReturnHomeRequest.getType().length() > 0) {
                if (makeReturnHomeRequest.getType().equals(MakeReturnHomeRequest.SWITCH_ON_RETURN_HOME)) {
                    this.returnHomeRawActuator.switchReturnOn();
                    this.returnHomeRawActuator.setChargerPosition(makeReturnHomeRequest.getPosition());
                    logger.info("Resource Status Updated: {}", this.returnHomeRawActuator.isValue());
                    this.indoorPositionRawSensor.setReturnFlag(this.returnHomeRawActuator.isValue());
                    this.indoorPositionRawSensor.setChargerPosition(this.returnHomeRawActuator.getChargerPosition());
                    exchange.respond(ResponseCode.CHANGED);
                    changed();
                } else if (makeReturnHomeRequest.getType().equals(MakeReturnHomeRequest.SWITCH_OFF_RETURN_HOME)) {
                    this.returnHomeRawActuator.switchReturnOff();
                    this.returnHomeRawActuator.setChargerPosition(null);
                    logger.info("Resource Status Updated: {}", this.returnHomeRawActuator.isValue());
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
