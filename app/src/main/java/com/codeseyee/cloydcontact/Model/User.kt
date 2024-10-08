package com.codeseyee.cloydcontact.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val id: String = "",  // Change to String
    val email: String = "",
    val phone: String = "",
    val name: String = "",
    val password: String = "",
    val companyName: String = "",
    val profileImageUrl: String = "",
    val accountType: String = "",
    val isSynced: Boolean = false,
    val lastUpdatedAt: Long = 0,
    val role: String = "",
    val department: String = ""
)
