package com.codeseyee.cloydcontact.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ContactDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateContact(contact: Contact)

    @Query("SELECT * FROM contacts WHERE lastUpdatedAt > :lastUpdateTime")
    suspend fun getUpdatedContacts(lastUpdateTime: Long): List<Contact>

    @Query("SELECT * FROM contacts WHERE isSynced = 0 LIMIT :limit")
    suspend fun getUnSyncedContacts(limit: Int): List<Contact>

    @Query("SELECT * FROM contacts")
    suspend fun getAllContacts(): List<Contact>

    @Query("DELETE FROM contacts WHERE id IN (:contactIds)")
    suspend fun deleteContactsById(contactIds: List<Long>)

    @Delete
    suspend fun deleteContacts(contacts: List<Contact>) // Ensure this method exists

    @Query("DELETE FROM contacts WHERE lastUpdatedAt < :timestamp")
    suspend fun deleteOldContacts(timestamp: Long) // Add this method

    @Query("SELECT MAX(lastUpdatedAt) FROM contacts")
    suspend fun getMaxLastUpdatedAtTime(): Long?

    @Query("UPDATE contacts SET isSynced = 1 WHERE id IN (:contactIds)")
    suspend fun markContactsAsSynced(contactIds: List<Long>)

    @Query("SELECT lastUpdatedAt FROM contacts WHERE id = :contactId")
    suspend fun getLastUpdateTime(contactId: Long): Long?

    @Query("SELECT * FROM contacts WHERE name LIKE :searchKey")
    suspend fun search(searchKey: String): List<Contact>

    @Query("DELETE FROM contacts WHERE id IN (:contactIds)")
    suspend fun deleteContactsByIds(contactIds: List<Long>)
}
