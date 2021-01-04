package com.lsurvila.callmonitortask.ui

import com.lsurvila.callmonitortask.model.CallMonitorState
import com.lsurvila.callmonitortask.util.DateTimeUtil
import java.util.*

class CallMonitorViewState(
    val toggleService: Boolean? = null,
    val serviceSwitchChecked: Boolean = false,
    val notificationMessage: String? = null,
    val consoleMessage: String? = null
)

class ViewStateMapper {

    fun map(serviceState: CallMonitorState): CallMonitorViewState {
        return when (serviceState) {
            CallMonitorState.NOT_STARTED -> CallMonitorViewState()
            CallMonitorState.STARTING -> CallMonitorViewState(
                toggleService = true,
                serviceSwitchChecked = true,
                consoleMessage = withDateTime("Starting...")
            )
            CallMonitorState.NOT_AVAILABLE -> CallMonitorViewState(
                consoleMessage = withDateTime("Service is not available")
            )
            CallMonitorState.PERMISSION_DENIED -> CallMonitorViewState(
                consoleMessage = withDateTime("Service permission was denied, if was not asked clear app data")
            )
            CallMonitorState.STARTED -> CallMonitorViewState(
                serviceSwitchChecked = true,
                notificationMessage = "Call Monitor is running...",
                consoleMessage = withDateTime("Started")
            )
            CallMonitorState.STOPPING -> CallMonitorViewState(
                toggleService = false,
                consoleMessage = withDateTime("Stopping...")
            )
            CallMonitorState.STOPPED -> CallMonitorViewState(
                consoleMessage = withDateTime("Stopped")
            )
            CallMonitorState.STOPPED_WITH_WARNING -> CallMonitorViewState(
                consoleMessage = withDateTime("Stopped. If you wish to restore call screening to default app uninstall Call Monitor")
            )
        }
    }

    fun mapOnlyToggleState(serviceState: CallMonitorState): CallMonitorViewState {
        return if (serviceState == CallMonitorState.STARTING || serviceState == CallMonitorState.STARTED)
            CallMonitorViewState(serviceSwitchChecked = true)
        else
            CallMonitorViewState()
    }

    private fun withDateTime(message: String): String {
        return "${DateTimeUtil.formatDateTime(Date())}: $message"
    }
}