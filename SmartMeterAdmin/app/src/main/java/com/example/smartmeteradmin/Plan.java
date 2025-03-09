package com.example.smartmeteradmin;

public class Plan {

    String planName;
    float units;
    float amount;


    public Plan(String planName, float units, float amount) {
        this.planName = planName;
        this.units = units;
        this.amount = amount;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public float getUnits() {
        return units;
    }

    public void setUnits(float units) {
        this.units = units;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }
}
