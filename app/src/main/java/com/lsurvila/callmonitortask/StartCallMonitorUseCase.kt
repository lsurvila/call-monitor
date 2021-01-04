package com.lsurvila.callmonitortask

import com.lsurvila.callmonitortask.model.CallMonitorState
import com.lsurvila.callmonitortask.service.callmonitor.CallMonitor

class StartCallMonitorUseCase(private val callMonitor: CallMonitor) {

    fun execute(permissionGranted: Boolean = false): CallMonitorState {
        if (callMonitor.currentState != CallMonitorState.STARTING) {
            if (callMonitor.isAvailable()) {
                callMonitor.currentState = CallMonitorState.STARTING
            } else {
                callMonitor.currentState = CallMonitorState.NOT_AVAILABLE
            }
        } else {
            if (permissionGranted) {
                callMonitor.currentState = CallMonitorState.STARTED
            } else {
                callMonitor.currentState = CallMonitorState.PERMISSION_DENIED
            }
        }
        return callMonitor.currentState
    }
}