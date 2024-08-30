package com.codeseyee.cloydcontact.Repository

import android.content.SharedPreferences
import com.codeseyee.cloydcontact.Model.User

class UserRepository(private val sharedPreferences: SharedPreferences) {

    fun getCurrentUser(): User {
        val id = sharedPreferences.getLong("userId", 0)
        val name = sharedPreferences.getString("userName", "Default User") ?: "Default User"
        val email = sharedPreferences.getString("userEmail", "default@example.com") ?: "default@example.com"

        return User(id, name, email)
    }
}
