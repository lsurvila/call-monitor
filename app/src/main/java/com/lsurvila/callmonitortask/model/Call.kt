package com.lsurvila.callmonitortask.model

import java.util.*

data class Call(
    val state: PhoneState,
    val number: String,
    val connectedTime: Date? = null,
    val disconnectedTime: Date? = null,
    val monitoredCount: Int = 0
)