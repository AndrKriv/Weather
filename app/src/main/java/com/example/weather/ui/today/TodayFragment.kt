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
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.weather.BottomActivity

class TodayFragment : Fragment() {
    var lat = ""
    var lon = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("WSWS","onCreate")
        super.onCreate(savedInstanceState)
        arguments?.let { args ->
            lat = args.getString("lat").toString()
            lon = args.getString("lon").toString()
        }
        //retainInstance = true
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("WSWS","onCreateView")

        var str:String = ""
        val view: View = inflater.inflate(R.layout.fragment_today, container, false)
        val tvCity = view.findViewById<TextView>(R.id.tv_city)
        val tvDegrees = view.findViewById<TextView>(R.id.tv_degrees)
        val tvDescription = view.findViewById<TextView>(R.id.tv_decription)
        val tvDate = view.findViewById<TextView>(R.id.tv_date)
        val btnCheck = view.findViewById<Button>(R.id.share)
        val ivDescr = view.findViewById<ImageView>(R.id.iv_img)


        val str1 = savedInstanceState?.getString("lat").toString()
        val str2 = savedInstanceState?.getString("lon").toString()
//        val bundle = arguments
//        val message1 = bundle?.getString("lat")
//        val lat1 = message1.toString()
//        val message2 = bundle?.getString("lon")
//        val lon1 = message2.toString()


//        val bundle:Bundle? = arguments
//        val message1 = bundle?.getString("lat")
//        val message2 = bundle?.getString("lon")
//        lat = message1.toString()
//        lon = message2.toString()

        //val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        //val defaultValue = resources.getInteger(R.integer.saved_high_score_default_key)
        //val highScore = sharedPref?.getInt(getString(R.string.saved_high_score_key), defaultValue)



        var f: SharedPreferences? = this.getActivity()?.getSharedPreferences("pref", Context.MODE_PRIVATE)
        lat = f?.getString("lat",null).toString()
        lon = f?.getString("lon",null).toString()

        Log.d("RTY",savedInstanceState?.getString(str1).toString())
        Log.d("RTY2",lat)
        Log.d("RTY2",lon)
        val dispose = if(savedInstanceState!=null)
        {urlTodayWeather(savedInstanceState.getString(str1).toString(),savedInstanceState.getString(str2).toString())}
        else{urlTodayWeather(lat,lon)}
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
               // tvCity?.text=m
//                tvDegrees?.text=it[1]
//                tvDescription?.text=it[2]
//                tvDate?.text=it[3]
//                ivDescr?.setImageResource(ImageChecker.imageWeather(tvDescription?.text.toString()))
//                str=createSharingString.wthString(m.toString(),m.toString(),m.toString())
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

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        Log.d("WSWS","onSaveInstanceState")
        super.onSaveInstanceState(savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("WSWS","onDestroyView")
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        Log.d("WSWS","onViewStateRestored")
        super.onViewStateRestored(savedInstanceState)
    }
    companion object {
        private const val STR_LAT = "lat"
        private const val STR_LON = "lon"
        fun newInstance(list1: String,list2: String): TodayFragment {
            val fragment = TodayFragment()
            val args = Bundle()
            args.getString(STR_LAT, list1)
            args.getString(STR_LON, list2)

            fragment.arguments = args
            return fragment
        }
    }
}