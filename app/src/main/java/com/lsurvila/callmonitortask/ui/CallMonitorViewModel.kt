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

    fun onStart() {
        val serviceState = getCallMonitorStateUseCase.execute()
        val viewState = mapper.mapOnlyToggleState(serviceState)
        _service.value = viewState
    }
}