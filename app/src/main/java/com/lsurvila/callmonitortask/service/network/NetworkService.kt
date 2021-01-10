package com.lsurvila.callmonitortask.service.network

import java.net.URI

interface NetworkService {
    fun isWifiConnected(): Boolean
    fun getWifiAddress(port: Int): URI?
}