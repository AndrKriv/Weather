package com.example.weather.utils

import android.view.View

fun View.isVisible() {
    this.visibility = View.VISIBLE
}

fun View.isGone() {
    this.visibility = View.GONE
}