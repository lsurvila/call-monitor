package com.lsurvila.callmonitortask.service.http.mapper

import java.net.URI

private const val SCHEME_HTTP = "http"

class UriMapper {
    fun mapHttpAddress(host: String, port: Int, path: String? = null): URI {
        return URI(SCHEME_HTTP, null, host, port, mapPath(path), null, null)
    }

    private fun mapPath(path: String?): String? {
        return if (path.isNullOrEmpty()) null else "/${path}"
    }
}