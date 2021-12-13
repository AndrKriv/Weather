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
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import com.example.weather.BottomActivity
import com.example.weather.OWM.ImageChecker
import com.example.weather.R
import com.example.weather.share.ShareText
import com.example.weather.share.createSharingString
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class TodayFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val bundle = arguments
        val message = bundle?.getString("message")
        val string = message
        Log.d("TXT", string.toString())
//        val a = BottomActivity()
//        a.string?.let { Log.d("TXT", it+" mess") }

        var str:String = ""
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_today, container, false)
        val tvCity = view.findViewById<TextView>(R.id.tv_city)
        val tvDegrees = view.findViewById<TextView>(R.id.tv_degrees)
        val tvDescription = view.findViewById<TextView>(R.id.tv_decription)
        val tvDate = view.findViewById<TextView>(R.id.tv_date)
        val btnCheck = view.findViewById<Button>(R.id.share)
        val ivDescr = view.findViewById<ImageView>(R.id.iv_img)
        val mTWD= TodayWeatherReader()



        val dispose = mTWD.urlTodayWeather()
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
                Log.e("XRENb2", it.localizedMessage)
            },{
                //make something
            })

        btnCheck.setOnClickListener {
            val intent=Intent()
            startActivity(ShareText.sendHard(intent,str
                    +"\nБыло отправлено с помощью приложения Weather"))
            Log.d("TPTR2",str) }
        return view
    }

}
