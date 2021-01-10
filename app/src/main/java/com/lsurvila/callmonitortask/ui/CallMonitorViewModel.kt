package com.lsurvila.callmonitortask.ui

import androidx.lifecycle.*
import com.lsurvila.callmonitortask.*
import kotlinx.coroutines.flow.*

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
        if (isSwitchedByUser) {
            if (isSwitchedOn) {
                onServiceSwitchedOn()
            } else {
                onServiceSwitchedOff()
            }
        }
    }

    private fun onServiceSwitchedOff() {
        stopCallMonitorUseCase.execute()
    }

    private fun onServiceSwitchedOn() {
        startCallMonitorUseCase.checkIfAvailable()
    }

    fun onPhonePermissionGranted(permissionGranted: Boolean) {
        startCallMonitorUseCase.handlePhonePermission(permissionGranted)
    }

    fun onContactsPermissionGranted(permissionGranted: Boolean) {
        startCallMonitorUseCase.handleContactPermission(permissionGranted)
    }

    fun onAnswerClicked() {
        answerPhoneCallUseCase.execute()
    }

    fun onRejectClicked() {
        rejectPhoneCallUseCase.execute()
    }
}