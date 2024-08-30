package com.codeseyee.cloydcontact.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.codeseyee.cloydcontact.Model.User
import com.google.firebase.firestore.FirebaseFirestore

class ContactsViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> get() = _users

    fun fetchUsers(userType: String) {
        val collection = if (userType == "individual") "individual" else "business"
        firestore.collection(collection)
            .get()
            .addOnSuccessListener { documents ->
                val userList = documents.map { doc ->
                    User(
                        id = doc.id,
                        name = doc.getString("name") ?: "No name",
                        role = doc.getString("accountType") ?: "No role",
                        department = doc.getString("department") ?: "No department",
                        profileImageUrl = doc.getString("profileImage") ?: ""
                    )
                }
                _users.value = userList
            }
            .addOnFailureListener {
                // Handle errors
            }
    }
}
