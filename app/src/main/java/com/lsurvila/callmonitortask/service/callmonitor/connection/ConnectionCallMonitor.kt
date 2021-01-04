package com.lsurvila.callmonitortask.service.callmonitor.connection

import android.content.ComponentName
import android.content.Context
import android.telecom.PhoneAccount
import android.telecom.PhoneAccountHandle
import android.telecom.TelecomManager
import com.lsurvila.callmonitortask.service.callmonitor.CallMonitor


class ConnectionCallMonitor(private val telecomManager: TelecomManager, private val context: Context): CallMonitor() {
    override fun isAvailable(): Boolean {
        val componentName = ComponentName(context, CallMonitorConnectionService::class.java)
        val phoneAccountHandle = PhoneAccountHandle(componentName, "CallMonitorHandle")
        val builder = PhoneAccount.Builder(phoneAccountHandle, "CallMonitor")
        builder.setCapabilities(PhoneAccount.CAPABILITY_CONNECTION_MANAGER)
        val phoneAccount = builder.build()

        telecomManager.registerPhoneAccount(phoneAccount)

//        telecomManager.addNewIncomingCall(phoneAccountHandle, null)
        return true
    }
}