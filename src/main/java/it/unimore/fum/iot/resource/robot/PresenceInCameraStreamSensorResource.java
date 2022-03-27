package it.unimore.fum.iot.resource.robot;

import com.google.gson.Gson;
import it.unimore.fum.iot.model.robot.PresenceInCameraStreamSensorDescriptor;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP.Type;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.server.resources.CoapExchange;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 27/03/2022 - 12:12
 */
public class PresenceInCameraStreamSensorResource extends CoapResource {

    private static final String OBJECT_TITLE = "PresenceInCameraStreamSensor";
    private Gson gson;
    private PresenceInCameraStreamSensorDescriptor presenceInCameraStreamSensorDescriptor;

    public PresenceInCameraStreamSensorResource(String name) {
        super(name);
        init();
    }

    private void init(){
        getAttributes().setTitle(OBJECT_TITLE);

        // enable observing and configure notification type
        setObservable(true);
        setObserveType(Type.CON);

        this.gson = new Gson();
        this.presenceInCameraStreamSensorDescriptor = new PresenceInCameraStreamSensorDescriptor();
    }

    // response to GET function
    @Override
    public void handleGET(CoapExchange exchange) {
        try{
            this.presenceInCameraStreamSensorDescriptor.checkPresenceInCameraStream();
            String responseBody = this.gson.toJson(this.presenceInCameraStreamSensorDescriptor);
            exchange.respond(ResponseCode.CONTENT, responseBody, MediaTypeRegistry.APPLICATION_JSON);
        }catch (Exception e){
            exchange.respond(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }
}
