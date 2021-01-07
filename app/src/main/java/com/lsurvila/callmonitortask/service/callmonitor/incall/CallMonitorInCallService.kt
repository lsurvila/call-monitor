package com.lsurvila.callmonitortask.service.callmonitor.incall

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.telecom.Call
import android.telecom.InCallService
import com.lsurvila.callmonitortask.HandlePhoneCallUseCase
import com.lsurvila.callmonitortask.service.callmonitor.CallEntityMapper
import com.lsurvila.callmonitortask.ui.CallMonitorActivity
import org.koin.android.ext.android.inject

class CallMonitorInCallService: InCallService() {

    private val handlePhoneCallUseCase: HandlePhoneCallUseCase by inject()
    private val entityMapper: CallEntityMapper by inject()

    override fun onCallAdded(call: Call) {
        val phoneState = entityMapper.mapState(call.state)
        handlePhoneCallUseCase.execute(phoneState)
        openPhoneDialer()
    }

    override fun onCallRemoved(call: Call) {
        val phoneState = entityMapper.mapState(call.state)
        handlePhoneCallUseCase.execute(phoneState)
    }

    private fun openPhoneDialer() {
        startActivity(
            Intent(this, CallMonitorActivity::class.java)
                .setFlags(FLAG_ACTIVITY_NEW_TASK)
        )
    }
}