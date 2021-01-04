package com.lsurvila.callmonitortask.service.network

import android.net.ConnectivityManager
import android.net.NetworkCapabilities


class AndroidNetworkService(private val connectivityManager: ConnectivityManager?): NetworkService {

    override fun isWifiConnected(): Boolean {
        val activeNetwork = connectivityManager?.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
    }
}