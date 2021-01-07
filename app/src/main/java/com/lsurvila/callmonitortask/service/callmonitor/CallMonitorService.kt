package com.lsurvila.callmonitortask.service.callmonitor

import com.lsurvila.callmonitortask.model.Call
import com.lsurvila.callmonitortask.model.CallMonitorState
import com.lsurvila.callmonitortask.model.PhoneState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow

abstract class CallMonitorService {

    private val _phoneState = MutableStateFlow(PhoneState.IDLE)

    fun phoneState(): StateFlow<PhoneState> {
        return _phoneState.asStateFlow()
    }

    fun setPhoneState(state: PhoneState) {
        _phoneState.value = state
    }

    var currentState: CallMonitorState = CallMonitorState.NOT_STARTED
    var onGoingCall: Call? = null

    abstract fun isAvailable(): Boolean
}