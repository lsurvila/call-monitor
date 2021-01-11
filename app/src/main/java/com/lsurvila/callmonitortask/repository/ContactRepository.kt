package com.lsurvila.callmonitortask.repository

interface ContactRepository {
    suspend fun getContactName(phoneNumber: String): String?
}