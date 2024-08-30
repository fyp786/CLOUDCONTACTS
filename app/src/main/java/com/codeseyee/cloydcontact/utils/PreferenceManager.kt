package com.codeseyee.cloydcontact.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.codeseyee.cloydcontact.Model.User
import com.google.gson.Gson

object PreferenceManager {

    private val context: Context
        get() = MyApplication.context ?: throw IllegalStateException("Context is not initialized")

    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences(
            "prefsv1${getCurrentUser()?.id ?: 1}",
            Context.MODE_PRIVATE
        )
    }
    private val editor: SharedPreferences.Editor by lazy { prefs.edit() }

    private val generalPrefs: SharedPreferences by lazy {
        context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
    }
    private val generalEditor: SharedPreferences.Editor by lazy { generalPrefs.edit() }

    fun getPreferredMode(): Int = prefs.getInt("PreferredMode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

    fun setPreferredMode(value: Int) {
        editor.putInt("PreferredMode", value).apply()
    }

    fun getCurrentUser(): User? {
        return Gson().fromJson(generalPrefs.getString("user", null), User::class.java)
    }

    fun setCurrentUser(user: User?) {
        generalEditor.putString("user", Gson().toJson(user)).apply()
    }

    fun removeCurrentUser() {
        generalEditor.remove("user").apply()
    }

    fun getApiToken(): String = generalPrefs.getString("api_token", "none") ?: "none"

    fun setApiToken(value: String) {
        generalEditor.putString("api_token", value).apply()
    }
}
