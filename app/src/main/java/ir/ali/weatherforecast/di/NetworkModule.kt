package ir.ali.weatherforecast.di

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.ali.weatherforecast.service.api.WeatherService
import ir.ali.weatherforecast.utils.Constants.BASE_URL
import ir.ali.weatherforecast.utils.Constants.DEFAULT_PROXY_IP
import ir.ali.weatherforecast.utils.Constants.DEFAULT_PROXY_PORT
import ir.ali.weatherforecast.utils.Constants.DEFAULT_PROXY_STATUS_USAGE
import ir.ali.weatherforecast.utils.Constants.DEFAULT_PROXY_TYPE
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.InetSocketAddress
import java.net.Proxy
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideGsonBuilder(): Gson =
        GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create()

    @Singleton
    @Provides
    fun provideOkHttpClient(proxy: Proxy, shp: SharedPreferences): OkHttpClient {
        val flag = shp.getBoolean("local_sts", DEFAULT_PROXY_STATUS_USAGE)
        return OkHttpClient.Builder()
            .proxy(if (flag) proxy else Proxy.NO_PROXY)
            .build()
    }

    @Singleton
    @Provides
    fun provideProxy(socketAddress: InetSocketAddress, proxyType: Proxy.Type): Proxy =
        Proxy(proxyType, socketAddress)

    @Singleton
    @Provides
    fun provideProxyType(shp: SharedPreferences): Proxy.Type =
        if (shp.getString("local_type", DEFAULT_PROXY_TYPE).equals(DEFAULT_PROXY_TYPE)
        ) Proxy.Type.HTTP else Proxy.Type.SOCKS


    @Singleton
    @Provides
    fun provideSocketAddress(shp: SharedPreferences): InetSocketAddress {
        val ip = shp.getString("local_ip", DEFAULT_PROXY_IP)
        val port = shp.getInt("local_port", DEFAULT_PROXY_PORT)
        return InetSocketAddress.createUnresolved(ip, port)
    }

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson, client: OkHttpClient): Retrofit.Builder =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))

    @Singleton
    @Provides
    fun provideWeatherService(retrofit: Retrofit.Builder): WeatherService =
        retrofit
            .build()
            .create(WeatherService::class.java)
}