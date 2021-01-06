package com.lsurvila.callmonitortask

import android.util.Log
import com.lsurvila.callmonitortask.model.Call
import com.lsurvila.callmonitortask.model.CallMonitorState
import com.lsurvila.callmonitortask.service.callmonitor.CallMonitorService

class LogOngoingCallUseCase(private val callMonitorService: CallMonitorService) {

    fun execute(call: Call) {
        if (callMonitorService.currentState == CallMonitorState.STARTED && call.incomingCall) {
            callMonitorService.onGoingCall = call
            Log.d("LIU", callMonitorService.onGoingCall.toString())
        } else {
            callMonitorService.onGoingCall = null
        }
    }
}