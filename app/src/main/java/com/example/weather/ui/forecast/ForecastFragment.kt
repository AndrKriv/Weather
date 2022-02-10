package com.example.weather.ui.forecast

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.weather.R
import com.example.weather.databinding.FragmentForecastBinding
import com.example.weather.objects.Constants
import com.example.weather.ui.viewBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ForecastFragment : Fragment(R.layout.fragment_forecast) {
    var latitude = ""
    var longitude = ""
    var sharedPreferences: SharedPreferences? = null
    private val binding: FragmentForecastBinding by viewBinding(FragmentForecastBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val latitude2 = savedInstanceState?.getString(Constants.latitude).toString()
        val longitude2 = savedInstanceState?.getString(Constants.longitude).toString()
        val spReader: SharedPreferences? =
            this.activity?.getSharedPreferences(Constants.preferencesName, Context.MODE_PRIVATE)
        latitude = spReader?.getString(Constants.latitude, null).toString()
        longitude = spReader?.getString(Constants.longitude, null).toString()

        binding.forecastRecyclerView.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )

        val dispose = if (savedInstanceState != null) {
            urlForecast(latitude2, longitude2)
        } else {
            urlForecast(latitude, longitude)
        }
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                binding.forecastRecyclerView.adapter = ForecastAdapter(it)
            }, {
                Toast.makeText(context, R.string.error, Toast.LENGTH_SHORT).show()
            }, {})
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        sharedPreferences = this.activity?.getSharedPreferences(Constants.preferencesName, Context.MODE_PRIVATE)
        latitude = sharedPreferences?.getString(Constants.latitude, null).toString()
        longitude = sharedPreferences?.getString(Constants.longitude, null).toString()
        savedInstanceState.putString(Constants.latitude, latitude)
        savedInstanceState.putString(Constants.longitude, longitude)
    }
}