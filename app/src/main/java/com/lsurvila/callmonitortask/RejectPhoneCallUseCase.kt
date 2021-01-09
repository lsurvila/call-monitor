package com.lsurvila.callmonitortask

import com.lsurvila.callmonitortask.service.callmonitor.CallMonitorService

class RejectPhoneCallUseCase(private val service: CallMonitorService) {

    fun execute() {
        service.rejectCall()
    }
}