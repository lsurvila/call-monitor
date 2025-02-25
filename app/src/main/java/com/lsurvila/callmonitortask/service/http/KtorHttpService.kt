package com.lsurvila.callmonitortask.service.http

import com.lsurvila.callmonitortask.ViewCallLogUseCase
import com.lsurvila.callmonitortask.ViewOngoingCallUseCase
import com.lsurvila.callmonitortask.ViewServicesStatusUseCase
import com.lsurvila.callmonitortask.model.CallMonitorState
import com.lsurvila.callmonitortask.model.State
import com.lsurvila.callmonitortask.util.DateTime
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import org.koin.ktor.ext.inject
import java.net.URI
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class KtorHttpService : HttpService() {

    private var server: ApplicationEngine? = null

    private fun createServerInstance() = embeddedServer(factory = CIO, port = PORT, host = address.host) {
        install(ContentNegotiation) {
            json(Json {
                encodeDefaults = false
            })
        }

        val viewServicesStatusUseCase: ViewServicesStatusUseCase by inject()
        val viewOngoingCallUseCase: ViewOngoingCallUseCase by inject()
        val viewCallLogUseCase: ViewCallLogUseCase by inject()

        routing {
            get(Methods.SERVICES.value) {
                call.respond(viewServicesStatusUseCase.execute(serverStarted, enumValues(), address))
            }
            get(Methods.STATUS.value) {
                call.respond(viewOngoingCallUseCase.execute())
            }
            get(Methods.LOG.value) {
                call.respond(viewCallLogUseCase.execute())
            }
        }
    }

    override suspend fun start(address: URI): CallMonitorState {
        this.address = address
        return withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                // ideally we could reuse instance, but had problems with restarting and getting
                // callbacks correctly, even with enough time to let it stop and using shutdown
                // hooks. Something to improve
                server = createServerInstance()
                server?.environment?.monitor?.subscribe(ApplicationStarted) {
                    serverStarted = DateTime.now()
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
