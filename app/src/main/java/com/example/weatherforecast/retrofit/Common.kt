package com.example.weatherforecast.retrofit

object Common {
    private const val WEATHER_URL = "https://api.openweathermap.org"

    val retrofitServiceWeather: OpenApi
        get() = RetrofitClient.getClient(WEATHER_URL).create(OpenApi::class.java)
}