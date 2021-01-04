package com.lsurvila.callmonitortask.ui

import androidx.lifecycle.*
import com.lsurvila.callmonitortask.GetCallMonitorStateUseCase
import com.lsurvila.callmonitortask.StartCallMonitorUseCase
import com.lsurvila.callmonitortask.StopCallMonitorUseCase
import com.lsurvila.callmonitortask.model.CallMonitorState

class CallMonitorViewModel(
    private val getCallMonitorStateUseCase: GetCallMonitorStateUseCase,
    private val startCallMonitorUseCase: StartCallMonitorUseCase,
    private val stopCallMonitorUseCase: StopCallMonitorUseCase,
    private val mapper: ViewStateMapper
) : ViewModel() {

    private val _service = MutableLiveData<CallMonitorViewState>()

    fun service() = _service

    fun toggleService(isToggledByUser: Boolean, isToggledOn: Boolean) {
        if (isToggledByUser) {
            if (isToggledOn) {
                startService()
            } else {
                stopService()
            }
        }
    }

    fun startService(permissionGranted: Boolean = false) {
        val serviceState = startCallMonitorUseCase.execute(permissionGranted)
        updateViewState(serviceState)
    }

    fun stopService(withWarning: Boolean = false) {
        val serviceState = stopCallMonitorUseCase.execute(withWarning)
        updateViewState(serviceState)
    }

    private fun updateViewState(serviceState: CallMonitorState) {
        val viewState = mapper.map(serviceState)
        _service.value = viewState
    }

    fun syncServiceToggle() {
        val serviceState = getCallMonitorStateUseCase.execute()
        val viewState = mapper.mapOnlyToggleState(serviceState)
        _service.value = viewState
    }


//    private val serverStartTrigger = MutableLiveData<Int>()
//

//    fun startServer(ipAddress: Int) {
//        serverStartTrigger.postValue(ipAddress)
//    }
//
//    fun stopServer() {
//        serverManager.stop()
//    }
//
//    val serverLiveData = serverStartTrigger.switchMap { serverManager.start(it).asLiveData() }
//
//    private val phoneStateListener = CallMonitorPhoneStateListener()
//
//    private val isOngoingCall = MutableLiveData<Boolean>()
//
//    fun isOngoingCall(): LiveData<Boolean> {
//        return isOngoingCall
//    }
//
//    fun startCallMonitor() {
//        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE)
//    }
//
//    fun stopCallMonitor() {
//        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE)
//    }
//
//    private inner class CallMonitorPhoneStateListener : PhoneStateListener() {
//        override fun onCallStateChanged(state: Int, incomingNumber: String) {
//            super.onCallStateChanged(state, incomingNumber)
//            when(state) {
//                TelephonyManager.CALL_STATE_IDLE -> isOngoingCall.value = false
//                TelephonyManager.CALL_STATE_RINGING,
//                TelephonyManager.CALL_STATE_OFFHOOK -> isOngoingCall.value = true
//            }
//        }
//    }
}