package com.lsurvila.callmonitortask.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lsurvila.callmonitortask.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class CallMonitorViewModel(
    private val viewCallMonitorStateUseCase: ViewCallMonitorStateUseCase,
    private val startCallMonitorUseCase: StartCallMonitorUseCase,
    private val stopCallMonitorUseCase: StopCallMonitorUseCase,
    private val viewPhoneStateUseCase: ViewPhoneStateUseCase,
    private val answerPhoneCallUseCase: AnswerPhoneCallUseCase,
    private val rejectPhoneCallUseCase: RejectPhoneCallUseCase,
    private val mapper: ViewStateMapper
) : ViewModel() {

    fun service(): Flow<CallMonitorViewState> {
        return viewCallMonitorStateUseCase.execute().map { mapper.map(it) }
    }

    fun phone(): Flow<CallMonitorViewState> {
        return viewPhoneStateUseCase.execute().map { mapper.mapFromPhoneCall(it) }
    }

    fun onServiceSwitched(isSwitchedByUser: Boolean, isSwitchedOn: Boolean) {
        viewModelScope.launch {
            if (isSwitchedByUser) {
                if (isSwitchedOn) {
                    onServiceSwitchedOn()
                } else {
                    onServiceSwitchedOff()
                }
            }
        }
    }

    private suspend fun onServiceSwitchedOff() {
        stopCallMonitorUseCase.execute()
    }

    private suspend fun onServiceSwitchedOn() {
        startCallMonitorUseCase.checkIfAvailable()
    }

    fun onPhonePermissionGranted(permissionGranted: Boolean) {
        viewModelScope.launch {
            startCallMonitorUseCase.handlePhonePermission(permissionGranted)
        }
    }

    fun onContactsPermissionGranted(permissionGranted: Boolean) {
        viewModelScope.launch {
            startCallMonitorUseCase.handleContactPermission(permissionGranted)
        }
    }

    fun onAnswerClicked() {
        answerPhoneCallUseCase.execute()
    }

    fun onRejectClicked() {
        rejectPhoneCallUseCase.execute()
    }
}