package com.lsurvila.callmonitortask.service.http

import java.net.URI


abstract class HttpService {

    companion object {
        const val TAG = "HttpService"
        const val PORT = 12345
    }

    var address: URI? = null

    abstract fun start()
    abstract fun stop()
}