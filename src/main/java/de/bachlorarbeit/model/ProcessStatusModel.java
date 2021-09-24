package de.bachlorarbeit.model;

public class ProcessStatusModel {
    private int percent;
    private String message;

    public ProcessStatusModel(int percent){
        this.percent=percent;
        this.message=percent+" percent of all indicators are already answered";
    }
    public int getAllanswers() {
        return percent;
    }

    public String getMessage() {
        return message;
    }
}
