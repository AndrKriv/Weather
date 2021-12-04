package com.example.weather.ui.forecast

data class OpenWeather(val time:String, val sost:String){
    companion object{
        fun albumList(): List<OpenWeather> {
            return listOf(
                OpenWeather("11:00","Облачно"),
                OpenWeather("12:00","Дождь"),
                OpenWeather("13:00","Снег"),
                OpenWeather("14:00","Град"),
                OpenWeather("15:00","Солнечно"),
                OpenWeather("16:00","Туман"),
                OpenWeather("17:00", "Снег с дождем")

            )
        }
    }
}
