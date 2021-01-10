package com.lsurvila.callmonitortask.service.callmonitor

import com.lsurvila.callmonitortask.model.Call
import com.lsurvila.callmonitortask.model.CallMonitorState
import com.lsurvila.callmonitortask.model.PhoneState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

interface CallIntentListener {
    fun onAnswerCall()
    fun onRejectCall()
}

abstract class CallMonitorService {

    private val _phoneCall = MutableStateFlow(Call(PhoneState.IDLE, null, null))

    private var listener: CallIntentListener? = null

    fun setCallIntentListener(listener: CallIntentListener) {
        this.listener = listener
    }

    fun phoneCall(): StateFlow<Call> {
        return _phoneCall.asStateFlow()
    }

    fun setPhoneCall(call: Call) {
        _phoneCall.value = call
    }

    fun answerCall() {
        listener?.onAnswerCall()
    }

    fun rejectCall() {
        listener?.onRejectCall()
    }

    private var _serviceState = MutableStateFlow(CallMonitorState.NOT_STARTED)

    fun serviceState(): StateFlow<CallMonitorState> {
        return _serviceState.asStateFlow()
    }

    fun setServiceState(state: CallMonitorState) {
        _serviceState.value = state
    }

    abstract fun isAvailable(): Boolean
    abstract fun hasPhonePermission(): Boolean
}