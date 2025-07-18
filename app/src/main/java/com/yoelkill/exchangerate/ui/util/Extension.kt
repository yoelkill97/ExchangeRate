package com.yoelkill.exchangerate.ui.util


import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.formatTimestamp() : String {
    val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm:ss", Locale.getDefault())
    val netDate = Date(this)
    return sdf.format(netDate)
}