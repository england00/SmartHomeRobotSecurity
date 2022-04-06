package it.unimore.fum.iot.model.raw;

import it.unimore.fum.iot.model.general.GeneralDataListener;
import it.unimore.fum.iot.model.general.GeneralDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Arrays;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 06/04/2022 - 12:41
 */
public class ReturnHomeRawActuator extends GeneralDescriptor<Boolean> {

    // actuator's parameters
    private long timestamp;
    private boolean value;
    private double[] chargerPosition = null;
    private String unit = "meter";

    // utility variables
    private static final Logger logger = LoggerFactory.getLogger(ReturnHomeRawActuator.class);

    public ReturnHomeRawActuator(String robotId, Number version) {
        super(robotId, version);
        this.value = false;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    public double[] getChargerPosition() {
        return chargerPosition;
    }

    public void setChargerPosition(double[] chargerPosition) {
        this.chargerPosition = chargerPosition;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void switchReturnOn(){
        this.value = true;
        notifyUpdate(isValue());
        this.timestamp = System.currentTimeMillis();
    }

    public void switchReturnOff(){
        this.value = false;
        notifyUpdate(isValue());
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public Boolean loadUpdatedValue() {
        return value;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ReturnHomeRawActuator{");
        sb.append("uuid='").append(getUuid()).append('\'');
        sb.append(", timestamp=").append(timestamp);
        sb.append(", version=").append(getVersion());
        sb.append(", value=").append(value);
        sb.append(", chargerPosition=").append(Arrays.toString(chargerPosition));
        sb.append(", unit='").append(unit).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public static void main(String[] args) {

        ReturnHomeRawActuator rawResource = new ReturnHomeRawActuator("robot-0001", 0.1);
        logger.info("New Resource Created with Id: {} ! {} New Value: {}",
                rawResource.getUuid(),
                "ReturnHomeActuator",
                rawResource.loadUpdatedValue());

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    for(int i=0; i<100; i++){
                        if (rawResource.loadUpdatedValue()) {
                            rawResource.switchReturnOff();
                        } else {
                            rawResource.switchReturnOn();
                        }
                        Thread.sleep(1000);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();

        rawResource.addDataListener(new GeneralDataListener<Boolean>() {
            @Override
            public void onDataChanged(GeneralDescriptor<Boolean> resource, Boolean updatedValue) {

                if(resource != null && updatedValue != null)
                    logger.info("Device: {} -> New Value Received: {}", resource.getUuid(), updatedValue);
                else
                    logger.error("onDataChanged Callback -> Null Resource or Updated Value !");
            }
        });
    }
}
