package com.raflisalam.weatherapp.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.raflisalam.weatherapp.ApiClient
import com.raflisalam.weatherapp.adapter.ForecastAdapter
import com.raflisalam.weatherapp.R
import com.raflisalam.weatherapp.databinding.ActivityHomeBinding
import com.raflisalam.weatherapp.model.Weather
import com.raflisalam.weatherapp.viewmodel.WeatherViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private var PERMISION_CODE: Int = 1000
    private lateinit var locationManager: LocationManager
    private lateinit var fusedLocation: FusedLocationProviderClient
    private lateinit var adapter: ForecastAdapter
    private lateinit var viewModel: WeatherViewModel
    private lateinit var cityName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        searching()
        setupAdapter()
        setViewModel()
        getPermission()
    }

    private fun searching() {
        binding.searching.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val string = query.toString()
                getWeather(string)
                viewModel.setForecast(string)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun setViewModel() {
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(WeatherViewModel::class.java)
        viewModel.getForecast().observe(this, {
            if (it !=null) {
                adapter.setListForecast(it)
                setRecycler()
            }
        })
    }

    private fun setupAdapter() {
        adapter = ForecastAdapter()
        adapter.notifyDataSetChanged()
    }

    private fun getPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), PERMISION_CODE)
        }
        fusedLocation = LocationServices.getFusedLocationProviderClient(this)
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val location: Location? = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        if (location != null) {
            cityName = getCityName(location.latitude, location.longitude)
            getWeather(cityName)
            viewModel.setForecast(cityName)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode==PERMISION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please provide the permission", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun getCityName(latitude: Double, longitude: Double): String {
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            val address: List<Address> = geocoder.getFromLocation(latitude, longitude, 10)
            for (adr in address) {
                val city = adr.subAdminArea
                cityName = city
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return cityName
    }

    private fun getWeather(query: String) {
        ApiClient.instance.getWeather(query).enqueue(object : Callback<Weather> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<Weather>, response: Response<Weather>) {
                binding.apply {
                    tvTemp.text = response.body()?.current?.tempC.toString() + "°"
                    tvCondition.text = response.body()?.current?.condition?.text.toString()
                    tvLocation.text = response.body()?.location?.city.toString() +", "+ response.body()?.location?.region.toString()
                    dateTime.text = response.body()?.location?.dateTime.toString()
                    tvWind.text = response.body()?.current?.wind.toString()
                    tvHumidity.text = response.body()?.current?.humidity.toString() + "%"
                    tvPressure.text = response.body()?.current?.pressure.toString()
                    val string = response.body()?.current?.condition?.text.toString()
                    setIcon(string)

                    val txtNo2 = response.body()?.current?.airQuality?.no2?.toInt()
                    val txtPM2 = response.body()?.current?.airQuality?.pm25?.toInt()
                    val txtO3 = response.body()?.current?.airQuality?.o3?.toInt()
                    percentNO2.text = txtNo2.toString() + "%"
                    percentO3.text = txtO3.toString() + "%"
                    percentPM2.text = txtPM2.toString() + "%"
                    progressNO2.progress = txtNo2!!
                    progressPM2.progress = txtPM2!!
                    progressO3.progress = txtO3!!
                }
            }

            override fun onFailure(call: Call<Weather>, t: Throwable) {
                t.message?.let { Log.d("Fail Load!", it) }
            }
        })
    }

    private fun setRecycler() {
        binding.rvForecast.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvForecast.setHasFixedSize(true)
        binding.rvForecast.adapter = adapter
    }

    private fun setIcon(string: String) {
        if (string == "Light rain" || string == "Light rain shower" || string == "Moderate rain" || string == "Patchy rain possible") {
            binding.iconTemp.setImageResource(R.drawable.ic_light_rain)
        } else if (string == "Partly cloudy") {
            binding.iconTemp.setImageResource(R.drawable.ic_partly_cloudy)
        } else if (string == "Overcast") {
            binding.iconTemp.setImageResource(R.drawable.ic_overcast)
        } else if (string == "Fog") {
            binding.iconTemp.setImageResource(R.drawable.ic_fog)
        } else if (string == "Clear") {
            binding.iconTemp.setImageResource(R.drawable.ic_clear)
        }
    }
}

