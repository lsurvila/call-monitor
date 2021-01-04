package com.lsurvila.callmonitortask.service.callmonitor.connection

import android.content.Intent
import android.telecom.Connection
import android.telecom.ConnectionRequest
import android.telecom.ConnectionService
import android.telecom.PhoneAccountHandle
import android.util.Log

class CallMonitorConnectionService: ConnectionService() {

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }

    override fun onCreateIncomingHandoverConnection(
        fromPhoneAccountHandle: PhoneAccountHandle?,
        request: ConnectionRequest?
    ): Connection {
        Log.d("LIU", "T" + fromPhoneAccountHandle.toString() + request.toString())
        return super.onCreateIncomingHandoverConnection(fromPhoneAccountHandle, request)
    }

    override fun onCreateIncomingConnection(
        connectionManagerPhoneAccount: PhoneAccountHandle?,
        request: ConnectionRequest?
    ): Connection {
        Log.d("LIU", "T" + connectionManagerPhoneAccount.toString() + request.toString())
        return Connection.createCanceledConnection()
//        return super.onCreateIncomingConnection(connectionManagerPhoneAccount, request)
    }

    override fun onCreateIncomingConnectionFailed(
        connectionManagerPhoneAccount: PhoneAccountHandle?,
        request: ConnectionRequest?
    ) {
        Log.d("LIU", "F" + connectionManagerPhoneAccount.toString() + request.toString())
        super.onCreateIncomingConnectionFailed(connectionManagerPhoneAccount, request)
    }

    override fun onCreateOutgoingConnection(
        connectionManagerPhoneAccount: PhoneAccountHandle?,
        request: ConnectionRequest?
    ): Connection {
        return super.onCreateOutgoingConnection(connectionManagerPhoneAccount, request)
    }

    override fun onCreateOutgoingConnectionFailed(
        connectionManagerPhoneAccount: PhoneAccountHandle?,
        request: ConnectionRequest?
    ) {
        super.onCreateOutgoingConnectionFailed(connectionManagerPhoneAccount, request)
    }
}