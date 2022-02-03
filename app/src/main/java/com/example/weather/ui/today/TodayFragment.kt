package com.example.weather.ui.today

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.weather.R
import com.example.weather.databinding.FragmentForecastBinding
import com.example.weather.databinding.FragmentTodayBinding
import com.example.weather.objects.toAllProject
import com.example.weather.share.ShareText
import com.example.weather.share.createSharingString
import com.example.weather.ui.viewBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class TodayFragment : Fragment(R.layout.fragment_today) {
    private var lat = ""
    private var lon = ""
    var sharedPreferences: SharedPreferences? = null

    //private val binding: FragmentTodayBinding by viewBinding (FragmentTodayBinding::bind)

    private val binding: FragmentTodayBinding by viewBinding {  FragmentTodayBinding.bind(
        requireView())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var stringToShare = ""

        val tvCity = binding.tvCity
        val tvTemp = binding.tvTemp
        val tvDescription = binding.tvDecription
        val tvDate = binding.tvDate

        val tvPressure =  binding.tvPressure
        val tvHumidity =  binding.tvHumidity
        val tvWindD =  binding.tvWind
        val tvWindSpeed =  binding.tvWindSpeed

        //val btnCheck =  binding.share
        val ivDescr =  binding.ivImg

//        binding.extendedFab?.setOnClickListener {
//            val intent=Intent()
//            startActivity(ShareText.sendWeather(intent,stringToShare+"\nБыло отправлено с помощью приложения Weather"))
//        }

        binding.share.setOnClickListener {
            val intent=Intent()
            startActivity(ShareText.sendWeather(intent,stringToShare+"\nБыло отправлено с помощью приложения Weather"))
        }
    }

//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        val view: View = inflater.inflate(R.layout.fragment_today, container, false)
//
//        val lat2 = savedInstanceState?.getString(toAllProject.latitude).toString()
//        val lon2 = savedInstanceState?.getString(toAllProject.longitude).toString()
//        var stringToShare:String = ""
//
////        val tvCity = view.findViewById<TextView>(R.id.tv_city)
////        val tvTemp = view.findViewById<TextView>(R.id.tv_temp)
////        val tvDescription = view.findViewById<TextView>(R.id.tv_decription)
////        val tvDate = view.findViewById<TextView>(R.id.tv_date)
////
////        val tvPressure = view.findViewById<TextView>(R.id.tv_pressure)
////        val tvHumidity = view.findViewById<TextView>(R.id.tv_humidity)
////        val tvWindD = view.findViewById<TextView>(R.id.tv_wind)
////        val tvWindSpeed = view.findViewById<TextView>(R.id.tv_wind_speed)
////
////        val btnCheck = view.findViewById<Button>(R.id.share)
////        val ivDescr = view.findViewById<ImageView>(R.id.iv_img)
//
//        sharedPreferences = this.activity?.getSharedPreferences(toAllProject.prefName, Context.MODE_PRIVATE)
//        lat = sharedPreferences?.getString(toAllProject.latitude,null).toString()
//        lon = sharedPreferences?.getString(toAllProject.longitude,null).toString()
//
//        val dispose = if(savedInstanceState!=null)
//        {
//            urlTodayWeather(lat2,lon2)
//        }
//        else{
//            urlTodayWeather(lat,lon)
//        }
//            .subscribeOn(Schedulers.newThread())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({
////                tvCity?.text=it[0]
////                tvTemp?.text=it[1]+"°С"
////                tvDescription?.text="На улице ${it[2]}"
////                tvDate?.text=it[3]
////                tvPressure?.text="Давление "+it[4]
////                tvHumidity?.text="Влажность "+it[5]+"%"
////                tvWindD?.text="Ветер ${it[6]}"
////                tvWindSpeed?.text="Скорость ветра\n"+it[7]+" м/c"
////                ivDescr?.setImageResource(ImageChecker.imageWeather(it[2]))
//                stringToShare = createSharingString.wthString(it[0],it[1],it[2],it[6],it[7],it[5],it[4])
//            },{
//                Toast.makeText(context,R.string.tap, Toast.LENGTH_LONG).show()
//            },{
//                //make something
//            })
//
////        btnCheck.setOnClickListener {
////            val intent=Intent()
////            startActivity(ShareText.sendWeather(intent,stringToShare+"\nБыло отправлено с помощью приложения Weather"))
////        }
//
//        return view
//    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        lat = sharedPreferences?.getString(toAllProject.latitude,"").toString()
        lon = sharedPreferences?.getString(toAllProject.longitude,"").toString()
        savedInstanceState.putString(toAllProject.latitude,lat)
        savedInstanceState.putString(toAllProject.longitude,lon)
    }
}