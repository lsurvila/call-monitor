package com.lsurvila.callmonitortask

import com.lsurvila.callmonitortask.model.CallMonitorState
import com.lsurvila.callmonitortask.service.callmonitor.CallMonitorService

class StartCallMonitorUseCase(private val callMonitorService: CallMonitorService) {

    fun execute(permissionGranted: Boolean = false): CallMonitorState {
        if (callMonitorService.currentState != CallMonitorState.STARTING) {
            if (callMonitorService.isAvailable()) {
                callMonitorService.currentState = CallMonitorState.STARTING
            } else {
                callMonitorService.currentState = CallMonitorState.NOT_AVAILABLE
            }
        } else {
            if (permissionGranted) {
                callMonitorService.currentState = CallMonitorState.STARTED
            } else {
                callMonitorService.currentState = CallMonitorState.PERMISSION_DENIED
            }
        }
        return callMonitorService.currentState
    }
}