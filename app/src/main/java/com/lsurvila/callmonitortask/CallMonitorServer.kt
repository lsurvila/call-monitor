package com.lsurvila.callmonitortask

import fi.iki.elonen.NanoHTTPD
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import java.math.BigInteger
import java.net.InetAddress
import java.nio.ByteOrder


class CallMonitorServer(private val server: HttpServer) {

    fun start(wifiIpAddress: Int): Flow<String> = flow {
        if (server.isAlive) {
            server.stop()
        }
        val ipAddress: String?
        try {
            ipAddress = resolveIpAddress(wifiIpAddress)
        } catch (e: Exception) {
            emit("Failed to resolve Wifi IP address")
            return@flow
        }
        try {
            @Suppress("BlockingMethodInNonBlockingContext") // taken care by flowOn
            server.start()
            if (server.isAlive) {
                emit("Server started on ${ipAddress}:${server.listeningPort}")
            } else {
                throw Exception()
            }
        } catch (e: Exception) {
            emit("Failed to start HTTP server")
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
}

private const val PORT = 12345

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
