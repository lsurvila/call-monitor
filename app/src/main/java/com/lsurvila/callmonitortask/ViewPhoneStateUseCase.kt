package com.lsurvila.callmonitortask

import com.lsurvila.callmonitortask.model.Call
import com.lsurvila.callmonitortask.model.PhoneState
import com.lsurvila.callmonitortask.service.callmonitor.CallMonitorService
import kotlinx.coroutines.flow.StateFlow

class ViewPhoneStateUseCase(private val callMonitorService: CallMonitorService) {

    fun execute(): StateFlow<Call> {
        return callMonitorService.phoneCall()
    }
}