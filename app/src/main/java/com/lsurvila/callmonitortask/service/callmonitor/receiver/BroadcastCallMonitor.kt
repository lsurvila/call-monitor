package com.lsurvila.callmonitortask.service.callmonitor.receiver

import com.lsurvila.callmonitortask.service.callmonitor.CallMonitor

class BroadcastCallMonitor: CallMonitor() {

    override fun isAvailable(): Boolean {
        return true
    }
}