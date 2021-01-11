package com.lsurvila.callmonitortask.service.http

import com.lsurvila.callmonitortask.model.CallMonitorState
import java.net.URI
import java.util.*

@Suppress("unused")
enum class Methods(val value: String) {
    SERVICES(""), // root
    STATUS("status"),
    LOG("log")
}

abstract class HttpService {

    companion object {
        const val PORT = 12345
    }

    lateinit var address: URI
    lateinit var serverStarted: Date

    abstract suspend fun start(): CallMonitorState
    abstract suspend fun stop(): CallMonitorState
}