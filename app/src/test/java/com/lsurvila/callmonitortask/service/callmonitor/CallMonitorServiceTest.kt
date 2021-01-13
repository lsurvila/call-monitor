package com.lsurvila.callmonitortask.service.callmonitor

import com.lsurvila.TestUtil
import com.lsurvila.callmonitortask.model.Call
import com.lsurvila.callmonitortask.model.PhoneState
import com.lsurvila.callmonitortask.repository.call.CallLogRepository
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class CallMonitorServiceTest {

    class CallMonitorServiceImpl(callLogRepository: CallLogRepository) :
        CallMonitorService(callLogRepository) {
        override fun isAvailable() = true
        override fun hasPhonePermission() = true
        override fun hasContactsPermission() = true
    }

    private val callLogRepository = mockk<CallLogRepository>(relaxed = true)
    private val listener = mockk<CallIntentListener>(relaxed = true)
    private val service = CallMonitorServiceImpl(callLogRepository).apply {
        this.setCallIntentListener(listener)
    }

    @Test
    fun `logPhoneCall opens phone`() {
        service.logPhoneCall(Call(PhoneState.CONNECTED, "(650) 555-1212"))

        verify { listener.onOpenPhone() }
    }

    @Test
    fun `logPhoneCall when only connected does not log call`() {
        service.logPhoneCall(Call(PhoneState.CONNECTED, "(650) 555-1212"))

        verify(exactly = 0) { callLogRepository.insert(any()) }
    }

    @Test
    fun `logPhoneCall when only disconnected logs call without connected time`() {
        val disconnectedTime = TestUtil.mockNow("20:00:00")
        service.logPhoneCall(Call(PhoneState.DISCONNECTED, "(650) 555-1212"))

        verify(exactly = 1) {
            callLogRepository.insert(
                Call(
                    PhoneState.DISCONNECTED,
                    "(650) 555-1212",
                    connectedTime = null,
                    disconnectedTime = disconnectedTime
                )
            )
        }
    }

    @Test
    fun `logPhoneCall with both connected and disconnected logs call with both times`() {
        val connectedTime = TestUtil.mockNow("19:50:00")
        service.logPhoneCall(Call(PhoneState.CONNECTED, "(650) 555-1212"))
        val disconnectedTime = TestUtil.mockNow("20:00:00")
        service.logPhoneCall(Call(PhoneState.DISCONNECTED, "(650) 555-1212"))

        verify(exactly = 1) {
            callLogRepository.insert(
                Call(
                    PhoneState.DISCONNECTED,
                    "(650) 555-1212",
                    connectedTime = connectedTime,
                    disconnectedTime = disconnectedTime
                )
            )
        }
    }

    @Test
    fun `logPhoneCall when connected after disconnected does not log new call`() {
        val connectedTime = TestUtil.mockNow("19:50:00")
        service.logPhoneCall(Call(PhoneState.CONNECTED, "(650) 555-1212"))
        val disconnectedTime = TestUtil.mockNow("20:00:00")
        service.logPhoneCall(Call(PhoneState.DISCONNECTED, "(650) 555-1212"))

        TestUtil.mockNow("20:00:10")
        service.logPhoneCall(Call(PhoneState.CONNECTED, "(650) 555-1212"))

        verify(exactly = 1) {
            callLogRepository.insert(
                Call(
                    PhoneState.DISCONNECTED,
                    "(650) 555-1212",
                    connectedTime = connectedTime,
                    disconnectedTime = disconnectedTime
                )
            )
        }
    }

    @Test
    fun `logPhoneCallMonitored increases monitored count`() {
        val connectedTime = TestUtil.mockNow("19:50:00")
        service.logPhoneCall(Call(PhoneState.CONNECTED, "(650) 555-1212"))

        service.logPhoneCallMonitored()
        service.logPhoneCallMonitored()

        val disconnectedTime = TestUtil.mockNow("20:00:00")
        service.logPhoneCall(Call(PhoneState.DISCONNECTED, "(650) 555-1212"))

        verify(exactly = 1) {
            callLogRepository.insert(
                Call(
                    PhoneState.DISCONNECTED,
                    "(650) 555-1212",
                    connectedTime = connectedTime,
                    disconnectedTime = disconnectedTime,
                    monitoredCount = 2
                )
            )
        }
    }

    @Test
    fun `logPhoneCallMonitored after call is logged does not increase monitored count`() {
        val connectedTime = TestUtil.mockNow("19:50:00")
        service.logPhoneCall(Call(PhoneState.CONNECTED, "(650) 555-1212"))

        service.logPhoneCallMonitored()
        service.logPhoneCallMonitored()

        val disconnectedTime = TestUtil.mockNow("20:00:00")
        service.logPhoneCall(Call(PhoneState.DISCONNECTED, "(650) 555-1212"))

        service.logPhoneCallMonitored()

        verify(exactly = 1) {
            callLogRepository.insert(
                Call(
                    PhoneState.DISCONNECTED,
                    "(650) 555-1212",
                    connectedTime = connectedTime,
                    disconnectedTime = disconnectedTime,
                    monitoredCount = 2
                )
            )
        }
    }

    @Test
    fun `answerCall answers call`() {
        service.answerCall()

        verify { listener.onAnswerCall() }
    }

    @Test
    fun `rejectCall rejects call`() {
        service.rejectCall()

        verify { listener.onRejectCall() }
    }
}