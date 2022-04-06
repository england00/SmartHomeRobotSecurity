package it.unimore.fum.iot.model.general;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 05/04/2022 - 12:30
 */
public interface GeneralDataListener<T> {

    public void onDataChanged(GeneralDescriptor<T> resource, T updatedValue);

}
