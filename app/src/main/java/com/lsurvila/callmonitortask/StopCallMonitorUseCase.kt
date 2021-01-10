package com.lsurvila.callmonitortask

import com.lsurvila.callmonitortask.model.State
import com.lsurvila.callmonitortask.service.callmonitor.CallMonitorService
import com.lsurvila.callmonitortask.service.http.HttpService

class StopCallMonitorUseCase(
    private val callMonitorService: CallMonitorService,
    private val httpService: HttpService
) {

    fun execute() {
        callMonitorService.setServiceState(State.STOPPING)
        httpService.stop()
        callMonitorService.setServiceState(State.STOPPED)
        callMonitorService.setServiceState(State.NOT_STARTED)
    }
}