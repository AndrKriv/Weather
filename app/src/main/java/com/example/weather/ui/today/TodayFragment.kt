package com.example.weather.ui.today

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.weather.OWM.ImageChecker
import com.example.weather.R
import com.example.weather.share.ShareText
import com.example.weather.share.createSharingString
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.weather.BottomActivity
import com.example.weather.ui.forecast.urlForecast

class TodayFragment : Fragment() {
    var lat = ""
    var lon = ""
    var sp: SharedPreferences? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("WSWS","onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("WSWS","onCreate")

//        if (savedInstanceState == null) {
//            Log.d("WSWS","savedInstanceState is null")
//        } else {
//            Log.d("WSWS","savedInstanceState is not null")
//            lat2 = savedInstanceState.getString("lat","a").toString()
//            lon2 = savedInstanceState.getString("lon","b").toString()
//            Log.d("WSWS","RTY "+savedInstanceState.getString(lat2).toString())
//        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_today, container, false)

        val lat2 = savedInstanceState?.getString("lat").toString()
        val lon2 = savedInstanceState?.getString("lon").toString()
        Log.d("WSWSWS2",  lat2)
        Log.d("WSWSWS2",  lon2)

        Log.d("WSWS","onCreateView")
        var str:String = ""

        val tvCity = view.findViewById<TextView>(R.id.tv_city)
        val tvDegrees = view.findViewById<TextView>(R.id.tv_degrees)
        val tvDescription = view.findViewById<TextView>(R.id.tv_decription)
        val tvDate = view.findViewById<TextView>(R.id.tv_date)
        val btnCheck = view.findViewById<Button>(R.id.share)
        val ivDescr = view.findViewById<ImageView>(R.id.iv_img)

        sp = this.activity?.getSharedPreferences("pref", Context.MODE_PRIVATE)

        lat = sp?.getString("lat",null).toString()
        lon = sp?.getString("lon",null).toString()

        Log.d("WSWS","RTY "+savedInstanceState?.getString(lat2).toString())
        Log.d("RTY2",lat)
        Log.d("RTY2",lon)
        val dispose = if(savedInstanceState!=null)
        {
            urlTodayWeather(lat2,lon2)
        }
        else{
            urlTodayWeather(lat,lon)
        }
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                tvCity?.text=it[0]
                tvDegrees?.text=it[1]
                tvDescription?.text=it[2]
                tvDate?.text=it[3]
                ivDescr?.setImageResource(ImageChecker.imageWeather(tvDescription?.text.toString()))
                str=createSharingString.wthString(it[0],it[1],it[2])
            },{
                Toast.makeText(context,"Возникла ошибка, попробуйте перезагрузить", Toast.LENGTH_SHORT).show()
                Log.e("XRENb2", it.localizedMessage)
            },{
                //make something
            })

        btnCheck.setOnClickListener {
            Log.d("TPTR2",str)
            val intent=Intent()
            startActivity(ShareText.sendHard(intent,str
                    +"\nБыло отправлено с помощью приложения Weather"))
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d("WSWS","onActivityCreated")
    }

    override fun onStart() {
        super.onStart()
        Log.d("WSWS","onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("WSWS","onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("WSWS","onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("WSWS","onStop")
    }


    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("WSWS","onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("WSWS","onDestroy")
        context?.getSharedPreferences ("pref", 0)?.edit()?.clear()?.commit ()
        //call destroy sh
        var editor = sp?.edit()
        editor?.clear()
        editor?.remove("pref")
        Log.d("WSWS","прошел?")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("WSWS","onDetach/")
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        Log.d("WSWS","onViewStateRestored")
        super.onViewStateRestored(savedInstanceState)
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        Log.d("WSWS","onSaveInstanceState")
        super.onSaveInstanceState(savedInstanceState)
            //sp = this.activity?.getSharedPreferences("pref", Context.MODE_PRIVATE)
        lat = sp?.getString("lat",null).toString()
        lon = sp?.getString("lon",null).toString()
        savedInstanceState.putString("lat",lat)
        Log.d("WSWSW",lat)
        savedInstanceState.putString("lon",lon)
        Log.d("WSWSW",lon)
    }
}