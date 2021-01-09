package com.lsurvila.callmonitortask.ui

import com.lsurvila.callmonitortask.model.Call
import com.lsurvila.callmonitortask.model.CallMonitorState
import com.lsurvila.callmonitortask.model.PhoneState
import com.lsurvila.callmonitortask.util.DateTimeUtil
import java.util.*

data class CallMonitorViewState(
    val toggleService: Boolean? = null,
    val serviceSwitchChecked: Boolean? = false,
    val consoleMessage: String? = null,
    val isAnswerButtonEnabled: Boolean? = null,
    val isRejectButtonEnabled: Boolean? = null,
)

class ViewStateMapper {

    fun map(serviceState: CallMonitorState): CallMonitorViewState {
        return when (serviceState) {
            CallMonitorState.IDLE -> CallMonitorViewState()
            CallMonitorState.NOT_STARTED -> CallMonitorViewState()
            CallMonitorState.STARTING -> CallMonitorViewState(
                toggleService = true,
                serviceSwitchChecked = true,
                consoleMessage = "Starting...".withDateTime()
            )
            CallMonitorState.AVAILABLE -> CallMonitorViewState(
                serviceSwitchChecked = true
            )
            CallMonitorState.NOT_AVAILABLE -> CallMonitorViewState(
                serviceSwitchChecked = false,
                consoleMessage = "Service is not available".withDateTime()
            )
            CallMonitorState.PERMISSION_DENIED -> CallMonitorViewState(
                consoleMessage = "Service permission was denied, if was not asked clear app data".withDateTime()
            )
            CallMonitorState.STARTED -> CallMonitorViewState(
                serviceSwitchChecked = true,
                consoleMessage = "Started".withDateTime()
            )
            CallMonitorState.STOPPING -> CallMonitorViewState(
                toggleService = false,
                consoleMessage = "Stopping...".withDateTime()
            )
            CallMonitorState.STOPPED -> CallMonitorViewState(
                consoleMessage = "Stopped".withDateTime()
            )
            CallMonitorState.STOPPED_WITH_WARNING -> CallMonitorViewState(
                consoleMessage = "Stopped. If you wish to restore call screening to default app uninstall Call Monitor"
                    .withDateTime()
            )
        }
    }

    fun mapOnlyToggleState(serviceState: CallMonitorState): CallMonitorViewState {
        return if (serviceState == CallMonitorState.STARTING || serviceState == CallMonitorState.STARTED)
            CallMonitorViewState(serviceSwitchChecked = true)
        else
            CallMonitorViewState()
    }

    private fun String.withDateTime(): String {
        return "${DateTimeUtil.formatDateTime(Date())}: $this"
    }

    fun mapFromPhoneCall(call: Call): CallMonitorViewState {
        return when (call.state) {
            PhoneState.IDLE -> CallMonitorViewState(
                isAnswerButtonEnabled = false,
                isRejectButtonEnabled = false
            )
            PhoneState.RINGING -> CallMonitorViewState(
                consoleMessage = "Incoming call from ${call.number}...".withDateTime(),
                isAnswerButtonEnabled = true,
                isRejectButtonEnabled = true
            )
            PhoneState.CONNECTING -> CallMonitorViewState(
                consoleMessage = "Call connecting to ${call.number}...".withDateTime(),
                isAnswerButtonEnabled = false,
                isRejectButtonEnabled = true
            )
            PhoneState.ACTIVE -> CallMonitorViewState(
                consoleMessage = "Call connected...".withDateTime(),
                isAnswerButtonEnabled = false,
                isRejectButtonEnabled = true,
            )
            PhoneState.DISCONNECTED -> CallMonitorViewState(
                consoleMessage = "Call ended.".withDateTime(),
                isAnswerButtonEnabled = false,
                isRejectButtonEnabled = false
            )
            PhoneState.UNKNOWN -> CallMonitorViewState() // don't change anything
        }
    }
}