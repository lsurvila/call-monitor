package com.lsurvila.callmonitortask

import com.lsurvila.TestUtil
import com.lsurvila.callmonitortask.model.Call
import com.lsurvila.callmonitortask.model.PhoneState
import com.lsurvila.callmonitortask.repository.call.CallLogRepository
import com.lsurvila.callmonitortask.repository.contact.ContactRepository
import com.lsurvila.callmonitortask.service.http.mapper.CallMapper
import com.lsurvila.callmonitortask.service.http.model.LoggedCall
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*

class ViewCallLogUseCaseTest {

    private val callLogRepository = mockk<CallLogRepository>()
    private val contactsRepository = mockk<ContactRepository>()
    private val mapper = mockk<CallMapper>()
    private val useCase = ViewCallLogUseCase(callLogRepository, contactsRepository, mapper)

    @Test
    fun execute() {
        val callLog = listOf(
            Call(PhoneState.DISCONNECTED, "(650) 555-1212", TestUtil.time("20:00:00"), TestUtil.time("20:00:10")),
            Call(PhoneState.DISCONNECTED, "(650) 555-1212", TestUtil.time("20:10:00"), TestUtil.time("20:10:10")),
            Call(PhoneState.DISCONNECTED, "(650) 555-1212", TestUtil.time("20:23:00"), TestUtil.time("20:25:10"))
        )
        every { callLogRepository.query() } returns callLog
        every { runBlocking { contactsRepository.query("(650) 555-1212") }} returns "John"
        every { mapper.mapLoggedCall(any(), "John") } returns LoggedCall("20:00:00", "10", "(650) 555-1212", "John", 0)

        val result = runBlocking {
            useCase.execute()
        }

        assertEquals(3, result.size)
    }

    @Test
    fun `execute filter out other states`() {
        val callLog = listOf(
            Call(PhoneState.CONNECTED, "(650) 555-1212", TestUtil.time("20:00:00"), TestUtil.time("20:00:10")),
            Call(PhoneState.DISCONNECTED, "(650) 555-1212", TestUtil.time("20:00:00"), TestUtil.time("20:00:10")),
            Call(PhoneState.DISCONNECTED, "(650) 555-1212", TestUtil.time("20:10:00"), TestUtil.time("20:10:10")),
            Call(PhoneState.DISCONNECTED, "(650) 555-1212", TestUtil.time("20:23:00"), TestUtil.time("20:25:10")),
            Call(PhoneState.UNKNOWN, "(650) 555-1212", TestUtil.time("20:00:00"), TestUtil.time("20:00:10")),
        )
        every { callLogRepository.query() } returns callLog
        every { runBlocking { contactsRepository.query("(650) 555-1212") }} returns "John"
        every { mapper.mapLoggedCall(any(), "John") } returns LoggedCall("20:00:00", "10", "(650) 555-1212", "John", 0)

        val result = runBlocking {
            useCase.execute()
        }

        assertEquals(3, result.size)
    }

    @Test
    fun `execute filter out with no connected time`() {
        val callLog = listOf(
            Call(PhoneState.DISCONNECTED, "(650) 555-1212", null, TestUtil.time("20:00:10")),
            Call(PhoneState.DISCONNECTED, "(650) 555-1212", TestUtil.time("20:10:00"), TestUtil.time("20:10:10")),
            Call(PhoneState.DISCONNECTED, "(650) 555-1212", TestUtil.time("20:23:00"), TestUtil.time("20:25:10")),
        )
        every { callLogRepository.query() } returns callLog
        every { runBlocking { contactsRepository.query("(650) 555-1212") }} returns "John"
        every { mapper.mapLoggedCall(any(), "John") } returns LoggedCall("20:00:00", "10", "(650) 555-1212", "John", 0)

        val result = runBlocking {
            useCase.execute()
        }

        assertEquals(2, result.size)
    }

    @Test
    fun `execute filter out with no disconnected time`() {
        val callLog = listOf(
            Call(PhoneState.DISCONNECTED, "(650) 555-1212", TestUtil.time("20:10:00"), TestUtil.time("20:00:10")),
            Call(PhoneState.DISCONNECTED, "(650) 555-1212", TestUtil.time("20:10:00"), TestUtil.time("20:10:10")),
            Call(PhoneState.DISCONNECTED, "(650) 555-1212", TestUtil.time("20:23:00"), null)
        )
        every { callLogRepository.query() } returns callLog
        every { runBlocking { contactsRepository.query("(650) 555-1212") }} returns "John"
        every { mapper.mapLoggedCall(any(), "John") } returns LoggedCall("20:00:00", "10", "(650) 555-1212", "John", 0)

        val result = runBlocking {
            useCase.execute()
        }

        assertEquals(2, result.size)
    }
}