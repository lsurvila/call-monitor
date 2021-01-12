package com.lsurvila.callmonitortask

import com.lsurvila.callmonitortask.repository.contact.ContactRepository
import com.lsurvila.callmonitortask.service.callmonitor.CallMonitorService
import com.lsurvila.callmonitortask.service.http.mapper.CallMapper
import com.lsurvila.callmonitortask.service.http.model.OngoingCall

class ViewOngoingCallUseCase(
    private val callMapper: CallMapper,
    private val repository: ContactRepository,
    private val callMonitorService: CallMonitorService
) {

    suspend fun execute(): OngoingCall {
        val call = callMonitorService.phoneCall().value
        val name = repository.query(call.number)
        return callMapper.map(call, name)
    }
}