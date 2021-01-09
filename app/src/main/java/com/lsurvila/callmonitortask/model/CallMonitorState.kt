package com.lsurvila.callmonitortask.model

enum class CallMonitorState {
    IDLE,
    NOT_STARTED,
    STARTING,
    NOT_AVAILABLE,
    AVAILABLE,
    PERMISSION_DENIED,
    STARTED,
    STOPPING,
    STOPPED,
    STOPPED_WITH_WARNING
}