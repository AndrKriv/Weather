package com.example.weather.ui.today

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.weather.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class TodayFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_today, container, false)
        val tv = view.findViewById<TextView>(R.id.highText)
        val tvCenter = view.findViewById<TextView>(R.id.centerText)
        val mTWD= TodayWeatherReader()
        val dispose = mTWD.urlTodayWeather()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                tv?.text=it[0]
                tvCenter?.text=it[1]+" - | - "+it[2]
            },{
                Log.e("XRENb2", it.localizedMessage)
            },{
                //make something
            })
        return view
    }

}