package com.lsurvila.callmonitortask.repository

import com.lsurvila.callmonitortask.model.Contact

interface ContactRepository {
    fun get(number: String): Contact
}