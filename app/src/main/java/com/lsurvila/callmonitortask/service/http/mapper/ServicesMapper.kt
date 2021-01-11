package com.lsurvila.callmonitortask.service.http.mapper

import com.lsurvila.callmonitortask.service.http.Methods
import com.lsurvila.callmonitortask.service.http.model.Service
import com.lsurvila.callmonitortask.service.http.model.Services
import com.lsurvila.callmonitortask.ui.DateTimeMapper
import java.net.URI
import java.util.*

class ServicesMapper(private val uriMapper: UriMapper, private val dateTimeMapper: DateTimeMapper) {
    fun map(serverStarted: Date, methods: Array<Methods>, address: URI): Services {
        return Services(
            dateTimeMapper.mapDateTimeLong(serverStarted),
            mapServices(methods, address)
        )
    }

    private fun mapServices(methods: Array<Methods>, address: URI): List<Service> {
        return methods
            .filter { it.value.isNotEmpty() } // don't show root method itself
            .map {
                Service(
                    it.value,
                    uriMapper.mapHttpAddress(address.host, address.port, it.value).toString()
                )
            }
    }
}