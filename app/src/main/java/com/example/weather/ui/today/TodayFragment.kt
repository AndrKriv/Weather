package com.example.weather.ui.today

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.weather.OWM.ImageChecker
import com.example.weather.R
import com.example.weather.databinding.FragmentTodayBinding
import com.example.weather.objects.Constants
import com.example.weather.share.CreateSharingString
import com.example.weather.share.ShareText
import com.example.weather.ui.viewBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class TodayFragment : Fragment(R.layout.fragment_today) {
    private var latitude = ""
    private var longitude = ""
    var sharedPreferences: SharedPreferences? = null
    private val binding: FragmentTodayBinding by viewBinding(FragmentTodayBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var stringToShare = ""
        val latitude2 = savedInstanceState?.getString(Constants.latitude).toString()
        val longitude2 = savedInstanceState?.getString(Constants.longitude).toString()

        sharedPreferences =
            this.activity?.getSharedPreferences(Constants.preferencesName, Context.MODE_PRIVATE)
        latitude = sharedPreferences?.getString(Constants.latitude, null).toString()
        longitude = sharedPreferences?.getString(Constants.longitude, null).toString()

        val dispose = if (savedInstanceState != null) {
            urlTodayWeather(latitude2, longitude2)
        } else {
            urlTodayWeather(latitude, longitude)
        }
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                with(binding) {
                    tvCity.text = it[0]
                    tvTemp.text = "${it[1]}°С"
                    tvDescription.text = "На улице ${it[2]}"
                    tvDate.text = it[3]
                    tvPressure.text = "Давление ${it[4]}"
                    tvHumidity.text = "Влажность ${it[5]}%"
                    tvWind.text = "Ветер ${it[6]}"
                    tvWindSpeed.text = "Скорость ветра: ${it[7]} м/c"
                    ivImg.setImageResource(ImageChecker.imageWeather(it[2]))
                }
                stringToShare =
                    CreateSharingString.wthString(it[0], it[1], it[2], it[6], it[7], it[5], it[4])
            }, {
                Toast.makeText(context, R.string.tap, Toast.LENGTH_LONG).show()
            }, {})

        binding.share.setOnClickListener {
            val chooser = Intent.createChooser(
                ShareText.createSharingIntent(
                    "$stringToShare ${R.string.sharing}"
                ), "Launch"
            )
            startActivity(
                chooser
            )
        }
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        latitude = sharedPreferences?.getString(Constants.latitude, "").toString()
        longitude = sharedPreferences?.getString(Constants.longitude, "").toString()
        savedInstanceState.putString(Constants.latitude, latitude)
        savedInstanceState.putString(Constants.longitude, longitude)
    }
}