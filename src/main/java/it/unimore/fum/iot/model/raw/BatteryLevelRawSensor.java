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
 * @created 05/04/2022 - 12:52
 */
public class BatteryLevelRawSensor extends GeneralDescriptor<Double> {

    // sensor's parameters
    private long timestamp;
    private double batteryLevel;
    private String unit = "%";

    // utility variables
    private transient Random random; // this variable mustn't be serialized
    private static final Logger logger = LoggerFactory.getLogger(BatteryLevelRawSensor.class);
    private boolean recharging = false;

    // variables associated to data update
    public static final long UPDATE_PERIOD = 5000;
    private static final long TASK_DELAY_TIME = 5000;

    public BatteryLevelRawSensor(String robotId, Number version, boolean recharging) {
        super(robotId, version);
        this.setRecharging(recharging);
        init();
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double getBatteryLevel() {
        return batteryLevel;
    }

    // reset on 100.0 by the CHARGING STATION
    public void setBatteryLevel(double batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public boolean isRecharging() {
        return recharging;
    }

    public void setRecharging(boolean recharging) {
        this.recharging = recharging;
    }

    private void init() {

        try {

            this.random = new Random(System.currentTimeMillis());
            this.timestamp = System.currentTimeMillis();

            if (!this.recharging) {
                this.batteryLevel = 100.0;
            } else {
                this.batteryLevel = 0.0;
            }

            startPeriodicEventValueUpdateTask();

        } catch (Exception e){
            logger.error("Error initializing Battery Level Sensor! Msg: {}", e.getLocalizedMessage());
        }
    }

    private void startPeriodicEventValueUpdateTask(){

        try{

            logger.info("Starting periodic Update Task with Period: {} ms", UPDATE_PERIOD);

            Timer updateTimer = new Timer();
            updateTimer.schedule(new TimerTask() {
                @Override
                public void run() {

                    if (!isRecharging()) {
                        if(batteryLevel > 0.0){
                            batteryLevel = batteryLevel - (random.nextDouble() * 5.0);
                            if (batteryLevel < 0.0) {
                                batteryLevel = 0.0;
                            }
                        } else {
                            batteryLevel = 0.0;
                        }
                    } else {
                        if(batteryLevel < 100.0){
                            batteryLevel = batteryLevel + (random.nextDouble() * 5.0);
                            if (batteryLevel > 100.0) {
                                batteryLevel = 100.0;
                            }
                        } else {
                            batteryLevel = 100.0;
                        }
                    }


                    notifyUpdate(batteryLevel);
                }
            }, TASK_DELAY_TIME, UPDATE_PERIOD);

            this.timestamp = System.currentTimeMillis();

        } catch (Exception e){
            logger.error("Error executing periodic Battery Level! Msg: {}", e.getLocalizedMessage());
        }
    }

    @Override
    public Double loadUpdatedValue() {
        return this.batteryLevel;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("BatteryLevelRawSensor{");
        sb.append("uuid='").append(getUuid()).append('\'');
        sb.append(", timestamp=").append(timestamp);
        sb.append(", version=").append(getVersion());
        sb.append(", batteryLevel=").append(batteryLevel);
        sb.append(", unit='").append(unit).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public static void main(String[] args) {

        BatteryLevelRawSensor rawSensor = new BatteryLevelRawSensor("robot-0001", 0.1, false);
        logger.info("New Resource Created with Id: {} ! {} New Value: {}",
                rawSensor.getUuid(),
                "BatteryLevelSensor",
                rawSensor.loadUpdatedValue());

        rawSensor.addDataListener(new GeneralDataListener<Double>() {
            @Override
            public void onDataChanged(GeneralDescriptor<Double> resource, Double updatedValue) {

                if(resource != null && updatedValue != null)
                    logger.info("Device: {} -> New Value Received: {}", resource.getUuid(), updatedValue);
                else
                    logger.error("onDataChanged Callback -> Null Resource or Updated Value !");
            }
        });
    }
}
