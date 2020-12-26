package com.lsurvila.callmonitortask

import com.lsurvila.callmonitortask.util.DateTimeUtil
import com.lsurvila.callmonitortask.util.NetworkUtil
import fi.iki.elonen.NanoHTTPD
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*

private const val SCHEME_HTTP = "http"
private const val MIME_TYPE_JSON = "application/json"
private const val PORT = 12345

class CallMonitorServer : NanoHTTPD(PORT) {

    private lateinit var ipAddress: String
    lateinit var address: String
    private lateinit var startTime: String

    fun start(ipAddress: String) {
        super.start()
        this.ipAddress = ipAddress
        this.startTime = DateTimeUtil.formatDateTime(Date())
        this.address = NetworkUtil.formatUri(SCHEME_HTTP, ipAddress, listeningPort)
    }

    override fun stop() {
        this.ipAddress = ""
        this.address = ""
        this.startTime = ""
        super.stop()
    }

    override fun serve(session: IHTTPSession): Response {
        val uri = session.uri
        if (uri == "/hello") {
            val response = "HelloWorld"
            return newFixedLengthResponse(response)
        }
        return newFixedLengthResponse(Response.Status.OK, MIME_TYPE_JSON, getAvailableServices())
    }

    private fun getAvailableServices(): String {
        return Json.Default
            .encodeToString(
                Services(
                    startTime,
                    listOf(
                        mapService(METHOD_STATUS),
                        mapService(METHOD_LOG)
                    )
                )
            )
    }

    private fun mapService(method: String) =
        Service(
            METHOD_STATUS,
            NetworkUtil.formatUri(SCHEME_HTTP, ipAddress, listeningPort, method)
        )
}

private const val METHOD_STATUS = "status"
private const val METHOD_LOG = "log"

@Serializable
data class Services(val start: String, val services: List<Service>)

@Serializable
data class Service(val name: String, val uri: String)
