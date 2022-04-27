package ir.ali.weatherforecast.service.repository

import ir.ali.weatherforecast.model.Weather
import ir.ali.weatherforecast.service.api.WeatherService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor (private val weatherService: WeatherService) {
    suspend fun getWeather(): Weather = weatherService.getWeather()
}
