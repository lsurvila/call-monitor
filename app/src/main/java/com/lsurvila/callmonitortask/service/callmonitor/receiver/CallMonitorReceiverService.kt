package com.lsurvila.callmonitortask.service.callmonitor.receiver

import com.lsurvila.callmonitortask.service.callmonitor.CallMonitorService

class CallMonitorReceiverService: CallMonitorService() {

    override fun isAvailable(): Boolean {
        return false
    }
}