package ir.ali.weatherforecast.service.api

import ir.ali.weatherforecast.model.Weather
import retrofit2.http.GET

interface WeatherService {
    @GET("weather/tehran")
    suspend fun getWeather(): Weather
}