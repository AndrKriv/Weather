package com.example.weather.mvvm.presentation.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.weather.R
import com.example.weather.databinding.FragmentTodayBinding
import com.example.weather.mvvm.domain.viewBinding
import com.example.weather.mvvm.presentation.viewmodel.TodayViewModel
import com.example.weather.objects.Constants
import retrofit2.HttpException

class TodayFragment : Fragment(R.layout.fragment_today) {
    private val binding: FragmentTodayBinding by viewBinding(FragmentTodayBinding::bind)
    private lateinit var todayVM: TodayViewModel

    //private val location= LocationImpl(context = requireContext())
    //private val locationUseCase = GetLocationUseCase(location)
    //private val useCase by lazy { GetLocationUseCase(location) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("AAA", "fragm create")
    }

    override fun onStart() {
        super.onStart()
        todayVM = ViewModelProvider(this).get(TodayViewModel::class.java)
        val lat = TodayViewModel.SharedPreference(requireContext())
            .getValueString(Constants.latitude).toString()
        val lon = TodayViewModel.SharedPreference(requireContext())
            .getValueString(Constants.longitude).toString()
//        if (lat.isEmpty() && lon.isEmpty() ) {
//            todayVM.getTodayData(requireContext(), "1", "1")
//        } else {}
        try {
                //todayVM.getTodayData(this.requireContext())
//                with(binding) {
//                    tvCity.text = todayVM.todayInfo.value?.city
//                    tvDate.text = todayVM.todayInfo.value?.date
//                    tvTemp.text = todayVM.todayInfo.value?.main?.temp
//                    tvDescription.text = todayVM.todayInfo.value?.weather?.get(0)?.description
//                    tvHumidity.text = todayVM.todayInfo.value?.main?.humidity
//                    tvPressure.text = todayVM.todayInfo.value?.main?.pressure
//                    tvWindSpeed.text = todayVM.todayInfo.value?.wind?.speed
//                }
//                binding.share.setOnClickListener {
//                    startActivity(
//                        todayVM.sendInfoChooser(
//                            TodayViewModel.CreateSharingString.wthString(
//                                binding.tvCity.text.toString(),
//                                binding.tvTemp.text.toString(),
//                                binding.tvDescription.text.toString(),
//                                binding.tvWindSpeed.text.toString(),
//                                binding.tvHumidity.text.toString(),
//                                binding.tvPressure.text.toString()
//                            )
//                        )
//                    )
//                }
//                binding.ivImg.setImageResource(todayVM.loadImage())
        }
        catch (e: HttpException){
            when(e.code()){
                400 ->  Log.e("AAA", e.code().toString())
            }
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //val a = locationUseCase.execute()
        //binding.tvCity.text = "${a.latitude} + ${a.longitude}"
    }

    override fun onDestroy() {
        super.onDestroy()
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        prefs.edit()
            .clear()
            .apply()
    }
}