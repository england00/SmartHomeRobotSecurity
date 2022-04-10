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
 * @created 06/04/2022 - 22:12
 */
public class EnergyConsumptionRawSensor extends GeneralDescriptor<Double> {

    // sensor's parameters
    private long timestamp;
    private double value;
    private String unit = "kWh";

    // utility variables
    private transient Random random; // this variable mustn't be serialized
    private static final Logger logger = LoggerFactory.getLogger(EnergyConsumptionRawSensor.class);
    private boolean active = true;

    // variables associated to data update
    public static final long UPDATE_PERIOD = 5000;
    private static final long TASK_DELAY_TIME = 5000;

    //kWh - kilowatt-hour
    private static final double MIN_ENERGY_VALUE = 0.5;
    private static final double MAX_ENERGY_VALUE = 1.0;
    private static final double MIN_ENERGY_VARIATION = 0.1;
    private static final double MAX_ENERGY_VARIATION = 0.5;

    public EnergyConsumptionRawSensor(String robotId, Number version) {
        super(robotId, version);
        init();
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    private void init() {

        try {

            this.random = new Random(System.currentTimeMillis());
            this.value = MIN_ENERGY_VALUE + this.random.nextDouble()*(MAX_ENERGY_VALUE - MIN_ENERGY_VALUE);
            this.timestamp = System.currentTimeMillis();


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

                    if(isActive()){
                        double variation = (MIN_ENERGY_VARIATION + MAX_ENERGY_VARIATION *random.nextDouble()) * (random.nextDouble() > 0.5 ? 1.0 : -1.0);
                        value = value + variation;
                    } else {
                        value = 0.0;
                    }

                    timestamp = System.currentTimeMillis();
                    notifyUpdate(value);
                }
            }, TASK_DELAY_TIME, UPDATE_PERIOD);

        } catch (Exception e){
            logger.error("Error executing periodic Battery Level! Msg: {}", e.getLocalizedMessage());
        }
    }

    @Override
    public Double loadUpdatedValue() {
        return this.value;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("EnergyConsumptionSensorDescriptor{");
        sb.append("uuid='").append(getUuid()).append('\'');
        sb.append("timestamp=").append(timestamp);
        sb.append(", version=").append(getVersion());
        sb.append(", value=").append(value);
        sb.append(", unit='").append(unit).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public static void main(String[] args) {

        EnergyConsumptionRawSensor rawResource = new EnergyConsumptionRawSensor("charger-0001", 0.1);
        rawResource.setActive(true);
        logger.info("New Resource Created with Id: {} ! {} New Value: {}",
                rawResource.getUuid(),
                "EnergyConsumptionRawSensor",
                rawResource.loadUpdatedValue());

        rawResource.addDataListener(new GeneralDataListener<Double>() {
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
