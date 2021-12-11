package com.example.weather.share

import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import com.example.weather.R



object ShareText{
    fun sendSimple(intent: Intent,txt:String):Intent {
        intent.setAction(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_TEXT, txt)
        intent.setType("text/plain")
        return intent
    }
    fun sendHard(intent: Intent,txt:String):Intent {
        intent.setAction(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_TEXT, txt)
        intent.setType("text/plain")
        val a = Intent.createChooser(intent,"Launch")
        return a
    }
}