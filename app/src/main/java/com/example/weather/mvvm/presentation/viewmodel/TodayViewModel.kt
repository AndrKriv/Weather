package com.example.weather.mvvm.presentation.viewmodel

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather.R
import com.example.weather.mvvm.core.TodayInfo
import com.example.weather.mvvm.data.APIService
import com.example.weather.mvvm.presentation.fragments.TodayFragment
import com.example.weather.objects.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class TodayViewModel : ViewModel() {
    private val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    val todayInfo = MutableLiveData<TodayInfo>()
    //private val location = MutableLiveData<com.example.weather.mvvm.domain.models.Location>
    private val disposable = CompositeDisposable()
    //private val getCurrentWeatherUseCase= GetCurrentWeatherUseCase()
    //private val currentWeather:CurrentWeather()

    lateinit var mService: APIService

    private fun send(messageText: String) = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, messageText)
        type = "text/plain"

    }

    fun sendInfoChooser(str: String): Intent {
        return Intent.createChooser(
            send(str), "Launch"
        )
    }

    private fun readDescription(): String? {
        return todayInfo.value?.weather?.get(0)?.description
    }

    fun loadImage(): Int {
        return when (readDescription()) {
            "ясно" -> R.drawable.sun
            "пасмурно" -> R.drawable.cloudy
            "облачно с прояснениями" -> R.drawable.cloud_sun
            "небольшая облачность" -> R.drawable.cloud
            "переменная облачность" -> R.drawable.cloud
            "сильный дождь" -> R.drawable.rain
            "дождь" -> R.drawable.rain
            "небольшой дождь" -> R.drawable.rain_small
            "проливной дождь" -> R.drawable.rain
            "небольшой снег" -> R.drawable.small_snow
            "снег" -> R.drawable.snowing
            "снег с дождем" -> R.drawable.snow_rain
            "туман" -> R.drawable.fog
            else -> R.drawable.unknown
        }
    }

//    object ShareText {
//        fun createSharingIntent(
//            messageText: String
//        ) = Intent().apply {
//            action = Intent.ACTION_SEND
//            putExtra(Intent.EXTRA_TEXT, messageText)
//            type = "text/plain"
//        }
//    }

    fun getTodayData(context: Context
                    // , lat: String, lon: String
    ) {
        if (SharedPreference(context).getValueString(Constants.latitude).toString()
                .isEmpty() ||
            SharedPreference(context).getValueString(Constants.longitude).toString()==""
        ) {
            Toast.makeText(context, "SharedPref is Empty!", Toast.LENGTH_SHORT).show()
        } else {
            val requestInterface = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(APIService::class.java)

            val observable = requestInterface.getTodayData(
                SharedPreference(context)
                    .getValueString(Constants.latitude).toString(),
                SharedPreference(context)
                    .getValueString(Constants.longitude).toString(),
               // lat, lon,
                Constants.KEY,
                "metric",
                "ru"
            )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse)

            disposable.add(observable)
            }
    }

    private fun handleResponse(userList: TodayInfo) {
        todayInfo.value = userList
    //        with(binding) {
    //            tvCity.text = userList.city
    //            tvDate.text = (userList.date)//.timestampConverter()
    //            tvTemp.text = userList.main.temp
    //            tvDescription.text = userList.weather[0].description
    //            tvHumidity.text = userList.main.humidity
    //            tvPressure.text = userList.main.pressure
    //            tvWindSpeed.text = userList.wind.speed
    //        }
    }

    class SharedPreference(val context: Context) {
        private val PREFS_NAME = Constants.preferencesName
        val sharedPref: SharedPreferences =
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        fun getValueString(KEY_NAME: String): String? {
            return sharedPref.getString(KEY_NAME, null)
        }
    }

    object CreateSharingString {
        fun wthString(//currentWeather: CurrentWeather,
        city: String,
        degrees: String,
        description: String,
        //wind: String,
        windSpeed: String,
        humidity: String,
        pressure: String
        ) =
//            """
//            В ${currentWeather.city} сейчас ${currentWeather.degrees}°C, на улице ${currentWeather.description},
//            , скорость ветра ${currentWeather.windSpeed} м/с
//            Влажность ${currentWeather.humidity}%, давление ${currentWeather.pressure}
//        """.trimIndent()
        """
            В $city сейчас $degrees°C, на улице $description,
            скорость ветра $windSpeed м/с.
            Влажность $humidity%, давление $pressure
        """.trimIndent()
    }

    override fun onCleared() {
        super.onCleared()
        Log.e("AAA","vm cleared")
    }
}