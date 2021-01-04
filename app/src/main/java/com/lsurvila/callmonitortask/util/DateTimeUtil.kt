package com.lsurvila.callmonitortask.util

import java.text.SimpleDateFormat
import java.util.*

class DateTimeUtil {

    companion object {
        fun formatDateTimeLong(dateTime: Date): String {
            return SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault()).format(dateTime)
        }

        fun formatDateTime(dateTime: Date): String {
            return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(dateTime)
        }
    }
}