package ir.ali.weatherforecast.view.ui

import android.os.Bundle
import android.view.View.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import ir.ali.weatherforecast.databinding.ActivityWeatherBinding
import ir.ali.weatherforecast.model.Weather
import ir.ali.weatherforecast.view.viewModel.WeatherViewModel
import java.lang.Exception

@AndroidEntryPoint
class WeatherActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWeatherBinding
    private val viewModel: WeatherViewModel by viewModels()

    private val errorObserver = Observer<Exception> {
        binding.textView.text = it.stackTraceToString()
    }
    private val dataObserver = Observer<Weather> {
        binding.textView.text = it.toString()
    }
    private val loadingObserver = Observer<Boolean> {
        binding.progressBar.visibility = if (it) VISIBLE else INVISIBLE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupObservers(viewModel)

        binding.fetchBtn.setOnClickListener {
            viewModel.loadWeatherForecastData(binding.tfCityName.text.toString())
        }
    }


    private fun setupObservers(viewModel: WeatherViewModel) {
        viewModel.error.observe(this, errorObserver)
        viewModel.weather.observe(this, dataObserver)
        viewModel.loading.observe(this, loadingObserver)
    }
}