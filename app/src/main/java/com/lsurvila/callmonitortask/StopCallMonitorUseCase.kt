package com.lsurvila.callmonitortask

import com.lsurvila.callmonitortask.model.CallMonitorState
import com.lsurvila.callmonitortask.service.callmonitor.CallMonitorService

class StopCallMonitorUseCase(private val callMonitorService: CallMonitorService) {

    fun execute(withWarning: Boolean = false): CallMonitorState {
        if (callMonitorService.currentState != CallMonitorState.STOPPING) {
            callMonitorService.currentState = CallMonitorState.STOPPING
        } else {
            if (withWarning) {
                callMonitorService.currentState = CallMonitorState.STOPPED_WITH_WARNING
            } else {
                callMonitorService.currentState = CallMonitorState.STOPPED
            }
        }
        return callMonitorService.currentState
    }
}