package com.example.weather.ui.forecast

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.ui.today.TodayFragment
import kotlinx.coroutines.*
import org.json.JSONObject
import java.net.URL

class ForecastFragment : Fragment() {
    var lat=""
    var lon=""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        val bundle:Bundle? = arguments
//        val message1 = bundle?.getString("lat")
//        val message2 = bundle?.getString("lon")
//        lat = message1.toString()
//        lon = message2.toString()
        val view: View = inflater.inflate(R.layout.fragment_forecast, container, false)
        // Inflate the layout for this fragment
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var f: SharedPreferences? = this.getActivity()?.getSharedPreferences("pref", Context.MODE_PRIVATE)
        lat = f?.getString("lat",null).toString()
        lon = f?.getString("lon",null).toString()
        val newsList: ArrayList<ForecastInfo> = urlForecast(lat, lon)
        super.onViewCreated(view, savedInstanceState)
        val recyclerView: RecyclerView? = view.findViewById(R.id.forecast_recycler_view)
        recyclerView?.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        //recyclerView?.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL,false);
        recyclerView?.adapter = ForecastAdapter(newsList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}