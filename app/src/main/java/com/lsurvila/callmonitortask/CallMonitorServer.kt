package com.lsurvila.callmonitortask

import fi.iki.elonen.NanoHTTPD
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


private const val PORT = 12345

class CallMonitorServer : NanoHTTPD(PORT) {

    private lateinit var ipAddress: String

    fun start(ipAddress: String) {
        this.ipAddress = ipAddress
        super.start()
    }

    override fun serve(session: IHTTPSession): Response {
        val uri = session.uri
        if (uri == "/hello") {
            val response = "HelloWorld"
            return newFixedLengthResponse(response)
        }
        // TODO
        return newFixedLengthResponse(Response.Status.OK, "application/json", getAvailableServices())
    }

    private fun getAvailableServices(): String {
        return Json.Default
            .encodeToString(
                Services(
                    "start",
                    listOf(
                        Service("name", "uri")
                    )
                )
            )
    }
}

@Serializable
data class Services(val start: String, val services: List<Service>)

@Serializable
data class Service(val name: String, val uri: String)
