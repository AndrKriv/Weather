package com.example.weather.mvvm.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.databinding.ItemForecastBinding
import com.example.weather.dateFormat.toDate
import com.example.weather.mvvm.core.ForecastInfo
import com.example.weather.mvvm.presentation.viewmodel.TodayViewModel

class ForecastViewHolder(private val binding: ItemForecastBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindView(item: ForecastInfo) {
        with(binding) {
            timeTv.text = item.time.toDate()
            degreesTv.text = item.temp.degrees
            descriptionTv.text = item.weather.single().description
            weatherImg.setImageResource(TodayViewModel().loadImg(item.weather.single().description)
            )
        }
    }
}

class ForecastAdapter : RecyclerView.Adapter<ForecastViewHolder>() {

    private val forecastList = mutableListOf<ForecastInfo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder =
        ForecastViewHolder(
            ItemForecastBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) =
        holder.bindView(forecastList[position])

    override fun getItemCount() = forecastList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(items: List<ForecastInfo>) {
        forecastList.clear()
        forecastList.addAll(items)
        notifyDataSetChanged()
    }
}