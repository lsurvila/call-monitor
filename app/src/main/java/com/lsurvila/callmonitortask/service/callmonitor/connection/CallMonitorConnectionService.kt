package com.lsurvila.callmonitortask.service.callmonitor.connection

import android.telecom.Connection
import android.telecom.ConnectionRequest
import android.telecom.ConnectionService
import android.telecom.PhoneAccountHandle
import android.util.Log

class CallMonitorConnectionService: ConnectionService() {

    override fun onCreateIncomingConnection(
        connectionManagerPhoneAccount: PhoneAccountHandle?,
        request: ConnectionRequest?
    ): Connection {
        Log.d("LIU", connectionManagerPhoneAccount.toString() + request.toString())
        return super.onCreateIncomingConnection(connectionManagerPhoneAccount, request)
    }
}