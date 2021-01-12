package com.lsurvila.callmonitortask.repository.call

import com.lsurvila.callmonitortask.model.Call

class InMemoryCallLogRepository: CallLogRepository {

    private val log = mutableListOf<Call>()

    override fun insert(call: Call) {
        log.add(call)
    }

    override fun query(): List<Call> {
        return log
    }

    override fun delete() {
        log.clear()
    }
}