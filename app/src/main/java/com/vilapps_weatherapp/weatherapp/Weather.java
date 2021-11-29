package com.vilapps_weatherapp.weatherapp;

public class Weather {
    int temp;
    int humidity;
    int feelsLike;
    int hour;
    int img;



//    public Weather(int temp, int humidity, int feelsLike) {
//        this.temp = temp;
//        this.humidity = humidity;
//        this.feelsLike = feelsLike;
//    }


    public Weather(int temp, int hour, int img) {
        this.temp = temp;
        this.hour = hour;
        this.img = img;
    }



    public Weather() {

    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getFeelsLike() {
        return feelsLike;
    }

    public void setFeelsLike(int feelsLike) {
        this.feelsLike = feelsLike;
    }

    public int getHour() {
        return hour;
    }

    public int getImg() {
        return img;
    }




}
