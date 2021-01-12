package com.lsurvila.callmonitortask.repository.call

import com.lsurvila.callmonitortask.model.Call

interface CallLogRepository {

    fun insert(call: Call)
    fun query(): List<Call>
    fun delete()
}