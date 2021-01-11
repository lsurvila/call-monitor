package com.lsurvila.callmonitortask

import com.lsurvila.callmonitortask.model.Call
import com.lsurvila.callmonitortask.service.callmonitor.CallIntentListener
import com.lsurvila.callmonitortask.service.callmonitor.CallMonitorService

class LogOngoingCallUseCase(private val callMonitorService: CallMonitorService) {

    fun execute(call: Call, listener: CallIntentListener) {
        callMonitorService.setCallIntentListener(listener)
        callMonitorService.logPhoneCall(call)
    }
}