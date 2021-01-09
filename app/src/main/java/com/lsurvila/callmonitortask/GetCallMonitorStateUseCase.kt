package com.lsurvila.callmonitortask

import com.lsurvila.callmonitortask.model.CallMonitorState
import com.lsurvila.callmonitortask.service.callmonitor.CallMonitorService
import kotlinx.coroutines.flow.StateFlow

class GetCallMonitorStateUseCase(private val callMonitorService: CallMonitorService) {

    fun execute(): StateFlow<CallMonitorState> {
        return callMonitorService.serviceState()
    }
}