package com.lsurvila.callmonitortask.service.network

interface NetworkService {
    fun isWifiConnected(): Boolean
    fun getWifiIpAddress(): String?
}