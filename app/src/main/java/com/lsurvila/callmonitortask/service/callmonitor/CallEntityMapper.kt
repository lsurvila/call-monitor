package com.lsurvila.callmonitortask.service.callmonitor

import android.telecom.Call
import android.telephony.PhoneNumberUtils
 import com.lsurvila.callmonitortask.model.PhoneState
import com.lsurvila.callmonitortask.util.VersionUtil
import java.util.*

class CallEntityMapper {

    fun map(call: Call): com.lsurvila.callmonitortask.model.Call {
        return com.lsurvila.callmonitortask.model.Call(
            mapState(call.state),
            call.details.handle.schemeSpecificPart,
            call.details.contactDisplayName
        )
    }

    private fun mapIsIncomingCall(state: Int, callDetails: Call.Details) =
        if (VersionUtil.isQ()) {
            callDetails.callDirection == Call.Details.DIRECTION_INCOMING
        } else {
            state == Call.STATE_RINGING
        }

    private fun mapNumber(phoneNumber: String): String =
        PhoneNumberUtils.formatNumber(phoneNumber, Locale.getDefault().country)

    private fun mapState(state: Int): PhoneState {
        return when (state) {
            Call.STATE_RINGING -> PhoneState.RINGING // incoming
            Call.STATE_CONNECTING -> PhoneState.CONNECTING // outgoing
            Call.STATE_DISCONNECTED -> PhoneState.DISCONNECTED
            else -> PhoneState.IDLE
        }
    }
}