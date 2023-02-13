package com.example.uitscuisine;

public class Step {
    private String step;
    private int numStep;

    public Step(String step, int numStep) {
        this.step = step;
        this.numStep = numStep;
    }

    public String getStep() {
        return step;
    }
    public Step(String step){
        this.step = step;
    }
    public void setStep(String step) {
        this.step = step;
    }

    public int getNumStep() {
        return numStep;
    }

    public void setNumStep(int numStep) {
        this.numStep = numStep;
    }
}
