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

    fun onServiceSwitched(isSwitchedByUser: Boolean, isSWitchedOn: Boolean) {
        if (isSwitchedByUser) {
            if (isSWitchedOn) {
                checkIfPhoneIsAvailable()
            } else {
                stopService()
            }
        }
    }

    private fun stopService() {
        stopCallMonitorUseCase.execute()
    }

    private fun checkIfPhoneIsAvailable() {
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