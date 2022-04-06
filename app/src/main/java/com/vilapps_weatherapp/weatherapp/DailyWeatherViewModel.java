package com.vilapps_weatherapp.weatherapp;

import java.io.Serializable;
import java.util.List;

public class DailyWeatherViewModel implements Serializable {
    String name;
    String date;
    List<Hour> hourWeatherlist;
    Double humidity;
    String sunrise;
    String sunset;
    Double windSpeedDir;
    Double visibility;

    public Double getUv() {
        return uv;
    }

    public void setUv(Double uv) {
        this.uv = uv;
    }

    Double uv;

    public DailyWeatherViewModel(String name, String date, Double humidity, String sunrise, String sunset, Double windSpeedDir, Double visibility, Double uv, List<Hour> hourWeatherlist) {
        this.name = name;
        this.date = date;
        this.humidity = humidity;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.windSpeedDir = windSpeedDir;
        this.visibility = visibility;
        this.uv = uv;
        this.hourWeatherlist = hourWeatherlist;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Hour> getHourWeatherlist() {
        return hourWeatherlist;
    }

    public void setHourWeatherlist(List<Hour> hourWeatherlist) {
        this.hourWeatherlist = hourWeatherlist;
    }

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public String getSunrise() {
        return sunrise;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }

    public Double getWindSpeedDir() {
        return windSpeedDir;
    }

    public void setWindSpeedDir(Double windSpeedDir) {
        this.windSpeedDir = windSpeedDir;
    }

    public Double getVisibility() {
        return visibility;
    }

    public void setVisibility(Double visibility) {
        this.visibility = visibility;
    }
}
