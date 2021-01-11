package com.lsurvila.callmonitortask.repository

import android.database.Cursor
import android.provider.ContactsContract

class ContactEntityMapper {

    fun map(cursor: Cursor?): String? {
        return if (cursor?.moveToNext() == true) {
            cursor.getString(cursor.getColumnIndex(ContactsContract.Data.DISPLAY_NAME))
        } else {
            null
        }
    }
}