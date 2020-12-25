package com.lsurvila.callmonitortask

import androidx.lifecycle.*

class CallMonitorViewModel(server: CallMonitorServer) : ViewModel() {

    val serverLiveData: LiveData<String> = server.start().asLiveData()
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