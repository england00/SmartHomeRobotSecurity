package it.unimore.fum.iot.resource.robot;

import com.google.gson.Gson;
import it.unimore.fum.iot.model.robot.BatteryLevelSensorDescriptor;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP.Type;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.server.resources.CoapExchange;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 26/03/2022 - 19:06
 */
public class BatteryLevelSensorResource extends CoapResource{

    private static final String OBJECT_TITLE = "BatteryLevelSensor";
    private Gson gson;
    private BatteryLevelSensorDescriptor batteryLevelSensorDescriptor;

    public BatteryLevelSensorResource(String name) {
        super(name);
        init();
    }

    private void init(){
        getAttributes().setTitle(OBJECT_TITLE);

        // enable observing and configure notification type
        setObservable(true);
        setObserveType(Type.CON);

        this.gson = new Gson();
        this.batteryLevelSensorDescriptor = new BatteryLevelSensorDescriptor();
    }

    // response to GET function
    @Override
    public void handleGET(CoapExchange exchange) {
        try{
            this.batteryLevelSensorDescriptor.checkBatteryLevel();
            String responseBody = this.gson.toJson(this.batteryLevelSensorDescriptor);
            exchange.respond(ResponseCode.CONTENT, responseBody, MediaTypeRegistry.APPLICATION_JSON);
        }catch (Exception e){
            exchange.respond(ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }
}
