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
    var lat = ""
    var lon = ""
    var sp: SharedPreferences? = null
    private val binding: FragmentForecastBinding by viewBinding(FragmentForecastBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val lat2 = savedInstanceState?.getString(Constants.latitude).toString()
        val lon2 = savedInstanceState?.getString(Constants.longitude).toString()
        val f: SharedPreferences? =
            this.activity?.getSharedPreferences(Constants.prefName, Context.MODE_PRIVATE)
        lat = f?.getString(Constants.latitude, null).toString()
        lon = f?.getString(Constants.longitude, null).toString()
        binding.forecastRecyclerView.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        val dispose = if (savedInstanceState != null) {
            urlForecast(lat2, lon2)
        } else {
            urlForecast(lat, lon)
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
        sp = this.activity?.getSharedPreferences(Constants.prefName, Context.MODE_PRIVATE)
        lat = sp?.getString(Constants.latitude, null).toString()
        lon = sp?.getString(Constants.longitude, null).toString()
        savedInstanceState.putString(Constants.latitude, lat)
        savedInstanceState.putString(Constants.longitude, lon)
    }
}