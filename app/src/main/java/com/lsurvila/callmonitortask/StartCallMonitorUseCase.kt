package com.lsurvila.callmonitortask

import com.lsurvila.callmonitortask.model.CallMonitorState
import com.lsurvila.callmonitortask.service.callmonitor.CallMonitorService

class StartCallMonitorUseCase(private val callMonitorService: CallMonitorService) {

    fun checkIfAvailable() {
        callMonitorService.setServiceState(CallMonitorState.STARTING)
        if (callMonitorService.isAvailable()) {
            callMonitorService.setServiceState(CallMonitorState.AVAILABLE)
        } else {
            callMonitorService.setServiceState(CallMonitorState.NOT_AVAILABLE)
        }

//        if (callMonitorService._serviceState != CallMonitorState.STARTING) {
//            if (callMonitorService.isAvailable()) {
//                callMonitorService._serviceState = CallMonitorState.STARTING
//            } else {
//                callMonitorService._serviceState = CallMonitorState.NOT_AVAILABLE
//            }
//        } else {
//            if (permissionGranted) {
//                callMonitorService._serviceState = CallMonitorState.STARTED
//            } else {
//                callMonitorService._serviceState = CallMonitorState.PERMISSION_DENIED
//            }
//        }
//        return callMonitorService._serviceState
    }
}