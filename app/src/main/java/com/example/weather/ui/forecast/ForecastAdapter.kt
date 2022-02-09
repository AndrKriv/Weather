package com.example.weather.ui.forecast

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.OWM.ImageChecker
import com.example.weather.databinding.ItemForecastBinding

class ForecastViewHolder(val binding: ItemForecastBinding) : RecyclerView.ViewHolder(binding.root)

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

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        with(holder.binding){
            timeTv.text = owList[position].time
            degreesTv.text = owList[position].degrees
            descriptionTv.text = owList[position].description
            weatherImg.setImageResource(ImageChecker.imageWeather(descriptionTv.text.toString()))
        }
    }

    override fun getItemCount() = owList.size
}