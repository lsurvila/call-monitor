package com.lsurvila.callmonitortask.repository

import android.content.ContentResolver
import android.net.Uri
import android.provider.ContactsContract
import com.lsurvila.callmonitortask.model.Contact

class ContactResolverRepository(private val contentResolver: ContentResolver,
                                private val contactEntityMapper: ContactEntityMapper): ContactRepository {

    override fun get(number: String): Contact {
        val uri = Uri.withAppendedPath(
            ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
            Uri.encode(number)
        )

        val projection = arrayOf(ContactsContract.PhoneLookup.DISPLAY_NAME)
        val cursor = contentResolver.query(uri, projection, null, null, null)
        val entity = contactEntityMapper.map(number, cursor)
        cursor?.close()
        return entity
    }
}