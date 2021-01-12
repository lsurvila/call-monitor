package com.lsurvila.callmonitortask

import com.lsurvila.callmonitortask.model.PhoneState
import com.lsurvila.callmonitortask.repository.call.CallLogRepository
import com.lsurvila.callmonitortask.repository.contact.ContactRepository
import com.lsurvila.callmonitortask.service.http.mapper.CallMapper
import com.lsurvila.callmonitortask.service.http.model.LoggedCall

class ViewCallLogUseCase(
    private val callLogRepository: CallLogRepository,
    private val contactRepository: ContactRepository,
    private val mapper: CallMapper
) {

    suspend fun execute(): List<LoggedCall> {
        return callLogRepository.query()
            .filter { it.state == PhoneState.DISCONNECTED && it.connectedTime != null && it.disconnectedTime != null }
            .map { mapper.mapLoggedCall(it, contactRepository.query(it.number)) }
    }
}