package com.lsurvila.callmonitortask.service.callmonitor

import android.telecom.Call
import android.telephony.PhoneNumberUtils
 import com.lsurvila.callmonitortask.model.PhoneState
import com.lsurvila.callmonitortask.util.VersionUtil
import java.util.*

class CallEntityMapper {

    fun map(state: Int, callDetails: Call.Details): com.lsurvila.callmonitortask.model.Call {
        return com.lsurvila.callmonitortask.model.Call(
            mapIsIncomingCall(state, callDetails),
            callDetails.handle.schemeSpecificPart,
            callDetails.contactDisplayName
        )
    }

    private fun mapIsIncomingCall(state: Int, callDetails: Call.Details) =
        if (VersionUtil.isQ()) {
            callDetails.callDirection == Call.Details.DIRECTION_INCOMING
        } else {
            state == Call.STATE_RINGING
        }

    private fun mapNumber(phoneNumber: String?): String? = if (!phoneNumber.isNullOrEmpty()) {
        PhoneNumberUtils.formatNumber(phoneNumber, Locale.getDefault().country)
    } else null

    fun mapState(state: Int): PhoneState {
        return when (state) {
            Call.STATE_ACTIVE -> PhoneState.ACTIVE
            Call.STATE_RINGING -> PhoneState.RINGING
            Call.STATE_CONNECTING -> PhoneState.CONNECTING
            Call.STATE_DIALING -> PhoneState.DIALING
            Call.STATE_DISCONNECTED -> PhoneState.DISCONNECTED
            else -> PhoneState.IDLE
        }
    }
}