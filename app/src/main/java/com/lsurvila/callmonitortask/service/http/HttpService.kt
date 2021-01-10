package com.lsurvila.callmonitortask.service.http

import com.lsurvila.callmonitortask.model.CallMonitorState
import java.net.URI

abstract class HttpService {

    companion object {
        const val PORT = 12345
    }

    var address: URI? = null

    abstract suspend fun start(): CallMonitorState
    abstract suspend fun stop(): CallMonitorState
}