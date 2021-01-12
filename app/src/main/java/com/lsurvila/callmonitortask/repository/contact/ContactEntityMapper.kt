package com.lsurvila.callmonitortask.repository.contact

import android.database.Cursor

class ContactEntityMapper {

    fun map(cursor: Cursor?, columnName: String): String? {
        return if (cursor?.moveToNext() == true) {
            cursor.getString(cursor.getColumnIndex(columnName))
        } else {
            null
        }
    }
}