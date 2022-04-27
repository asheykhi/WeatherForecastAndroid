package ir.ali.weatherforecast.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.ali.weatherforecast.service.api.WeatherService
import ir.ali.weatherforecast.utils.Constants.BASE_URL
import ir.ali.weatherforecast.utils.Constants.PROXY_IP
import ir.ali.weatherforecast.utils.Constants.PROXY_PORT
import ir.ali.weatherforecast.utils.Constants.PROXY_TYPE
import ir.ali.weatherforecast.utils.proxyStatus
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
    fun provideOkHttpClient(proxy: Proxy): OkHttpClient =
        OkHttpClient.Builder()
            .proxy(proxyStatus(proxy))
            .build()

    @Singleton
    @Provides
    fun provideProxy(socketAddress: InetSocketAddress,): Proxy =
        Proxy(PROXY_TYPE, socketAddress)

    @Singleton
    @Provides
    fun provideSocketAddress(): InetSocketAddress =
        InetSocketAddress.createUnresolved(PROXY_IP, PROXY_PORT)

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