package com.codeseyee.cloydcontact.contactutils

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Ignore

@Entity(tableName = "contacts")
public class Contact(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val displayName: String? = null,
    val givenName: String? = null,
    val familyName: String? = null,
    val lastUpdatedAt: String? = null,
    val name: String = "",
    val phoneNumbers: String = "",
    val photoUri: String? = null,
    val emails: String? = null,
    val birthday: String? = null,
    val companyName: String? = null,
    val companyTitle: String? = null,
    val websites: String? = null,
    val addresses: String? = null,
    val note: String? = null,
    val updatedAt: String? = null,
    val updatedAtLong: Long = 0L,
    @Ignore val isFavourite: Int = 0,
    @Ignore val isSynced: Boolean = false,
    @Ignore val isSelected: Boolean = false
) {
    // Room requires an empty constructor
    constructor() : this(
        id = 0L,
        displayName = null,
        givenName = null,
        familyName = null,
        lastUpdatedAt = null,
        name = "",
        phoneNumbers = "",
        photoUri = null,
        emails = null,
        birthday = null,
        companyName = null,
        companyTitle = null,
        websites = null,
        addresses = null,
        note = null,
        updatedAt = null,
        updatedAtLong = 0L,
        isFavourite = 0,
        isSynced = false,
        isSelected = false
    )
}
