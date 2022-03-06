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
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.raflisalam.weatherapp.ApiClient
import com.raflisalam.weatherapp.R
import com.raflisalam.weatherapp.databinding.ActivityHomeBinding
import com.raflisalam.weatherapp.model.Weather
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private var PERMISION_CODE: Int = 1
    private lateinit var locationManager: LocationManager
    private lateinit var fusedLocation: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.searching.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val string = query.toString()
                getWeather(string)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

        fusedLocation = LocationServices.getFusedLocationProviderClient(this)
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCityName()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), PERMISION_CODE)
        }
    }

    private fun getCityName() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            return
        }
        fusedLocation.lastLocation.addOnCompleteListener {
            val location: Location = it.result
            val geoCoder = Geocoder(this, Locale.getDefault())
            try {
                val address: List<Address> = geoCoder.getFromLocation(location.latitude, location.longitude, 1)
                val string = address[0].subAdminArea
                getWeather(string)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
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

    private fun setIcon(string: String) {
        if (string.equals("Light rain") || string.equals("Moderate rain")) {
            binding.iconTemp.setImageResource(R.drawable.ic_light_rain)
        } else if (string.equals("Partly cloudy")) {
            binding.iconTemp.setImageResource(R.drawable.ic_partly_cloudy)
        } else if (string.equals("Overcast")) {
            binding.iconTemp.setImageResource(R.drawable.ic_overcast)
        } else if (string.equals("Fog")) {
            binding.iconTemp.setImageResource(R.drawable.ic_fog)
        } else if (string.equals("Clear")) {
            binding.iconTemp.setImageResource(R.drawable.ic_clear)
        }
    }

}

