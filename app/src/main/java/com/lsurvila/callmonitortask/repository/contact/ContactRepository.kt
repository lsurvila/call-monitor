package com.lsurvila.callmonitortask.repository.contact

interface ContactRepository {
    suspend fun query(phoneNumber: String): String?
}