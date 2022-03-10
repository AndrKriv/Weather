package com.example.weather.mvvm.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.OWM.ImageChecker
import com.example.weather.databinding.ItemForecastBinding
import com.example.weather.mvvm.core.ForecastInfo

class ForecastViewHolder(private val binding: ItemForecastBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindView(item: ForecastInfo) {
        with(binding) {
            timeTv.text = item.time
            degreesTv.text = item.temp.degrees
            descriptionTv.text = item.weather.single().description
            weatherImg.setImageResource(ImageChecker.imageWeather(descriptionTv.text.toString()))
        }
    }
}

class ForecastAdapter(private val owmList: List<ForecastInfo>) :
    RecyclerView.Adapter<ForecastViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        return ForecastViewHolder(
            ItemForecastBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) =
        holder.bindView(owmList[position])

    override fun getItemCount() = owmList.size
}