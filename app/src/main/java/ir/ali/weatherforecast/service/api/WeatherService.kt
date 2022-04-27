package ir.ali.weatherforecast.service.api

import ir.ali.weatherforecast.model.Weather
import retrofit2.http.GET
import retrofit2.http.Path

interface WeatherService {
    @GET("weather/{city}")
    suspend fun getWeather(@Path("city") city: String): Weather
}