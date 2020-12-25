package com.lsurvila.callmonitortask

import androidx.lifecycle.*

class CallMonitorViewModel(private val serverManager: CallMonitorServerManager) : ViewModel() {

    private val serverStartTrigger = MutableLiveData<Int>()

    fun startServer(wifiIpAddress: Int) {
        serverStartTrigger.postValue(wifiIpAddress)
    }

    fun stopServer() {
        serverManager.stop()
    }

    val serverLiveData = serverStartTrigger.switchMap { serverManager.start(it).asLiveData() }
}

class CallMonitorViewModelFactory(private val serverManager: CallMonitorServerManager) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CallMonitorViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CallMonitorViewModel(serverManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}