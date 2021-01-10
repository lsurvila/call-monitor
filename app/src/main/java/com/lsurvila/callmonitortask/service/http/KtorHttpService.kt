package com.lsurvila.callmonitortask.service.http

import com.lsurvila.callmonitortask.model.CallMonitorState
import com.lsurvila.callmonitortask.model.State
import com.lsurvila.callmonitortask.service.http.model.Service
import com.lsurvila.callmonitortask.service.http.model.Services
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

private const val METHOD_ROOT = "/"

class KtorHttpService : HttpService() {

    private var server: ApplicationEngine? = null

    private fun createServerInstance() = embeddedServer(CIO, PORT) {
        install(ContentNegotiation) {
            json()
        }
        routing {
            get(METHOD_ROOT) {
                call.respond(
                    Services(
                        "adasd",
                        listOf(Service("asd", "asdas"), Service("asd", "s"))
                    )
                )
            }
        }
    }

    override suspend fun start(): CallMonitorState {
        return withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                server = createServerInstance()
                server?.environment?.monitor?.subscribe(ApplicationStarted) {
                    continuation.resume(CallMonitorState(State.STARTED, address))
                }
                server?.start(wait = false)
            }
        }
    }

    override suspend fun stop(): CallMonitorState {
        return withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                server?.environment?.monitor?.subscribe(ApplicationStopped) {
                    continuation.resume(CallMonitorState(State.STOPPED))
                }
                server?.stop(0, 0)
            }
        }
    }
}
