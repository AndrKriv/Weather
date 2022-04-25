package com.example.weather.mvvm.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.weather.R
import com.example.weather.databinding.FragmentTodayBinding
import com.example.weather.mvvm.domain.viewBinding
import com.example.weather.mvvm.presentation.app.App
import com.example.weather.mvvm.presentation.viewmodel.TodayViewModel
import com.example.weather.utils.*

class TodayFragment : BaseFragment(R.layout.fragment_today) {

    private val binding: FragmentTodayBinding by viewBinding(FragmentTodayBinding::bind)
    private val todayViewModel: TodayViewModel by viewModels { viewModelFactory }

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
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
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
            if (it)
                showProgressBar()
            else
                hideProgressBar()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireContext().applicationContext as App)
            .appComponent
            .inject(this)
    }

    override fun onLocationReceived(latitude: String, longitude: String) {
        todayViewModel.reloadLiveData.observeForever{
            todayViewModel.getTodayData(latitude, longitude)
        }
    }

    override fun showProgressBar() = binding.todayProgressBar.isVisible()

    override fun hideProgressBar() = binding.todayProgressBar.isGone()
}