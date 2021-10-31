package com.example.weatherforecast.retrofit

import retrofit2.Call
import com.example.weatherforecast.models.weather.WeatherParameters
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenApi {
    @GET("/data/2.5/weather")
    fun getWeatherForecast(@Query("q") name: String, @Query("appid") appid: String): Call<WeatherParameters>
}