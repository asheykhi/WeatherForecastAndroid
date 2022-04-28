package ir.ali.weatherforecast.view.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.ali.weatherforecast.utils.DialogAppear
import ir.ali.weatherforecast.model.Weather
import ir.ali.weatherforecast.service.repository.ProxyRepository
import ir.ali.weatherforecast.service.repository.WeatherRepository
import ir.ali.weatherforecast.utils.Constants.DEFAULT_PROXY_IP
import ir.ali.weatherforecast.utils.Constants.DEFAULT_PROXY_PORT
import ir.ali.weatherforecast.utils.Constants.DEFAULT_PROXY_STATUS_USAGE
import ir.ali.weatherforecast.utils.Constants.DEFAULT_PROXY_TYPE
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val proxyRepo: ProxyRepository
    ) : ViewModel() {

    private val _notifyUser: MutableLiveData<String> = MutableLiveData()
    val notifyUser: LiveData<String> get() = _notifyUser

    private val _weather: MutableLiveData<Weather> = MutableLiveData()
    val weather: LiveData<Weather> get() = _weather

    private val _loading: MutableLiveData<Boolean> = MutableLiveData(false)
    val loading: LiveData<Boolean> get() = _loading


    fun updateLocalProxyData(ip: String, port: Int,status:Boolean, type:String) {
        viewModelScope.launch {
            try {
                proxyRepo.getLocalProxyData().edit().putString("local_ip", ip).apply()
                proxyRepo.getLocalProxyData().edit().putInt("local_port", port).apply()
                proxyRepo.getLocalProxyData().edit().putBoolean("local_sts", status).apply()
                proxyRepo.getLocalProxyData().edit().putString("local_type", type).apply()
            } catch (e: Exception) {
                e.printStackTrace()
                _notifyUser.value = e.message
            } finally {
                _notifyUser.value = "Proxy Updated Successfully"
            }
        }
    }

    fun loadLocalProxyData(dialogAppear: DialogAppear) {

        viewModelScope.launch {
            try {
                val ip = proxyRepo.getLocalProxyData().getString("local_ip", DEFAULT_PROXY_IP)
                val port = proxyRepo.getLocalProxyData().getInt("local_port", DEFAULT_PROXY_PORT)
                val usage = proxyRepo.getLocalProxyData().getBoolean("local_sts", DEFAULT_PROXY_STATUS_USAGE)
                val type = proxyRepo.getLocalProxyData().getString("local_type", DEFAULT_PROXY_TYPE)
                dialogAppear.onDialogAppeared(ip, port, usage, type)
            } catch (e: Exception) {
                e.printStackTrace()
                _notifyUser.value = e.message
            }
        }
    }

    fun loadWeatherForecastData(cityName: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                _weather.value = weatherRepository.getWeather(cityName)
            } catch (e: Exception) {
                e.printStackTrace()
                _notifyUser.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

}