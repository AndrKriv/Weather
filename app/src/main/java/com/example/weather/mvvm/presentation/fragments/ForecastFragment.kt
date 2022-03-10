package com.example.weather.mvvm.presentation.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.weather.R
import com.example.weather.databinding.FragmentForecastBinding
import com.example.weather.mvvm.core.ForecastInfo
import com.example.weather.mvvm.core.ForecastList
import com.example.weather.mvvm.data.APIService
import com.example.weather.mvvm.domain.viewBinding
import com.example.weather.mvvm.presentation.adapter.ForecastAdapter
import com.example.weather.mvvm.presentation.viewmodel.ForecastViewModel
import com.example.weather.objects.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class ForecastFragment : Fragment(R.layout.fragment_forecast) {

    lateinit var recyclerAdapter: ForecastAdapter

    var list: ArrayList<ForecastInfo> = ArrayList()
    private val BASE_URL = "https://api.openweathermap.org/data/2.5/"

    private val disposable = CompositeDisposable()

    private val binding: FragmentForecastBinding by viewBinding(FragmentForecastBinding::bind)

    private lateinit var forecastVM: ForecastViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //api.openweathermap.org/data/2.5/weather?lat=35&lon=139&appid={API key}
        super.onViewCreated(view, savedInstanceState)
        val requestInterface = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(APIService::class.java)
        //lat={lat}&lon=28.3&appid=${ Constants.KEY}&units=metric&lang=ru
        val observable = requestInterface.getForecastData(
            SharedPreference(this.requireContext()).getValueString(Constants.latitude).toString(),
            SharedPreference(this.requireContext()).getValueString(Constants.longitude).toString(),
            Constants.KEY,
            "metric",
            "ru"
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(this::handleResponse)

        disposable.add(observable)

        recyclerAdapter = ForecastAdapter(list)
        binding.forecastRecyclerView.adapter = recyclerAdapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //binding.forecastRecyclerView.adapter = ForecastAdapter(ArrayList<ForecastInfo>())

//        vm = ViewModelProvider(this).get(ForecastViewModel::class.java)
//        vm.loadData()
//
//        binding.forecastRecyclerView.adapter = ForecastAdapter(vm.loadData())
    }


    private fun handleResponse(userList: ForecastList) {
        userList.list.let { list.addAll(it) }
        recyclerAdapter.notifyDataSetChanged()
    }

    class SharedPreference(val context: Context) {
        private val PREFS_NAME = Constants.preferencesName
        val sharedPref: SharedPreferences =
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        fun getValueString(KEY_NAME: String): String? {
            return sharedPref.getString(KEY_NAME, null)
        }
    }


}