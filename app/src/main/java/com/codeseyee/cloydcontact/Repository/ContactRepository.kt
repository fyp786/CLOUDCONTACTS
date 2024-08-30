package com.codeseyee.cloydcontact.Repository

import com.codeseyee.cloydcontact.room.ContactDao

class ContactRepository(private val contactDao: ContactDao) {

    suspend fun getAllContacts(): List<Contact> {
        return contactDao.getAllContacts()
    }

    suspend fun insertContact(contact: Contact) {
        contactDao.insertOrUpdateContact(contact)
    }

    suspend fun updateContact(contact: Contact) {
        contactDao.insertOrUpdateContact(contact)
    }

    suspend fun deleteContact(contact: Contact) {
        contactDao.deleteContactsById(listOf(contact.id))
    }

    suspend fun deleteOldContacts(timestamp: Long) {
        contactDao.deleteOldContacts(timestamp)
    }

    suspend fun deleteContacts(contacts: List<Contact>) {
        contactDao.deleteContactsById(contacts.map { it.id })
    }
}
