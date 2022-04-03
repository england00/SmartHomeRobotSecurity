package it.unimore.fum.iot.resource.robot;

import com.google.gson.Gson;
import it.unimore.fum.iot.model.robot.IIndoorPositionSensorDescriptor;
import it.unimore.fum.iot.utils.SenMLPack;
import it.unimore.fum.iot.utils.SenMLRecord;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP.Type;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.server.resources.CoapExchange;
import it.unimore.fum.iot.utils.CoreInterfaces;
import java.util.Optional;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 25/03/2022 - 02:00
 */
public class IndoorPositionSensorResource extends CoapResource {

    private static final String OBJECT_TITLE = "IndoorPositionSensor";
    private Gson gson;
    private final IIndoorPositionSensorDescriptor indoorPositionSensorDescriptor;
    private static final String UNIT = "m";

    public IndoorPositionSensorResource(String name, IIndoorPositionSensorDescriptor indoorPositionSensorDescriptor) {
        super(name);
        this.indoorPositionSensorDescriptor = indoorPositionSensorDescriptor;
        init(this.indoorPositionSensorDescriptor.getRoomDimensions(), this.indoorPositionSensorDescriptor.getOrigin());
    }

    private void init(double[] roomDimensions, double[] origin){
        this.gson = new Gson();

        // enable observing
        setObservable(true);
        setObserveType(Type.CON);

        getAttributes().setTitle(OBJECT_TITLE);
        getAttributes().addAttribute("rt", "it.unimore.robot.sensor.position");
        getAttributes().addAttribute("if", CoreInterfaces.CORE_S.getValue());
        getAttributes().addAttribute("ct", Integer.toString(MediaTypeRegistry.APPLICATION_SENML_JSON));
        getAttributes().addAttribute("ct", Integer.toString(MediaTypeRegistry.TEXT_PLAIN));
    }

    private Optional<String> getJsonSenmlResponse(){

        try {

            SenMLPack senMLPack = new SenMLPack();

            SenMLRecord senMLRecord = new SenMLRecord();
            senMLRecord.setBn(this.indoorPositionSensorDescriptor.getRobotId());
            senMLRecord.setN("position");
            senMLRecord.setT(this.indoorPositionSensorDescriptor.getTimestamp());
            senMLRecord.setBver(this.indoorPositionSensorDescriptor.getVersion());

            SenMLRecord measureRecordX = new SenMLRecord();
            senMLRecord.setU("X");
            senMLRecord.setV(this.indoorPositionSensorDescriptor.getPosition()[0]);
            senMLRecord.setU(UNIT);

            SenMLRecord measureRecordY = new SenMLRecord();
            senMLRecord.setU("Y");
            senMLRecord.setV(this.indoorPositionSensorDescriptor.getPosition()[1]);
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
            this.indoorPositionSensorDescriptor.updateIndoorPosition();

            // if the request specify the MediaType as JSON or JSON+SenML
            if (exchange.getRequestOptions().getAccept() == MediaTypeRegistry.APPLICATION_SENML_JSON ||
                    exchange.getRequestOptions().getAccept() == MediaTypeRegistry.APPLICATION_JSON){

                Optional<String> senmlPayload = getJsonSenmlResponse();

                if (senmlPayload.isPresent())
                    exchange.respond(ResponseCode.CONTENT, senmlPayload.get(), exchange.getRequestOptions().getAccept());
                else
                    exchange.respond(ResponseCode.INTERNAL_SERVER_ERROR);
            } else
                exchange.respond(ResponseCode.CONTENT, String.valueOf(this.indoorPositionSensorDescriptor.toString()), MediaTypeRegistry.TEXT_PLAIN);

        } catch (Exception e){
            exchange.respond(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }
}
