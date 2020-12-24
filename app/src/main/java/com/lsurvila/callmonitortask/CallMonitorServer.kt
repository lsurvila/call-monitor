package com.lsurvila.callmonitortask

import fi.iki.elonen.NanoHTTPD
import java.io.IOException

private const val PORT = 12345

class CallMonitorServer : NanoHTTPD(PORT) {

    override fun start() {
        try {
            super.start()
            // TODO server started on
        } catch (e: IOException) {

        }
    }

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