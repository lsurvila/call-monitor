package com.lsurvila.callmonitortask

import fi.iki.elonen.NanoHTTPD
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.math.BigInteger
import java.net.InetAddress
import java.nio.ByteOrder


private const val PORT = 12345

class CallMonitorServer(private val wifiIpAddress: Int) {

    fun start(): Flow<String> = flow {
        try {
            emit(resolveIpAddress())
        } catch (e: Exception) {
            emit("Could not resolve Wifi address")
        }
    }

    private fun resolveIpAddress(): String {
        var ipAddress = wifiIpAddress
        if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
            ipAddress = Integer.reverseBytes(wifiIpAddress)
        }
        val ipByteArray: ByteArray = BigInteger.valueOf(ipAddress.toLong()).toByteArray()
        return InetAddress.getByAddress(ipByteArray).hostAddress
    }
}

class HttpServer : NanoHTTPD(PORT) {

    override fun serve(session: IHTTPSession): Response {
        val uri = session.uri
        if (uri == "/hello") {
            val response = "HelloWorld"
            return newFixedLengthResponse(response)
        }
        // TODO
        return newFixedLengthResponse("not_found")
    }
}
