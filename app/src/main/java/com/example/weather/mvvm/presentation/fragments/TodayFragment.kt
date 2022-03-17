package com.example.weather.mvvm.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weather.R
import com.example.weather.databinding.FragmentTodayBinding
import com.example.weather.dateFormat.today
import com.example.weather.degreesCheck.convertPressure
import com.example.weather.degreesCheck.toWindDirection
import com.example.weather.mvvm.domain.viewBinding
import com.example.weather.mvvm.presentation.viewmodel.TodayViewModel

class TodayFragment : Fragment(R.layout.fragment_today) {
    private val binding: FragmentTodayBinding by viewBinding(FragmentTodayBinding::bind)
    private lateinit var todayVM: TodayViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        todayVM = ViewModelProvider(this)[TodayViewModel::class.java]
        todayVM.getTodayData("55", "30")
        todayVM.todayLiveData.observe(this, Observer {
            with(binding) {
                tvCity.text = getString(R.string.city, it.city)
                tvDate.text = getString(R.string.date, it.date.today())
                tvTemp.text = getString(R.string.temp, it.main.temp)
                tvDescription.text = it.weather[0].description
                tvHumidity.text = getString(R.string.humidity, it.main.humidity)
                tvPressure.text = getString(R.string.pressure, it.main.pressure.convertPressure())
                val windDirection = requireContext().toWindDirection(it.wind.deg)
                tvWind.text = getString(R.string.wind, windDirection)
                tvWindSpeed.text = getString(R.string.wind_speed, it.wind.speed)
                ivImg.setImageResource(todayVM.loadImg(it.weather[0].description))
            }
        })
        todayVM.errorLiveData.observe(this, Observer {
            binding.tvCity.text = it
        })

        binding.share.setOnClickListener {
            todayVM.todayLiveData.value?.let {
                startActivity(
                    with(it){
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