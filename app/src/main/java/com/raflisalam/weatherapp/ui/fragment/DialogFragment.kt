package com.raflisalam.weatherapp.ui.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.raflisalam.weatherapp.R
import com.raflisalam.weatherapp.databinding.FragmentDialogBinding
import com.raflisalam.weatherapp.viewmodel.WeatherViewModel


class  DialogFragment: DialogFragment() {

    private var _fragmentDialog: FragmentDialogBinding? = null
    private val binding get() = _fragmentDialog as FragmentDialogBinding
    private lateinit var viewModel: WeatherViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View? {
        _fragmentDialog = FragmentDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val location = arguments?.getString("location")
        setViewModel(location)
    }

    private fun setViewModel(location: String?) {
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(WeatherViewModel::class.java)
        if (location != null){
            setAirQualityData(location)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setAirQualityData(location: String) {
        viewModel.setForecast(location)
        viewModel.getWeather().observe(this, {
            if (it !=null ) {
                binding.apply {
                    tvLocation.text = "What is the current air quality in " + it.location?.city + "?"
                    val aqiIndex: Int? = it.current?.airQuality?.aqiIndex
                    tvAqiIndex.text = it.current?.airQuality?.aqiIndex?.toString()
                    getAqiData(aqiIndex)
                    setRisk(aqiIndex)
                    setVisibilty()
                }
            }
        })
    }

    private fun setVisibilty() {
        binding.apply {
            txt3.visibility = View.VISIBLE
            txt4.visibility = View.VISIBLE
            txtAqiIndex.visibility = View.VISIBLE
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setRisk(aqiIndex: Int?) {
        binding.txtRiskIndividual.visibility = View.VISIBLE
        binding.txtRiskGeneral.visibility = View.VISIBLE
        if (aqiIndex != null) {
            binding.apply {
                when (aqiIndex) {
                    in 1..3 -> {
                        tvRiskIndividual.text = getString(R.string.string_healthy_individual)
                        tvRiskGeneral.text = getString(R.string.string_healthy_individual)
                        imgHealth1.setImageResource(R.drawable.ic_health_window_open)
                        tvImgHealth1.text = "Open your window to bring clean, fresh air indoors"
                        imgHealth2.setImageResource(R.drawable.ic_health_sport)
                        tvImgHealth2.text = "Enjoy outdoor activities"
                    }
                    in 4..6 -> {
                        tvRiskIndividual.text = getString(R.string.string_moderate_individuals)
                        tvRiskGeneral.text = getString(R.string.string_healthy_individual)
                        imgHealth1.setImageResource(R.drawable.ic_health_window_close)
                        tvImgHealth1.text = "Close your windows to avoid dirty outdoor air"
                        imgHealth2.setImageResource(R.drawable.ic_health_sport)
                        tvImgHealth2.text = "Sensitive groups should reduce outdoor exercise"
                    }
                    in 7..9 -> {
                        tvRiskIndividual.text = getString(R.string.string_unhealthy_individual)
                        tvRiskGeneral.text = getString(R.string.string_unhealthy_general)
                        imgHealth1.setImageResource(R.drawable.ic_health_mask)
                        tvImgHealth1.text = "Wear a mask outdoors"
                        imgHealth2.setImageResource(R.drawable.ic_health_window_close)
                        tvImgHealth2.text = "Close your windows to avoid dirty outdoor air"
                        imgHealth3.setImageResource(R.drawable.ic_health_airpurifier)
                        tvImgHealth3.text = "Run an air purifier"
                        imgHealth4.setImageResource(R.drawable.ic_health_sport)
                        tvImgHealth4.text = "Avoid outdoor exercise"
                    }
                    in 10..20 -> {
                        tvRiskIndividual.text = getString(R.string.string_very_unhealthy_individual)
                        tvRiskGeneral.text = getString(R.string.string_very_unhealthy_general)
                        imgHealth1.setImageResource(R.drawable.ic_health_mask)
                        tvImgHealth1.text = "Wear a mask outdoors"
                        imgHealth2.setImageResource(R.drawable.ic_health_window_close)
                        tvImgHealth2.text = "Close your windows to avoid dirty outdoor air"
                        imgHealth3.setImageResource(R.drawable.ic_health_airpurifier)
                        tvImgHealth3.text = "Run an air purifier"
                        imgHealth4.setImageResource(R.drawable.ic_health_sport)
                        tvImgHealth4.text = "Avoid outdoor exercise"
                    }
                }
            }
        }
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

}