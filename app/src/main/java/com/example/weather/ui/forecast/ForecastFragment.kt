package com.example.weather.ui.forecast

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.databinding.FragmentForecastBinding
import com.example.weather.databinding.FragmentTodayBinding
import com.example.weather.objects.toAllProject
import com.example.weather.ui.viewBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ForecastFragment : Fragment() {
    var lat = ""
    var lon = ""
    var sp: SharedPreferences? = null

   // private val binding:FragmentForecastBinding by viewBinding(FragmentForecastBinding::bind)

    private val binding: FragmentForecastBinding by viewBinding {  FragmentForecastBinding.bind(
        requireView())
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = binding.forecastRecyclerView
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view: View = inflater.inflate(R.layout.fragment_forecast, container, false)
        val lat2 = savedInstanceState?.getString(toAllProject.latitude).toString()
        val lon2 = savedInstanceState?.getString(toAllProject.longitude).toString()
        val f: SharedPreferences? =
            this.activity?.getSharedPreferences(toAllProject.prefName, Context.MODE_PRIVATE)
        lat = f?.getString(toAllProject.latitude, null).toString()
        lon = f?.getString(toAllProject.longitude, null).toString()
//        val recyclerView: RecyclerView? = view.findViewById(R.id.forecast_recycler_view)
//        recyclerView?.addItemDecoration(
//            DividerItemDecoration(
//                requireContext(),
//                DividerItemDecoration.VERTICAL
//            )
//        )
        val dispose = if (savedInstanceState != null) {
            urlForecast(lat2, lon2)
        } else {
            urlForecast(lat, lon)
        }
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                //recyclerView?.adapter = ForecastAdapter(it)
            }, {
                Toast.makeText(context, R.string.error, Toast.LENGTH_SHORT).show()
            }, {})
        return view
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        sp = this.activity?.getSharedPreferences(toAllProject.prefName, Context.MODE_PRIVATE)
        lat = sp?.getString(toAllProject.latitude, null).toString()
        lon = sp?.getString(toAllProject.longitude, null).toString()
        savedInstanceState.putString(toAllProject.latitude, lat)
        savedInstanceState.putString(toAllProject.longitude, lon)
    }
}