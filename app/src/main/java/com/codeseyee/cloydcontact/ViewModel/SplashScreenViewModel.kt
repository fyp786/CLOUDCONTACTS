package com.codeseyee.cloydcontact.ViewModel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*

class SplashScreenViewModel : ViewModel() {

    private val _navigateToLogin = MutableLiveData<Boolean>()
    val navigateToLogin: LiveData<Boolean> get() = _navigateToLogin

    init {
        startSplashScreenTimer()
    }

    private fun startSplashScreenTimer() {
        CoroutineScope(Dispatchers.Main).launch {
            delay(1500)
            _navigateToLogin.value = true
        }
    }

    override fun onCleared() {
        super.onCleared()
        CoroutineScope(Dispatchers.Main).cancel()
    }
}