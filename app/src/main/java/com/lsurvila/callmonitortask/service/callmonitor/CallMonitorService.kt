package com.lsurvila.callmonitortask.service.callmonitor

import com.lsurvila.callmonitortask.model.Call
import com.lsurvila.callmonitortask.model.CallMonitorState
import com.lsurvila.callmonitortask.model.PhoneState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class CallMonitorService {

    private val _phoneCall = MutableStateFlow(Call(PhoneState.IDLE, null, null))

    fun phoneCall(): StateFlow<Call> {
        return _phoneCall.asStateFlow()
    }

    fun setPhoneCall(call: Call) {
        _phoneCall.value = call
    }





    var currentState: CallMonitorState = CallMonitorState.NOT_STARTED

    abstract fun isAvailable(): Boolean
}