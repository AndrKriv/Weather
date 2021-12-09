package com.example.weather.ui.forecast

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
import kotlinx.coroutines.*
import org.json.JSONObject
import java.net.URL

class ForecastFragment : Fragment() {
    val newsList: ArrayList<ForecastInfo> = ForecastReader.urlForecast()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.fragment_forecast, container, false)

        // Inflate the layout for this fragment
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        val recyclerView: RecyclerView? = view.findViewById(R.id.forecast_recycler_view)
        recyclerView?.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        //recyclerView?.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL,false);
        recyclerView?.adapter = ForecastAdapter(newsList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
       //_binding = null
    }


}