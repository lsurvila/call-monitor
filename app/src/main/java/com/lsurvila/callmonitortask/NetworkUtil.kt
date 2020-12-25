package com.lsurvila.callmonitortask

import android.content.Context
import android.content.Context.WIFI_SERVICE
import android.net.wifi.WifiManager


class NetworkUtil {

    companion object {
        fun getWifiIpAddress(context: Context): Int {
            val wifiManager = context.applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
            return wifiManager.connectionInfo.ipAddress
        }
    }
}