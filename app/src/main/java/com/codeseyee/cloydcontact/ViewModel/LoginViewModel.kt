package com.codeseyee.cloydcontact.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginViewModel : ViewModel() {

    private val _loginResult = MutableLiveData<String>()
    val loginResult: LiveData<String> get() = _loginResult

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    fun loginUser(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _loginResult.value = "Email or Password cannot be empty"
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid ?: return@addOnCompleteListener
                    firestore.collection("individual").document(userId).get()
                        .addOnSuccessListener { document ->
                            if (document.exists()) {
                                _loginResult.value = "Login successful"
                            } else {
                                firestore.collection("business").document(userId).get()
                                    .addOnSuccessListener { doc ->
                                        if (doc.exists()) {
                                            _loginResult.value = "Login successful"
                                        } else {
                                            _loginResult.value = "User does not exist"
                                        }
                                    }
                                    .addOnFailureListener {
                                        _loginResult.value = "Failed to check user type"
                                    }
                            }
                        }
                        .addOnFailureListener {
                            _loginResult.value = "Failed to get user data"
                        }
                } else {
                    _loginResult.value = "Login failed: ${task.exception?.message}"
                }
            }
    }
}