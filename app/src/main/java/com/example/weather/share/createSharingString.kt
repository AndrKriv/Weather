package com.example.weather.share

object createSharingString {
    fun wthString(city:String,degrees:String,description:String):String =
        "Сейчас в "+city+ " "+degrees+" °C, и на улице "+description
}


