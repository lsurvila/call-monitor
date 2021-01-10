package com.lsurvila.callmonitortask

import com.lsurvila.callmonitortask.model.CallMonitorState
import com.lsurvila.callmonitortask.service.callmonitor.CallMonitorService

class StopCallMonitorUseCase(private val callMonitorService: CallMonitorService) {

    fun execute() {
        callMonitorService.setServiceState(CallMonitorState.STOPPING)
        callMonitorService.setServiceState(CallMonitorState.STOPPED)
        callMonitorService.setServiceState(CallMonitorState.NOT_STARTED)
    }
}