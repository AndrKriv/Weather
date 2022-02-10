package com.example.weather.ui.forecast

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.OWM.ImageChecker
import com.example.weather.databinding.ItemForecastBinding

class ForecastViewHolder(private val binding: ItemForecastBinding) : RecyclerView.ViewHolder(binding.root){

    fun bindView(item: ForecastInfo) {
        with(binding) {
            timeTv.text  = item.time
            degreesTv.text = item.degrees
            descriptionTv.text = item.description
            weatherImg.setImageResource(ImageChecker.imageWeather(descriptionTv.text.toString()))
        }
    }
}

class ForecastAdapter(private val owList: ArrayList<ForecastInfo>) :
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

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) = holder.bindView(owList[position])

    override fun getItemCount() = owList.size
}