package ir.ali.weatherforecast.utils

import java.net.Proxy

object Constants {
    const val BASE_URL: String = "https://goweather.herokuapp.com/"
    const val PROXY_ENABLE = false
    val PROXY_TYPE:Proxy.Type = Proxy.Type.HTTP
    const val PROXY_IP = "46.243.220.234"
    const val PROXY_PORT = 8118
}