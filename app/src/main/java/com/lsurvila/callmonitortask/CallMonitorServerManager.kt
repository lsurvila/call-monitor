package com.lsurvila.callmonitortask

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import java.math.BigInteger
import java.net.InetAddress
import java.nio.ByteOrder

private const val ERROR_WIFI = "Failed to resolve Wifi IP address"
private const val ERROR_HTTP = "Failed to start HTTP server"

class CallMonitorServerManager(private val server: CallMonitorServer) {

    fun start(wifiIpAddress: Int): Flow<String> = flow {
        val ipAddress: String?
        try {
            ipAddress = resolveIpAddress(wifiIpAddress)
        } catch (e: Exception) {
            e.printStackTrace()
            emit(ERROR_WIFI)
            return@flow // stop flow
        }
        try {
            @Suppress("BlockingMethodInNonBlockingContext") // taken care by flowOn
            server.start(ipAddress)
            if (server.isAlive) {
                emit("Server started on ${ipAddress}:${server.listeningPort}")
            } else {
                throw Exception()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(ERROR_HTTP)
        }
    }.flowOn(Dispatchers.IO)

    private fun resolveIpAddress(wifiIpAddress: Int): String {
        var ipAddress = wifiIpAddress
        if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
            ipAddress = Integer.reverseBytes(wifiIpAddress)
        }
        val ipByteArray: ByteArray = BigInteger.valueOf(ipAddress.toLong()).toByteArray()
        return InetAddress.getByAddress(ipByteArray).hostAddress
    }

    fun stop() {
        server.stop()
    }
}