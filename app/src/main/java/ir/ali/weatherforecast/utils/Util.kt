package ir.ali.weatherforecast.utils

import android.util.Range
import ir.ali.weatherforecast.R
import java.text.SimpleDateFormat
import java.util.*


fun getWall(desc: String): Int = when (desc) {
    "Clear" -> R.drawable.wall_0
    "Heavy rain" -> R.drawable.wall_1
    "Light drizzle" -> R.drawable.wall_2
    "Light rain shower" -> R.drawable.wall_3
    "Light rain" -> R.drawable.wall_4
    "Moderate or heavy rain shower" -> R.drawable.wall_5
    "Partly cloudy" -> R.drawable.wall_6
    "Patchy rain possible" -> R.drawable.wall_7
    "Sunny" -> R.drawable.wall_8
    "Thundery outbreaks possible" -> R.drawable.wall_9
    "Windy" -> R.drawable.wall_10
    else -> R.drawable.wall_10
}

fun getDaysArray(): ArrayList<String> {
    val sdf = SimpleDateFormat("EEEE dd-MMM-yyyy", Locale.getDefault())
    val d = arrayListOf<String>()
    for (i in 1..3) {
        val calendar: Calendar = GregorianCalendar()
        calendar.add(Calendar.DATE, i)
        val day: String = sdf.format(calendar.time)
        d.add(day)
    }
    return d
}
