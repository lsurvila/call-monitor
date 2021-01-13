package com.lsurvila.callmonitortask.service.http.mapper

import com.lsurvila.TestUtil
import com.lsurvila.callmonitortask.service.http.Methods
import com.lsurvila.callmonitortask.service.http.model.Service
import com.lsurvila.callmonitortask.service.http.model.Services
import org.junit.Test

import org.junit.Assert.*
import java.net.URI

class ServicesMapperTest {

    private val mapper = ServicesMapper(UriMapper())

    @Test
    fun map() {
        val result = mapper.map(TestUtil.time("20:00:00"), enumValues(), URI.create("http://localhost:8080"))

        assertEquals(
            Services(
                "1970-01-01T20:00:00+01:00",
                listOf(
                    Service(Methods.STATUS.value, "http://localhost:8080/status"),
                    Service(Methods.LOG.value, "http://localhost:8080/log")
                )
            ), result
        )
    }
}