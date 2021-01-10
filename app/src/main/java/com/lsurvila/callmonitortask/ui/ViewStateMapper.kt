package com.lsurvila.callmonitortask.ui

import com.lsurvila.callmonitortask.model.Call
import com.lsurvila.callmonitortask.model.CallMonitorState
import com.lsurvila.callmonitortask.model.PhoneState
import com.lsurvila.callmonitortask.util.DateTimeUtil
import java.util.*

data class CallMonitorViewState(
    val isRejectButtonEnabled: Boolean? = null,
    val isAnswerButtonEnabled: Boolean? = null,
    val serviceSwitchChecked: Boolean? = false,
    val consoleMessage: String? = null,
    val askForPhonePermission: Boolean? = null,
    val askForContactsPermission: Boolean? = null
)

class ViewStateMapper {

    fun map(serviceState: CallMonitorState): CallMonitorViewState {
        return when (serviceState) {
            CallMonitorState.NOT_STARTED -> CallMonitorViewState()
            CallMonitorState.STARTING -> CallMonitorViewState(
                serviceSwitchChecked = true,
                consoleMessage = "Starting...".withDateTime()
            )
            CallMonitorState.PHONE_NOT_AVAILABLE -> CallMonitorViewState(
                serviceSwitchChecked = false,
                consoleMessage = "Phone service is not available on this device".withDateTime()
            )
            CallMonitorState.PHONE_PERMISSION_NEEDED -> CallMonitorViewState(
                serviceSwitchChecked = true,
                askForPhonePermission = true
            )
            CallMonitorState.PHONE_PERMISSION_DENIED -> CallMonitorViewState(
                serviceSwitchChecked = false,
                consoleMessage = "Phone permission was denied. You can set it manually in app settings".withDateTime()
            )
            CallMonitorState.CONTACTS_PERMISSION_NEEDED -> CallMonitorViewState(
                serviceSwitchChecked = true,
                askForContactsPermission = true
            )
            CallMonitorState.CONTACTS_PERMISSION_DENIED -> CallMonitorViewState(
                serviceSwitchChecked = false,
                consoleMessage = "Contacts permission was denied. You can set it manually in app settings".withDateTime()
            )
            CallMonitorState.STARTED -> CallMonitorViewState(
                serviceSwitchChecked = true,
                consoleMessage = "Started".withDateTime()
            )

            CallMonitorState.STOPPING -> CallMonitorViewState(
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