package com.lsurvila.callmonitortask.service.http

abstract class HttpService {

    var ipAddress: String? = null

    abstract fun start(ipAddress: String)
    abstract fun stop()
}