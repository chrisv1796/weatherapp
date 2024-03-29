
package com.vilapps_weatherapp.weatherapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Hour implements Serializable {

    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("temp_f")
    @Expose
    private Double tempF;
    @SerializedName("condition")
    @Expose
    private Condition__ condition;
    @SerializedName("will_it_rain")
    @Expose
    private int willItRain;



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

    public int getWillItRain() {
        return willItRain;
    }

    public void setWillItRain(int willItRain) {
        this.willItRain = willItRain;
    }

}
