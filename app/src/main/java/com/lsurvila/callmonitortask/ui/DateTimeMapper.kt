package com.lsurvila.callmonitortask.ui

import java.text.SimpleDateFormat
import java.util.*

class DateTimeMapper {

    fun mapDateTimeLong(dateTime: Date): String {
        return SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault()).format(dateTime)
    }

    fun mapDateTime(dateTime: Date): String {
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(dateTime)
    }
}