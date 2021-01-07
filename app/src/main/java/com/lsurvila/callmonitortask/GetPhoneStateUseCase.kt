package com.lsurvila.callmonitortask

import com.lsurvila.callmonitortask.model.PhoneState
import com.lsurvila.callmonitortask.service.callmonitor.CallMonitorService
import kotlinx.coroutines.flow.StateFlow

class GetPhoneStateUseCase(private val callMonitorService: CallMonitorService) {

    fun execute(): StateFlow<PhoneState> {
        return callMonitorService.phoneState()
    }
}