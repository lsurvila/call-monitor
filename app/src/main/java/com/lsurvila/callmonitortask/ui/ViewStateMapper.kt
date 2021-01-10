package com.lsurvila.callmonitortask.ui

import com.lsurvila.callmonitortask.model.Call
import com.lsurvila.callmonitortask.model.CallMonitorState
import com.lsurvila.callmonitortask.model.PhoneState
import com.lsurvila.callmonitortask.model.State
import com.lsurvila.callmonitortask.util.DateTimeUtil
import java.util.*

data class CallMonitorViewState(
    val isRejectButtonEnabled: Boolean? = null,
    val isAnswerButtonEnabled: Boolean? = null,
    val isServiceSwitchChecked: Boolean? = false,
    val consoleMessage: String? = null,
    val askForPhonePermission: Boolean? = null,
    val askForContactsPermission: Boolean? = null
)

class ViewStateMapper {

    fun map(serviceState: CallMonitorState): CallMonitorViewState {
        return when (serviceState.state) {
            State.NOT_STARTED -> CallMonitorViewState(
                isServiceSwitchChecked = false
            )
            State.STARTING -> CallMonitorViewState(
                isServiceSwitchChecked = true,
                consoleMessage = "Starting...".withDateTime()
            )
            State.PHONE_NOT_AVAILABLE -> CallMonitorViewState(
                isServiceSwitchChecked = true,
                consoleMessage = "Phone service is not available on this device".withDateTime()
            )
            State.PHONE_PERMISSION_NEEDED -> CallMonitorViewState(
                isServiceSwitchChecked = true,
                askForPhonePermission = true
            )
            State.CONTACTS_PERMISSION_NEEDED -> CallMonitorViewState(
                isServiceSwitchChecked = true,
                askForContactsPermission = true
            )
            State.STARTED -> CallMonitorViewState(
                isServiceSwitchChecked = true,
                consoleMessage = "Started on ${serviceState.address}".withDateTime()
            )
            State.STOPPING -> CallMonitorViewState(
                isServiceSwitchChecked = false,
                consoleMessage = "Stopping...".withDateTime()
            )
            State.STOPPED -> CallMonitorViewState(
                isServiceSwitchChecked = false,
                consoleMessage = "Stopped. If you wish to restore Phone and Contacts permissions, you can do it in app settings".withDateTime()
            )
            // Error states
            State.PHONE_PERMISSION_DENIED -> CallMonitorViewState(
                isServiceSwitchChecked = false,
                consoleMessage = "Phone permission was denied. You can set it manually in app settings".withDateTime()
            )
            State.CONTACTS_PERMISSION_DENIED -> CallMonitorViewState(
                isServiceSwitchChecked = false,
                consoleMessage = "Contacts permission was denied. You can set it manually in app settings".withDateTime()
            )
            State.WIFI_DISCONNECTED -> CallMonitorViewState(
                isServiceSwitchChecked = false,
                consoleMessage = "Service requires active Wifi connection, currently it's offline".withDateTime()
            )
            State.WIFI_IP_FAILED_TO_RESOLVE -> CallMonitorViewState(
                isServiceSwitchChecked = false,
                consoleMessage = "Failed to resolve Wifi IP address".withDateTime()
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
            PhoneState.INCOMING -> CallMonitorViewState(
                consoleMessage = "Incoming call from ${call.number}...".withDateTime(),
                isAnswerButtonEnabled = true,
                isRejectButtonEnabled = true
            )
            PhoneState.OUTGOING -> CallMonitorViewState(
                consoleMessage = "Call connecting to ${call.number}...".withDateTime(),
                isAnswerButtonEnabled = false,
                isRejectButtonEnabled = true
            )
            PhoneState.CONNECTED -> CallMonitorViewState(
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