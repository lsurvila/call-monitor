package com.lsurvila.callmonitortask

import com.lsurvila.callmonitortask.model.CallMonitorState
import com.lsurvila.callmonitortask.service.callmonitor.CallMonitor

class GetCallMonitorStateUseCase(private val callMonitor: CallMonitor) {

    fun execute(): CallMonitorState {
        return callMonitor.currentState
    }
}