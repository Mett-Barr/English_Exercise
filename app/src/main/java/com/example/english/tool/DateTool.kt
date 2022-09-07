package com.example.english.tool

import java.text.SimpleDateFormat
import java.util.*

fun utcToGmt(utc: String): String {
    val utcFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }
    val sdf = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
//            .apply { timeZone = TimeZone.getTimeZone("UTC") }
    val date = utcFormat.parse(utc)?.let { sdf.format(it) }
    return date ?: ""
}

fun getDate(): String {
    val sdf = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
    val calendar = Calendar.getInstance()
    return sdf.format(calendar.time)
}

//object DateTool {
//    val sdf = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
//}