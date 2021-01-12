package com.lsurvila.callmonitortask.repository.contact

import android.content.ContentResolver
import android.net.Uri
import android.provider.ContactsContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ResolverContactRepository(private val contentResolver: ContentResolver,
                                private val contactEntityMapper: ContactEntityMapper
): ContactRepository {

    override suspend fun query(phoneNumber: String): String? {
        return withContext(Dispatchers.IO) {
            if (phoneNumber.isNotEmpty()) {
                val uri: Uri = Uri.withAppendedPath(
                    ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber)
                )
                val columnName = ContactsContract.Data.DISPLAY_NAME
                val contactLookup = contentResolver.query(
                    uri,
                    arrayOf(columnName), null, null, null
                )
                val contactName = contactEntityMapper.map(contactLookup, columnName)
                contactLookup?.close()
                contactName
            } else {
                null
            }
        }
    }
}