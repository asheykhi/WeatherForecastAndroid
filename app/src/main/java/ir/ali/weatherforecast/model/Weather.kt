package ir.ali.weatherforecast.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Weather(
    @Expose @SerializedName("description") var desc: String,
    @Expose @SerializedName("temperature")  val temp: String,
    @Expose @SerializedName("wind") val wind: String,
    @Expose val forecast: List<Forecast>
)