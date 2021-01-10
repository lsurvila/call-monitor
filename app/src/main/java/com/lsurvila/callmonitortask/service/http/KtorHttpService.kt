package com.lsurvila.callmonitortask.service.http

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

class KtorHttpService: HttpService() {

    private val server = embeddedServer(Netty, port = 12345) {
        routing {
            get("/") {
                call.respondText("Hello, world!")
            }
        }
    }

    override fun start(ipAddress: String) {
        this.ipAddress = ipAddress
        server.start(wait = false)
    }

    override fun stop() {
        server.stop(0, 0)
    }
}