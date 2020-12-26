package com.lsurvila.callmonitortask.util

import android.content.Context
import android.content.Context.WIFI_SERVICE
import android.net.Uri
import android.net.wifi.WifiManager
import java.math.BigInteger
import java.net.InetAddress
import java.nio.ByteOrder


class NetworkUtil {

    companion object {
        fun getWifiIpAddress(context: Context): Int {
            val wifiManager = context.applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
            return wifiManager.connectionInfo.ipAddress
        }

        fun ipToHostAddress(ip: Int): String {
            var ipAddress = ip
            if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
                ipAddress = Integer.reverseBytes(ip)
            }
            val ipByteArray: ByteArray = BigInteger.valueOf(ipAddress.toLong()).toByteArray()
            return InetAddress.getByAddress(ipByteArray).hostAddress
        }

        fun formatUri(scheme: String, authority: String, port: Int, method: String? = null): String {
            val uri = Uri.Builder()
                .scheme(scheme)
                .encodedAuthority("${authority}:${port}")
            method?.let {
                uri.appendPath(method)
            }
            return uri.toString()
        }
    }
}