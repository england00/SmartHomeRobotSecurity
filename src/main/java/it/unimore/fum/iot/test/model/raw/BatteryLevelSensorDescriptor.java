package it.unimore.fum.iot.test.model.raw;

import it.unimore.fum.iot.test.model.IBatteryLevelSensorDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 13/03/2022 - 18:19
 */
public class BatteryLevelSensorDescriptor implements IBatteryLevelSensorDescriptor {

    // sensor's parameters
    private String robotId;
    private long timestamp;
    private Number version;
    private double batteryLevel;
    private String unit = "%";

    // utility variables
    private transient Random random; // this variable mustn't be serialized
    private static final Logger logger = LoggerFactory.getLogger(BatteryLevelSensorDescriptor.class);

    // variables associated to data update
    public static final long UPDATE_PERIOD = 5000;
    private static final long TASK_DELAY_TIME = 5000;

    public BatteryLevelSensorDescriptor(String robotId, Number version) {
        this.robotId = robotId;
        this.version = version;
        init();
    }

    private void init() {

        try {

            this.random = new Random(System.currentTimeMillis());
            this.batteryLevel = 100.0;
            this.timestamp = System.currentTimeMillis();

            startPeriodicEventValueUpdateTask();

        } catch (Exception e){
            logger.error("Error initializing the IoT Resource! Msg: {}", e.getLocalizedMessage());
        }
    }

    private void startPeriodicEventValueUpdateTask(){

        try{

            logger.info("Starting periodic Update Task with Period: {} ms", UPDATE_PERIOD);

            Timer updateTimer = new Timer();
            updateTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if(batteryLevel > 0.0){
                        batteryLevel = batteryLevel - (random.nextDouble() * 5.0);
                    } else {
                        batteryLevel = 0.0;
                    }
                }
            }, TASK_DELAY_TIME, UPDATE_PERIOD);

            this.timestamp = System.currentTimeMillis();

        } catch (Exception e){
            logger.error("Error executing periodic resource value ! Msg: {}", e.getLocalizedMessage());
        }
    }

    @Override
    public String getRobotId() {
        return robotId;
    }

    public void setRobotId(String robotId) {
        this.robotId = robotId;
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public Number getVersion() {
        return version;
    }

    public void setVersion(Number version) {
        this.version = version;
    }

    @Override
    public double getBatteryLevel() {
        return batteryLevel;
    }

    @Override
    // reset on 100.0 by the CHARGING STATION
    public void setBatteryLevel(double batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    @Override
    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public void checkBatteryLevel(){
        // managing battery values
        this.batteryLevel = this.batteryLevel - (this.random.nextDouble() * 5.0);
        if (this.batteryLevel <= 0) {
            this.batteryLevel = 0.0;
        }

        // managing timestamp
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("BatteryLevelSensorDescriptor{");
        sb.append("robotId='").append(robotId).append('\'');
        sb.append(", timestamp=").append(timestamp);
        sb.append(", version=").append(version);
        sb.append(", batteryLevel=").append(batteryLevel);
        sb.append(", unit='").append(unit).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
