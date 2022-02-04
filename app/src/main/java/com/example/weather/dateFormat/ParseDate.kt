package com.example.weather

import java.text.SimpleDateFormat
import java.util.*

//extensions-fun преобразовать toDate
//как utils в java
fun parseDate(startDate:String):String {
    val oldDateFormat = SimpleDateFormat(startDateFormat(), Locale.getDefault())
    val newDateFormat = SimpleDateFormat(usefullDateFormat(), Locale.getDefault())
    val date = oldDateFormat.parse(startDate)
    val result = newDateFormat.format(date)
    return result
}
fun today():String{
    val onlyDate = SimpleDateFormat(usefullDateFormat()).format(Date())
    val date = onlyDate
    return date
}

fun usefullDateFormat() = "HH:mm, dd.MM.yyyy"

fun startDateFormat() = "yyyy-MM-dd HH:mm:ss"