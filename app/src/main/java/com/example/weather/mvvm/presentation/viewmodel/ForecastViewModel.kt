package com.example.weather.mvvm.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.mvvm.core.ForecastInfo
import com.example.weather.mvvm.data.APIService
import com.example.weather.mvvm.presentation.adapter.ForecastAdapter
import io.reactivex.disposables.CompositeDisposable

class ForecastViewModel : ViewModel() {

    private var myAdapter: ForecastAdapter? = null
    private var myCompositeDisposable: CompositeDisposable? = null
    private var myRetroCryptoArrayList: ArrayList<ForecastInfo>? = null
    private lateinit var recyclerView:RecyclerView

    lateinit var mService: APIService

    //-----------------------------------------------------------------
//    fun loadData() {
//    }

}