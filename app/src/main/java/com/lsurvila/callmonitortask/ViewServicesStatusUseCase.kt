package com.lsurvila.callmonitortask

import com.lsurvila.callmonitortask.service.http.Methods
import com.lsurvila.callmonitortask.service.http.mapper.ServicesMapper
import com.lsurvila.callmonitortask.service.http.model.Services
import java.net.URI
import java.util.*

class ViewServicesStatusUseCase(private val mapper: ServicesMapper) {

    fun execute(serverStartedTime: Date, methods: Array<Methods>, address: URI): Services {
        return mapper.map(serverStartedTime, methods, address)
    }
}