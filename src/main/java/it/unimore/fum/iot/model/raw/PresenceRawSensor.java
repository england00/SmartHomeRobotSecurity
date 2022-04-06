package it.unimore.fum.iot.model.raw;

import it.unimore.fum.iot.model.general.GeneralDataListener;
import it.unimore.fum.iot.model.general.GeneralDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 06/04/2022 - 01:02
 */
public class PresenceRawSensor extends GeneralDescriptor<Boolean> {

    // sensor's parameters
    private long timestamp;
    private Boolean value; // Boolean class permits null value

    // utility variables
    private boolean activate = true;
    private transient Random random; // this variable mustn't be serialized
    private static final Logger logger = LoggerFactory.getLogger(PresenceRawSensor.class);

    // variables associated to data update
    public static final long UPDATE_PERIOD = 5000;
    private static final long TASK_DELAY_TIME = 5000;

    public PresenceRawSensor(String robotId, Number version, boolean activate) {
        super(robotId, version);
        this.setActivate(activate);
        init();
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Boolean getValue() {
        return value;
    }

    public void setValue(Boolean value) {
        this.value = value;
    }

    public boolean isActivate() {
        return activate;
    }

    public void setActivate(boolean activate) {
        this.activate = activate;
    }

    private void init() {

        try {

            this.random = new Random(System.currentTimeMillis());
            this.timestamp = System.currentTimeMillis();

            startPeriodicEventValueUpdateTask();

        } catch (Exception e){
            logger.error("Error initializing Presence In Camera Stream Sensor! Msg: {}", e.getLocalizedMessage());
        }
    }

    private void startPeriodicEventValueUpdateTask(){

        try{

            logger.info("Starting periodic Update Task with Period: {} ms", UPDATE_PERIOD);

            Timer updateTimer = new Timer();
            updateTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if(isActivate()){
                        int num = random.nextInt(10);
                        value = num == 9; // if num == 9, return true, otherwise is always false
                    } else {
                        value = false;
                    }
                    notifyUpdate(value);
                }
            }, TASK_DELAY_TIME, UPDATE_PERIOD);

            this.timestamp = System.currentTimeMillis();

        } catch (Exception e){
            logger.error("Error executing periodic Battery Level! Msg: {}", e.getLocalizedMessage());
        }
    }

    public void checkPresenceInCameraStream(){
        // managing value
        int num = this.random.nextInt(10);
        this.value = num == 9; // if num == 9, return true, otherwise is always false

        // managing timestamp
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public Boolean loadUpdatedValue() {
        return this.value;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("PresenceRawSensor{");
        sb.append("uuidId='").append(getUuid()).append('\'');
        sb.append(", timestamp=").append(timestamp);
        sb.append(", version=").append(getVersion());
        sb.append(", value=").append(value);
        sb.append('}');
        return sb.toString();
    }

    public static void main(String[] args) {

        PresenceRawSensor rawSensor = new PresenceRawSensor("robot-0001", 0.1, false);
        rawSensor.setActivate(true);
        logger.info("New Resource Created with Id: {} ! {} New Value: {}",
                rawSensor.getUuid(),
                "PresenceRawSensor",
                rawSensor.loadUpdatedValue());

        rawSensor.addDataListener(new GeneralDataListener<Boolean>() {
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
