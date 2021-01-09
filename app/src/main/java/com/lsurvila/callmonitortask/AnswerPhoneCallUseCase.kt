package com.lsurvila.callmonitortask

import com.lsurvila.callmonitortask.service.callmonitor.CallMonitorService

class AnswerPhoneCallUseCase(private val callMonitorService: CallMonitorService) {

    fun execute() {
        callMonitorService.answerCall()
    }
}