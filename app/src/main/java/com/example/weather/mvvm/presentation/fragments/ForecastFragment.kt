package com.example.weather.mvvm.presentation.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weather.R
import com.example.weather.databinding.FragmentForecastBinding
import com.example.weather.mvvm.domain.viewBinding
import com.example.weather.mvvm.presentation.adapter.ForecastAdapter
import com.example.weather.mvvm.presentation.viewmodel.ForecastViewModel

class ForecastFragment : BaseFragment(R.layout.fragment_forecast) {

    private val binding: FragmentForecastBinding by viewBinding(FragmentForecastBinding::bind)
    private lateinit var forecastVM: ForecastViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val forecastAdapter = ForecastAdapter()
        binding.forecastRecyclerView.adapter = forecastAdapter
        forecastVM = ViewModelProvider(this).get(ForecastViewModel::class.java)
        forecastVM.forecastLiveData.observe(viewLifecycleOwner) {
            forecastAdapter.setItems(it.list)
        }
        forecastVM.errorLiveData.observe(viewLifecycleOwner, Observer {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        })
    }

    override fun onWeatherDataReceived(latitude: String, longitude: String) {
        forecastVM.getForecastData(latitude, longitude)
    }
}