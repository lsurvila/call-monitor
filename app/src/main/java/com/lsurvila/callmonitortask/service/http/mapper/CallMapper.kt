package com.lsurvila.callmonitortask.service.http.mapper

import com.lsurvila.callmonitortask.model.Call
import com.lsurvila.callmonitortask.model.PhoneState
import com.lsurvila.callmonitortask.service.http.model.OngoingCall

class CallMapper {

    fun map(call: Call, name: String?): OngoingCall {
        return if (call.number != null && call.state == PhoneState.CONNECTED) {
            OngoingCall(true, call.number, name)
        } else {
            OngoingCall(false)
        }
    }
}