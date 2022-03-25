package com.example.weather.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

private const val DATE_FORMAT_PATTERN = "HH:mm, dd.MM.yyyy"
private const val OLD_DATE_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss"

fun String.toDate(): String {
    val oldDateFormat = SimpleDateFormat(OLD_DATE_FORMAT_PATTERN, Locale.getDefault())
    val newDateFormat = SimpleDateFormat(DATE_FORMAT_PATTERN, Locale.getDefault())
    val date = oldDateFormat.parse(this)
    return newDateFormat.format(date)
}

@SuppressLint("SimpleDateFormat")
fun String.today(): String = SimpleDateFormat(DATE_FORMAT_PATTERN).format(Date())