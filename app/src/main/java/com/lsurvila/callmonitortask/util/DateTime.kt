package com.lsurvila.callmonitortask.util

import java.text.SimpleDateFormat
import java.util.*

object DateTime {

    fun now() = Date()
    fun formatLong(dateTime: Date): String =
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault()).format(dateTime)
    private fun format(dateTime: Date): String =
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(dateTime)
    fun formatNow() = format(now())
}