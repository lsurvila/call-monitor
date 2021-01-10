package com.lsurvila.callmonitortask.service.network

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import android.util.Log
import java.math.BigInteger
import java.net.InetAddress
import java.net.UnknownHostException
import java.nio.ByteOrder

private const val TAG = "NetworkService"

class AndroidNetworkService(
    private val connectivityManager: ConnectivityManager?,
    private val wifiService: WifiManager
) : NetworkService {

    override fun isWifiConnected(): Boolean {
        val activeNetwork = connectivityManager?.activeNetwork ?: return false
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
    }

    override fun getWifiIpAddress(): String? {
        var ipAddress: String? = null
        var ipAddressInt = wifiService.connectionInfo.ipAddress

        // Convert little-endian to big-endian if needed
        if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
            ipAddressInt = Integer.reverseBytes(ipAddressInt)
        }

        val ipByteArray: ByteArray = BigInteger.valueOf(ipAddressInt.toLong()).toByteArray()

        try {
            ipAddress = InetAddress.getByAddress(ipByteArray).hostAddress
        } catch (ex: UnknownHostException) {
            Log.e(TAG, "Failed to resolve ip address", ex)
        }

        return ipAddress
    }
}