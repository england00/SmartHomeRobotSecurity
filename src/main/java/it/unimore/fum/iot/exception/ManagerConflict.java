package it.unimore.fum.iot.exception;

/**
 * @author Luca Inghilterra, 271359@studenti.unimore.it
 * @project SMART-HOME-robot-security
 * @created 23/03/2022 - 17:43
 */
public class ManagerConflict extends Exception {

    public ManagerConflict(String errorMessage){
        super(errorMessage);
    }
}
