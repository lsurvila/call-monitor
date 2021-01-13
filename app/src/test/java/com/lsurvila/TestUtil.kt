package com.lsurvila

import com.lsurvila.callmonitortask.util.DateTime
import io.mockk.every
import io.mockk.mockkObject
import java.text.SimpleDateFormat
import java.util.*

object TestUtil {

    fun mockNow(time: String): Date {
        mockkObject(DateTime)
        val now = requireNotNull(SimpleDateFormat("HH:mm:ss", Locale.getDefault()).parse(time))
        every { DateTime.now() } returns now
        return now
    }
}