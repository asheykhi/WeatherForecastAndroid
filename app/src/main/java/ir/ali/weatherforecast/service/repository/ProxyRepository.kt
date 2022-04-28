package ir.ali.weatherforecast.service.repository

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProxyRepository @Inject constructor(private val localProxyData: SharedPreferences) {
    fun getLocalProxyData(): SharedPreferences = localProxyData
}