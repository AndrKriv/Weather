package com.example.weather.share

import android.content.Intent

object ShareText {

    fun createSharingIntent(intent: Intent, txt: String): Intent {
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_TEXT, txt)
        intent.type = "text/plain"
        return Intent.createChooser(intent, "Launch")
    }
}