package it.unimore.fum.iot.model.raw;

import it.unimore.fum.iot.model.general.GeneralDataListener;
import it.unimore.fum.iot.model.general.GeneralDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 05/04/2022 - 16:53
 */
public class IndoorPositionRawSensor extends GeneralDescriptor<Double[]> {

    // sensor's parameters
    private long timestamp = System.currentTimeMillis();
    private Double[] position = new Double[2]; // 2 values array
    private String unit = "meter";

    // utility variables
    private transient Random random; // this variable mustn't be serialized
    private static final Logger logger = LoggerFactory.getLogger(IndoorPositionRawSensor.class);
    private final double[] roomDimensions; // 2 values array
    private final double[] origin; // offset from the robot begin
    private static final double SPEED = 0.00005; // 50 cm/s
    private double[] chargerPosition;
    private boolean returnFlag = false;

    // variables associated to data update
    public static final long UPDATE_PERIOD = 1000;
    private static final long TASK_DELAY_TIME = 1000;

    public IndoorPositionRawSensor(String robotId, Number version, double[] roomDimensions, double[] origin) {
        super(robotId, version);
        this.roomDimensions = roomDimensions;
        this.origin = origin;
        init();
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Double[] getPosition() {
        return position;
    }

    public void setPosition(Double[] position) {
        this.position = position;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Random getRandom() {
        return random;
    }

    public double[] getRoomDimensions() {
        return roomDimensions;
    }

    public double[] getOrigin() {
        return origin;
    }

    public double[] getChargerPosition() {
        return chargerPosition;
    }

    // set by the RETURN HOME ACTUATOR
    public void setChargerPosition(double[] chargerPosition) {
        this.chargerPosition = chargerPosition;
    }

    public boolean isReturnFlag() {
        return returnFlag;
    }

    // set by the RETURN HOME ACTUATOR
    public void setReturnFlag(boolean returnFlag) {
        this.returnFlag = returnFlag;
    }

    public void init() {

        try {

            this.random = new Random(System.currentTimeMillis());
            this.position[0] = 0.0;
            this.position[1] = 0.0;
            this.timestamp = System.currentTimeMillis();

            startPeriodicEventValueUpdateTask();

        } catch (Exception e){
            logger.error("Error initializing Indoor Position Sensor! Msg: {}", e.getLocalizedMessage());
        }
    }

    private void startPeriodicEventValueUpdateTask(){

        try{

            logger.info("Starting periodic Update Task with Period: {} ms", UPDATE_PERIOD);

            Timer updateTimer = new Timer();
            updateTimer.schedule(new TimerTask() {
                @Override
                public void run() {

                    if(!returnFlag){

                        // max distance traveled during time
                        double distance = (SPEED * (System.currentTimeMillis() - timestamp));
                        if (distance == 0.0) {
                            distance = 1.0;
                        }

                        // simulating normal robot's moves inside the dimensions of the room
                        boolean sign;
                        boolean control = false;

                        // obtaining x
                        while (!control) {
                            sign = random.nextBoolean();
                            if (!sign) {
                                position[0] -= random.nextDouble(distance);
                            } else {
                                position[0] += random.nextDouble(distance);
                            }
                            if (position[0] < (roomDimensions[0] + origin[0]) && position[0] > origin[0]) {
                                control = true;
                            }
                        }

                        // obtaining y
                        while (control) {
                            sign = random.nextBoolean();
                            if (!sign) {
                                position[1] -= random.nextDouble(distance);
                            } else {
                                position[1] += random.nextDouble(distance);
                            }
                            if (position[1] < (roomDimensions[1] + origin[1]) && position[1] > origin[1]) {
                                control = false;
                            }
                        }

                    } else {

                        // simulating robot's returning home moves inside the dimensions of the room
                        // angular coefficient and q
                        double m = (position[1] - chargerPosition[1]) / (position[0] - chargerPosition[0]);
                        double q = (chargerPosition[1] - m * chargerPosition[0]);

                        // effective distance from destination
                        double dDestination = Math.pow((Math.pow((position[1] - chargerPosition[1]), 2) +
                                Math.pow((position[0] - chargerPosition[0]), 2)), 0.5);

                        if (dDestination > 0.001) {
                            if (position[0] > chargerPosition[0]) {
                                position[0] -= random.nextDouble(position[0] - chargerPosition[0]);
                            } else {
                                position[0] += random.nextDouble(chargerPosition[0] - position[0]);
                            }
                            position[1] = position[0] * m + q;
                        } else {
                            position[0] = chargerPosition[0];
                            position[1] = chargerPosition[1];
                        }
                    }

                    timestamp = System.currentTimeMillis();
                    notifyUpdate(position);

                }
            }, TASK_DELAY_TIME, UPDATE_PERIOD);

        } catch (Exception e){
            logger.error("Error executing periodic Indoor Position! Msg: {}", e.getLocalizedMessage());
        }
    }

    @Override
    public Double[] loadUpdatedValue() {
        return this.position;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("IndoorPositionRawSensor{");
        sb.append("uuid='").append(getUuid()).append('\'');
        sb.append(", timestamp=").append(timestamp);
        sb.append(", version=").append(getVersion());
        sb.append(", position=").append(Arrays.toString(position));
        sb.append(", unit='").append(unit).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public static void main(String[] args) {

        IndoorPositionRawSensor rawSensor = new IndoorPositionRawSensor("robot-0001", 0.1, new double[] {15.0, 20.0}, new double[] {3.0, 4.0});
        rawSensor.setReturnFlag(false);
        rawSensor.setChargerPosition(new double[] {1.5, 2.7});
        logger.info("New Resource Created with Id: {} ! {} New Value: {}",
                rawSensor.getUuid(),
                "IndoorPositionSensor",
                rawSensor.loadUpdatedValue());

        rawSensor.addDataListener(new GeneralDataListener<Double[]>() {
            @Override
            public void onDataChanged(GeneralDescriptor<Double[]> resource, Double[] updatedValue) {

                if(resource != null && updatedValue != null)
                    logger.info("Device: {} -> New Value Received: {}", resource.getUuid(), updatedValue);
                else
                    logger.error("onDataChanged Callback -> Null Resource or Updated Value !");
            }
        });
    }
}
