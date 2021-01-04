package com.lsurvila.callmonitortask.repository

import android.database.Cursor
import com.lsurvila.callmonitortask.model.Contact

class ContactEntityMapper {

    fun map(number: String, cursor: Cursor?): Contact {
        val name = cursor?.use {
            when (cursor.moveToFirst()) {
                true -> cursor.getString(0)
                else -> null
            }
        }
        return Contact(number, name)
    }
}