package com.lsurvila.callmonitortask

import com.lsurvila.callmonitortask.model.CallMonitorState
import com.lsurvila.callmonitortask.model.State
import com.lsurvila.callmonitortask.service.callmonitor.CallMonitorService
import com.lsurvila.callmonitortask.service.http.HttpService
import com.lsurvila.callmonitortask.service.network.NetworkService

class StartCallMonitorUseCase(
    private val callMonitorService: CallMonitorService,
    private val networkService: NetworkService,
    private val httpService: HttpService
) {

    fun checkIfAvailable() {
        callMonitorService.setServiceState(State.STARTING)
        if (callMonitorService.isAvailable()) {
            if (callMonitorService.hasPhonePermission()) {
                if (callMonitorService.hasContactsPermission()) {
                    startServer()
                } else {
                    callMonitorService.setServiceState(State.CONTACTS_PERMISSION_NEEDED)
                }
            } else {
                callMonitorService.setServiceState(State.PHONE_PERMISSION_NEEDED)
            }
        } else {
            handleServiceNotStarted(State.PHONE_NOT_AVAILABLE)
        }
    }

    fun handlePhonePermission(permissionGranted: Boolean) {
        if (permissionGranted) {
            callMonitorService.setServiceState(State.CONTACTS_PERMISSION_NEEDED)
        } else {
            handleServiceNotStarted(State.PHONE_PERMISSION_DENIED)
        }
    }

    fun handleContactPermission(permissionGranted: Boolean) {
        if (permissionGranted) {
            startServer()
        } else {
            handleServiceNotStarted(State.CONTACTS_PERMISSION_DENIED)
        }
    }

    private fun startServer() {
        if (networkService.isWifiConnected()) {
            val ipAddress = networkService.getWifiIpAddress()
            if (ipAddress != null) {
                httpService.start(ipAddress)
                callMonitorService.setServiceState(CallMonitorState(State.STARTED, httpService.address))
            } else {
                handleServiceNotStarted(State.WIFI_IP_FAILED_TO_RESOLVE)
            }
        } else {
            handleServiceNotStarted(State.WIFI_DISCONNECTED)
        }
    }

    private fun handleServiceNotStarted(reasonState: State) {
        callMonitorService.setServiceState(reasonState)
        callMonitorService.setServiceState(State.NOT_STARTED)
    }
}