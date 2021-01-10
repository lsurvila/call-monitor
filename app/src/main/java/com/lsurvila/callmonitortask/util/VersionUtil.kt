package com.lsurvila.callmonitortask.util

import android.os.Build

class VersionUtil {

    companion object {
        fun isN() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
        fun isQ() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
    }
}