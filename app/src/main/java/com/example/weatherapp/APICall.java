package com.example.weatherapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APICall {

    @GET("current.json?key=0707ff189ef94a7bb9e133029212203&q=")
    Call<WeatherModel> getWeather(@Query("q") String latLong);

}
