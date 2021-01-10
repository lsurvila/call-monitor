package com.lsurvila.callmonitortask.service.http.model

import kotlinx.serialization.Serializable

@Serializable
data class Services(val start: String, val services: List<Service>)

@Serializable
data class Service(val name: String, val uri: String)