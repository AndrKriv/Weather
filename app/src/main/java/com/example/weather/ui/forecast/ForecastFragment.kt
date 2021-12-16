package com.example.weather.ui.forecast

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.ui.today.TodayFragment
import com.example.weather.ui.today.urlTodayWeather
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import org.json.JSONObject
import java.net.URL

class ForecastFragment : Fragment() {
    var lat=""
    var lon=""
    var sp: SharedPreferences? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val lat2 =  savedInstanceState?.getString("lat").toString()
        val lon2 =  savedInstanceState?.getString("lon").toString()
        Log.d("WSWSWS2",  lat2)
        Log.d("WSWSWS2",  lon2)
        val view: View = inflater.inflate(R.layout.fragment_forecast, container, false)
        val f: SharedPreferences? = this.getActivity()?.getSharedPreferences("pref", Context.MODE_PRIVATE)
        lat = f?.getString("lat",null).toString()
        lon = f?.getString("lon",null).toString()
        Log.d("WSWSWS3",  lat)
        Log.d("WSWSWS3",  lon)
        val recyclerView: RecyclerView? = view.findViewById(R.id.forecast_recycler_view)
        recyclerView?.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        //val newsList: ArrayList<ForecastInfo> =
            //val dispose = urlForecast(lat, lon)
        val dispose = if(savedInstanceState!=null)
        {
            urlForecast(lat2,lon2)
        }
        else{
            urlForecast(lat,lon)
        }
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    recyclerView?.adapter = ForecastAdapter(it)
                },{
                Toast.makeText(context,"Возникла ошибка, попробуйте перезагрузить", Toast.LENGTH_SHORT).show()
                },{})


        return view
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        Log.d("WSWS","onSaveInstanceState")
        super.onSaveInstanceState(savedInstanceState)
        sp = this.activity?.getSharedPreferences("pref", Context.MODE_PRIVATE)
        lat = sp?.getString("lat",null).toString()
        lon = sp?.getString("lon",null).toString()
        savedInstanceState.putString("lat",lat)
        Log.d("WSWSWS",lat)
        savedInstanceState.putString("lon",lon)
        Log.d("WSWSWS",lon)
    }

}