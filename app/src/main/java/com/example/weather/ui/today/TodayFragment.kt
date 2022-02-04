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
import com.example.weather.objects.ConstForAllProject
import com.example.weather.share.CreateSharingString
import com.example.weather.share.ShareText
import com.example.weather.ui.viewBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class TodayFragment : Fragment(R.layout.fragment_today) {
    private var lat = ""
    private var lon = ""
    var sharedPreferences: SharedPreferences? = null
    private val binding: FragmentTodayBinding by viewBinding(FragmentTodayBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var stringToShare = ""
        val lat2 = savedInstanceState?.getString(ConstForAllProject.latitude).toString()
        val lon2 = savedInstanceState?.getString(ConstForAllProject.longitude).toString()

        sharedPreferences =
            this.activity?.getSharedPreferences(ConstForAllProject.prefName, Context.MODE_PRIVATE)
        lat = sharedPreferences?.getString(ConstForAllProject.latitude, null).toString()
        lon = sharedPreferences?.getString(ConstForAllProject.longitude, null).toString()

        val dispose = if (savedInstanceState != null) {
            urlTodayWeather(lat2, lon2)
        } else {
            urlTodayWeather(lat, lon)
        }
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                binding.tvCity.text = it[0]
                binding.tvTemp.text = it[1] + "°С"
                binding.tvDescription.text = "На улице ${it[2]}"
                binding.tvDate.text = it[3]
                binding.tvPressure.text = "Давление " + it[4]
                binding.tvHumidity.text = "Влажность " + it[5] + "%"
                binding.tvWind.text = "Ветер " + it[6]
                binding.tvWindSpeed.text = "Скорость ветра: " + it[7] + " м/c"
                binding.ivImg.setImageResource(ImageChecker.imageWeather(it[2]))

                stringToShare =
                    CreateSharingString.wthString(it[0], it[1], it[2], it[6], it[7], it[5], it[4])
            }, {
                Toast.makeText(context, R.string.tap, Toast.LENGTH_LONG).show()
            }, {})

        binding.share.setOnClickListener {
            val intent = Intent()
            startActivity(
                ShareText.sendWeather(
                    intent,
                    stringToShare + R.string.sharing
                )
            )
        }
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        lat = sharedPreferences?.getString(ConstForAllProject.latitude, "").toString()
        lon = sharedPreferences?.getString(ConstForAllProject.longitude, "").toString()
        savedInstanceState.putString(ConstForAllProject.latitude, lat)
        savedInstanceState.putString(ConstForAllProject.longitude, lon)
    }
}