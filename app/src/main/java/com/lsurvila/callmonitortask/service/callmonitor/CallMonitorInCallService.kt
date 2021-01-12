package com.lsurvila.callmonitortask.service.callmonitor

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.telecom.Call
import android.telecom.InCallService
import android.telecom.VideoProfile
import com.lsurvila.callmonitortask.model.PhoneState
import com.lsurvila.callmonitortask.ui.CallMonitorActivity
import org.koin.android.ext.android.inject

class CallMonitorInCallService : InCallService(), CallIntentListener {

    private val callMonitorService: CallMonitorService by inject()
    private val entityMapper: CallEntityMapper by inject()

    private var currentCall: Call? = null

    init {
        callMonitorService.setCallIntentListener(this)
    }

    override fun onCallAdded(call: Call) {
        currentCall = call
        currentCall?.registerCallback(callCallback)
        updateOngoingCall(call)
    }

    override fun onCallRemoved(call: Call) {
        currentCall = call
        updateOngoingCall(call)
        currentCall?.unregisterCallback(callCallback)
    }

    private val callCallback = object : Call.Callback() {
        override fun onStateChanged(call: Call, state: Int) {
            updateOngoingCall(call)
        }
    }

    private fun updateOngoingCall(call: Call) {
        val callData = entityMapper.map(call)
        if (callData.state != PhoneState.UNKNOWN && callData.state != callMonitorService.phoneCall().value.state) {
            callMonitorService.logPhoneCall(callData)
        }
    }

    override fun onOpenPhone() {
        startActivity(
            Intent(this, CallMonitorActivity::class.java)
                .setFlags(FLAG_ACTIVITY_NEW_TASK)
        )
    }

    override fun onAnswerCall() {
        currentCall?.answer(VideoProfile.STATE_AUDIO_ONLY)
    }

    override fun onRejectCall() {
        currentCall?.disconnect()
    }
}