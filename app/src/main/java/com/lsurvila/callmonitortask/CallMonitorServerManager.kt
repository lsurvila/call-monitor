package com.lsurvila.callmonitortask

import com.lsurvila.callmonitortask.util.NetworkUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import java.io.IOException

private const val ERROR_WIFI = "Failed to resolve Wifi IP address"
private const val ERROR_HTTP = "Failed to start HTTP server"

class CallMonitorServerManager(private val server: CallMonitorServer) {

    @Suppress("EXPERIMENTAL_API_USAGE") // for actual prod app would use stable api
    fun start(ipAddressInt: Int): Flow<String> = flow {
        val ipAddress: String?
        try {
            ipAddress = NetworkUtil.ipToHostAddress(ipAddressInt)
        } catch (e: Exception) {
            e.printStackTrace()
            emit(ERROR_WIFI)
            return@flow // stop flow
        }
        try {
            @Suppress("BlockingMethodInNonBlockingContext") // taken care by flowOn
            server.start(ipAddress)
//            if (server.isAlive) {
//                emit("Server started on ${server.address}")
//            } else {
//                throw IOException()
//            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(ERROR_HTTP)
        }
    }.flowOn(Dispatchers.IO)

    fun stop() {
//        server.stop()
    }
}