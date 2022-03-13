package com.raflisalam.weatherapp.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
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
import com.raflisalam.weatherapp.R
import com.raflisalam.weatherapp.adapter.ContentFragmentAdapter
import com.raflisalam.weatherapp.adapter.ForecastAdapter
import com.raflisalam.weatherapp.databinding.ActivityHomeBinding
import com.raflisalam.weatherapp.ui.fragment.Content1Fragment
import com.raflisalam.weatherapp.ui.fragment.Content2Fragment
import com.raflisalam.weatherapp.ui.fragment.DialogFragment
import com.raflisalam.weatherapp.viewmodel.WeatherViewModel
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private var PERMISION_CODE: Int = 1000
    private lateinit var locationManager: LocationManager
    private lateinit var fusedLocation: FusedLocationProviderClient
    private lateinit var adapter: ForecastAdapter
    private lateinit var fragmentAdapter: ContentFragmentAdapter
    private lateinit var viewModel: WeatherViewModel
    private lateinit var cityName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

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

        setupAdapter()
        setViewModel()
        getPermission()
        setDateFormat()
    }

    @SuppressLint("SetTextI18n")
    private fun setDateFormat() {
        val dayFormat = SimpleDateFormat("EEEE")
        val dateFormat = SimpleDateFormat("dd")
        val monthFormat = SimpleDateFormat("MMM")
        val day = Date()
        val strDay = dayFormat.format(day)
        val strDate = dateFormat.format(day)
        val strMonth = monthFormat.format(day)
        binding.tvDay.text = "$strDay, $strDate $strMonth"
    }

    private fun openDialogAqi(string: String) {
        binding.viewAqi1.setOnClickListener {
            val dialogFragment = DialogFragment()
            val bundle = Bundle()
            bundle.putString(LOCATION, string)
            dialogFragment.arguments = bundle
            dialogFragment.show(supportFragmentManager, "aqiDetail")
        }
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

    @SuppressLint("SetTextI18n")
    private fun getWeather(query: String) {
        viewModel.setForecast(query)
        viewModel.getWeather().observe(this, {
            if (it !=null ) {
                binding.apply {
                    //setContent 1
                    tvTemp.text = it.current?.tempC?.toInt().toString() + "°"
                    tvCondition.text = it.current?.condition?.text
                    tvLocation.text = it.location?.city + ", " + it.location?.region
                    tvLastUpdated.text = "Last Updated " + it.current?.lastUpdated
                    val strCondition = it.current?.condition?.text.toString()
                    setIcon(strCondition)
                    val argsLocation = it.location?.city.toString()
                    openDialogAqi(argsLocation)
                    putBundleData(argsLocation)

                    //setContent 2
                    tvAqiIndex.text = it.current?.airQuality?.aqiIndex?.toString()
                    val aqiIndex: Int? = it.current?.airQuality?.aqiIndex
                    getAqiData(aqiIndex)

                    val txtNo2 = it.current?.airQuality?.no2?.toInt()
                    val txtO3 = it.current?.airQuality?.o3?.toInt()
                    val txtPM2 = it.current?.airQuality?.pm25?.toInt()
                    setupColorPM2(txtPM2)
                    setupColorNo2(txtNo2)
                    setupColor03(txtO3)
                    setupVisibility()
                }
            } else {
                Log.d("Fail Load!", ""+it)
            }
        })
    }

    private fun putBundleData(argsLocation: String) {
        val content1Fragment = Content1Fragment()
        val content2Fragment = Content2Fragment()
        val bundle = Bundle()
        bundle.putString(LOCATION, argsLocation)
        content1Fragment.arguments = bundle
        content2Fragment.arguments = bundle
        setupViewPager(argsLocation)
    }

    private fun setupViewPager(location: String) {
        fragmentAdapter = ContentFragmentAdapter(this, location)
        binding.viewPager.adapter = fragmentAdapter
    }


    private fun setupColorPM2(txtPM2: Int?) {
        binding.percentPM2.text = txtPM2.toString()
        binding.progressPM2.progress = txtPM2!!
        when (txtPM2) {
            in 0..12 -> {
                binding.progressPM2.progressDrawable = getDrawable(R.drawable.healthy_progressbar)
            }
            in 13..35 -> {
                binding.progressPM2.progressDrawable = getDrawable(R.drawable.moderate_progressbar)
            }
            in 36..55 -> {
                binding.progressPM2.progressDrawable = getDrawable(R.drawable.unhealthy_progressbar)
            }
            in 56..1500 -> {
                binding.progressPM2.progressDrawable = getDrawable(R.drawable.very_unhealthy_progressbar)
            }
        }
    }

    private fun setupColor03(txtO3: Int?) {
        binding.percentO3.text = txtO3.toString()
        binding.progressO3.progress = txtO3!!
        when (txtO3) {
            in 0..12 -> {
                binding.progressO3.progressDrawable = getDrawable(R.drawable.healthy_progressbar)
            }
            in 13..35 -> {
                binding.progressO3.progressDrawable = getDrawable(R.drawable.moderate_progressbar)
            }
            in 36..55 -> {
                binding.progressO3.progressDrawable = getDrawable(R.drawable.unhealthy_progressbar)
            }
            in 56..1500 -> {
                binding.progressO3.progressDrawable = getDrawable(R.drawable.very_unhealthy_progressbar)
            }
        }
    }

    private fun setupColorNo2(txtNo2: Int?) {
        binding.percentNO2.text = txtNo2.toString()
        binding.progressNO2.progress = txtNo2!!
        when (txtNo2) {
            in 0..12 -> {
                binding.progressNO2.progressDrawable = getDrawable(R.drawable.healthy_progressbar)
            }
            in 13..35 -> {
                binding.progressNO2.progressDrawable = getDrawable(R.drawable.moderate_progressbar)
            }
            in 36..55 -> {
                binding.progressNO2.progressDrawable = getDrawable(R.drawable.unhealthy_progressbar)
            }
            in 56..1500 -> {
                binding.progressNO2.progressDrawable = getDrawable(R.drawable.very_unhealthy_progressbar)
            }
        }
    }

    private fun setupVisibility() {
        binding.tvDay.visibility = View.VISIBLE
        binding.vectorLocation.visibility = View.VISIBLE
        binding.txtAqiIndex.visibility = View.VISIBLE
    }

    @SuppressLint("SetTextI18n")
    private fun getAqiData(aqiIndex: Int?) {
        if (aqiIndex != null) {
            when (aqiIndex) {
                in 1..3 -> {
                    binding.viewAqi2.setBackgroundColor(Color.parseColor("#87C13C"))
                    binding.viewAqi1.setBackgroundResource(R.drawable.bg_healthy)
                    binding.txtAqiIndex.setTextColor(Color.parseColor("#5A7E2A"))
                    binding.tvAqiNote.text = "Healthy"
                    binding.imgAqi.setImageResource(R.drawable.ic_face_healthy)
                }
                in 4..6 -> {
                    binding.tvAqiNote.text = "Moderate"
                    binding.viewAqi2.setBackgroundColor(Color.parseColor("#EFBE1D"))
                    binding.viewAqi1.setBackgroundResource(R.drawable.bg_moderate)
                    binding.txtAqiIndex.setTextColor(Color.parseColor("#CEA10C"))
                    binding.imgAqi.setImageResource(R.drawable.ic_face_moderate)

                }
                in 7..9 -> {
                    binding.tvAqiNote.text = "Unhealthy"
                    binding.viewAqi2.setBackgroundColor(Color.parseColor("#E84B50"))
                    binding.viewAqi1.setBackgroundResource(R.drawable.bg_unhealthy)
                    binding.txtAqiIndex.setTextColor(Color.parseColor("#942431"))
                    binding.imgAqi.setImageResource(R.drawable.ic_face_red)

                }
                in 10..20 -> {
                    binding.viewAqi2.setBackgroundColor(Color.parseColor("#8A5D9D"))
                    binding.viewAqi1.setBackgroundResource(R.drawable.bg_very_unhealthy)
                    binding.txtAqiIndex.setTextColor(Color.parseColor("#8A5D9D"))
                    binding.tvAqiNote.text = "Very Unhealthy"
                    binding.imgAqi.setImageResource(R.drawable.ic_face_very_unhealthy)

                }
            }
        }
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
        } else if (string == "Clear" || string == "Sunny") {
            binding.iconTemp.setImageResource(R.drawable.ic_clear)
        } else if (string == "Light snow") {
            binding.iconTemp.setImageResource(R.drawable.ic_light_snow)
        } else if (string == "Heavy snow") {
            binding.iconTemp.setImageResource(R.drawable.ic_heavy_snow)
        } else if (string == "Blizzard") {
            binding.iconTemp.setImageResource(R.drawable.ic_blizzard)
        }
    }

    companion object {
        const val LOCATION = "location"
    }
}

