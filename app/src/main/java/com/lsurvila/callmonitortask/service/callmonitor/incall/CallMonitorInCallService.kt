package com.lsurvila.callmonitortask.service.callmonitor.incall

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.telecom.Call
import android.telecom.InCallService
import com.lsurvila.callmonitortask.service.callmonitor.CallEntityMapper
import com.lsurvila.callmonitortask.service.callmonitor.CallMonitorService
import com.lsurvila.callmonitortask.ui.CallMonitorActivity
import org.koin.android.ext.android.inject

class CallMonitorInCallService: InCallService() {

    private val callMonitorService: CallMonitorService by inject()
    private val entityMapper: CallEntityMapper by inject()

    override fun onCallAdded(call: Call) {
        val callData = entityMapper.map(call)
        callMonitorService.setPhoneCall(callData)
//        handlePhoneCallUseCase.execute(phoneState)
        openPhoneDialer()
    }

    override fun onCallRemoved(call: Call) {
        val callData = entityMapper.map(call)
        callMonitorService.setPhoneCall(callData)
//        handlePhoneCallUseCase.execute(phoneState)
    }

    private fun openPhoneDialer() {
        startActivity(
            Intent(this, CallMonitorActivity::class.java)
                .setFlags(FLAG_ACTIVITY_NEW_TASK)
        )
    }
}