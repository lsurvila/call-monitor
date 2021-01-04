package com.lsurvila.callmonitortask.model

enum class CallMonitorState {
    NOT_STARTED,
    STARTING,
    NOT_AVAILABLE,
    PERMISSION_DENIED,
    STARTED,
    STOPPING,
    STOPPED,
    STOPPED_WITH_WARNING
}