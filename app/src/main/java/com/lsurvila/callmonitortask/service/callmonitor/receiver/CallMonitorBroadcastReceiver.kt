package com.lsurvila.callmonitortask.service.callmonitor.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.util.Log


class CallMonitorBroadcastReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent) {
        val extras = intent.extras
        if (extras != null) {
            val state = extras.getString(TelephonyManager.EXTRA_STATE)
            state?.let { Log.d("LIU", it) }
            if (state == TelephonyManager.EXTRA_STATE_RINGING) {
                val phoneNumber = extras
                    .getString(TelephonyManager.EXTRA_INCOMING_NUMBER)
                phoneNumber?.let { Log.d("MY_DEBUG_TAG", it) }
            }
        }
    }
}