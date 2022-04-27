package ir.ali.weatherforecast.utils

import ir.ali.weatherforecast.utils.Constants.PROXY_ENABLE
import java.net.Proxy

fun proxyStatus(proxy:Proxy) = (if (PROXY_ENABLE) proxy else Proxy.NO_PROXY)!!