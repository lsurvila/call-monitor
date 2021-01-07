package com.lsurvila.callmonitortask

import android.util.Log
import com.lsurvila.callmonitortask.model.Call
import com.lsurvila.callmonitortask.model.CallMonitorState
import com.lsurvila.callmonitortask.model.PhoneState
import com.lsurvila.callmonitortask.service.callmonitor.CallMonitorService

class HandlePhoneCallUseCase(private val callMonitorService: CallMonitorService) {

    fun execute(call: Call) {
        if (callMonitorService.currentState == CallMonitorState.STARTED && call.incomingCall) {
            callMonitorService.onGoingCall = call
            Log.d("LIU", callMonitorService.onGoingCall.toString())
        } else {
            callMonitorService.onGoingCall = null
        }
    }

    fun execute(state: PhoneState) {
        callMonitorService.setPhoneState(state)
    }
}