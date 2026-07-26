package com.synapse.mobile.core.resolver

import android.content.Context
import android.provider.ContactsContract
import java.util.Locale

class ContactResolver(
    private val context: Context
) {

    /**
     * Returns all contacts having at least one phone number.
     */
    fun getAllContacts(): List<ResolvedContact> {

        val contacts = mutableListOf<ResolvedContact>()

        val cursor = context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER
            ),
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )

        cursor?.use {

            val idIndex =
                it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)

            val nameIndex =
                it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)

            val numberIndex =
                it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER)

            while (it.moveToNext()) {

                contacts.add(
                    ResolvedContact(
                        id = it.getLong(idIndex),
                        name = it.getString(nameIndex),
                        phoneNumber = it.getString(numberIndex)
                    )
                )
            }
        }

        return contacts
            .distinctBy { it.id to it.phoneNumber }
            .sortedBy { it.name.lowercase(Locale.getDefault()) }
    }

    /**
     * Resolves a contact using:
     * 1. Exact match
     * 2. Starts with
     * 3. Contains
     */
    fun resolve(name: String): ResolvedContact? {

        val query = name.trim().lowercase(Locale.getDefault())

        val contacts = getAllContacts()

        contacts.firstOrNull {
            it.name.lowercase(Locale.getDefault()) == query
        }?.let { return it }

        contacts.firstOrNull {
            it.name.lowercase(Locale.getDefault()).startsWith(query)
        }?.let { return it }

        contacts.firstOrNull {
            it.name.lowercase(Locale.getDefault()).contains(query)
        }?.let { return it }

        return null
    }

    /**
     * Returns every matching contact.
     */
    fun search(name: String): List<ResolvedContact> {

        val query = name.trim().lowercase(Locale.getDefault())

        return getAllContacts().filter {
            it.name.lowercase(Locale.getDefault()).contains(query)
        }
    }

    /**
     * Checks whether a contact exists.
     */
    fun exists(name: String): Boolean {
        return resolve(name) != null
    }

    /**
     * Returns only the phone number.
     */
    fun getPhoneNumber(name: String): String? {
        return resolve(name)?.phoneNumber
    }
}