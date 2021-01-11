package com.lsurvila.callmonitortask.di

import android.os.Build

class VersionUtil {

    companion object {
        fun isNOrLater() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
        fun isQOrLater() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
    }
}