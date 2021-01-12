package com.lsurvila.callmonitortask

import com.lsurvila.callmonitortask.model.State
import com.lsurvila.callmonitortask.repository.call.CallLogRepository
import com.lsurvila.callmonitortask.service.callmonitor.CallMonitorService
import com.lsurvila.callmonitortask.service.http.HttpService

class StopCallMonitorUseCase(
    private val callMonitorService: CallMonitorService,
    private val httpService: HttpService,
    private val callLogRepository: CallLogRepository,
) {

    suspend fun execute() {
        callMonitorService.setServiceState(State.STOPPING)
        val stopped = httpService.stop()
        callLogRepository.delete()
        callMonitorService.setServiceState(stopped)
        callMonitorService.setServiceState(State.NOT_STARTED)
    }
}