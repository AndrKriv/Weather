package com.example.weather.share

import android.content.Intent

object ShareText {

    fun sendWeather(intent: Intent, txt: String): Intent {
        intent.setAction(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_TEXT, txt)
        intent.setType("text/plain")
        val a = Intent.createChooser(intent, "Launch")
        return a
    }
}