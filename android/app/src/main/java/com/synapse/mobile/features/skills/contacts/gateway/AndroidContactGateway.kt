package com.synapse.mobile.features.skills.contacts.gateway

import android.content.ContentResolver
import android.provider.ContactsContract

class AndroidContactGateway(
    private val contentResolver: ContentResolver
) : ContactGateway {

    override fun findContactNumber(
        name: String
    ): String? {

        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
            ),
            "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} LIKE ?",
            arrayOf("%$name%"),
            null
        )

        cursor?.use {

            if (it.moveToFirst()) {

                val index = it.getColumnIndex(
                    ContactsContract.CommonDataKinds.Phone.NUMBER
                )

                return it.getString(index)

            }

        }

        return null

    }

}