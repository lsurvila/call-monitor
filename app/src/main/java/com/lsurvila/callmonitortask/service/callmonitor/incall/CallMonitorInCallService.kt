package com.lsurvila.callmonitortask.service.callmonitor.incall

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.telecom.Call
import android.telecom.InCallService
import android.util.Log
import com.lsurvila.callmonitortask.LogOngoingCallUseCase
import com.lsurvila.callmonitortask.service.callmonitor.CallEntityMapper
import com.lsurvila.callmonitortask.ui.CallMonitorActivity
import org.koin.android.ext.android.inject

class CallMonitorInCallService: InCallService() {

    private val logOngoingCallUseCase: LogOngoingCallUseCase by inject()
    private val entityMapper: CallEntityMapper by inject()

    override fun onCallAdded(call: Call) {
        val callEntity = entityMapper.map(call.state, call.details)
        logOngoingCallUseCase.execute(callEntity)
        openPhoneDialer()
    }

    private fun openPhoneDialer() {
        startActivity(
            Intent(this, CallMonitorActivity::class.java)
                .setFlags(FLAG_ACTIVITY_NEW_TASK)
        )
    }
}