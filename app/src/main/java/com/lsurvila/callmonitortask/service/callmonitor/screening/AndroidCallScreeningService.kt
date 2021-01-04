package com.lsurvila.callmonitortask.service.callmonitor.screening

import android.os.Build
import android.telecom.Call.Details
import android.telecom.CallScreeningService
import androidx.annotation.RequiresApi
import com.lsurvila.callmonitortask.LogOngoingCallUseCase
import com.lsurvila.callmonitortask.service.callmonitor.CallEntityMapper
import org.koin.android.ext.android.inject

@RequiresApi(Build.VERSION_CODES.Q)
class AndroidCallScreeningService : CallScreeningService() {

    private val logOngoingCallUseCase: LogOngoingCallUseCase by inject()
    private val entityMapper: CallEntityMapper by inject()

    override fun onScreenCall(callDetails: Details) {
        val call = entityMapper.map(callDetails)
        logOngoingCallUseCase.execute(call)
    }
}