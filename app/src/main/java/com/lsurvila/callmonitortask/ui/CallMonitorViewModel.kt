package com.lsurvila.callmonitortask.ui

import androidx.lifecycle.*
import com.lsurvila.callmonitortask.*
import com.lsurvila.callmonitortask.model.CallMonitorState
import kotlinx.coroutines.flow.*

class CallMonitorViewModel(
    private val getCallMonitorStateUseCase: GetCallMonitorStateUseCase,
    private val startCallMonitorUseCase: StartCallMonitorUseCase,
    private val stopCallMonitorUseCase: StopCallMonitorUseCase,
    private val getPhoneStateUseCase: GetPhoneStateUseCase,
    private val answerPhoneCallUseCase: AnswerPhoneCallUseCase,
    private val rejectPhoneCallUseCase: RejectPhoneCallUseCase,
    private val mapper: ViewStateMapper
) : ViewModel() {

    private val _service = MutableLiveData<CallMonitorViewState>()

    fun service(): Flow<CallMonitorViewState> {
        return getCallMonitorStateUseCase.execute().map { mapper.map(it) }
    }

    fun phone(): Flow<CallMonitorViewState> {
        return getPhoneStateUseCase.execute().map { mapper.mapFromPhoneCall(it) }
    }

    fun onServiceSwitched(isSwitchedByUser: Boolean, isSWitchedOn: Boolean) {
        if (isSwitchedByUser) {
            if (isSWitchedOn) {
                checkForPhonePermission()
            } else {
                stopService()
            }
        }
    }
    
    private fun checkForPhonePermission() {
        startCallMonitorUseCase.checkIfAvailable()
    }

    fun onPhonePermissionGranted(permissionGranted: Boolean) {
        startCallMonitorUseCase.handlePhonePermission(permissionGranted)
    }

    fun onContactsPermissionGranted(permissionGranted: Boolean) {
        startCallMonitorUseCase.handleContactPermission(permissionGranted)
    }

    fun onContactsPermissionDenied() {
        startCallMonitorUseCase.handleContactPermissionDenied()
    }


    fun stopService(withWarning: Boolean = false) {
        val serviceState = stopCallMonitorUseCase.execute(withWarning)
        updateViewState(serviceState)
    }

    private fun updateViewState(serviceState: CallMonitorState) {
        val viewState = mapper.map(serviceState)
        _service.value = viewState
    }

    fun onAnswerClicked() {
        answerPhoneCallUseCase.execute()
    }

    fun onRejectClicked() {
        rejectPhoneCallUseCase.execute()
    }
}