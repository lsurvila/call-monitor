package com.lsurvila.callmonitortask.service.callmonitor

import com.lsurvila.callmonitortask.model.Call
import com.lsurvila.callmonitortask.model.CallMonitorState
import com.lsurvila.callmonitortask.model.PhoneState
import com.lsurvila.callmonitortask.model.State
import com.lsurvila.callmonitortask.repository.call.CallLogRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.*

interface CallIntentListener {
    fun onOpenPhone()
    fun onAnswerCall()
    fun onRejectCall()
}

abstract class CallMonitorService(private val callLogRepository: CallLogRepository) {

    private val _phoneCall = MutableStateFlow(Call(PhoneState.IDLE, ""))
    private var callConnectedTime: Date? = null
    private var callDisconnectedTime: Date? = null
    private var callMonitoredCount = 0

    private var listener: CallIntentListener? = null

    fun setCallIntentListener(listener: CallIntentListener) {
        this.listener = listener
    }

    fun phoneCall(): StateFlow<Call> {
        return _phoneCall.asStateFlow()
    }

    fun logPhoneCall(call: Call) {
        listener?.onOpenPhone()
        _phoneCall.value = call
        if (call.state == PhoneState.CONNECTED) {
            callConnectedTime = Date()
        } else if (call.state == PhoneState.DISCONNECTED) {
            callDisconnectedTime = Date()
            callLogRepository.insert(
                call.copy(
                    connectedTime = callConnectedTime,
                    disconnectedTime = callDisconnectedTime,
                    monitoredCount = callMonitoredCount
                )
            )
            callConnectedTime = null
            callDisconnectedTime = null
            callMonitoredCount = 0
        }
    }

    fun logPhoneCallMonitored() {
        callMonitoredCount++
    }

    fun answerCall() {
        listener?.onAnswerCall()
    }

    fun rejectCall() {
        listener?.onRejectCall()
    }

    private var _serviceState = MutableStateFlow(CallMonitorState(State.NOT_STARTED))

    fun serviceState(): StateFlow<CallMonitorState> {
        return _serviceState.asStateFlow()
    }

    fun setServiceState(state: State) {
        _serviceState.value = CallMonitorState(state)
    }

    fun setServiceState(state: CallMonitorState) {
        _serviceState.value = state
    }

    abstract fun isAvailable(): Boolean
    abstract fun hasPhonePermission(): Boolean
    abstract fun hasContactsPermission(): Boolean
}