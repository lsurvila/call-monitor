package com.lsurvila.callmonitortask

import androidx.lifecycle.*

class CallMonitorViewModel(server: CallMonitorServer) : ViewModel() {

    private val restartTrigger = MutableLiveData<Int> ()

    fun restartServer(wifiIpAddress: Int) {
        restartTrigger.postValue(wifiIpAddress)
    }

    val serverLiveData = restartTrigger.switchMap { server.start(it).asLiveData() }
}

class CallMonitorViewModelFactory(private val server: CallMonitorServer) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CallMonitorViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CallMonitorViewModel(server) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}