package it.unimore.fum.iot.resource.robot;

import com.google.gson.Gson;
import it.unimore.fum.iot.model.raw.BatteryLevelRawSensor;
import it.unimore.fum.iot.model.general.GeneralDataListener;
import it.unimore.fum.iot.model.general.GeneralDescriptor;
import it.unimore.fum.iot.utils.CoreInterfaces;
import it.unimore.fum.iot.utils.SenMLPack;
import it.unimore.fum.iot.utils.SenMLRecord;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP.Type;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Optional;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 26/03/2022 - 19:06
 */
public class BatteryLevelSensorResource extends CoapResource{

    private final static Logger logger = LoggerFactory.getLogger(BatteryLevelSensorResource.class);
    private static final String OBJECT_TITLE = "BatteryLevelSensor";
    private Gson gson;
    private final BatteryLevelRawSensor batteryLevelRawSensor;
    private static final String UNIT = "%EL";

    public BatteryLevelSensorResource(String name, BatteryLevelRawSensor batteryLevelRawSensor) {
        super(name);
        this.batteryLevelRawSensor = batteryLevelRawSensor;

        if (batteryLevelRawSensor != null && batteryLevelRawSensor.getUuid() != null) {
            init();
        } else {
            logger.error("Error -> NULL Raw Reference !");
        }

        assert this.batteryLevelRawSensor != null;
        this.batteryLevelRawSensor.addDataListener(new GeneralDataListener<Double>() {
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
        setObserveType(Type.CON);

        getAttributes().setTitle(OBJECT_TITLE);
        getAttributes().setObservable();
        getAttributes().addAttribute("rt", "it.unimore.robot.sensor.battery");
        getAttributes().addAttribute("if", CoreInterfaces.CORE_S.getValue());
        getAttributes().addAttribute("ct", Integer.toString(MediaTypeRegistry.APPLICATION_SENML_JSON));
        getAttributes().addAttribute("ct", Integer.toString(MediaTypeRegistry.TEXT_PLAIN));
    }

    private Optional<String> getJsonSenmlResponse(){

        try {

            SenMLPack senMLPack = new SenMLPack();

            SenMLRecord senMLRecord = new SenMLRecord();
            senMLRecord.setBn(this.batteryLevelRawSensor.getUuid());
            senMLRecord.setN("battery");
            senMLRecord.setT(this.batteryLevelRawSensor.getTimestamp());
            senMLRecord.setBver(this.batteryLevelRawSensor.getVersion());
            senMLRecord.setV(this.batteryLevelRawSensor.getBatteryLevel());
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
                    exchange.respond(ResponseCode.CONTENT, senmlPayload.get(), exchange.getRequestOptions().getAccept());
                else
                    exchange.respond(ResponseCode.INTERNAL_SERVER_ERROR);
            } else
                exchange.respond(ResponseCode.CONTENT, String.valueOf(this.batteryLevelRawSensor.toString()), MediaTypeRegistry.TEXT_PLAIN);

        } catch (Exception e){
            logger.error("Error Handling GET -> {}", e.getLocalizedMessage());
            exchange.respond(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }
}
