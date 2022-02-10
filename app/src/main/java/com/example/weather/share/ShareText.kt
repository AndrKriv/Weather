package com.example.weather.share

import android.content.Intent

object ShareText {

    fun createSharingIntent(
        messageText: String
    ) = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, messageText)
            type = "text/plain"
    }
}
