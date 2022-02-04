package com.example.weather.ui.forecast

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.OWM.ImageChecker
import com.example.weather.databinding.ItemForecastBinding

class ForecastAdapter(private val owList: ArrayList<ForecastInfo>) :
    RecyclerView.Adapter<ForecastAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemForecastBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemForecastBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.timeTv.text = owList[position].time
        holder.binding.degreesTv.text = owList[position].degrees
        holder.binding.descriptionTv.text = owList[position].description
        holder.binding.weatherImg.setImageResource(ImageChecker.imageWeather(holder.binding.descriptionTv.text.toString()))
    }

    override fun getItemCount() = owList.size
}