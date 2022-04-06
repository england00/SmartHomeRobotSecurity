package it.unimore.fum.iot.model.robot.raw;

import it.unimore.fum.iot.model.robot.GeneralDataListener;
import it.unimore.fum.iot.model.robot.GeneralDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 06/04/2022 - 02:55
 */
public class ModeRawActuator extends GeneralDescriptor<String> {

    // actuator's parameters
    private long timestamp;
    private String value;

    // utility variables
    private static final Logger logger = LoggerFactory.getLogger(ModeRawActuator.class);

    public ModeRawActuator(String robotId, Number version) {
        super(robotId, version);
        this.value = "STOP";
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void modeStart(){
        this.value = "START";
        notifyUpdate(getValue());
        this.timestamp = System.currentTimeMillis();
    }

    public void modePause(){
        this.value = "PAUSE";
        notifyUpdate(getValue());
        this.timestamp = System.currentTimeMillis();
    }

    public void modeStop(){
        this.value = "STOP";
        notifyUpdate(getValue());
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public String loadUpdatedValue() {
        return value;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ModeRawActuator{");
        sb.append("uuid='").append(getUuid()).append('\'');
        sb.append(", timestamp=").append(timestamp);
        sb.append(", version=").append(getVersion());
        sb.append(", value='").append(value).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public static void main(String[] args) {

        ModeRawActuator rawResource = new ModeRawActuator("robot-0001", 0.1);
        logger.info("New Resource Created with Id: {} ! {} New Value: {}",
                rawResource.getUuid(),
                "ModeActuator",
                rawResource.loadUpdatedValue());

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    for(int i=0; i<100; i++){
                        if (rawResource.getValue().equals("STOP")) {
                            rawResource.modeStart();
                        } else if (rawResource.getValue().equals("START")) {
                            rawResource.modePause();
                        } else {
                            rawResource.modeStop();
                        }
                        Thread.sleep(1000);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();

        rawResource.addDataListener(new GeneralDataListener<String>() {
            @Override
            public void onDataChanged(GeneralDescriptor<String> resource, String updatedValue) {

                if(resource != null && updatedValue != null)
                    logger.info("Device: {} -> New Value Received: {}", resource.getUuid(), updatedValue);
                else
                    logger.error("onDataChanged Callback -> Null Resource or Updated Value !");
            }
        });
    }
}
