package com.example.weather.ui.forecast

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R


class ForeCastAdapter(
    private val owList: List<OpenWeather>
) : RecyclerView.Adapter<ForeCastAdapter.ForecastViewHolder>() {

    class ForecastViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        private val time: TextView = itemView.findViewById(R.id.item_time)
        private val sost: TextView = itemView.findViewById(R.id.item_weath_sost)

        fun bind(ow: OpenWeather) {
            time.text = ow.time
            sost.text = ow.sost
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ForecastViewHolder {
        return ForecastViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_forecast,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        val ow: OpenWeather = owList[position]
        holder.bind(ow)
    }

    override fun getItemCount() = owList.size
}