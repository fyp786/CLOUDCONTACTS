package com.codeseyee.cloydcontact.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.codeseyee.cloydcontact.contactutils.ContactHelper
import com.codeseyee.cloydcontact.room.AppDatabase
import com.codeseyee.cloydcontact.Repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ContactsViewModel(application: Application) : AndroidViewModel(application) {

    private val appDatabase = AppDatabase.getDatabase(application)
    private val userRepository = UserRepository(application.getSharedPreferences("MyPrefs", Application.MODE_PRIVATE))
    private val _contactsList = MutableLiveData<List<Any>>()
    val contactsList: LiveData<List<Any>> get() = _contactsList

    private val _isRefreshing = MutableLiveData<Boolean>(false)
    val isRefreshing: LiveData<Boolean> get() = _isRefreshing

    private val _isSelectionMode = MutableLiveData<Boolean>(false)
    val isSelectionMode: LiveData<Boolean> get() = _isSelectionMode

    fun refresh(searchKey: String = "") {
        _isRefreshing.value = true
        viewModelScope.launch {
            val contactsList = withContext(Dispatchers.IO) {
                val temp = appDatabase.contactDao().search("%${searchKey.trim().replace(" ", "%")}%")
                val contactHelper = ContactHelper(getApplication())
                val users = contactHelper.getUsersWhichAreContacts()
                val currentUser = userRepository.getCurrentUser() // Fetch current user using UserRepository

                mutableListOf<Any>().apply {
                    add("addContactRow")
                    add(currentUser) // Add current user here
                    if (users.isNotEmpty())
                        add("label:Found on Cloud Contacts (${users.size})")
                    addAll(users)
                    if (temp.isNotEmpty())
                        add("label:All Contacts (${temp.size})")
                    addAll(temp)
                }
            }
            _contactsList.value = contactsList
            _isRefreshing.value = false
        }
    }

    fun enterSelectionMode() {
        _isSelectionMode.value = true
    }

    fun exitSelectionMode() {
        _isSelectionMode.value = false
        refresh()
    }

    fun deleteSelectedContacts(selectedContacts: List<Contact>) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                appDatabase.contactDao().deleteContacts(selectedContacts)
                ContactHelper.deleteContactFromPhonebook(getApplication(), selectedContacts)
            }
        }
    }
}
