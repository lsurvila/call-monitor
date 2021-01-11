package com.lsurvila.callmonitortask.service.callmonitor

import android.telecom.Call
import com.lsurvila.callmonitortask.model.PhoneState

class CallEntityMapper {

    fun map(call: Call): com.lsurvila.callmonitortask.model.Call {
        return com.lsurvila.callmonitortask.model.Call(
            mapState(call.state),
            call.details.handle.schemeSpecificPart
        )
    }

    private fun mapState(state: Int): PhoneState {
        return when (state) {
            Call.STATE_RINGING -> PhoneState.INCOMING
            Call.STATE_CONNECTING -> PhoneState.OUTGOING
            Call.STATE_ACTIVE -> PhoneState.CONNECTED
            Call.STATE_DISCONNECTED -> PhoneState.DISCONNECTED
            else -> PhoneState.UNKNOWN
        }
    }
}