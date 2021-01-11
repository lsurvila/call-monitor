package com.lsurvila.callmonitortask.service.http.model

import kotlinx.serialization.Serializable

@Serializable
data class OngoingCall(val ongoing: Boolean, val number: String? = null, val name: String? = null)