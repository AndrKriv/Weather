package com.example.weather.share

object createSharingString {//нэйминг с большой буквы
    fun wthString(city:String,degrees:String,description:String, wind:String, speedWind:String, humidity:String, pressure:String):String
    = "В "+city+ " сейчас "+degrees+"°C, на улице " + description + ", ветер "+ wind +", и скорость ветра "+speedWind+" м/с"+
                "\nВлажность " + humidity +"%, давление " +pressure
}


