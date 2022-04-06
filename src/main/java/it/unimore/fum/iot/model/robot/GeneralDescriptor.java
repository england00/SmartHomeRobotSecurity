package it.unimore.fum.iot.model.robot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 05/04/2022 - 12:27
 */
public abstract class GeneralDescriptor<T> {

    // object's parameters
    private static final Logger logger = LoggerFactory.getLogger(GeneralDescriptor.class);
    protected List<GeneralDataListener<T>> generalDataListeners;
    private String uuid;
    private Number version;

    public GeneralDescriptor(String uuid, Number version) {
        this.uuid = uuid;
        this.version = version;
        this.generalDataListeners = new ArrayList<>();
    }

    public abstract T loadUpdatedValue();

    public void addDataListener(GeneralDataListener<T> generalDataListener){
        if(this.generalDataListeners != null)
            this.generalDataListeners.add(generalDataListener);
    }

    public void removeDataListener(GeneralDataListener<T> generalDataListener){
        if(this.generalDataListeners != null && this.generalDataListeners.contains(generalDataListener))
            this.generalDataListeners.remove(generalDataListener);
    }

    protected void notifyUpdate(T updatedValue){
        if(this.generalDataListeners != null && this.generalDataListeners.size() > 0)
            this.generalDataListeners.forEach(robotDataListener -> {
                if(robotDataListener != null)
                    robotDataListener.onDataChanged(this, updatedValue);
            });
        else
            logger.error("Empty or Null Robot Data Listener! Nothing to notify ...");
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Number getVersion() {
        return version;
    }

    public void setVersion(Number version) {
        this.version = version;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("RobotGeneralDescriptor{");
        sb.append("uuid='").append(uuid).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
