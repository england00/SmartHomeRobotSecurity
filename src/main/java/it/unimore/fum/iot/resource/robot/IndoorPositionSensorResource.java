package it.unimore.fum.iot.resource.robot;

import com.google.gson.Gson;
import it.unimore.fum.iot.model.general.GeneralDataListener;
import it.unimore.fum.iot.model.general.GeneralDescriptor;
import it.unimore.fum.iot.model.raw.IndoorPositionRawSensor;
import it.unimore.fum.iot.utils.SenMLPack;
import it.unimore.fum.iot.utils.SenMLRecord;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP.Type;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.server.resources.CoapExchange;
import it.unimore.fum.iot.utils.CoreInterfaces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Optional;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 25/03/2022 - 02:00
 */
public class IndoorPositionSensorResource extends CoapResource {

    private final static Logger logger = LoggerFactory.getLogger(IndoorPositionSensorResource.class);
    private static final String OBJECT_TITLE = "IndoorPositionSensor";
    private Gson gson;
    private final IndoorPositionRawSensor indoorPositionRawSensor;
    private static final String UNIT = "m";

    public IndoorPositionSensorResource(String name, IndoorPositionRawSensor indoorPositionRawSensor) {
        super(name);
        this.indoorPositionRawSensor = indoorPositionRawSensor;

        if (indoorPositionRawSensor != null && indoorPositionRawSensor.getUuid() != null) {
            init();
        } else {
            logger.error("Error -> NULL Raw Reference !");
        }

        assert this.indoorPositionRawSensor != null;
        this.indoorPositionRawSensor.addDataListener(new GeneralDataListener<Double[]>() {
            @Override
            public void onDataChanged(GeneralDescriptor<Double[]> resource, Double[] updatedValue) {
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
        getAttributes().addAttribute("rt", "it.unimore.robot.sensor.position");
        getAttributes().addAttribute("if", CoreInterfaces.CORE_S.getValue());
        getAttributes().addAttribute("ct", Integer.toString(MediaTypeRegistry.APPLICATION_SENML_JSON));
        getAttributes().addAttribute("ct", Integer.toString(MediaTypeRegistry.TEXT_PLAIN));
    }

    private Optional<String> getJsonSenmlResponse(){

        try {

            SenMLPack senMLPack = new SenMLPack();

            SenMLRecord baseRecord = new SenMLRecord();
            baseRecord.setBn(this.indoorPositionRawSensor.getUuid());
            baseRecord.setN("position");
            baseRecord.setT(this.indoorPositionRawSensor.getTimestamp());
            baseRecord.setBver(this.indoorPositionRawSensor.getVersion());

            SenMLRecord measureRecordX = new SenMLRecord();
            measureRecordX.setN("X");
            measureRecordX.setV(this.indoorPositionRawSensor.getPosition()[0]);
            measureRecordX.setU(UNIT);

            SenMLRecord measureRecordY = new SenMLRecord();
            measureRecordY.setN("Y");
            measureRecordY.setV(this.indoorPositionRawSensor.getPosition()[1]);
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
            // the Max-Age value should match the update interval
            exchange.setMaxAge(IndoorPositionRawSensor.UPDATE_PERIOD);

            // if the request specify the MediaType as JSON or JSON+SenML
            if (exchange.getRequestOptions().getAccept() == MediaTypeRegistry.APPLICATION_SENML_JSON ||
                    exchange.getRequestOptions().getAccept() == MediaTypeRegistry.APPLICATION_JSON){

                Optional<String> senmlPayload = getJsonSenmlResponse();

                if (senmlPayload.isPresent())
                    exchange.respond(ResponseCode.CONTENT, senmlPayload.get(), exchange.getRequestOptions().getAccept());
                else
                    exchange.respond(ResponseCode.INTERNAL_SERVER_ERROR);
            } else
                exchange.respond(ResponseCode.CONTENT, String.valueOf(this.indoorPositionRawSensor.toString()), MediaTypeRegistry.TEXT_PLAIN);

        } catch (Exception e){
            logger.error("Error Handling GET -> {}", e.getLocalizedMessage());
            exchange.respond(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }
}
