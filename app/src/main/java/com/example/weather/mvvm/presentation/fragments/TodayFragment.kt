package com.example.weather.mvvm.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.weather.R
import com.example.weather.databinding.FragmentTodayBinding
import com.example.weather.di.component.DaggerAppComponent
import com.example.weather.mvvm.domain.viewBinding
import com.example.weather.mvvm.presentation.app.App
import com.example.weather.mvvm.presentation.viewmodel.TodayViewModel
import com.example.weather.utils.*

class TodayFragment : BaseFragment(R.layout.fragment_today) {

    private val binding: FragmentTodayBinding by viewBinding(FragmentTodayBinding::bind)
    lateinit var todayViewModel: TodayViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        todayViewModel = ViewModelProvider(this, viewModelsFactory).get(TodayViewModel::class.java)
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
            binding.tvCity.text = getString(R.string.connection)
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
        DaggerAppComponent
            .builder()
            .application(App())
            .baseUrl(Constants.BASE_URL)
            .build()
            .inject(this)
    }

    override fun onWeatherDataReceived(latitude: String, longitude: String) {
        todayViewModel.getTodayData(latitude, longitude)
    }
}