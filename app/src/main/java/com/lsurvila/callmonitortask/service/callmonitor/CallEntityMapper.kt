package com.lsurvila.callmonitortask.service.callmonitor

import android.os.Build
import android.telecom.Call
import android.telephony.PhoneNumberUtils
import androidx.annotation.RequiresApi
import java.util.*

class CallEntityMapper {

    @RequiresApi(Build.VERSION_CODES.Q)
    fun map(callDetails: Call.Details): com.lsurvila.callmonitortask.model.Call {
        return com.lsurvila.callmonitortask.model.Call(
            mapIsIncomingCall(callDetails.callDirection),
            mapNumber(callDetails.handle.schemeSpecificPart),
            callDetails.callerDisplayName + callDetails.contactDisplayName
        )
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun mapIsIncomingCall(callDirection: Int) =
        callDirection == Call.Details.DIRECTION_INCOMING

    private fun mapNumber(phoneNumber: String?): String? = if (!phoneNumber.isNullOrEmpty()) {
        PhoneNumberUtils.formatNumber(phoneNumber, Locale.getDefault().country)
    } else null
}