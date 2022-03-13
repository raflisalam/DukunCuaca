package com.raflisalam.weatherapp.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.raflisalam.weatherapp.R
import com.raflisalam.weatherapp.databinding.FragmentContent2Binding
import com.raflisalam.weatherapp.viewmodel.WeatherViewModel

class Content2Fragment : Fragment() {

    private var _content2Fragment: FragmentContent2Binding? = null
    private val binding get() = _content2Fragment as FragmentContent2Binding
    private lateinit var viewModel: WeatherViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View? {
        _content2Fragment = FragmentContent2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val location = arguments?.getString(LOCATION)
        setViewModel(location)
    }

    private fun setViewModel(location: String?) {
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(WeatherViewModel::class.java)
        if (location != null) {
            getWeatherData(location)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getWeatherData(location: String) {
        viewModel.setForecast(location)
        viewModel.getWeather().observe(viewLifecycleOwner, {
            binding.tvCloud.text = it.current?.cloud.toString() + "%"
        })
        viewModel.getDay().observe(viewLifecycleOwner, {
            binding.tvPreciptation.text = it.precip?.toInt().toString() + "%"
            binding.tvRainy.text = it.chanceRainy.toString() + "%"
        })
    }

    companion object {

        const val ARG_SECTION_NUMBER = "section_number"
        private const val LOCATION = "location"

        fun newInstance(index: Int, location: String) = Content2Fragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_SECTION_NUMBER, index)
                putString(LOCATION, location)
            }
        }
    }

}