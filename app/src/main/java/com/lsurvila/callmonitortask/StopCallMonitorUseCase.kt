package com.lsurvila.callmonitortask

import com.lsurvila.callmonitortask.model.CallMonitorState
import com.lsurvila.callmonitortask.service.callmonitor.CallMonitor

class StopCallMonitorUseCase(private val callMonitor: CallMonitor) {

    fun execute(withWarning: Boolean = false): CallMonitorState {
        if (callMonitor.currentState != CallMonitorState.STOPPING) {
            callMonitor.currentState = CallMonitorState.STOPPING
        } else {
            if (withWarning) {
                callMonitor.currentState = CallMonitorState.STOPPED_WITH_WARNING
            } else {
                callMonitor.currentState = CallMonitorState.STOPPED
            }
        }
        return callMonitor.currentState
    }
}