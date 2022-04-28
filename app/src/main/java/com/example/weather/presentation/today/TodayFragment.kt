package com.example.weather.presentation.today

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
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
        todayViewModel.todayLiveData.observe(viewLifecycleOwner) {
            with(binding) {
                tvCity.text = getString(R.string.city, it.city)
                tvDate.text = getString(R.string.date, it.date.today())
                tvTemp.text = getString(R.string.temp, it.degrees)
                tvDescription.text = it.description
                tvHumidity.text = getString(R.string.humidity, it.humidity)
                tvPressure.text =
                    getString(R.string.pressure, it.pressure)
                val windDirection = it.wind.toWindDirection(requireContext())
                tvWind.text = getString(R.string.wind, windDirection)
                tvWindSpeed.text = getString(R.string.wind_speed, it.windSpeed)
                ivImg.setImageResource(loadImg(it.description))
            }
        }
        todayViewModel.errorLiveData.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), getString(R.string.no_data), Toast.LENGTH_SHORT).show()
        }
        binding.share.setOnClickListener {
            todayViewModel.todayLiveData.value?.let {
                startActivity(
                    with(it) {
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
            } ?: Log.e("AAA", "LiveData is Empty")
        }
        todayViewModel.loaderLiveData.observe(viewLifecycleOwner) {
            binding.todayProgressBar.isVisible = it
        }
        todayViewModel.reloadLiveData.observe(viewLifecycleOwner) {
            retrieveData()
        }
    }

    override fun onLocationReceived(latitude: String, longitude: String) =
        todayViewModel.getTodayData(latitude, longitude)
}