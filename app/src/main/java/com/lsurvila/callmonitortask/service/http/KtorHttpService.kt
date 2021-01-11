package com.lsurvila.callmonitortask.service.http

import com.lsurvila.callmonitortask.ViewServicesStatusUseCase
import com.lsurvila.callmonitortask.model.CallMonitorState
import com.lsurvila.callmonitortask.model.State
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class KtorHttpService(viewServicesStatusUseCase: ViewServicesStatusUseCase) : HttpService(viewServicesStatusUseCase) {

    private var server: ApplicationEngine? = null

    private fun createServerInstance() = embeddedServer(CIO, PORT) {
        install(ContentNegotiation) {
            json()
        }
        routing {
            get(Methods.SERVICES.value) {
                call.respond(getAvailableServices())
            }
        }
    }

    override suspend fun start(): CallMonitorState {
        return withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                // ideally we could reuse instance, but had problems with restarting and getting
                // callbacks correctly, even with enough time to let it stop and using shutdown
                // hooks. Something to improve
                server = createServerInstance()
                server?.environment?.monitor?.subscribe(ApplicationStarted) {
                    serverStarted = Date()
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
