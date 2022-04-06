package it.unimore.fum.iot.resource.charger;

import com.google.gson.Gson;
import it.unimore.fum.iot.model.general.GeneralDataListener;
import it.unimore.fum.iot.model.general.GeneralDescriptor;
import it.unimore.fum.iot.model.raw.BatteryLevelRawSensor;
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
 * @created 03/04/2022 - 21:19
 */
public class RobotBatteryLevelSensorResource extends CoapResource {

    private final static Logger logger = LoggerFactory.getLogger(RobotBatteryLevelSensorResource.class);
    private static final String OBJECT_TITLE = "RobotBatteryLevelSensor";
    private Gson gson;
    private final BatteryLevelRawSensor robotBatteryLevelSensor;
    private static final String UNIT = "%EL";

    public RobotBatteryLevelSensorResource(String name, BatteryLevelRawSensor robotBatteryLevelSensor) {
        super(name);
        this.robotBatteryLevelSensor = robotBatteryLevelSensor;

        if (robotBatteryLevelSensor != null && robotBatteryLevelSensor.getUuid() != null) {
            init();
        } else {
            logger.error("Error -> NULL Raw Reference !");
        }

        assert this.robotBatteryLevelSensor != null;
        this.robotBatteryLevelSensor.addDataListener(new GeneralDataListener<Double>() {
            @Override
            public void onDataChanged(GeneralDescriptor<Double> resource, Double updatedValue) {
                changed();
            }
        });
    }

    private void init(){
        this.gson = new Gson();

        // enable observing and configure notification type
        setObservable(true);
        setObserveType(CoAP.Type.CON);

        getAttributes().setTitle(OBJECT_TITLE);
        getAttributes().setObservable();
        getAttributes().addAttribute("rt", "it.unimore.charger.sensor.recharging_battery");
        getAttributes().addAttribute("if", CoreInterfaces.CORE_S.getValue());
        getAttributes().addAttribute("ct", Integer.toString(MediaTypeRegistry.APPLICATION_SENML_JSON));
        getAttributes().addAttribute("ct", Integer.toString(MediaTypeRegistry.TEXT_PLAIN));
    }

    private Optional<String> getJsonSenmlResponse(){

        try {

            SenMLPack senMLPack = new SenMLPack();

            SenMLRecord senMLRecord = new SenMLRecord();
            senMLRecord.setBn(this.robotBatteryLevelSensor.getUuid());
            senMLRecord.setN("recharging battery");
            senMLRecord.setT(this.robotBatteryLevelSensor.getTimestamp());
            senMLRecord.setBver(this.robotBatteryLevelSensor.getVersion());
            senMLRecord.setV(this.robotBatteryLevelSensor.getBatteryLevel());
            senMLRecord.setBu(UNIT);

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
                exchange.respond(CoAP.ResponseCode.CONTENT, String.valueOf(this.robotBatteryLevelSensor.toString()), MediaTypeRegistry.TEXT_PLAIN);

        } catch (Exception e){
            logger.error("Error Handling GET -> {}", e.getLocalizedMessage());
            exchange.respond(CoAP.ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }
}
