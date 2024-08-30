package com.codeseyee.cloydcontact.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    private val _selectedFragmentIndex = MutableLiveData<Int>()
    val selectedFragmentIndex: LiveData<Int> = _selectedFragmentIndex

    fun selectFragment(index: Int) {
        _selectedFragmentIndex.value = index
    }
}
