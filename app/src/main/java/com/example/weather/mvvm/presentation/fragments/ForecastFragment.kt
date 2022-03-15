package com.example.weather.mvvm.presentation.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weather.R
import com.example.weather.databinding.FragmentForecastBinding
import com.example.weather.mvvm.domain.viewBinding
import com.example.weather.mvvm.presentation.adapter.ForecastAdapter
import com.example.weather.mvvm.presentation.viewmodel.ForecastViewModel

class ForecastFragment : Fragment(R.layout.fragment_forecast) {
    lateinit var recyclerAdapter: ForecastAdapter
    private val binding: FragmentForecastBinding by viewBinding(FragmentForecastBinding::bind)
    private lateinit var forecastVM: ForecastViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        forecastVM = ViewModelProvider(this)[ForecastViewModel::class.java]
        forecastVM.getForecastData()
        forecastVM.liveData.observe(this, Observer {
            recyclerAdapter = ForecastAdapter(it.list)
            binding.forecastRecyclerView.adapter = recyclerAdapter
        })
    }
}