package com.lsurvila.callmonitortask

import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import androidx.lifecycle.*


class CallMonitorViewModel(
    private val serverManager: CallMonitorServerManager,
    private val telephonyManager: TelephonyManager
) : ViewModel() {

    private val serverStartTrigger = MutableLiveData<Int>()

    fun startServer(ipAddress: Int) {
        serverStartTrigger.postValue(ipAddress)
    }

    fun stopServer() {
        serverManager.stop()
    }

    val serverLiveData = serverStartTrigger.switchMap { serverManager.start(it).asLiveData() }

    private val phoneStateListener = CallMonitorPhoneStateListener()

    private val isOngoingCall = MutableLiveData<Boolean>()

    fun isOngoingCall(): LiveData<Boolean> {
        return isOngoingCall
    }

    fun startCallMonitor() {
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE)
    }

    fun stopCallMonitor() {
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE)
    }

    private inner class CallMonitorPhoneStateListener : PhoneStateListener() {
        override fun onCallStateChanged(state: Int, incomingNumber: String) {
            super.onCallStateChanged(state, incomingNumber)
            when(state) {
                TelephonyManager.CALL_STATE_IDLE -> isOngoingCall.value = false
                TelephonyManager.CALL_STATE_RINGING,
                TelephonyManager.CALL_STATE_OFFHOOK -> isOngoingCall.value = true
            }
        }
    }
}


class CallMonitorViewModelFactory(
    private val serverManager: CallMonitorServerManager,
    private val telephonyManager: TelephonyManager
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CallMonitorViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CallMonitorViewModel(serverManager, telephonyManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}