package com.lsurvila.callmonitortask.service.callmonitor

import com.lsurvila.callmonitortask.model.Call
import com.lsurvila.callmonitortask.model.CallMonitorState

abstract class CallMonitor {

    var currentState: CallMonitorState = CallMonitorState.NOT_STARTED
    var onGoingCall: Call? = null

    abstract fun isAvailable(): Boolean
}