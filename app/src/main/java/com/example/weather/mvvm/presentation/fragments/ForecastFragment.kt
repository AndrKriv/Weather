package com.example.weather.mvvm.presentation.fragments

import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weather.databinding.FragmentForecastBinding
import com.example.weather.mvvm.domain.viewBinding
import com.example.weather.mvvm.presentation.adapter.ForecastAdapter
import com.example.weather.mvvm.presentation.viewmodel.ForecastViewModel

class ForecastFragment : BaseFragment() {

    private val binding: FragmentForecastBinding by viewBinding(FragmentForecastBinding::bind)
    private lateinit var forecastVM: ForecastViewModel

    override fun onWeatherDataReceived(latitude: String, longitude: String) {
        val forecastAdapter = ForecastAdapter()
        binding.forecastRecyclerView.adapter = forecastAdapter
        forecastVM = ViewModelProvider(this).get(ForecastViewModel::class.java)
        forecastVM.getForecastData(latitude, longitude)
        forecastVM.forecastLiveData.observe(viewLifecycleOwner) {
            forecastAdapter.setItems(it.list)
        }
        forecastVM.errorLiveData.observe(viewLifecycleOwner, Observer {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        })
    }
}