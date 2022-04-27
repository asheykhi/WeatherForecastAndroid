package ir.ali.weatherforecast.view.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.ali.weatherforecast.model.Weather
import ir.ali.weatherforecast.service.repository.WeatherRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(private val weatherRepository: WeatherRepository) : ViewModel() {

    private val _weather: MutableLiveData<Weather> = MutableLiveData()
    val weather: LiveData<Weather> get() = _weather

    private val _loading: MutableLiveData<Boolean> = MutableLiveData(false)
    val loading: LiveData<Boolean> get() = _loading

    private val _error: MutableLiveData<Exception> = MutableLiveData()
    val error: LiveData<Exception> get() = _error

    fun loadWeatherForecastData(cityName:String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                _weather.value = weatherRepository.getWeather(cityName)
            } catch (e: Exception) {
                _error.value = e
                e.printStackTrace()
            } finally {
                _loading.value = false
            }
        }
    }

}