package com.example.weatherapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APICall {

    @GET("current.json?key=  ENTER API KEY  &q=")
    Call<WeatherModel> getWeather(@Query("q") String latLong);

}
