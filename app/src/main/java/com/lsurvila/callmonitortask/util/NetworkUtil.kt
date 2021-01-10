package com.lsurvila.callmonitortask.util

import io.ktor.http.*
import java.net.URI

class NetworkUtil {

    companion object {
        fun buildHttpAddress(host: String, port: Int): URI {
            return URI(URLProtocol.HTTP.name, null, host, port, null, null, null)
        }
    }
}