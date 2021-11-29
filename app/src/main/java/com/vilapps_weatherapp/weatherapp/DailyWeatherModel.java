package com.vilapps_weatherapp.weatherapp;

import org.w3c.dom.Text;

public class DailyWeatherModel {
    String date;
    String precipChance;
    String highTemp;
    String lowTemp;
    String weatherIcon;

    public String getWeatherIcon() {
        return weatherIcon;
    }

    public void setWeatherIcon(String weatherIcon) {
        this.weatherIcon = weatherIcon;
    }

    public DailyWeatherModel(String date, String highTemp, String lowTemp, String precipChance, String weatherIcon) {
        this.date = date;
        this.highTemp = highTemp;
        this.lowTemp = lowTemp;
        this.precipChance = precipChance;
        this.weatherIcon = weatherIcon;

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPrecipChance() {
        return precipChance;
    }

    public void setPrecipChance(String precipChance) {
        this.precipChance = precipChance;
    }

    public String getHighTemp() {
        return highTemp;
    }

    public void setHiTemp(String hiTemp) {
        this.highTemp = hiTemp;
    }

    public String getLowTemp() {
        return lowTemp;
    }

    public void setLowTemp(String lowTemp) {
        this.lowTemp = lowTemp;
    }
}
