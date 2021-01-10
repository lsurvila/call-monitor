package com.lsurvila.callmonitortask.service.http

import com.lsurvila.callmonitortask.service.http.model.Service
import com.lsurvila.callmonitortask.service.http.model.Services
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.netty.util.internal.logging.InternalLoggerFactory
import io.netty.util.internal.logging.JdkLoggerFactory

private const val METHOD_ROOT = "/"

class KtorHttpService : HttpService() {

    init {
        InternalLoggerFactory.setDefaultFactory(JdkLoggerFactory.INSTANCE)
    }

    private val server = embeddedServer(Netty, PORT) {
        install(ContentNegotiation) {
            json()
        }
        routing {
            get(METHOD_ROOT) {
                call.respond(Services("adasd", listOf(Service("asd", "asdas"), Service("asd", "s"))))
            }
        }
    }

    override fun start() {
        server.start(wait = false)
    }

    override fun stop() {
        server.stop(0, 0)
    }
}