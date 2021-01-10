package com.lsurvila.callmonitortask.service.network

import java.net.URI

interface NetworkService {
    fun isWifiConnected(): Boolean
    suspend fun getWifiAddress(port: Int): URI?
}