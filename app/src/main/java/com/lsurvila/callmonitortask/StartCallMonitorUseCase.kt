package com.lsurvila.callmonitortask

import com.lsurvila.callmonitortask.model.State
import com.lsurvila.callmonitortask.service.callmonitor.CallMonitorService
import com.lsurvila.callmonitortask.service.http.HttpService
import com.lsurvila.callmonitortask.service.network.NetworkService

class StartCallMonitorUseCase(
    private val callMonitorService: CallMonitorService,
    private val networkService: NetworkService,
    private val httpService: HttpService
) {

    suspend fun checkIfAvailable() {
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

    suspend fun handleContactPermission(permissionGranted: Boolean) {
        if (permissionGranted) {
            startServer()
        } else {
            handleServiceNotStarted(State.CONTACTS_PERMISSION_DENIED)
        }
    }

    private suspend fun startServer() {
        if (networkService.isWifiConnected()) {
            val wifiAddress = networkService.getWifiAddress(HttpService.PORT)
            if (wifiAddress != null) {
                httpService.address = wifiAddress
                val state = httpService.start()
                callMonitorService.setServiceState(state)
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