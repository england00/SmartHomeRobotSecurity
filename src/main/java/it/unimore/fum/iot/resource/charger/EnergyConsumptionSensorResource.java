package it.unimore.fum.iot.resource.charger;

import com.google.gson.Gson;
import it.unimore.fum.iot.model.charger.IEnergyConsumptionSensorDescriptor;
import it.unimore.fum.iot.utils.CoreInterfaces;
import it.unimore.fum.iot.utils.SenMLPack;
import it.unimore.fum.iot.utils.SenMLRecord;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.server.resources.CoapExchange;

import java.util.Optional;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 03/04/2022 - 20:46
 */
public class EnergyConsumptionSensorResource extends CoapResource {

    private static final String OBJECT_TITLE = "EnergyConsumptionSensor";
    private Gson gson;
    private final IEnergyConsumptionSensorDescriptor energyConsumptionSensorDescriptor;
    private static final String UNIT = "kWh";

    public EnergyConsumptionSensorResource(String name, IEnergyConsumptionSensorDescriptor energyConsumptionSensorDescriptor) {
        super(name);
        this.energyConsumptionSensorDescriptor = energyConsumptionSensorDescriptor;
        init();
    }

    private void init(){
        this.gson = new Gson();

        // enable observing and configure notification type
        setObservable(true);
        setObserveType(CoAP.Type.CON);

        getAttributes().setTitle(OBJECT_TITLE);
        getAttributes().addAttribute("rt", "it.unimore.charger.sensor.energy_consumption");
        getAttributes().addAttribute("if", CoreInterfaces.CORE_S.getValue());
        getAttributes().addAttribute("ct", Integer.toString(MediaTypeRegistry.APPLICATION_SENML_JSON));
        getAttributes().addAttribute("ct", Integer.toString(MediaTypeRegistry.TEXT_PLAIN));
    }

    private Optional<String> getJsonSenmlResponse(){

        try {

            SenMLPack senMLPack = new SenMLPack();

            SenMLRecord senMLRecord = new SenMLRecord();
            senMLRecord.setBn(this.energyConsumptionSensorDescriptor.getChargerId());
            senMLRecord.setN("energy consumption");
            senMLRecord.setT(this.energyConsumptionSensorDescriptor.getTimestamp());
            senMLRecord.setBver(this.energyConsumptionSensorDescriptor.getVersion());
            senMLRecord.setV(this.energyConsumptionSensorDescriptor.getValue());
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
            this.energyConsumptionSensorDescriptor.checkEnergyConsumption();

            // if the request specify the MediaType as JSON or JSON+SenML
            if (exchange.getRequestOptions().getAccept() == MediaTypeRegistry.APPLICATION_SENML_JSON ||
                    exchange.getRequestOptions().getAccept() == MediaTypeRegistry.APPLICATION_JSON){

                Optional<String> senmlPayload = getJsonSenmlResponse();

                if (senmlPayload.isPresent())
                    exchange.respond(CoAP.ResponseCode.CONTENT, senmlPayload.get(), exchange.getRequestOptions().getAccept());
                else
                    exchange.respond(CoAP.ResponseCode.INTERNAL_SERVER_ERROR);
            } else
                exchange.respond(CoAP.ResponseCode.CONTENT, String.valueOf(this.energyConsumptionSensorDescriptor.toString()), MediaTypeRegistry.TEXT_PLAIN);

        } catch (Exception e){
            exchange.respond(CoAP.ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }
}
