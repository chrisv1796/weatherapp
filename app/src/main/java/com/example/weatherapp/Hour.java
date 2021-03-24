
package com.example.weatherapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Hour {

    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("temp_f")
    @Expose
    private Double tempF;
    @SerializedName("condition")
    @Expose
    private Condition__ condition;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Double getTempF() {
        return tempF;
    }

    public void setTempF(Double tempF) {
        this.tempF = tempF;
    }

    public Condition__ getCondition() {
        return condition;
    }

    public void setCondition(Condition__ condition) {
        this.condition = condition;
    }

}
