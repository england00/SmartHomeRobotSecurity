package it.unimore.fum.iot.resource.robot;

import com.google.gson.Gson;
import it.unimore.fum.iot.model.robot.IndoorPositionSensorDescriptor;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP.Type;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.server.resources.CoapExchange;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 25/03/2022 - 02:00
 */
public class IndoorPositionSensorResource extends CoapResource {

    private static final String OBJECT_TITLE = "IndoorPositionSensor";
    private Gson gson;
    private IndoorPositionSensorDescriptor indoorPositionSensorDescriptor;

    public IndoorPositionSensorResource(String name, double[] roomDimensions) {
        super(name);
        init(roomDimensions);
    }

    private void init(double[] roomDimensions){
        getAttributes().setTitle(OBJECT_TITLE);

        // enable observing and configure the notification type
        setObservable(true);
        setObserveType(Type.CON);

        this.gson = new Gson();
        this.indoorPositionSensorDescriptor = new IndoorPositionSensorDescriptor(roomDimensions);
    }

    // response to GET function
    @Override
    public void handleGET(CoapExchange exchange) {
        try{
            this.indoorPositionSensorDescriptor.updateIndoorPosition();
            String responseBody = this.gson.toJson(this.indoorPositionSensorDescriptor);
            exchange.respond(ResponseCode.CONTENT, responseBody, MediaTypeRegistry.APPLICATION_JSON);
        }catch (Exception e){
            exchange.respond(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }
}
