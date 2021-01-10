package com.lsurvila.callmonitortask.model

import java.net.URI

enum class State {
    NOT_STARTED,
    STARTING,
    PHONE_PERMISSION_NEEDED,
    CONTACTS_PERMISSION_NEEDED,
    STARTED,
    STOPPING,
    STOPPED,
    // error state
    PHONE_NOT_AVAILABLE,
    PHONE_PERMISSION_DENIED,
    CONTACTS_PERMISSION_DENIED,
    WIFI_DISCONNECTED,
    WIFI_IP_FAILED_TO_RESOLVE,
    HTTP_SERVER_FAILED
}

data class CallMonitorState(val state: State, val address: URI? = null)