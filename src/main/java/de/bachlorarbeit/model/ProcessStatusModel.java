package de.bachlorarbeit.model;

public class ProcessStatusModel {
    private int percent;
    private String message;

    public ProcessStatusModel(int percent){
        this.percent=percent;
        this.message=percent+" percent of all indicators already have an indicator-value";
    }
    public int getPercent() {
        return percent;
    }

    public String getMessage() {
        return message;
    }
}
