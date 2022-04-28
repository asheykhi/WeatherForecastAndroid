package ir.ali.weatherforecast.view.ui

import android.graphics.drawable.Drawable
import android.graphics.drawable.TransitionDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.AutoCompleteTextView
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import com.google.android.material.switchmaterial.SwitchMaterial
import dagger.hilt.android.AndroidEntryPoint
import ir.ali.weatherforecast.R
import ir.ali.weatherforecast.databinding.ActivityWeatherBinding
import ir.ali.weatherforecast.model.Weather
import ir.ali.weatherforecast.utils.Constants
import ir.ali.weatherforecast.utils.DialogAppear
import ir.ali.weatherforecast.utils.getDaysArray
import ir.ali.weatherforecast.utils.getWall
import ir.ali.weatherforecast.view.viewModel.WeatherViewModel
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class WeatherActivity : AppCompatActivity(), DialogAppear {

    private lateinit var binding: ActivityWeatherBinding
    private val viewModel: WeatherViewModel by viewModels()
    private lateinit var inputIpWidget: AutoCompleteTextView
    private lateinit var inputPortWidget: AutoCompleteTextView
    private lateinit var proxyUsageToggleWidget: SwitchMaterial
    private lateinit var proxyTypeWidget: RadioGroup

    private val dataObserver = Observer<Weather> { weather ->

        setUiVisibilities(true)

        binding.container.tvTemperature.text = weather.temp
        binding.container.tvDescription.text = weather.desc
        binding.container.tvWind.text = weather.wind

        /** can provide by di */
        val currentTime: Date = Calendar.getInstance().time
        val s = android.text.format.DateFormat.format("EEEE", currentTime)

        val days = getDaysArray()

        /*binding.container.included.tvForecastDay1.text = weather.forecast[0].day
        binding.container.included.tvForecastDay2.text = weather.forecast[1].day
        binding.container.included.tvForecastDay3.text = weather.forecast[2].day*/

        binding.container.included.tvForecastDay1.text = days[0]
        binding.container.included.tvForecastDay2.text = days[1]
        binding.container.included.tvForecastDay3.text = days[2]
        binding.container.tvToday.text = s

        binding.container.included.tvForecastTemp1.text = weather.forecast[0].temp
        binding.container.included.tvForecastTemp2.text = weather.forecast[1].temp
        binding.container.included.tvForecastTemp3.text = weather.forecast[2].temp

        binding.container.included.tvForecastWind1.text = weather.forecast[0].wind
        binding.container.included.tvForecastWind2.text = weather.forecast[1].wind
        binding.container.included.tvForecastWind3.text = weather.forecast[2].wind


        val currentBG: Drawable? = binding.root.background
        val resId = getWall(weather.desc)
        val nextBG: Drawable? = AppCompatResources.getDrawable(this, resId)
        val array = arrayOf(currentBG, nextBG)
        val transition = TransitionDrawable(array)
        binding.root.background = transition
        transition.startTransition(1500)

    }
    private val loadingObserver = Observer<Boolean> {
        binding.progressBar.visibility = if (it) VISIBLE else INVISIBLE
    }
    private val userNotifier = Observer<String> {
        notifyUser(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupObservers(viewModel)
        setUiVisibilities(false)
    }

    /** This dialog will provide by di in future */
    private fun initializeAlertDialog() {

        val dialogLayout = LayoutInflater.from(this)
            .inflate(R.layout.dialog_proxy, null, false)

        inputIpWidget = dialogLayout.findViewById(R.id.input_ip)
        inputPortWidget = dialogLayout.findViewById(R.id.input_port)
        proxyUsageToggleWidget = dialogLayout.findViewById(R.id.proxy_toggle_switch)
        proxyTypeWidget = dialogLayout.findViewById(R.id.toggle)

        AlertDialog.Builder(this)
            .setTitle("support: SOCKS / HTTP")
            .setView(dialogLayout)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                if (inputIpWidget.text.isNotEmpty() && inputPortWidget.text.isNotEmpty()) {
                    viewModel.updateLocalProxyData(
                        inputIpWidget.text.toString(),
                        inputPortWidget.text.toString().toInt(),
                        proxyUsageToggleWidget.isChecked,
                        if (proxyTypeWidget.checkedRadioButtonId == R.id.rb_http) "HTTP" else "SOCKS"
                    )
                } else {
                    notifyUser("please fill all fields ")
                }
            }
            .setNegativeButton(android.R.string.cancel) { dI, _ ->
                dI.dismiss()
            }
            .show()

    }

    private fun setupObservers(viewModel: WeatherViewModel) {
        viewModel.weather.observe(this, dataObserver)
        viewModel.loading.observe(this, loadingObserver)
        viewModel.notifyUser.observe(this, userNotifier)
    }

    private fun setUiVisibilities(flag: Boolean) {
        val visibility = if (flag) VISIBLE else INVISIBLE
        binding.container.root.visibility = visibility
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        val searchMenuItem = menu?.findItem(R.id.search_bar)
        val optionMenus = menu?.findItem(R.id.option_bar)

        val searchView = searchMenuItem?.actionView as SearchView
        val view: MenuItem = optionMenus?.subMenu?.getItem(0) as MenuItem

        view.setOnMenuItemClickListener {
            initializeAlertDialog()
            viewModel.loadLocalProxyData(this)
            false
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.loadWeatherForecastData(query.toString())
                binding.container.tvLocation.text = query.toString()
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    private fun notifyUser(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun onDialogAppeared(ip: String?, port: Int?, status: Boolean?, type: String?) {
        inputIpWidget.setText(ip)
        inputPortWidget.setText(port.toString())
        proxyUsageToggleWidget.isChecked = status!!
        val resId = if (type.equals(Constants.DEFAULT_PROXY_TYPE)) R.id.rb_http else R.id.rb_socks
        proxyTypeWidget.check(resId)
    }

}