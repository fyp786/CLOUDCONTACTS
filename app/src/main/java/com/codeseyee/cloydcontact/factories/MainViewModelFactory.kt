package com.codeseyee.cloydcontact.factories


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.codeseyee.cloydcontact.Repository.ContactRepository
import com.codeseyee.cloydcontact.ViewModel.MainViewModel

class MainViewModelFactory(private val repository: ContactRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
