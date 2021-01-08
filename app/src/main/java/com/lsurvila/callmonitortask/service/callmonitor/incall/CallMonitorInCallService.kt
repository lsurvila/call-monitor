package com.lsurvila.callmonitortask.service.callmonitor.incall

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import android.telecom.Call
import android.telecom.InCallService
import android.util.Log
import com.lsurvila.callmonitortask.service.callmonitor.CallEntityMapper
import com.lsurvila.callmonitortask.service.callmonitor.CallMonitorService
import com.lsurvila.callmonitortask.ui.CallMonitorActivity
import org.koin.android.ext.android.inject

class CallMonitorInCallService: InCallService() {

    private val callMonitorService: CallMonitorService by inject()
    private val entityMapper: CallEntityMapper by inject()

    override fun onConnectionEvent(call: Call?, event: String?, extras: Bundle?) {
        Log.d("Liu", "onConnectionEvent $call $event $extras")
        super.onConnectionEvent(call, event, extras)
    }

    override fun onCallAdded(call: Call) {
        Log.d("Liu", "onCallAdded" + call.state + call.details.contactDisplayName)
        val callData = entityMapper.map(call)
        callMonitorService.setPhoneCall(callData)
        openPhoneDialer()
    }

    override fun onCallRemoved(call: Call) {
        Log.d("Liu", "onCallRemoved" + call.state + call.details.contactDisplayName)
        val callData = entityMapper.map(call)
        callMonitorService.setPhoneCall(callData)
    }

    private fun openPhoneDialer() {
        startActivity(
            Intent(this, CallMonitorActivity::class.java)
                .setFlags(FLAG_ACTIVITY_NEW_TASK)
        )
    }
}