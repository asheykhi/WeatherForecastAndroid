package ir.ali.weatherforecast.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import ir.ali.weatherforecast.utils.Constants.SHARED_PREF;


@Module
@InstallIn(SingletonComponent::class)
object SharedPrefModule {
    @Singleton
    @Provides
    fun provideAppSharedPref(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
    
}