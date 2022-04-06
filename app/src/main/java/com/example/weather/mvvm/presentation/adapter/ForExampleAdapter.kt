package com.example.weather.mvvm.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.databinding.ItemForecastBinding
import com.example.weather.mvvm.presentation.UIModel
import com.example.weather.utils.loadImg

class ForecastViewHolder(private val binding: ItemForecastBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindView(item: UIModel) {
        with(binding) {
            timeTv.text = item.date
            degreesTv.text = item.degrees.toString()
            descriptionTv.text = item.description
            weatherImg.setImageResource(
                loadImg(item.description)
            )
        }
    }
}

class ForExampleAdapter : RecyclerView.Adapter<ForecastViewHolder>() {

    private val items = mutableListOf<UIModel>()

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
    fun setItems(items: List<UIModel>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }
}