package com.codeseyee.cloydcontact.ViewModel


import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class SignUpViewModel : ViewModel() {

    private val _signupResult = MutableLiveData<String>()
    val signupResult: LiveData<String> get() = _signupResult

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    fun signUpUser(email: String, phone: String, name: String, password: String, profileImageUri: String?, accountType: String) {
        if (profileImageUri != null) {
            val profileImageRef = storage.reference.child("profile_images/${System.currentTimeMillis()}.jpg")
            profileImageRef.putFile(Uri.parse(profileImageUri))
                .addOnSuccessListener {
                    profileImageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                        registerUser(email, phone, name, password, downloadUrl.toString(), accountType)
                    }
                }
                .addOnFailureListener {
                    _signupResult.value = "Failed to upload profile image"
                }
        } else {
            registerUser(email, phone, name, password, "", accountType)
        }
    }

    private fun registerUser(email: String, phone: String, name: String, password: String, profileImageUrl: String, accountType: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _signupResult.value = "Email and password cannot be empty"
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid ?: return@addOnCompleteListener
                    val userMap = mapOf(
                        "email" to email,
                        "phone" to phone,
                        "name" to name,
                        "profileImage" to profileImageUrl,
                        "accountType" to accountType
                    )
                    val collection = if (accountType == "Individual") "individual" else "business"
                    firestore.collection(collection).document(userId)
                        .set(userMap)
                        .addOnSuccessListener {
                            _signupResult.value = "Signup successful"
                        }
                        .addOnFailureListener {
                            _signupResult.value = "Failed to save user data"
                        }
                } else {
                    _signupResult.value = "Signup failed: ${task.exception?.message}"
                }
            }
    }

}