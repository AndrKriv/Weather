package com.example.weather.mvvm.presentation.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.weather.R
import com.example.weather.databinding.FragmentForecastBinding
import com.example.weather.mvvm.domain.viewBinding
import com.example.weather.mvvm.presentation.adapter.ForecastAdapter
import com.example.weather.mvvm.presentation.app.App
import com.example.weather.mvvm.presentation.viewmodel.ForecastViewModel
import com.example.weather.utils.isGone
import com.example.weather.utils.isVisible

class ForecastFragment : BaseFragment(R.layout.fragment_forecast) {

    private val binding: FragmentForecastBinding by viewBinding(FragmentForecastBinding::bind)
    private val forecastViewModel: ForecastViewModel by viewModels { viewModelFactory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val forecastAdapter = ForecastAdapter()
        binding.forecastRecyclerView.adapter = forecastAdapter
        forecastViewModel.forecastLiveData.observe(viewLifecycleOwner) {
            forecastAdapter.setItems(it)
        }
        forecastViewModel.errorLiveData.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), getString(R.string.no_data), Toast.LENGTH_SHORT)
                .show()
        }

        forecastViewModel.loaderLiveData.observe(viewLifecycleOwner) {
            if (it)
                showProgressBar()
            else
                hideProgressBar()
        }
        forecastViewModel.reloadLiveData.observe(viewLifecycleOwner) {
            it
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireContext().applicationContext as App)
            .appComponent
            .inject(this)
    }

    override fun onLocationReceived(latitude: String, longitude: String) =
        forecastViewModel.getForecastData(latitude, longitude)

    override fun showProgressBar() = binding.forecastProgressBar.isVisible()

    override fun hideProgressBar() = binding.forecastProgressBar.isGone()
}