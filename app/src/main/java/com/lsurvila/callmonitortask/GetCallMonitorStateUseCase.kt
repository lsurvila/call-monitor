package com.lsurvila.callmonitortask

import com.lsurvila.callmonitortask.model.CallMonitorState
import com.lsurvila.callmonitortask.service.callmonitor.CallMonitorService

class GetCallMonitorStateUseCase(private val callMonitorService: CallMonitorService) {

    fun execute(): CallMonitorState {
        return callMonitorService.currentState
    }
}