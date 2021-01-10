package com.lsurvila.callmonitortask.service.http

import com.lsurvila.callmonitortask.util.NetworkUtil
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.netty.util.internal.logging.InternalLoggerFactory
import io.netty.util.internal.logging.JdkLoggerFactory

class KtorHttpService: HttpService() {

    init {
        InternalLoggerFactory.setDefaultFactory(JdkLoggerFactory.INSTANCE);
    }

    private val server = embeddedServer(Netty, port) {
        routing {
            get("/") {
                call.respondText("Hello, world!")
            }
        }
    }

    override fun start(host: String) {
        super.start(host)
        server.start(wait = false)
    }

    override fun stop() {
        server.stop(0, 0)
    }
}