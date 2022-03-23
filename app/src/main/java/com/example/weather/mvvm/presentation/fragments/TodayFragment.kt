package com.example.weather.mvvm.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weather.R
import com.example.weather.databinding.FragmentTodayBinding
import com.example.weather.dateFormat.today
import com.example.weather.degreesCheck.convertPressure
import com.example.weather.degreesCheck.toWindDirection
import com.example.weather.mvvm.domain.viewBinding
import com.example.weather.mvvm.presentation.viewmodel.TodayViewModel

class TodayFragment : BaseFragment() {

    private val binding: FragmentTodayBinding by viewBinding(FragmentTodayBinding::bind)
    private lateinit var todayVM: TodayViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_today, container, false)
    }

    override fun onReadWeather(latitude: String, longitude: String) {
        todayVM = ViewModelProvider(this)[TodayViewModel::class.java]
        todayVM.getTodayData(latitude, longitude)
        todayVM.todayLiveData.observe(viewLifecycleOwner, Observer {
            with(binding) {
                tvCity.text = getString(R.string.city, it.city)
                tvDate.text = getString(R.string.date, it.date.today())
                tvTemp.text = getString(R.string.temp, it.main.temp)
                tvDescription.text = it.weather[0].description
                tvHumidity.text = getString(R.string.humidity, it.main.humidity)
                tvPressure.text =
                    getString(R.string.pressure, it.main.pressure.convertPressure())
                val windDirection = requireContext().toWindDirection(it.wind.deg)
                tvWind.text = getString(R.string.wind, windDirection)
                tvWindSpeed.text = getString(R.string.wind_speed, it.wind.speed)
                ivImg.setImageResource(todayVM.loadImg(it.weather[0].description))
            }
        })
        todayVM.errorLiveData.observe(viewLifecycleOwner, Observer {
            binding.tvCity.text = it
        })
        binding.share.setOnClickListener {
            todayVM.todayLiveData.value?.let {
                startActivity(
                    with(it) {
                        todayVM.sendInfoChooser(
                            todayVM.stringToShare(
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
}