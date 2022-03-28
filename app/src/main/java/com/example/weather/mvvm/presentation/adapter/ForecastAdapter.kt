package com.example.weather.mvvm.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.databinding.ItemForecastBinding
import com.example.weather.mvvm.core.ForecastInfo
import com.example.weather.utils.loadImg
import com.example.weather.utils.toDate
import javax.inject.Inject

class ForecastViewHolder @Inject constructor(
    private val binding: ItemForecastBinding
    //,private val api: ApiService
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindView(item: ForecastInfo) {
        with(binding) {
            timeTv.text = item.time.toDate()
            degreesTv.text = item.temp.degrees
            descriptionTv.text = item.weather.single().description
            weatherImg.setImageResource(
                loadImg(item.weather.single().description)
            )
        }
    }
}

class ForecastAdapter : RecyclerView.Adapter<ForecastViewHolder>() {

    private val items = mutableListOf<ForecastInfo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder =
        ForecastViewHolder(
            ItemForecastBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) =
        holder.bindView(items[position])

    override fun getItemCount() = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(items: List<ForecastInfo>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }
}