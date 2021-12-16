package com.example.weather

import java.text.SimpleDateFormat
import java.util.*

fun parseDate(startDate:String):String {
    val oldDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val newDateFormat = SimpleDateFormat("HH:mm, dd MMMM yyyy", Locale.getDefault())
    val date = oldDateFormat.parse(startDate)
    val result = newDateFormat.format(date)
    return result
}
fun today():String{
    val onlyDate = SimpleDateFormat("dd.MM.yyyy").format(Date())
    val onlyTime = SimpleDateFormat("HH:mm").format(Date())
    val date =  onlyTime+" "+onlyDate
    return date
}