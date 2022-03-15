package com.example.weather.mvvm.presentation.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weather.R
import com.example.weather.databinding.FragmentTodayBinding
import com.example.weather.dateFormat.today
import com.example.weather.degreesCheck.WindDirection.windDirection
import com.example.weather.degreesCheck.convertPressure
import com.example.weather.mvvm.domain.viewBinding
import com.example.weather.mvvm.presentation.viewmodel.TodayViewModel

class TodayFragment : Fragment(R.layout.fragment_today) {
    private val binding: FragmentTodayBinding by viewBinding(FragmentTodayBinding::bind)
    private lateinit var todayVM: TodayViewModel

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        todayVM = ViewModelProvider(this)[TodayViewModel::class.java]
        todayVM.getTodayData()

        todayVM.todayInfo.observe(this, Observer {
            with(binding) {
                tvCity.text = "Город: ${it.city}"
                tvDate.text = "Дата: ${it.date.today()}"
                tvTemp.text = "Температура: ${it.main.temp}"
                tvDescription.text = it.weather[0].description
                tvHumidity.text = "Влажность: ${it.main.humidity}%"
                tvPressure.text = "Давление: ${it.main.pressure.convertPressure()}"
                tvWind.text = "Ветер: ${getString(it.wind.deg.windDirection())}"
                tvWindSpeed.text = "Скорость ветра: ${it.wind.speed} м/с"
                ivImg.setImageResource(todayVM.loadImg())

                share.setOnClickListener {
                    startActivity(
                        with(todayVM){
                            sendInfoChooser(
                                stringToShare(
                                    todayInfo.value?.city.toString(),
                                    todayInfo.value?.main?.temp.toString(),
                                    todayInfo.value?.weather?.get(0)?.description.toString(),
                                    todayInfo.value?.wind?.speed.toString(),
                                    todayInfo.value?.main?.humidity.toString(),
                                    todayInfo.value?.main?.pressure.toString().convertPressure()
                               )
                            )
                        }
                    )
                }
            }
        })
    }
}