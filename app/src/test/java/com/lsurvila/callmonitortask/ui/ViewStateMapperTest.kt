package com.lsurvila.callmonitortask.ui

import com.lsurvila.TestUtil
import com.lsurvila.callmonitortask.model.Call
import com.lsurvila.callmonitortask.model.CallMonitorState
import com.lsurvila.callmonitortask.model.PhoneState
import com.lsurvila.callmonitortask.model.State
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.net.URI

class ViewStateMapperTest {

    private val mapper = ViewStateMapper()

    @Before
    fun setup() {
        TestUtil.mockNow("20:00:00")
    }

    @Test
    fun `map not started`() {
        val result = mapper.map(CallMonitorState(State.NOT_STARTED))

        assertEquals(CallMonitorViewState(isServiceSwitchChecked = false), result)
    }

    @Test
    fun `map starting`() {
        val result = mapper.map(CallMonitorState(State.STARTING))

        assertEquals(
            CallMonitorViewState(
                isServiceSwitchChecked = true,
                consoleMessage = "1970-01-01 20:00:00: Starting..."
            ), result
        )
    }

    @Test
    fun `map phone not available`() {
        val result = mapper.map(CallMonitorState(State.PHONE_NOT_AVAILABLE))

        assertEquals(
            CallMonitorViewState(
                isServiceSwitchChecked = true,
                consoleMessage = "1970-01-01 20:00:00: Phone service is not available on this device"
            ), result
        )
    }

    @Test
    fun `map phone permission needed`() {
        val result = mapper.map(CallMonitorState(State.PHONE_PERMISSION_NEEDED))

        assertEquals(
            CallMonitorViewState(
                isServiceSwitchChecked = true,
                askForPhonePermission = true
            ), result
        )
    }

    @Test
    fun `map contacts permission needed`() {
        val result = mapper.map(CallMonitorState(State.CONTACTS_PERMISSION_NEEDED))

        assertEquals(
            CallMonitorViewState(
                isServiceSwitchChecked = true,
                askForContactsPermission = true
            ), result
        )
    }

    @Test
    fun `map started`() {
        val result =
            mapper.map(CallMonitorState(State.STARTED, URI.create("http://localhost:8080")))

        assertEquals(
            CallMonitorViewState(
                isServiceSwitchChecked = true,
                consoleMessage = "1970-01-01 20:00:00: Started on http://localhost:8080"
            ), result
        )
    }

    @Test
    fun `map stopping`() {
        val result = mapper.map(CallMonitorState(State.STOPPING))

        assertEquals(
            CallMonitorViewState(
                isServiceSwitchChecked = false,
                consoleMessage = "1970-01-01 20:00:00: Stopping..."
            ), result
        )
    }

    @Test
    fun `map stopped`() {
        val result = mapper.map(CallMonitorState(State.STOPPED))

        assertEquals(
            CallMonitorViewState(
                isServiceSwitchChecked = false,
                consoleMessage = "1970-01-01 20:00:00: Stopped. You can restore default Phone app in app settings"
            ), result
        )
    }

    @Test
    fun `map phone permission denied`() {
        val result = mapper.map(CallMonitorState(State.PHONE_PERMISSION_DENIED))

        assertEquals(
            CallMonitorViewState(
                isServiceSwitchChecked = false,
                consoleMessage = "1970-01-01 20:00:00: Phone permission was denied. You can set it manually in app settings"
            ), result
        )
    }

    @Test
    fun `map contacts permission denied`() {
        val result = mapper.map(CallMonitorState(State.CONTACTS_PERMISSION_DENIED))

        assertEquals(
            CallMonitorViewState(
                isServiceSwitchChecked = false,
                consoleMessage = "1970-01-01 20:00:00: Contacts permission was denied. You can set it manually in app settings"
            ), result
        )
    }

    @Test
    fun `map wifi disconnected`() {
        val result = mapper.map(CallMonitorState(State.WIFI_DISCONNECTED))

        assertEquals(
            CallMonitorViewState(
                isServiceSwitchChecked = false,
                consoleMessage = "1970-01-01 20:00:00: Service requires active Wifi connection, currently it's offline"
            ), result
        )
    }

    @Test
    fun `map wifi ip failed to resolve`() {
        val result = mapper.map(CallMonitorState(State.WIFI_IP_FAILED_TO_RESOLVE))

        assertEquals(
            CallMonitorViewState(
                isServiceSwitchChecked = false,
                consoleMessage = "1970-01-01 20:00:00: Failed to resolve Wifi IP address"
            ), result
        )
    }


    @Test
    fun `mapFromPhoneCall idle`() {
        val call = Call(PhoneState.IDLE, "(650) 555-1212")

        val result = mapper.mapFromPhoneCall(call)

        assertEquals(
            CallMonitorViewState(
                isAnswerButtonEnabled = false,
                isRejectButtonEnabled = false
            ), result
        )
    }

    @Test
    fun `mapFromPhoneCall incoming`() {
        val call = Call(PhoneState.INCOMING, "(650) 555-1212")

        val result = mapper.mapFromPhoneCall(call)

        assertEquals(
            CallMonitorViewState(
                isAnswerButtonEnabled = true,
                isRejectButtonEnabled = true,
                consoleMessage = "1970-01-01 20:00:00: Incoming call from (650) 555-1212..."
            ), result
        )
    }

    @Test
    fun `mapFromPhoneCall outgoing`() {
        val call = Call(PhoneState.OUTGOING, "(650) 555-1212")

        val result = mapper.mapFromPhoneCall(call)

        assertEquals(
            CallMonitorViewState(
                isAnswerButtonEnabled = false,
                isRejectButtonEnabled = true,
                consoleMessage = "1970-01-01 20:00:00: Call connecting to (650) 555-1212..."
            ), result
        )
    }

    @Test
    fun `mapFromPhoneCall connected`() {
        val call = Call(PhoneState.CONNECTED, "(650) 555-1212")

        val result = mapper.mapFromPhoneCall(call)

        assertEquals(
            CallMonitorViewState(
                isAnswerButtonEnabled = false,
                isRejectButtonEnabled = true,
                consoleMessage = "1970-01-01 20:00:00: Call connected..."
            ), result
        )
    }

    @Test
    fun `mapFromPhoneCall disconnected`() {
        val call = Call(PhoneState.DISCONNECTED, "(650) 555-1212")

        val result = mapper.mapFromPhoneCall(call)

        assertEquals(
            CallMonitorViewState(
                isAnswerButtonEnabled = false,
                isRejectButtonEnabled = false,
                consoleMessage = "1970-01-01 20:00:00: Call ended"
            ), result
        )
    }

    @Test
    fun `mapFromPhoneCall unknown`() {
        val call = Call(PhoneState.UNKNOWN, "(650) 555-1212")

        val result = mapper.mapFromPhoneCall(call)

        assertEquals(CallMonitorViewState(), result)
    }
}