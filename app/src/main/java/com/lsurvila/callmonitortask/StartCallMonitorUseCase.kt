package com.lsurvila.callmonitortask

import com.lsurvila.callmonitortask.model.CallMonitorState
import com.lsurvila.callmonitortask.service.callmonitor.CallMonitorService
import com.lsurvila.callmonitortask.service.network.NetworkService

class StartCallMonitorUseCase(private val callMonitorService: CallMonitorService,
                              private val networkService: NetworkService) {

    fun checkIfAvailable() {
        callMonitorService.setServiceState(CallMonitorState.STARTING)
        if (callMonitorService.isAvailable()) {
            if (callMonitorService.hasPhonePermission()) {
                if (callMonitorService.hasContactsPermission()) {
                    startServer()
                } else {
                    callMonitorService.setServiceState(CallMonitorState.CONTACTS_PERMISSION_NEEDED)
                }
            } else {
                callMonitorService.setServiceState(CallMonitorState.PHONE_PERMISSION_NEEDED)
            }
        } else {
            handleServiceNotStarted(CallMonitorState.PHONE_NOT_AVAILABLE)
        }
    }

    fun handlePhonePermission(permissionGranted: Boolean) {
        if (permissionGranted) {
            callMonitorService.setServiceState(CallMonitorState.CONTACTS_PERMISSION_NEEDED)
        } else {
            handleServiceNotStarted(CallMonitorState.PHONE_PERMISSION_DENIED)
        }
    }

    fun handleContactPermission(permissionGranted: Boolean) {
        if (permissionGranted) {
            startServer()
        } else {
            handleServiceNotStarted(CallMonitorState.CONTACTS_PERMISSION_DENIED)
        }
    }

    private fun startServer() {
        if (networkService.isWifiConnected()) {
            callMonitorService.setServiceState(CallMonitorState.STARTED)
        } else {
            handleServiceNotStarted(CallMonitorState.WIFI_DISCONNECTED)
        }
    }

    private fun handleServiceNotStarted(reasonState: CallMonitorState) {
        callMonitorService.setServiceState(reasonState)
        callMonitorService.setServiceState(CallMonitorState.NOT_STARTED)
    }
}