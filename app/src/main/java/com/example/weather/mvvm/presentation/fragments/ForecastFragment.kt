package com.example.weather.mvvm.presentation.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.weather.R
import com.example.weather.databinding.FragmentForecastBinding
import com.example.weather.mvvm.presentation.UIModel
import com.example.weather.utils.fromForecastDatabaseToUIModel
import com.example.weather.mvvm.domain.viewBinding
import com.example.weather.mvvm.presentation.adapter.ForExampleAdapter
import com.example.weather.mvvm.presentation.app.App
import com.example.weather.mvvm.presentation.viewmodel.ForecastViewModel

class ForecastFragment : BaseFragment(R.layout.fragment_forecast) {

    private val binding: FragmentForecastBinding by viewBinding(FragmentForecastBinding::bind)
    private val forecastViewModel: ForecastViewModel by viewModels { viewModelFactory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val forecastAdapter = ForExampleAdapter()
        binding.forecastRecyclerView.adapter = forecastAdapter
        forecastViewModel.forecastLiveData.observe(viewLifecycleOwner) {
            val list = mutableListOf<UIModel>()
            for ((i, value) in it.withIndex()) {
                list.add(i, value.fromForecastDatabaseToUIModel())
            }
            forecastAdapter.setItems(list)
        }
        forecastViewModel.errorLiveData.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), getString(R.string.connection), Toast.LENGTH_SHORT)
                .show()
        }
        forecastViewModel.databaseLiveData.observe(viewLifecycleOwner) {
            forecastAdapter.setItems(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireContext().applicationContext as App)
            .appComponent
            .inject(this)
    }

    override fun onWeatherDataReceived(latitude: String, longitude: String) {
        forecastViewModel.getForecastData(latitude, longitude)
    }
}