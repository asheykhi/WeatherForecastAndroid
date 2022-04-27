package ir.ali.weatherforecast.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Forecast(
    @Expose @SerializedName("temperature") val temp: String,
    @Expose val day: String,
    @Expose val wind: String
)