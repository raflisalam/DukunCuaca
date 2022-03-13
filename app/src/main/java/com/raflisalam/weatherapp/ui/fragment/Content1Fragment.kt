package com.raflisalam.weatherapp.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.raflisalam.weatherapp.R
import com.raflisalam.weatherapp.databinding.FragmentContent1Binding
import com.raflisalam.weatherapp.databinding.FragmentDialogBinding
import com.raflisalam.weatherapp.viewmodel.WeatherViewModel

class Content1Fragment : Fragment() {

    private var _content1Fragment: FragmentContent1Binding? = null
    private val binding get() = _content1Fragment as FragmentContent1Binding
    private lateinit var viewModel: WeatherViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View? {
        _content1Fragment = FragmentContent1Binding.inflate(inflater, container, false)
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
            binding.tvWind.text = it.current?.wind.toString()
            binding.tvHumidity.text = it.current?.humidity.toString() + "%"
            binding.tvPressure.text = it.current?.pressure.toString()
            setupVisibility()
        })
    }

    private fun setupVisibility() {
        binding.txtMph.visibility = View.VISIBLE
        binding.txtMbar.visibility = View.VISIBLE
    }

    companion object {

        const val ARG_SECTION_NUMBER = "section_number"
        private const val LOCATION = "location"

        fun newInstance(index: Int, location: String) = Content1Fragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_SECTION_NUMBER, index)
                putString(LOCATION, location)
            }
        }
    }


}