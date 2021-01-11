package com.lsurvila.callmonitortask.repository

import android.content.ContentResolver
import android.net.Uri
import android.provider.ContactsContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ResolverContactRepository(private val contentResolver: ContentResolver,
                                private val contactEntityMapper: ContactEntityMapper): ContactRepository{

    override suspend fun getContactName(phoneNumber: String): String? {
        return withContext(Dispatchers.IO) {
            val uri: Uri = Uri.withAppendedPath(
                ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber)
            )
            val contactLookup = contentResolver.query(uri, null, null, null, null)
            val contactName = contactEntityMapper.map(contactLookup)
            contactLookup?.close()
            contactName
        }
    }
}