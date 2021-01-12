package com.lsurvila.callmonitortask.service.http.model

import kotlinx.serialization.Serializable

@Serializable
data class LoggedCall(val beginning: String, val duration: String, val number: String, val name: String?, val timesQueried: Int)
