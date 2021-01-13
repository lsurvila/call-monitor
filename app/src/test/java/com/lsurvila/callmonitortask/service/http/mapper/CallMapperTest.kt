package com.lsurvila.callmonitortask.service.http.mapper

import com.lsurvila.TestUtil
import com.lsurvila.callmonitortask.model.Call
import com.lsurvila.callmonitortask.model.PhoneState
import com.lsurvila.callmonitortask.service.http.model.LoggedCall
import com.lsurvila.callmonitortask.service.http.model.OngoingCall
import org.junit.Assert.*
import org.junit.Test

class CallMapperTest {

    private val mapper = CallMapper()

    @Test
    fun `mapOngoingCall if connected`() {
        val result = mapper.mapOngoingCall(Call(PhoneState.CONNECTED, "(650) 555-1212"), "John")

        assertEquals(OngoingCall(true, "(650) 555-1212", "John"), result)
    }

    @Test
    fun `mapOngoingCall if disconnected`() {
        val result = mapper.mapOngoingCall(Call(PhoneState.DISCONNECTED, "(650) 555-1212"), "John")

        assertEquals(OngoingCall(false), result)
    }

    @Test
    fun mapLoggedCall() {
        val result = mapper.mapLoggedCall(
            Call(
                PhoneState.CONNECTED,
                "(650) 555-1212",
                TestUtil.time("20:00:00"),
                TestUtil.time("20:00:10"),
                5
            ), "John"
        )

        assertEquals(LoggedCall(
            "1970-01-01T20:00:00+01:00",
            "10",
            "(650) 555-1212",
            "John",
            5
        ), result)
    }
}