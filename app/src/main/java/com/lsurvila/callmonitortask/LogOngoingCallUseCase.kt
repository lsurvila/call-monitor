package com.lsurvila.callmonitortask

import android.util.Log
import com.lsurvila.callmonitortask.model.CallMonitorState
import com.lsurvila.callmonitortask.repository.ContactRepository
import com.lsurvila.callmonitortask.model.Call
import com.lsurvila.callmonitortask.service.callmonitor.CallMonitor

class LogOngoingCallUseCase(
    private val callMonitor: CallMonitor,
    private val contactRepository: ContactRepository
) {

    fun execute(call: Call) {
        if (callMonitor.currentState == CallMonitorState.STARTED && call.incomingCall) {
            call.number?.let {
                //call.copy(name = contactRepository.get(it).name)
            }
            callMonitor.onGoingCall = call
            Log.d("LIU", callMonitor.onGoingCall.toString())
        } else {
            callMonitor.onGoingCall = null
        }
    }
}