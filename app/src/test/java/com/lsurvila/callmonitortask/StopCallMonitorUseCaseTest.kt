package com.lsurvila.callmonitortask

import com.lsurvila.callmonitortask.model.CallMonitorState
import com.lsurvila.callmonitortask.model.State
import com.lsurvila.callmonitortask.repository.call.CallLogRepository
import com.lsurvila.callmonitortask.service.callmonitor.CallMonitorService
import com.lsurvila.callmonitortask.service.http.HttpService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Test

class StopCallMonitorUseCaseTest {

    private val callMonitorService = mockk<CallMonitorService>(relaxed = true)
    private val httpService = mockk<HttpService>(relaxed = true)
    private val callLogRepository = mockk<CallLogRepository>(relaxed = true)
    private val useCase = StopCallMonitorUseCase(callMonitorService, httpService, callLogRepository)

    @Test
    fun execute() {
        every { runBlocking { httpService.stop() } } returns CallMonitorState(State.STOPPED)

        runBlocking {
            useCase.execute()
        }

        verify { callMonitorService.setServiceState(State.STOPPING) }
        verify { callLogRepository.delete() }
        verify { callMonitorService.setServiceState(CallMonitorState(State.STOPPED)) }
        verify { callMonitorService.setServiceState(State.NOT_STARTED) }
    }
}