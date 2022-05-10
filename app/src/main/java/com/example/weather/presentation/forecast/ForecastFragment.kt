package com.example.weather.presentation.forecast

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.weather.R
import com.example.weather.app.App
import com.example.weather.core.BaseFragment
import com.example.weather.databinding.FragmentForecastBinding
import com.example.weather.extension.viewBinding
import com.example.weather.presentation.forecast.adapter.ForecastAdapter
import kotlinx.coroutines.launch

class ForecastFragment : BaseFragment(R.layout.fragment_forecast) {

    private val binding: FragmentForecastBinding by viewBinding(FragmentForecastBinding::bind)
    private val forecastViewModel: ForecastViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireContext().applicationContext as App)
            .appComponent
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val forecastAdapter = ForecastAdapter()
        binding.forecastRecyclerView.adapter = forecastAdapter
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            launch {
                forecastViewModel.forecastStateFlow.collect { forecastUIModel ->
                        forecastAdapter.setItems(forecastUIModel)
                }
            }
            launch {
                forecastViewModel.errorSharedFlow.collect {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.no_data),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            launch {
                forecastViewModel.loaderStateFlow.collect { isVisible ->
                    binding.forecastProgressBar.isVisible = isVisible
                }
            }
            launch {
                forecastViewModel.reloadFlow.collect { retrieveData() }
            }
        }
        binding.forecastRecyclerView.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
    }

    override fun onLocationReceived(latitude: String, longitude: String) =
        forecastViewModel.getForecastData(latitude, longitude)
}