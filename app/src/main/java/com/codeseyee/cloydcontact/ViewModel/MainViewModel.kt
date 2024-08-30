package com.codeseyee.cloydcontact.ViewModel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codeseyee.cloydcontact.Repository.ContactRepository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: ContactRepository) : ViewModel() {

    private val _contacts = MutableLiveData<List<Contact>>()
    val contacts: LiveData<List<Contact>> get() = _contacts

    fun loadContacts() {
        viewModelScope.launch {
            _contacts.value = repository.getAllContacts()
        }
    }

    fun deleteOldContacts() {
        viewModelScope.launch {
            repository.deleteOldContacts(System.currentTimeMillis() - 3 * 24 * 60 * 60 * 1000) // 3 days in millis
        }
    }
}
