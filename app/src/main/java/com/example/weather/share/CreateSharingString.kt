package com.example.weather.share

object CreateSharingString {
    fun wthString(
        city: String,
        degrees: String,
        description: String,
        wind: String,
        speedWind: String,
        humidity: String,
        pressure: String
    ) =
        """
            В $city сейчас $degrees°C, на улице $description,
            ветер $wind, и скорость ветра $speedWind м/с
            Влажность $humidity%, давление $pressure
        """.trimIndent()
}


