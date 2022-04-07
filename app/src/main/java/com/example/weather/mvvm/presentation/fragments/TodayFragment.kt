package com.example.weather.mvvm.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.weather.R
import com.example.weather.databinding.FragmentTodayBinding
import com.example.weather.mvvm.domain.connection.NetworkStateManager
import com.example.weather.mvvm.domain.viewBinding
import com.example.weather.mvvm.presentation.app.App
import com.example.weather.mvvm.presentation.viewmodel.TodayViewModel
import com.example.weather.utils.convertPressure
import com.example.weather.utils.loadImg
import com.example.weather.utils.toWindDirection
import com.example.weather.utils.today
import javax.inject.Inject

class TodayFragment : BaseFragment(R.layout.fragment_today) {

    private val binding: FragmentTodayBinding by viewBinding(FragmentTodayBinding::bind)
    private val todayViewModel: TodayViewModel by viewModels { viewModelFactory }

    @Inject
    lateinit var networkStateManager: NetworkStateManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        todayViewModel.todayLiveData.observe(viewLifecycleOwner) {
            with(binding) {
                tvCity.text = getString(R.string.city, it.city)
                tvDate.text = getString(R.string.date, it.date.today())
                tvTemp.text = getString(R.string.temp, it.main.temp)
                tvDescription.text = it.weather[0].description
                tvHumidity.text = getString(R.string.humidity, it.main.humidity)
                tvPressure.text =
                    getString(R.string.pressure, it.main.pressure.convertPressure())
                val windDirection = it.wind.deg.toWindDirection(requireContext())
                tvWind.text = getString(R.string.wind, windDirection)
                tvWindSpeed.text = getString(R.string.wind_speed, it.wind.speed)
                ivImg.setImageResource(loadImg(it.weather[0].description))
            }
        }
        todayViewModel.errorLiveData.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), getString(R.string.connection), Toast.LENGTH_SHORT)
                .show()
        }
        binding.share.setOnClickListener {
            todayViewModel.todayLiveData.value?.let {
                startActivity(
                    with(it) {
                        todayViewModel.sendInfoChooser(
                            todayViewModel.stringToShare(
                                city,
                                main.temp.toDouble(),
                                weather.get(0).description,
                                wind.speed.toDouble(),
                                main.humidity.toDouble(),
                                main.pressure.convertPressure()
                                    .toDouble()
                            ) + "\n" + getString(R.string.sharing)
                        )
                    }
                )
            } ?: Log.e("AAA", "LiveData is Empty")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireContext().applicationContext as App)
            .appComponent
            .inject(this)
    }

    override fun onWeatherDataReceived(latitude: String, longitude: String) {
        networkStateManager.networkStatusLiveData.observe(viewLifecycleOwner) {
            if (it) {
                todayViewModel.getTodayData(latitude, longitude)
            } else Log.e("AAA", "error is in onWeatherDataReceived")
        }
    }
}