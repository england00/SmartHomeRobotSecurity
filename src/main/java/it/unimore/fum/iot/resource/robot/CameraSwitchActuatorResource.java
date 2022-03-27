package it.unimore.fum.iot.resource.robot;

import com.google.gson.Gson;
import it.unimore.fum.iot.model.robot.CameraSwitchActuatorDescriptor;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 27/03/2022 - 12:20
 */
public class CameraSwitchActuatorResource extends CoapResource {

    private static final String OBJECT_TITLE = "CameraSwitchActuator";
    private Gson gson;
    private CameraSwitchActuatorDescriptor cameraSwitchActuatorDescriptor;

    public CameraSwitchActuatorResource (String name) {
        super(name);
        init();
    }

    public void init() {
        getAttributes().setTitle(OBJECT_TITLE);
        this.gson = new Gson();
        this.cameraSwitchActuatorDescriptor = new CameraSwitchActuatorDescriptor();

    }
}
