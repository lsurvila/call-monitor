package com.lsurvila.callmonitortask

import com.lsurvila.callmonitortask.model.CallMonitorState
import com.lsurvila.callmonitortask.service.callmonitor.CallMonitorService

class StartCallMonitorUseCase(private val callMonitorService: CallMonitorService) {

    fun checkIfAvailable() {
        callMonitorService.setServiceState(CallMonitorState.STARTING)
        if (callMonitorService.isAvailable()) {
            if (callMonitorService.hasPhonePermission()) {
                callMonitorService.setServiceState(CallMonitorState.CONTACTS_PERMISSION_NEEDED)
            } else {
                callMonitorService.setServiceState(CallMonitorState.PHONE_PERMISSION_NEEDED)
            }
        } else {
            callMonitorService.setServiceState(CallMonitorState.PHONE_NOT_AVAILABLE)
        }
    }

    fun handlePhonePermission(phonePermissionWasGranted: Boolean) {
        if (phonePermissionWasGranted) {
            callMonitorService.setServiceState(CallMonitorState.CONTACTS_PERMISSION_NEEDED)
        } else {
            callMonitorService.setServiceState(CallMonitorState.PHONE_PERMISSION_DENIED)
        }
    }

    fun handleContactPermission(permissionGranted: Boolean) {
        if (permissionGranted) {
            callMonitorService.setServiceState(CallMonitorState.STARTED)
        } else {
            callMonitorService.setServiceState(CallMonitorState.CONTACTS_PERMISSION_REQUIRED)
        }
    }

    fun handleContactPermissionDenied() {
        callMonitorService.setServiceState(CallMonitorState.CONTACTS_PERMISSION_DENIED)
    }
}