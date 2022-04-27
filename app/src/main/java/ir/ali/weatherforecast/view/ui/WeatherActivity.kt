package ir.ali.weatherforecast.view.ui

import android.os.Bundle
import android.view.Menu
import android.view.View.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import ir.ali.weatherforecast.R
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
    }

    private fun setupObservers(viewModel: WeatherViewModel) {
        viewModel.error.observe(this, errorObserver)
        viewModel.weather.observe(this, dataObserver)
        viewModel.loading.observe(this, loadingObserver)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        val menuItem = menu?.findItem(R.id.search_bar)
        val searchView = menuItem?.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.loadWeatherForecastData(query.toString())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false

            }

        })
        return super.onCreateOptionsMenu(menu)
    }
}