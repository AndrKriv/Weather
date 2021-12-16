package com.example.weather.ui.forecast

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.example.weather.OWM.ImageChecker
import com.example.weather.R


class ForecastAdapter(
    private val owList: ArrayList<ForecastInfo>
) : RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>() {

    class ForecastViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

         val time: TextView = itemView.findViewById(R.id.time_tv)
         val description: TextView = itemView.findViewById(R.id.description_tv)
         val degrees: TextView = itemView.findViewById(R.id.degrees_tv)
         val wthImg:ImageView = itemView.findViewById(R.id.weather_img)

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
        //val ow = ForecastInfo()
        //holder.bind()=ow[position]
        holder.time.text=owList[position].time
        holder.degrees.text =owList[position].degrees
        holder.description.text =owList[position].description
        owList[position].description.let {
            ImageChecker.imageWeather(
                it
            )
        }.let { holder.wthImg.setImageResource(it) }
    }

    override fun getItemCount() = owList.size
}