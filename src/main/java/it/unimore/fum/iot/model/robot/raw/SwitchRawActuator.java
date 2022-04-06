package it.unimore.fum.iot.model.robot.raw;

import it.unimore.fum.iot.model.robot.GeneralDataListener;
import it.unimore.fum.iot.model.robot.GeneralDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 06/04/2022 - 10:59
 */
public class SwitchRawActuator extends GeneralDescriptor<Boolean> {

    // actuator's parameters
    private long timestamp;
    private boolean value;

    // utility variables
    private static final Logger logger = LoggerFactory.getLogger(SwitchRawActuator.class);

    public SwitchRawActuator(String robotId, Number version) {
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

    public void switchStatusOn(){
        this.value = true;
        notifyUpdate(isValue());
        this.timestamp = System.currentTimeMillis();
    }

    public void switchStatusOff(){
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
        final StringBuffer sb = new StringBuffer("SwitchRawActuator{");
        sb.append("uuid='").append(getUuid()).append('\'');
        sb.append(", timestamp=").append(timestamp);
        sb.append(", version=").append(getVersion());
        sb.append(", value=").append(value);
        sb.append('}');
        return sb.toString();
    }

    public static void main(String[] args) {

        SwitchRawActuator rawResource = new SwitchRawActuator("robot-0001", 0.1);
        logger.info("New Resource Created with Id: {} ! {} New Value: {}",
                rawResource.getUuid(),
                "SwitchActuator",
                rawResource.loadUpdatedValue());

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    for(int i=0; i<100; i++){
                        if (rawResource.loadUpdatedValue()) {
                            rawResource.switchStatusOff();
                        } else {
                            rawResource.switchStatusOn();
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
