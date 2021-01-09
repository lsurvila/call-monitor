package com.lsurvila.callmonitortask

import com.lsurvila.callmonitortask.model.CallMonitorState
import com.lsurvila.callmonitortask.service.callmonitor.CallMonitorService

class StopCallMonitorUseCase(private val callMonitorService: CallMonitorService) {

    fun execute(withWarning: Boolean = false): CallMonitorState {
//        if (callMonitorService._serviceState != CallMonitorState.STOPPING) {
//            callMonitorService._serviceState = CallMonitorState.STOPPING
//        } else {
//            if (withWarning) {
//                callMonitorService._serviceState = CallMonitorState.STOPPED_WITH_WARNING
//            } else {
//                callMonitorService._serviceState = CallMonitorState.STOPPED
//            }
//        }
//        return callMonitorService._serviceState
        return CallMonitorState.STOPPING
    }
}