package com.example.group5.fitnessapp;

public class StepInformation {
    public String step;
    public String calories;
    public String monday ;
    public String tuesday ;
    public String wednesday;
    public String thursday ;
    public String friday ;
    public String saturday ;
    public String sunday ;

    public StepInformation(String step) {

        this.step = step;
        calories = "0"; //TODO pass calories into the constructor
        monday = step;
        tuesday=  step ;
        wednesday= step;
        thursday = step;
        friday = step;
        saturday = step;
        sunday = step;

    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }
}
