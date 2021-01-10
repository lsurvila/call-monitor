package com.lsurvila.callmonitortask.service.http

import com.lsurvila.callmonitortask.util.NetworkUtil
import java.net.URI


abstract class HttpService {

    internal val port = 12345
    lateinit var address: URI

    open fun start(host: String) {
        this.address = NetworkUtil.buildHttpAddress(host, port)
    }

    abstract fun stop()
}