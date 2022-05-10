package com.example.weather.presentation.today

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.weather.R
import com.example.weather.app.App
import com.example.weather.core.BaseFragment
import com.example.weather.databinding.FragmentTodayBinding
import com.example.weather.extension.toWindDirection
import com.example.weather.extension.today
import com.example.weather.extension.viewBinding
import com.example.weather.utils.loadImg

class TodayFragment : BaseFragment(R.layout.fragment_today) {

    private val binding: FragmentTodayBinding by viewBinding(FragmentTodayBinding::bind)
    private val todayViewModel: TodayViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireContext().applicationContext as App)
            .appComponent
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            todayViewModel.todaySharedFlow.collect { todayUIModel ->
                with(binding) {
                    tvCity.text = getString(R.string.city, todayUIModel.city)
                    tvDate.text = getString(R.string.date, todayUIModel.date.today())
                    tvTemp.text = getString(R.string.temp, todayUIModel.degrees)
                    tvDescription.text = todayUIModel.description
                    tvHumidity.text = getString(R.string.humidity, todayUIModel.humidity)
                    tvPressure.text =
                        getString(R.string.pressure, todayUIModel.pressure)
                    val windDirection = todayUIModel.wind.toWindDirection(requireContext())
                    tvWind.text = getString(R.string.wind, windDirection)
                    tvWindSpeed.text = getString(R.string.wind_speed, todayUIModel.windSpeed)
                    ivImg.setImageResource(loadImg(todayUIModel.description))
                }
                binding.share.setOnClickListener {
                    startActivity(
                        with(todayUIModel) {
                            todayViewModel.sendInfoChooser(
                                todayViewModel.stringToShare(
                                    city,
                                    degrees,
                                    description,
                                    windSpeed,
                                    humidity,
                                    pressure
                                ) + "\n" + getString(R.string.sharing)
                            )
                        }
                    )
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            todayViewModel.errorSharedFlow.collect {
                Toast.makeText(requireContext(), getString(R.string.no_data), Toast.LENGTH_SHORT).show()
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            todayViewModel.loaderSharedFlow.collect { isVisible ->
                binding.todayProgressBar.isVisible = isVisible
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            todayViewModel.reloadFlow.collect { retrieveData() }
        }
    }

    override fun onLocationReceived(latitude: String, longitude: String) =
        todayViewModel.getTodayData(latitude, longitude)
}