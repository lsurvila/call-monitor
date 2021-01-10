package com.lsurvila.callmonitortask

import android.util.Log
import com.lsurvila.callmonitortask.model.State
import com.lsurvila.callmonitortask.service.callmonitor.CallMonitorService
import com.lsurvila.callmonitortask.service.http.HttpService
import java.lang.Exception

class StopCallMonitorUseCase(
    private val callMonitorService: CallMonitorService,
    private val httpService: HttpService
) {

    fun execute() {
        callMonitorService.setServiceState(State.STOPPING)
        try {
            httpService.stop()
        } catch (ex: Exception) {
            Log.e(HttpService.TAG, "Failed to stop HTTP server", ex)
        }
        callMonitorService.setServiceState(State.STOPPED)
        callMonitorService.setServiceState(State.NOT_STARTED)
    }
}