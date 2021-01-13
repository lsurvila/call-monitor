package com.lsurvila.callmonitortask.service.http.mapper

import com.lsurvila.callmonitortask.model.Call
import com.lsurvila.callmonitortask.model.PhoneState
import com.lsurvila.callmonitortask.service.http.model.LoggedCall
import com.lsurvila.callmonitortask.service.http.model.OngoingCall
import com.lsurvila.callmonitortask.util.DateTime
import java.util.*
import java.util.concurrent.TimeUnit

class CallMapper {

    fun map(call: Call, name: String?): OngoingCall {
        return if (call.state == PhoneState.CONNECTED) {
            OngoingCall(true, call.number, name)
        } else {
            OngoingCall(false)
        }
    }

    fun mapLoggedCall(call: Call, name: String?): LoggedCall {
        return LoggedCall(
            mapBeginning(call.connectedTime),
            mapDuration(call.connectedTime, call.disconnectedTime),
            call.number,
            name,
            call.monitoredCount
        )
    }

    private fun mapDuration(connectedTime: Date?, disconnectedTime: Date?): String {
        return if (connectedTime != null && disconnectedTime != null) {
            "${TimeUnit.MILLISECONDS.toSeconds(disconnectedTime.time - connectedTime.time)}"
        } else ""
    }

    private fun mapBeginning(connectedTime: Date?): String {
        return if (connectedTime != null) DateTime.formatLong(connectedTime) else ""
    }
}